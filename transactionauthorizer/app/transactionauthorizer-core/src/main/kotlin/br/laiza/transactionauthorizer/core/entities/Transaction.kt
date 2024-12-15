package br.laiza.transactionauthorizer.core.entities

import br.laiza.transactionauthorizer.core.enums.StatusEnum
import jakarta.persistence.*
import java.util.*

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
    var totalAmount: Double,

    @Column(nullable = false)
    var merchant: String,


    @Column(nullable = false)
    var status: StatusEnum,


    )