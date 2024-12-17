package br.laiza.txauthorizerconsumer.infra.messaging.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory
import io.awspring.cloud.messaging.listener.support.AcknowledgmentHandlerMethodArgumentResolver
import io.awspring.cloud.messaging.listener.support.VisibilityHandlerMethodArgumentResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
open class SqsClientConfigLocal {

    @Value("\${localstack.url}")
    lateinit var endpointOverride: String

    @Value("\${localstack.accessKey}")
    lateinit var accessKey: String

    @Value("\${localstack.secreteKey}")
    lateinit var secreteKey: String

    private val region: String = "sa-east-1"


    @Bean
    @Primary
    open fun amazonSQSLocal(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secreteKey)))
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpointOverride, region))
//            .withRequestHandlers(RequestHandler2())
            .build()
    }

    @Bean
    @Primary
    open fun simpleMessageListenerContainerFactory(
        mapper: ObjectMapper,
        amazonSQSAsync: AmazonSQSAsync
    ): QueueMessageHandlerFactory {
        val acknowledgment = AcknowledgmentHandlerMethodArgumentResolver("Acknowledgement")
        val visibility = VisibilityHandlerMethodArgumentResolver("Visibility")

        val queueHandlerFactory = QueueMessageHandlerFactory()
        queueHandlerFactory.setAmazonSqs(amazonSQSAsync)
        queueHandlerFactory.setArgumentResolvers(
            listOf(
                acknowledgment, visibility
//            ,PayloadMethodArgumentResolver(mappinJackson2)
            )
        )
        return queueHandlerFactory
    }

}