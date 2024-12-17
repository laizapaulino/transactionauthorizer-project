package br.laiza.transactionauthorizer.infra.repository


import br.laiza.transactionauthorizer.core.interfaces.RedisRepository
import br.laiza.transactionauthorizer.core.message.MessageRedis
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
open class RedisRepositoryImpl @Autowired constructor(
    private val redisTemplate: RedisTemplate<String, String>, private val objectMapper: ObjectMapper
) : RedisRepository {

    override fun saveDataTransaction(key: String, transaction: MessageRedis) {
        redisTemplate.opsForValue().set("account:$key", objectMapper.writeValueAsString(transaction))
    }

    override fun getTransactionData(account: String): MessageRedis? {

        var transactionsJson =
            redisTemplate.keys("account:$account").associateWith { redisTemplate.opsForValue().get(it) }

        if (transactionsJson.isEmpty()) {
            return null
        }

        return transactionsJson.map { objectMapper.readValue(it.value, MessageRedis::class.java) }.first()
    }

    fun clearRedis() {
        redisTemplate.connectionFactory?.connection?.flushAll()
    }


}




