package com.khamroev.springbootfullstack.customer

import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Int> {
    fun existsByEmail(email: String): Boolean
}
