package br.laiza.transactionauthorizer.usecases.handler

import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.usecases.dto.TransactionRequest

class MccFromTransactionHandler : MccHandler {
    override fun handle(transaction: TransactionRequest, next: () -> WalletEnum?): WalletEnum? {
        val walletMCC = WalletEnum.fromMcc(transaction.mcc)
        return walletMCC
    }
}