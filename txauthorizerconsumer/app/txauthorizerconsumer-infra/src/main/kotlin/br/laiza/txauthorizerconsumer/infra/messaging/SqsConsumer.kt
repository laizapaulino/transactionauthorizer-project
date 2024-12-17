package br.laiza.txauthorizerconsumer.infra.messaging


import br.laiza.txauthorizerconsumer.core.interfaces.MessageConsumer
import br.laiza.txauthorizerconsumer.core.interfaces.TransactionProcessor
import br.laiza.txauthorizerconsumer.core.message.TransactionMessage
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.messaging.listener.annotation.SqsListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.model.SqsException

@Service
class SqsConsumer(
    val processor: TransactionProcessor,
    val objectMapper: ObjectMapper,
    @Value("\${queue.transaction.name}") private val queueUrl: String?

): MessageConsumer {


    @SqsListener(value = ["\${queue.transaction.name}"])
    override fun consumeMessage(@Payload event: String) {
        try {
            var message: TransactionMessage =
                objectMapper.readValue(event, TransactionMessage::class.java)
            processor.process(message)

        } catch (e: SqsException) {
            println("Error processing the message: ${e.message}")

            throw e
        }
    }


}