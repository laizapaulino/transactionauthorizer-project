package br.laiza.transactionauthorizer.usecases.dto

class TransactionRequest(
    val account: String,
    var mcc: String,
    val totalAmount: Double,
    val merchant: String,
)