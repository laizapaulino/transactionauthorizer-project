package br.laiza.txauthorizerconsumer.core.interfaces

import br.laiza.txauthorizerconsumer.core.message.TransactionMessage


interface TransactionProcessor {
    fun process(message: TransactionMessage)

}