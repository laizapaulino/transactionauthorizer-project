package br.laiza.transactionauthorizer.infra.messaging

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
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

        val sqsClient: SqsClient = SqsClient.builder()
            .endpointOverride(URI.create(endpointOverride))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")))
            .region(Region.SA_EAST_1)
            .build()


        return builder.build()
    }


}