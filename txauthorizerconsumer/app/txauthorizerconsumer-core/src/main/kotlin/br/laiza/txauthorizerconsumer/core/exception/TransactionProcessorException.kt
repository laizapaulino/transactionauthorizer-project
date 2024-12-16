package br.laiza.txauthorizerconsumer.core.exception

class TransactionProcessorException(override val message: String) : RuntimeException(message) {
}