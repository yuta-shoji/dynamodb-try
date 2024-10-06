package com.sjyt.dynamodb_try.dto

data class CustomerCreateDto(
    var id: String,
    var name: String,
    var email: String,
    var address: String,
)