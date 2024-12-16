package br.laiza.txauthorizerconsumer.core.interfaces

import org.springframework.stereotype.Service

@Service
interface MessageProducer {
    fun produceMessage(messageBody: String, delaySeconds: Int = 0)
}