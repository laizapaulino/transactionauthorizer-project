package br.laiza.transactionauthorizer.core.interfaces.repository

import br.laiza.transactionauthorizer.core.entities.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
open interface AccountRepository : JpaRepository<Account, String>