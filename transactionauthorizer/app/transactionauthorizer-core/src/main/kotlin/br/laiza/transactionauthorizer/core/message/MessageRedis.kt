package br.laiza.transactionauthorizer.core.message

import br.laiza.transactionauthorizer.core.enums.WalletEnum
import java.math.BigDecimal
import java.time.LocalDateTime

data class MessageRedis(
    var account: String? = null,
    var name: String? = null,
    var dateLastAccessed: LocalDateTime?,
    var listWallet: MutableMap<String, BigDecimal> = mutableMapOf(
        WalletEnum.FOOD.toString() to BigDecimal.valueOf(0.00),
        WalletEnum.MEAL.toString() to BigDecimal.valueOf(0.00),
        WalletEnum.CASH.toString() to BigDecimal.valueOf(0.00)
    )
)