package br.laiza.txauthorizerconsumer.infra.messaging


import br.laiza.txauthorizerconsumer.core.interfaces.MessageProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import software.amazon.awssdk.services.sqs.model.SqsException

@Component
class SqsDLQProducer(
    val sqsClient: SqsClient,
    @Value("\${queue.transaction.dlq.name}") private val queueDLQUrl: String?
) : MessageProducer {

    override fun produceMessage(messageBody: String, delaySeconds: Int) {
        try {
            val request = SendMessageRequest.builder()
                .queueUrl(queueDLQUrl)
                .messageBody(messageBody)
                .delaySeconds(delaySeconds)
                .build()

            val response = sqsClient.sendMessage(request)
            println("Mensagem enviada com sucesso para DLQ! MessageId: ${response.messageId()}")
        } catch (e: SqsException) {
            println("Erro ao enviar mensagem: ${e.message}")
            throw e
        }
    }
}