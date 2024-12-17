package br.laiza.transactionauthorizer.rest


import br.laiza.transactionauthorizer.core.interfaces.AmountService
import br.laiza.transactionauthorizer.core.message.MessageRedis
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/fundsAccount")
class FundsController(
    private val amountService: AmountService,

    ) {
    @GetMapping("/{id}")
    fun findAccounts(
        @PathVariable id: String
    ): ResponseEntity<GenericResponse> {
        println("Start request: findAccounts ")
        try {
            var messageRedis: MessageRedis? = amountService.availableAmount(id)
            var response: GenericResponse = GenericResponse(
                data = messageRedis
            )
            return ResponseEntity.ok(response)

        } catch (exception: Exception) {
            println(exception.localizedMessage)
            return ResponseEntity.ok(
                GenericResponse(
                    data = {}
                )
            )
        }
    }
}

class GenericResponse(
    val data: Any?,
)