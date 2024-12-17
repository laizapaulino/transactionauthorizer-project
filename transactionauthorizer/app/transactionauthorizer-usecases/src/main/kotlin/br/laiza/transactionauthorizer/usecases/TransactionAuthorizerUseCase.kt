package br.laiza.transactionauthorizer.usecases


import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.core.enums.WalletEnum.*
import br.laiza.transactionauthorizer.core.exception.InsuficientFundsException
import br.laiza.transactionauthorizer.core.interfaces.AmountService
import br.laiza.transactionauthorizer.core.interfaces.MessageProducer

import br.laiza.transactionauthorizer.core.interfaces.RedisRepository
import br.laiza.transactionauthorizer.core.message.MessageRedis
import br.laiza.transactionauthorizer.core.message.TransactionMessage

import br.laiza.transactionauthorizer.usecases.dto.TransactionRequest
import br.laiza.transactionauthorizer.usecases.handler.MccFromMerchantHandler
import br.laiza.transactionauthorizer.usecases.handler.MccFromTransactionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
open class TransactionAuthorizerUseCase(
    private val amountService: AmountService,
    private val redisRepository: RedisRepository,
    private val messageProducer: MessageProducer,
    private val objectMapper: ObjectMapper
) {

    fun authorize(request: TransactionRequest) {
        var messageRedis: MessageRedis? = amountService.availableAmount(request.account)

        if (messageRedis == null) {
            throw Exception()
        }
        val wallet: WalletEnum = this.findTheMCC(request)
        request.mcc = wallet.toString()


        messageRedis = this.validateAmountIsEnough(
            wallet = wallet,
            transactionRequest = request,
            message = messageRedis
        )


        val transactionMessage = TransactionMessage(
            account = request.account,
            transactionDate = LocalDateTime.now(),
            amountTransaction = request.totalAmount,
            newAmountWalletFood = messageRedis.listWallet[FOOD.name]!!,
            newAmountWalletMeal = messageRedis.listWallet[MEAL.name]!!,
            newAmountWalletCash = messageRedis.listWallet[CASH.name]!!,
            mcc = request.mcc,
            merchant = request.merchant
        )

        messageRedis.dateLastAccessed = LocalDateTime.now()

        redisRepository.saveDataTransaction(request.account, messageRedis)

        try {
            messageProducer.produceMessage(objectMapper.writeValueAsString(transactionMessage))

        } catch (e: Exception) {
            println("aws sqs not available")
        }

    }


    fun findTheMCC(transaction: TransactionRequest): WalletEnum {
        val chain = listOf(
            MccFromMerchantHandler(),
            MccFromTransactionHandler(),
        )

        var result: WalletEnum? = null

        for (handler in chain) {
            result = handler.handle(transaction) { result }
            if (result != null) break
        }

        return result ?: CASH
    }


    private fun validateAmountIsEnough(
        wallet: WalletEnum,
        transactionRequest: TransactionRequest,
        message: MessageRedis
    ): MessageRedis {

        var amountAvailable: BigDecimal = message.listWallet.get(wallet.name)!!
        amountAvailable = amountAvailable.subtract(transactionRequest.totalAmount)


        if (amountAvailable.compareTo(BigDecimal.ZERO) < 0) {
            if (wallet.name == CASH.name) {
                throw InsuficientFundsException("Insuficient funds")
            }
            var amountAvailableAux: BigDecimal = message.listWallet.get(CASH.name)!!
            amountAvailableAux = amountAvailableAux.add(amountAvailable)
            if (amountAvailableAux.compareTo(BigDecimal.ZERO)  < 0) {
                throw InsuficientFundsException("Insuficient funds")
            }
            message.listWallet[CASH.name] = amountAvailableAux.setScale(2)
            message.listWallet[wallet.name] = BigDecimal.valueOf(0.00).setScale(2)

        } else {
            message.listWallet[wallet.name] = amountAvailable.setScale(2)
        }
        return message
    }

}


