package br.laiza.transactionauthorizer.usecases.handler



import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.usecases.dto.TransactionRequest

interface MccHandler {
    fun handle(transaction: TransactionRequest, next: () -> WalletEnum?): WalletEnum?
}