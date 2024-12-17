package br.laiza.transactionauthorizer.core.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account,

    @Column(nullable = false)
    var totalAmount: BigDecimal,

    @Column(nullable = false)
    var merchant: String,

    @Column(nullable = false)
    var mcc: String,

    @Column(nullable = false)
    val transactionDate: LocalDateTime,
) {
    override fun toString(): String {
        return "Transaction(id=$id)"
    }
}