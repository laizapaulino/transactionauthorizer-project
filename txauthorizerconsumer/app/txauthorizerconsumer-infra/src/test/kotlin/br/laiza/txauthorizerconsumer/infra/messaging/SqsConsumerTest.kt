package br.laiza.txauthorizerconsumer.infra.messaging


import br.laiza.txauthorizerconsumer.core.interfaces.MessageProducer
import br.laiza.txauthorizerconsumer.core.interfaces.TransactionProcessor
import br.laiza.txauthorizerconsumer.core.message.TransactionMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.math.BigDecimal
import java.time.LocalDateTime

class SqsConsumerTest {

    @Mock
    private lateinit var sqsClient: SqsClient

    @Mock
    private lateinit var processor: TransactionProcessor

    @Mock
    private lateinit var producerDLQ: MessageProducer

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @InjectMocks
    private lateinit var sqsConsumer: SqsConsumer

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

    }

    fun transactionMessage(): TransactionMessage {
        return TransactionMessage(
            "12345",
            LocalDateTime.now(),
            BigDecimal.valueOf(100.0),
            BigDecimal.valueOf(100.0),
            BigDecimal.valueOf(100.0),
            BigDecimal.valueOf(100.0),
            "1231",
            "Loja"
        )
    }

    @Test
    fun `test consumeMessages successfully processes messages`() {
        val mockMessage = mock(Message::class.java)
        val messageBody = """{"transactionId":"12345","amount":100.0}"""
        `when`(mockMessage.body()).thenReturn(messageBody)

        `when`(sqsClient.receiveMessage(any(ReceiveMessageRequest::class.java)))
            .thenReturn(mockReceiveMessageResult(mockMessage))


        `when`(objectMapper.readValue(messageBody, TransactionMessage::class.java))
            .thenReturn(transactionMessage())

        sqsConsumer.consumeMessages()


        val deleteMessageCaptor = argumentCaptor<DeleteMessageRequest>()
        verify(sqsClient).deleteMessage(deleteMessageCaptor.capture())
        assert(deleteMessageCaptor.firstValue.receiptHandle() == mockMessage.receiptHandle())
    }


    private fun mockReceiveMessageResult(vararg messages: Message): software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse {
        return software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse.builder()
            .messages(messages.toList())
            .build()
    }
}
