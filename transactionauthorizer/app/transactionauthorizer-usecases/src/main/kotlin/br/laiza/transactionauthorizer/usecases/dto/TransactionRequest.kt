package br.laiza.transactionauthorizer.usecases.dto

import java.math.BigDecimal

class TransactionRequest(
    val account: String,
    var mcc: String,
    val totalAmount: BigDecimal,
    val merchant: String,
)