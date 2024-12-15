package br.laiza.transactionauthorizer.core.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.time.LocalDateTime

data class TransactionMessage(
    val account: String,
    val transactionDate: LocalDateTime,
    val amountTransaction: Double,
    val newAmountWalletFood: Double,
    val newAmountWalletMeal: Double,
    val newAmountWalletCash: Double,
    val mcc: String
)