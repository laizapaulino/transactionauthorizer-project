package br.laiza.transactionauthorizer.infra.messaging

import br.laiza.transactionauthorizer.core.interfaces.MessageProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import software.amazon.awssdk.services.sqs.model.SqsException

@Component
class SqsProducer(
    val sqsClient: SqsClient,
    @Value("\${queue.transaction.name}") private val queueUrl: String?

) : MessageProducer {

    override fun produceMessage(messageBody: String, delaySeconds: Int) {
        try {
            val request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .delaySeconds(delaySeconds)
                .build()

            val response = sqsClient.sendMessage(request)
            println("Message sent! MessageId: ${response.messageId()}")
        } catch (e: SqsException) {
            println("Error message not sent: ${e.message}")
            throw e
        }
    }
}