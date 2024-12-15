package br.laiza.transactionauthorizer.usecases.handler


import br.laiza.transactionauthorizer.core.enums.WalletEnum
import br.laiza.transactionauthorizer.usecases.dto.TransactionRequest

class MccFromMerchantHandler : MccHandler {
    override fun handle(transaction: TransactionRequest, next: () -> WalletEnum?): WalletEnum? {
        val walletName = WalletEnum.fromMerchantName(transaction.merchant)

        return if (walletName != WalletEnum.CASH) walletName else next()
    }
}