package br.laiza.transactionauthorizer.usecases

import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.core.exception.InsuficientFundsException
import br.laiza.transactionauthorizer.core.interfaces.AmountService
import br.laiza.transactionauthorizer.core.interfaces.MessageProducer
import br.laiza.transactionauthorizer.core.interfaces.RedisRepository
import br.laiza.transactionauthorizer.core.message.TransactionMessage
import br.laiza.transactionauthorizer.usecases.dto.TransactionRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class TransactionAuthorizerUseCase(
    private val amountService: AmountService,
    private val redisRepository: RedisRepository,
    private val messageProducer: MessageProducer,
    private val objectMapper: ObjectMapper
) {

    fun authorize(request: TransactionRequest) {
        var mapWallets: HashMap<String, Double> = amountService.availableAmount(request.account)

        //descobrir a wallet correta
        val wallet: WalletEnum = this.findTheMCC(request)
        request.mcc = wallet.toString()

        //descobrir se dinheiro disponivel é suficiente
        mapWallets = this.validateAmountIsEnough(
            wallet = wallet,
            transactionRequest = request,
            mapWallets = mapWallets
        )


        val transactionMessage: TransactionMessage = TransactionMessage(
            account = request.account,
            transactionDate = LocalDateTime.now(),
            amountTransaction = request.totalAmount,
            newAmountWalletFood = mapWallets.get(WalletEnum.FOOD.name)!!,
            newAmountWalletMeal = mapWallets.get(WalletEnum.MEAL.name)!!,
            newAmountWalletCash = mapWallets.get(WalletEnum.CASH.name)!!
        )

        redisRepository.saveDataTransaction("123", transactionMessage)


        redisRepository.saveDataTransaction(request.account, transactionMessage)

        messageProducer.produceMessage(objectMapper.writeValueAsString(transactionMessage))


    }

    private fun findTheMCC(transaction: TransactionRequest): WalletEnum {
        var walletMCC: WalletEnum = WalletEnum.fromMcc(transaction.mcc)
        var walletName: WalletEnum = WalletEnum.fromName(transaction.merchant)

        return if (walletMCC == walletName) walletMCC else walletName
    }

    private fun validateAmountIsEnough(
        wallet: WalletEnum,
        transactionRequest: TransactionRequest,
        mapWallets: HashMap<String, Double>
    ): HashMap<String, Double> {

        var amountAvailable: Double = mapWallets.get(wallet.name)!!
        amountAvailable -= transactionRequest.totalAmount
        mapWallets[WalletEnum.CASH.name] = amountAvailable

        if (amountAvailable < 0) {
            if (wallet.name == WalletEnum.CASH.name) {
                throw InsuficientFundsException("Insuficient funds")
            }
            var amountAvailableAux: Double = mapWallets.get(WalletEnum.CASH.name)!!
            amountAvailableAux -= transactionRequest.totalAmount
            if (amountAvailable < 0) {
                throw Exception("Insuficient balance")
            }

            mapWallets[WalletEnum.CASH.name] = amountAvailableAux

        }
        return mapWallets
    }

}


