package br.laiza.transactionauthorizer.infra.repository

import br.laiza.transactionauthorizer.core.entities.Transaction
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TransactionRepository : JpaRepository<Transaction, String> {



    fun findByAccountId(account_id: String): List<Transaction>
}