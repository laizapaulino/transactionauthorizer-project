package br.laiza.transactionauthorizer.core.message

import java.time.LocalDateTime

data class TransactionMessage(
    val account: String,
    val transactionDate: LocalDateTime,
    val amountTransaction: Double,
    val newAmountWalletFood: Double,
    val newAmountWalletMeal: Double,
    val newAmountWalletCash: Double,
    val mcc: String,
    val merchant: String,
)