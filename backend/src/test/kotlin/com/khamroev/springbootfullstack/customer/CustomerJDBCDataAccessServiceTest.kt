package com.khamroev.springbootfullstack.customer

import com.khamroev.springbootfullstack.AbstractTestcontainers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class CustomerJDBCDataAccessServiceTest : AbstractTestcontainers() {
    private lateinit var underTest: CustomerJDBCDataAccessService
    private val rowMapper: CustomerRowMapper = CustomerRowMapper()

    @BeforeEach
    fun setUp() {
        underTest = CustomerJDBCDataAccessService(getJdbcTemplate(), rowMapper)
    }

    @Test
    fun selectAllCustomers() {
        // Given
        val customer = generateCustomer()
        underTest.insertCustomer(customer)
        // When
        val customers = underTest.selectAllCustomers()
        // Then
        assertThat(customers).isNotEmpty()
    }

    @Test
    fun selectCustomerById() {
        // Given
        val customer = generateCustomer()
        underTest.insertCustomer(customer)
        // When
        val actual = underTest.selectCustomerById(customer.id)
        // Then
        assertThat(actual).isNotNull
            .hasFieldOrPropertyWithValue("id", customer.id)
            .hasFieldOrPropertyWithValue("name", customer.name)
            .hasFieldOrPropertyWithValue("email", customer.email)
            .hasFieldOrPropertyWithValue("age", customer.age)
    }

    @Test
    fun `will return null when select customer by id`() {
        // Given
        val id = -1
        // When
        val actual = underTest.selectCustomerById(id)
        // Then
        assertThat(actual).isNull()
    }

    @Test
    fun insertCustomer() {
        // Given
        val customer = generateCustomer()
        // When
        val actual = underTest.insertCustomer(customer)
        // Then
        assertThat(actual).isNotEqualTo(0)
    }

    @Test
    fun existsCustomerWithEmail() {
        // Given
        val customer = generateCustomer()
        underTest.insertCustomer(customer)
        // When
        val actual = underTest.existsCustomerWithEmail(customer.email)
        // Then
        assertThat(actual).isTrue
    }

    @Test
    fun existsCustomerWithEmail_false() {
        // Given
        val customer = generateCustomer()
        // When
        val actual = underTest.existsCustomerWithEmail(customer.email)
        // Then
        assertThat(actual).isFalse
    }

    @Test
    fun existsCustomerWithId() {
        // Given
        val customer = generateCustomer()
        underTest.insertCustomer(customer)
        // When
        val actual = underTest.existsCustomerWithId(customer.id)
        // Then
        assertThat(actual).isTrue
    }

    @Test
    fun deleteCustomerById() {
        // Given
        val customer = generateCustomer()
        underTest.insertCustomer(customer)
        // When
        underTest.deleteCustomerById(customer.id)
        val actual = underTest.existsCustomerWithId(customer.id)
        // Then
        assertThat(actual).isFalse
    }

    @Test
    fun updateCustomer() {
        // Given
        val customer = generateCustomer()
        underTest.insertCustomer(customer)
        // When
        customer.name = FAKER.name().fullName()
        underTest.updateCustomer(customer)
        // Then
        val actual = underTest.selectCustomerById(customer.id)
        assertThat(actual).isNotNull
            .hasFieldOrPropertyWithValue("id", customer.id)
            .hasFieldOrPropertyWithValue("name", customer.name)
            .hasFieldOrPropertyWithValue("email", customer.email)
            .hasFieldOrPropertyWithValue("age", customer.age)
    }

    @Test
    fun `will not update when nothing to update`() {
        // Given
        val customer = generateCustomer()
        underTest.insertCustomer(customer)
        // When
        underTest.updateCustomer(customer)
        // Then
    }

    private fun generateCustomer(): Customer {
        return Customer(
            FAKER.name().fullName(),
            FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID(),
            20
        )
    }
}
