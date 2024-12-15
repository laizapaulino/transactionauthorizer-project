package br.laiza.transactionauthorizer.infra.repository

import br.laiza.transactionauthorizer.core.entities.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, String>