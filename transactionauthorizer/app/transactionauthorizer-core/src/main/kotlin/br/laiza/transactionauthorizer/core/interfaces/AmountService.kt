package br.laiza.transactionauthorizer.core.interfaces

interface AmountService {
    fun availableAmount(account: String): HashMap<String, Double>
}