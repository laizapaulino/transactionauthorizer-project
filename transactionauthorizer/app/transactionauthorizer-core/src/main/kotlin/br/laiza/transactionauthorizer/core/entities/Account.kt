package br.laiza.transactionauthorizer.core.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    val id: String,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var accountList: List<Wallet> = listOf(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var transactioList: List<Transaction> = listOf()


)