package br.laiza.transactionauthorizer.core.entities

import br.laiza.transactionauthorizer.core.enums.WalletEnum
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

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
    @JsonIgnore
    val account: Account
) {
    override fun toString(): String {
        return "Wallet(id=$id)"
    }
}
