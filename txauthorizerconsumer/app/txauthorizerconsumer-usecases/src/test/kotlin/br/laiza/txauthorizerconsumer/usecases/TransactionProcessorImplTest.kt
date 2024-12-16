package br.laiza.txauthorizerconsumer.usecases


import br.laiza.transactionauthorizer.core.message.TransactionMessage
import br.laiza.txauthorizerconsumer.core.entities.Account
import br.laiza.txauthorizerconsumer.core.entities.Transaction
import br.laiza.txauthorizerconsumer.core.entities.Wallet
import br.laiza.txauthorizerconsumer.core.enums.WalletEnum
import br.laiza.txauthorizerconsumer.core.interfaces.RedisRepository
import br.laiza.txauthorizerconsumer.core.interfaces.repository.AccountRepository
import br.laiza.txauthorizerconsumer.core.interfaces.repository.TransactionRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*
import kotlin.test.BeforeTest


class TransactionProcessorImplTest {
    @InjectMocks
    lateinit var processor: TransactionProcessorImpl

    @Mock
    lateinit var accountRepository: AccountRepository

    @Mock
    lateinit var transactionRepository: TransactionRepository

    @Mock
    lateinit var redisRepository: RedisRepository




    @BeforeTest
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `should proccess`() {
        var account: Account = Account(
            id = "1234000012312",
            name = "cliente 123",
            email = "cliente@email.com",
            createdAt = LocalDateTime.now(),
            walletList = listOf(
            ),
            transactioList = listOf()
        )

        var walletList: List<Wallet> = listOf(
            Wallet(id = "12312", type = WalletEnum.CASH, amount = 10.0, account = account),
            Wallet(id = "12313", type = WalletEnum.FOOD, amount = 10.0, account = account),
            Wallet(id = "12314", type = WalletEnum.MEAL, amount = 10.0, account = account)
        )
        account.walletList = walletList


        whenever(accountRepository.findById(any())).thenReturn(Optional.of(account))
        whenever(accountRepository.save(any())).thenReturn(account)
        val t = Transaction(
            id = "",
            account = account,
            totalAmount = 10.0,
            merchant = "PADARIA ",
            mcc = "1234",
            transactionDate = LocalDateTime.now()
        )
        whenever(transactionRepository.save(any())).thenReturn(t)


        val request = TransactionMessage(
            account = "1234",
            mcc = "5412",
            amountTransaction = 10.00,
            transactionDate = LocalDateTime.now(),
            newAmountWalletFood = 10.00,
            newAmountWalletMeal = 20.00,
            newAmountWalletCash = 20.00,
            merchant = "PADARIA ABC"
        )
        assertDoesNotThrow {
            processor.process(
                message = request
            )
        }


    }
}