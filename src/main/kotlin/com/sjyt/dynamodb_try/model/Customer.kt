package com.sjyt.dynamodb_try.model

import com.sjyt.dynamodb_try.dto.CustomerCreateDto
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.time.Instant

@DynamoDbBean
data class Customer(
    @get:DynamoDbPartitionKey
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var address: String = "",
    val registrationDate: Instant = Instant.now(),
) {
    companion object {
        fun buildFrom(dto: CustomerCreateDto): Customer {
            return Customer(dto.id, dto.name, dto.email, dto.address)
        }
    }
}
