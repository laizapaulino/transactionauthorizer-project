package br.laiza.txauthorizerconsumer.core.interfaces.mappers


import br.laiza.transactionauthorizer.core.message.TransactionMessage
import br.laiza.txauthorizerconsumer.core.entities.Account
import br.laiza.txauthorizerconsumer.core.entities.Transaction
import java.util.*


class TransactionMapper {
    companion object {
        fun toEntity(message: TransactionMessage, account: Account): Transaction {
            return Transaction(
                id = UUID.randomUUID().toString(),
                account = account,
                totalAmount = message.amountTransaction,
                merchant = message.merchant,
                mcc = message.mcc,
                transactionDate = message.transactionDate
            )

        }
    }


}