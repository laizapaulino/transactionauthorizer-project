package br.laiza.transactionauthorizer.core.interfaces

import br.laiza.transactionauthorizer.core.message.MessageRedis


interface RedisRepository {
    fun saveDataTransaction(key: String, message: MessageRedis)
    fun getTransactionData(account: String): MessageRedis?
}