package br.laiza.transactionauthorizer.rest


import br.laiza.transactionauthorizer.core.interfaces.repository.AccountRepository

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/accounts")
class AccountController(
    private val accountRepository: AccountRepository
) {
    @GetMapping
    fun findAccounts(

    ): ResponseEntity<AccountResponse> {
        println("Start request ")
        try {
            var response: AccountResponse = AccountResponse(
                data = listOf(
                    accountRepository.findAll()
                )
            )
            return ResponseEntity.ok(response)

        } catch (exception: Exception) {
            return ResponseEntity.ok(
                AccountResponse(
                    data = listOf()
                )
            )
        }


    }
}

class AccountResponse(
    val data: List<Any>,
)