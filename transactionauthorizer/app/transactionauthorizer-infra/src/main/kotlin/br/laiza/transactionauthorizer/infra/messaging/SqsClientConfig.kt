package br.laiza.transactionauthorizer.infra.messaging

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
open class SqsClientConfig {

    @Value("\${queue.endpoint}")
    lateinit var endpointOverride: String

    private val region: String = "sa-east-1"


    @Bean
    open fun createSqsClient(): SqsClient {
        val builder = SqsClient.builder()
            .region(Region.of(region))


        if (endpointOverride != null) {
            builder.endpointOverride(URI.create(endpointOverride))
        }

        return builder.build()
    }


}