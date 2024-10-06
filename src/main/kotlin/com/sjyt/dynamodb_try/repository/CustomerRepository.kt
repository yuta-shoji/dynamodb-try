package com.sjyt.dynamodb_try.repository

import com.sjyt.dynamodb_try.model.Customer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import java.util.*
import java.util.stream.Collectors

interface CustomerRepository {
    fun save(customer: Customer): Customer
    fun findById(id: String): Optional<Customer>
    fun findAll(): List<Customer>
    fun delete(id: String)
}

@Repository
class DefaultCustomerRepository(
    @Value("\${dynamodb.table.customer.name}")
    private val tableName: String,
    @Autowired private val enhancedClient: DynamoDbEnhancedClient,
) : CustomerRepository {
    private val customerTable: DynamoDbTable<Customer> = enhancedClient
        .table(
            tableName, TableSchema.fromBean(Customer::class.java)
        )

    override fun save(customer: Customer): Customer {
        customerTable.putItem(customer)
        return customer
    }

    override fun findById(id: String): Optional<Customer> {
        val key = Key.builder()
            .partitionValue(id)
            .build()
        return Optional.ofNullable(customerTable.getItem(key))
    }

    override fun findAll(): List<Customer> {
        return customerTable.scan().items().stream().collect(Collectors.toList())
    }

    override fun delete(id: String) {
        val key = Key.builder()
            .partitionValue(id)
            .build()
        customerTable.deleteItem(key)
    }
}