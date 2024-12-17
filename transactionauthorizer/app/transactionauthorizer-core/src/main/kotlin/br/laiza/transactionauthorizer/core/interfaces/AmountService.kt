package br.laiza.transactionauthorizer.core.interfaces

import br.laiza.transactionauthorizer.core.message.MessageRedis

interface AmountService {
    fun availableAmount(account: String): MessageRedis?
}