package com.sjyt.dynamodb_try.config

import com.sjyt.dynamodb_try.model.Customer
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException

@Component
class DynamoDBInitializer(
    private val standardClient: DynamoDbClient,
    dynamoDbEnhancedClient: DynamoDbEnhancedClient,
    @Value("\${dynamodb.table.customer.name}")
    private val tableName: String,
) {
    private val customerTable: DynamoDbTable<Customer> = dynamoDbEnhancedClient
        .table(
            tableName, TableSchema.fromBean(Customer::class.java)
        )

    @PostConstruct
    fun initializeDynamoDB() {
        createCustomerTable()
    }

    private fun createCustomerTable() {
        try {
            customerTable.createTable { builder ->
                builder.provisionedThroughput { throughput ->
                    throughput
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build()
                }
            }

            waitForTableBecomeActive()
        } catch (error: ResourceInUseException) {
            println("Customer Table already exists...skip creating tables.")
        } catch (error: Exception) {
            println("Error creating Customer table")
        }
    }

    private fun waitForTableBecomeActive() {
        val waiter = standardClient.waiter()
        val request = DescribeTableRequest.builder()
            .tableName(tableName)
            .build()

        try {
            waiter.waitUntilTableExists(request)
        } catch (error: Exception) {
            throw RuntimeException("Table did not become active within the specified time", error)
        }
    }
}