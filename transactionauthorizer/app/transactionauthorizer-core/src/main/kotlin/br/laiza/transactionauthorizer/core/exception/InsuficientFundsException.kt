package br.laiza.transactionauthorizer.core.exception

class InsuficientFundsException(override val message: String) : RuntimeException(message)