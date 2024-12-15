package br.laiza.txauthorizerconsumer.infra.messaging



import br.laiza.transactionauthorizer.core.message.TransactionMessage
import br.laiza.txauthorizerconsumer.core.exception.TransactionProcessorException
import br.laiza.txauthorizerconsumer.core.interfaces.MessageConsumer
import br.laiza.txauthorizerconsumer.core.interfaces.MessageProducer
import br.laiza.txauthorizerconsumer.core.interfaces.TransactionProcessor


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import software.amazon.awssdk.services.sqs.model.SqsException

@Component
@EnableScheduling
class SqsConsumer(
    val sqsClient: SqsClient,
    val processor: TransactionProcessor,
    val producerDLQ: MessageProducer,
    val objectMapper: ObjectMapper,
    @Value("\${queue.transaction.name}") private val queueUrl: String?

) : MessageConsumer {

    @Scheduled(fixedRate = 1000)
    override fun consumeMessages() {
        try {
            val messages = receiveMessagesFromQueue()
            processMessage(messages)

        } catch (e: SqsException) {
            println("Erro ao processar mensagem: ${e.message}")


            throw e
        }
    }


    private fun processMessage(messages: MutableList<Message>?) {
        if (messages != null) {

            messages.forEach {
                val transactionMessage: TransactionMessage = objectMapper.readValue(it.body())
                try {
                    processor.process(transactionMessage)
                    deleteMessageFromQueue(it)

                } catch (exception: TransactionProcessorException) {
                    producerDLQ.produceMessage(it.body())
                }

            }

        }

    }


    private fun receiveMessagesFromQueue(): MutableList<Message>? {
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(10)
            .waitTimeSeconds(10)
            .build();

        val result = sqsClient.receiveMessage(receiveMessageRequest)
        return result.messages()
    }

    private fun deleteMessageFromQueue(message: Message) {
        val deleteMessageRequest = DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(message.receiptHandle())
            .build()

        sqsClient.deleteMessage(deleteMessageRequest)
        println("Mensagem deletada com sucesso: ${message.messageId()}")
    }


}