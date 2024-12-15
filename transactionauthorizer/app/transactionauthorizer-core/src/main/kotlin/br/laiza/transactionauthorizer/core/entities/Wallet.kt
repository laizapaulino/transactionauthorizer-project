package br.laiza.transactionauthorizer.core.entities

import br.laiza.transactionauthorizer.core.enums.WalletEnum
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "wallet")
 data class Wallet(
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    val id: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: WalletEnum,

    @Column(nullable = false)
    var amount: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account
)
