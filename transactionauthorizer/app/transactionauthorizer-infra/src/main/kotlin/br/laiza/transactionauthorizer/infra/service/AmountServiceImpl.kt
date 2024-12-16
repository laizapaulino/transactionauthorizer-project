package br.laiza.transactionauthorizer.infra.service


import br.laiza.transactionauthorizer.core.entities.Account
import br.laiza.transactionauthorizer.core.entities.Wallet
import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.core.interfaces.AmountService
import br.laiza.transactionauthorizer.core.interfaces.RedisRepository
import br.laiza.transactionauthorizer.core.interfaces.repository.AccountRepository
import br.laiza.transactionauthorizer.core.message.TransactionMessage
import org.springframework.beans.factory.annotation.Autowired


import org.springframework.stereotype.Service

@Service
open class AmountServiceImpl(
    private val redisRepository: RedisRepository,
    @Autowired private val accountRepository: AccountRepository
) : AmountService {

    fun storeTransactionData(key: String, value: TransactionMessage) {
        redisRepository.saveDataTransaction(key, value)
    }

    override fun availableAmount(account: String): HashMap<String, Double> {
        var amountWallets: Map<String, Double>
        val list: List<TransactionMessage> = redisRepository.getTransactionData(account)

        if (!list.isEmpty()) {
            amountWallets = mapAmountWalletsFromMessage(list)
        } else {
            val account: Account = accountRepository.findById(account).get()
            amountWallets = mapAmountWalletsFromWallet(account.walletList)

        }

        return amountWallets as HashMap<String, Double>
    }

    private fun mapAmountWalletsFromMessage(
        list: List<TransactionMessage>
    ): Map<String, Double> {
        val lastTransaction: TransactionMessage = list.maxBy { it.transactionDate }
        return mapOf(
            WalletEnum.FOOD.toString() to lastTransaction.newAmountWalletFood,
            WalletEnum.MEAL.toString() to lastTransaction.newAmountWalletMeal,
            WalletEnum.CASH.toString() to lastTransaction.newAmountWalletCash
        )
    }

    private fun mapAmountWalletsFromWallet(
        list: List<Wallet>
    ): Map<String, Double> {
        return mapOf(
            WalletEnum.FOOD.toString() to list.filter { it.type == WalletEnum.FOOD }.first().amount,
            WalletEnum.MEAL.toString() to list.filter { it.type == WalletEnum.MEAL }.first().amount,
            WalletEnum.CASH.toString() to list.filter { it.type == WalletEnum.CASH }.first().amount,
        )
    }


}