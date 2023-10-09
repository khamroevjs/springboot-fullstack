package com.khamroev.springbootfullstack.customer

data class CustomerUpdateRequest (
    val name: String?,
    val email: String?,
    val age: Int?
)