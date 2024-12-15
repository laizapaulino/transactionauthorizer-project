package br.laiza.transactionauthorizer.infra.repository

import br.laiza.transactionauthorizer.core.interfaces.RedisRepository
import br.laiza.transactionauthorizer.core.message.TransactionMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
open class RedisRepositoryImpl @Autowired constructor(
    private val redisTemplate: RedisTemplate<String, String>, private val objectMapper: ObjectMapper
) : RedisRepository {

    override fun saveDataTransaction(key: String, transaction: TransactionMessage) {
        redisTemplate.opsForList().rightPush("account:$key", objectMapper.writeValueAsString(transaction))
    }

    override fun getTransactionData(account: String): List<TransactionMessage> {

        val transactionsJson = redisTemplate.opsForList().range("account:$account", 0, -1)
        if (transactionsJson.isEmpty()) {
            return listOf()
        }


        return transactionsJson.map { objectMapper.readValue(it, TransactionMessage::class.java) }
    }

    override fun deleteData(key: String, transactionToRemove: TransactionMessage) {
        objectMapper.registerModule(JavaTimeModule())
        val transactionJson = objectMapper.writeValueAsString(transactionToRemove)
        redisTemplate.opsForList().remove("account:$key", 1, transactionJson)
    }



}




