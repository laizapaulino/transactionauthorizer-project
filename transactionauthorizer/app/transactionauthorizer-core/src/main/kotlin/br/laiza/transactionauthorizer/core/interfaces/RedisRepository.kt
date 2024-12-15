package br.laiza.transactionauthorizer.core.interfaces

import br.laiza.transactionauthorizer.core.message.TransactionMessage

interface RedisRepository {
    fun saveDataTransaction(key: String, transaction: TransactionMessage)
    fun getTransactionData(account: String): List<TransactionMessage>
    fun deleteData(key: String, transactionToRemove: TransactionMessage)
}