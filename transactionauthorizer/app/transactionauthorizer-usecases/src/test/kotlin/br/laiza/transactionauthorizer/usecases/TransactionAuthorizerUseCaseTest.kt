package br.laiza.transactionauthorizer.usecases


import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.core.exception.InsuficientFundsException
import br.laiza.transactionauthorizer.core.interfaces.AmountService
import br.laiza.transactionauthorizer.core.interfaces.MessageProducer
import br.laiza.transactionauthorizer.core.interfaces.RedisRepository
import br.laiza.transactionauthorizer.usecases.dto.TransactionRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import kotlin.test.BeforeTest

class TransactionAuthorizerUseCaseTest {

    @InjectMocks
    lateinit var transaction: TransactionAuthorizerUseCase

    @Mock
    lateinit var amountService: AmountService

    @Mock
    lateinit var redisRepository: RedisRepository

    @Mock
    lateinit var messageProducer: MessageProducer

    @Mock
    lateinit var objectMapper: ObjectMapper


    @BeforeTest
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }


    @ParameterizedTest
    @CsvSource(
        "10.0, 10.0, 10.0, 10.0, PADARIA ABC, 5412",  //  funds from 1 wallet
        "10.0, 10.0, 10.0, 10.0, ABC, 5412",  //  funds from 1 wallet
        "10.0, 10.0, 10.0, 20.0, ABC, 5412",  //  funds from 2 wallet
        "10.0, 10.0, 10.0, 5.0, BOUTIQUE FASHION, 9999",  //  funds from 2 wallet
    )
    fun `should proccess authorization`(
        foodWallet: Double, mealWallet: Double, cashWallet: Double, amount: Double,
        merchantName: String, mcc: String
    ) {

        whenever(amountService.availableAmount(any())).thenReturn(
            hashMapOf(
                WalletEnum.FOOD.toString() to foodWallet,
                WalletEnum.MEAL.toString() to mealWallet,
                WalletEnum.CASH.toString() to cashWallet
            )
        )

        whenever(objectMapper.writeValueAsString(any())).thenReturn("{\"key\":\"value\"}")


        doNothing().whenever(messageProducer).produceMessage(any(), any())

        val request = TransactionRequest(
            account = "1234",
            mcc = mcc,
            totalAmount = amount,
            merchant = merchantName
        )
        assertDoesNotThrow {
            transaction.authorize(request)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "PADARIA ABC, 5412",
        "BOUTIQUE FASHION, 9999",
    )
    fun `should proccess authorization with no funds`(merchantName: String, mcc: String) {

        whenever(amountService.availableAmount(any())).thenReturn(
            hashMapOf(
                WalletEnum.FOOD.toString() to 10.0,
                WalletEnum.MEAL.toString() to 10.0,
                WalletEnum.CASH.toString() to 10.0
            )
        )
        whenever(objectMapper.writeValueAsString(any())).thenReturn("{\"key\":\"value\"}")

        val request = TransactionRequest(
            account = "1234",
            mcc = mcc,
            totalAmount = 21.00,
            merchant = merchantName
        )
        assertThrows<InsuficientFundsException> { transaction.authorize(request) }

    }


}