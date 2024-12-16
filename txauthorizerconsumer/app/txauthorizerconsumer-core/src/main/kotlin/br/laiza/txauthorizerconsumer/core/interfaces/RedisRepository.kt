package br.laiza.txauthorizerconsumer.core.interfaces

import br.laiza.txauthorizerconsumer.core.message.TransactionMessage


interface RedisRepository {
    fun getTransactionData(account: String): List<TransactionMessage>
    fun deleteData(key: String, transactionToRemove: TransactionMessage)
}