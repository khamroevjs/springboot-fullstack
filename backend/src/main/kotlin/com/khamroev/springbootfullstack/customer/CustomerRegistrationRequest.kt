package com.khamroev.springbootfullstack.customer

data class CustomerRegistrationRequest(
    val name: String,
    val email: String,
    val age: Int
)
