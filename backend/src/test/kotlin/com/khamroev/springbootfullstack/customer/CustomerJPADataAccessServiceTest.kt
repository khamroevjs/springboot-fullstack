package com.khamroev.springbootfullstack.customer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class CustomerJPADataAccessServiceTest {

    private lateinit var underTest: CustomerJPADataAccessService

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setUp() {
        underTest = CustomerJPADataAccessService(customerRepository)
    }

    @Test
    fun selectAllCustomers() {
        underTest.selectAllCustomers()
        verify(customerRepository).findAll()
    }

    @Test
    fun selectCustomerById() {
        val id = 1
        underTest.selectCustomerById(id)
        verify(customerRepository).findById(id)
    }

    @Test
    fun insertCustomer() {
        val customer = Customer("ali", "ali@gmail.com", 20)
        val expected = Customer(1, "ali", "ali@gmail.com", 20)
        whenever(customerRepository.save(customer)).thenReturn(expected)
        val actual = underTest.insertCustomer(customer)
        verify(customerRepository).save(customer)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun existsCustomerWithEmail() {
        val email = "ali@gmail.com"
        underTest.existsCustomerWithEmail(email)
        verify(customerRepository).existsByEmail(email)
    }

    @Test
    fun existsCustomerWithId() {
        val id = 1
        underTest.existsCustomerWithId(id)
        verify(customerRepository).existsById(id)
    }

    @Test
    fun deleteCustomerById() {
        val id = 1
        underTest.deleteCustomerById(id)
        verify(customerRepository).deleteById(id)
    }

    @Test
    fun updateCustomer() {
        val email = "ali_${UUID.randomUUID()}@gmail.com"
        val customer = Customer("ali", email, 20)
        underTest.updateCustomer(customer)
        verify(customerRepository).save(customer)
    }
}
