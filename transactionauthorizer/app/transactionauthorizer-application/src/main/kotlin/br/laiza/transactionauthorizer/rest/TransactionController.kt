package br.laiza.transactionauthorizer.rest


import br.laiza.transactionauthorizer.core.exception.InsuficientFundsException
import br.laiza.transactionauthorizer.usecases.TransactionAuthorizerUseCase
import br.laiza.transactionauthorizer.usecases.dto.TransactionRequest
import br.laiza.transactionauthorizer.usecases.dto.TransactionResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionAuthorizerUseCase: TransactionAuthorizerUseCase
) {
    @PostMapping
    fun createTransaction(
        @RequestBody request: TransactionRequest
    ): ResponseEntity<TransactionResponse> {
        println("Start request " + request.toString())
        var response: TransactionResponse
        try {
            transactionAuthorizerUseCase.authorize(request)
            response = TransactionResponse(
                code = "00"
            )
        } catch (exception: InsuficientFundsException) {
            response = TransactionResponse(
                code = "51"
            )
        } catch (exception: Exception) {
            print(exception.localizedMessage)
            response = TransactionResponse(
                code = "07"
            )
        }

        return ResponseEntity.ok(response)

    }
}