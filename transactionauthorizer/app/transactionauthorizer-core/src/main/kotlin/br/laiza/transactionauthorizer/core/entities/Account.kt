package br.laiza.transactionauthorizer.core.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "account")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    var id: String? = null,

    @Column(unique = true, nullable = false)
    var name: String? = null,

    @Column(nullable = false)
    var email: String? = null,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var walletList: List<Wallet> = listOf(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var transactioList: List<Transaction> = listOf()
) {

    // Construtor padrão sem argumentos para o Hibernate
    constructor() : this(id = null, name = null, email = null)

    override fun toString(): String {
        return "Account(id=$id)"
    }
}
