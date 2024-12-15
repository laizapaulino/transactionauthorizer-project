package br.laiza.transactionauthorizer.core.enums

import java.util.*

enum class WalletEnum {
    FOOD,
    MEAL,
    CASH;

    companion object {
        fun fromMcc(mcc: String): WalletEnum {
            return when (mcc) {
                "5411", "5412" -> FOOD
                "5811", "5812" -> MEAL
                else -> CASH
            }
        }

        fun fromMerchantName(name: String): WalletEnum {
            val nameUpper = name.uppercase(Locale.getDefault())
            return when {
                listOf("MERCADO", "MARKET", "FOOD", "ACOUGUE").any { nameUpper.contains(it) } -> FOOD
                listOf("PADARIA", "BAR", "BAKERY", "RESTAURANT", "EATS", "LANCH").any { nameUpper.contains(it) } -> MEAL
                else -> CASH
            }
        }
    }
}