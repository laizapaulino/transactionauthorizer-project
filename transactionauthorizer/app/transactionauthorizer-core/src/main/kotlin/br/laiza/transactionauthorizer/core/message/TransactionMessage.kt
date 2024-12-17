package br.laiza.transactionauthorizer.core.message

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionMessage(
    val account: String,
    val transactionDate: LocalDateTime,
    val amountTransaction: BigDecimal,
    val newAmountWalletFood: BigDecimal,
    val newAmountWalletMeal: BigDecimal,
    val newAmountWalletCash: BigDecimal,
    val mcc: String,
    val merchant: String,
)

