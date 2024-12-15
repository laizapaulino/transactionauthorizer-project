package br.laiza.transactionauthorizer.core.interfaces

interface MessageProducer {
    fun produceMessage(messageBody: String, delaySeconds: Int = 0)
}