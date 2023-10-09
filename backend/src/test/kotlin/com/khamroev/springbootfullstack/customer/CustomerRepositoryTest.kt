package com.khamroev.springbootfullstack.customer

import com.khamroev.springbootfullstack.AbstractTestcontainers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.ApplicationContext
import java.util.*

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest : AbstractTestcontainers() {

    @Autowired
    private lateinit var underTest: CustomerRepository

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @BeforeEach
    fun setUp() {
        println(applicationContext.beanDefinitionCount)
        println("Debug")
    }

    @Test
    fun existsByEmail() {
        // Given
        val customer = generateCustomer()
        underTest.save(customer)
        // When
        val actual = underTest.existsByEmail(customer.email)
        // Then
        assertThat(actual).isTrue
    }

    @Test
    fun `existsByEmail fails when email not present`() {
        // Given
        val customer = generateCustomer()
        // When
        val actual = underTest.existsByEmail(customer.email)
        // Then
        assertThat(actual).isFalse
    }

    private fun generateCustomer(): Customer {
        return Customer(
            FAKER.name().fullName(),
            FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID(),
            20
        )
    }
}