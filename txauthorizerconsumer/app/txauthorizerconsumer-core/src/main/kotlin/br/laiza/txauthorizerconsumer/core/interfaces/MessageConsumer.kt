package br.laiza.txauthorizerconsumer.core.interfaces

interface MessageConsumer {
    fun consumeMessage(event: String)
}