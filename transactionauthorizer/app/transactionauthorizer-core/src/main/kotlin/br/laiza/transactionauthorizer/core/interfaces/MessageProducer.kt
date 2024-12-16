package br.laiza.transactionauthorizer.core.interfaces

import org.springframework.stereotype.Service

@Service
interface MessageProducer {
    fun produceMessage(messageBody: String, delaySeconds: Int = 0)
}