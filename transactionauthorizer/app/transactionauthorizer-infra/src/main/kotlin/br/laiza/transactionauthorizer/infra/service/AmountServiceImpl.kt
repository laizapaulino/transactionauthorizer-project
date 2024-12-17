package br.laiza.transactionauthorizer.infra.service


import br.laiza.transactionauthorizer.core.entities.Account
import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.core.interfaces.AmountService
import br.laiza.transactionauthorizer.core.interfaces.RedisRepository
import br.laiza.transactionauthorizer.core.interfaces.repository.AccountRepository
import br.laiza.transactionauthorizer.core.message.MessageRedis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class AmountServiceImpl(
    private val redisRepository: RedisRepository,
    @Autowired private val accountRepository: AccountRepository
) : AmountService {


    override fun availableAmount(account: String): MessageRedis? {
        val data: MessageRedis? = redisRepository.getTransactionData(account)

        if (data != null) {
            return data
        } else {
            return searchAccount(account)
        }


        return null
    }

    private fun searchAccount(id: String): MessageRedis {
        val account: Account = accountRepository.findById(id).get()
        return mapToMessageRedis(account)
    }

    private fun mapToMessageRedis(
        account: Account
    ): MessageRedis {
        return MessageRedis(
            account = account.id.toString(),
            name = account.name.toString(),
            listWallet = hashMapOf(
                WalletEnum.FOOD.toString() to account.walletList.filter { it.type == WalletEnum.FOOD }.first().amount,
                WalletEnum.MEAL.toString() to account.walletList.filter { it.type == WalletEnum.MEAL }.first().amount,
                WalletEnum.CASH.toString() to account.walletList.filter { it.type == WalletEnum.CASH }.first().amount
            ),
            dateLastAccessed = LocalDateTime.now(),
        )

    }


}