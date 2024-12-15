package br.laiza.txauthorizerconsumer.core.interfaces

import br.laiza.transactionauthorizer.core.message.TransactionMessage


interface TransactionProcessor {
    fun process(message: TransactionMessage)

}