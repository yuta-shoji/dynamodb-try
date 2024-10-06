package com.sjyt.dynamodb_try.config

import com.sjyt.dynamodb_try.model.Customer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
class DynamoDBConfig(
    @Value("\${dynamodb.endpoint}")
    private val endpoint: String,
) {
    @Bean
    fun dynamoDBClient(): DynamoDbClient {

        return DynamoDbClient.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(Region.US_EAST_1.toString()))
            .credentialsProvider(DefaultCredentialsProvider.create())
//            .credentialsProvider(StaticCredentialsProvider.create(
//                AwsBasicCredentials.create("dummy-access-key", "dummy-secret-key")
//            ))
            .build()
    }

    @Bean
    fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }

    @Bean
    fun customerTable(
        enhancedClient: DynamoDbEnhancedClient
    ): DynamoDbTable<Customer> {
        return enhancedClient.table("Customer", TableSchema.fromBean(Customer::class.java))
    }
}