package br.laiza.txauthorizerconsumer.usecases


import br.laiza.txauthorizerconsumer.core.entities.Account
import br.laiza.txauthorizerconsumer.core.entities.Transaction
import br.laiza.txauthorizerconsumer.core.entities.Wallet
import br.laiza.txauthorizerconsumer.core.enums.WalletEnum
import br.laiza.txauthorizerconsumer.core.exception.TransactionProcessorException
import br.laiza.txauthorizerconsumer.core.interfaces.TransactionProcessor
import br.laiza.txauthorizerconsumer.core.interfaces.mappers.TransactionMapper
import br.laiza.txauthorizerconsumer.core.interfaces.repository.AccountRepository
import br.laiza.txauthorizerconsumer.core.interfaces.repository.TransactionRepository
import br.laiza.txauthorizerconsumer.core.message.TransactionMessage
import org.springframework.stereotype.Service

@Service
class TransactionProcessorImpl(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
) : TransactionProcessor {

    //    @Transactional(propagation = Propagation.REQUIRED)
    override fun process(message: TransactionMessage) {

        var account: Account = accountRepository.findById(message.account.toString()).get()

        var accountUpdated: Account = updateWallet(account, message)

        var entityTransaction: Transaction = TransactionMapper.toEntity(message, account)

        try {
            accountRepository.save(accountUpdated)
            transactionRepository.save(entityTransaction)

        } catch (exception: Exception) {
            print("The message will be sent to DLQ")
            throw TransactionProcessorException(exception.localizedMessage)
        }

    }

    private fun updateWallet(
        account: Account,
        message: TransactionMessage
    ): Account {
        var accountUpdated: Account = account
        accountUpdated.walletList.map { wallet: Wallet ->
            when (wallet.type) {
                WalletEnum.CASH -> wallet.copy(amount = message.newAmountWalletCash)
                WalletEnum.FOOD -> wallet.copy(amount = message.newAmountWalletFood)
                WalletEnum.MEAL -> wallet.copy(amount = message.newAmountWalletMeal)
                else -> wallet
            }
        }
        return accountUpdated
    }
}


