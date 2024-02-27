package com.khamroev.springbootfullstack.customer

import com.khamroev.springbootfullstack.exception.DuplicateResourceException
import com.khamroev.springbootfullstack.exception.RequestValidationException
import com.khamroev.springbootfullstack.exception.ResourceNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class CustomerServiceTest {
    @Mock
    private lateinit var dao: CustomerDao
    private lateinit var underTest: CustomerService

    @BeforeEach
    fun setUp() {
        underTest = CustomerService(dao)
    }

    @Test
    fun getAllCustomers() {
        underTest.getAllCustomers()
        verify(dao).selectAllCustomers()
    }

    @Test
    fun getCustomer() {
        val id = 10
        val customer = Customer(id, "Alex", "alex@gmail.com", 20)

        whenever(dao.selectCustomerById(id)).thenReturn(customer)
        val actual = underTest.getCustomer(id)
        assertThat(actual).isEqualTo(customer)
    }

    @Test
    fun `getCustomer throws exception when customer not found`() {
        val id = 10
        whenever(dao.selectCustomerById(id)).thenReturn(null)
        assertThatThrownBy { underTest.getCustomer(id) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining("Customer with id $id not found")
    }

    @Test
    fun addCustomer() {
        val request = CustomerRegistrationRequest("Alex", "alex@gmail.com", 20)
        whenever(dao.existsCustomerWithEmail(request.email)).thenReturn(false)
        underTest.addCustomer(request)
        val argumentCaptor = argumentCaptor<Customer>()
        verify(dao).insertCustomer(argumentCaptor.capture())
        val capturedCustomer = argumentCaptor.firstValue
        assertThat(capturedCustomer.id).isEqualTo(0)
        assertThat(capturedCustomer.name).isEqualTo(request.name)
        assertThat(capturedCustomer.email).isEqualTo(request.email)
        assertThat(capturedCustomer.age).isEqualTo(request.age)
    }

    @Test
    fun `addCustomer throws exception when customer with email already exists`() {
        val request = CustomerRegistrationRequest("Bob", "bob@gmail.com", 20)
        whenever(dao.existsCustomerWithEmail(request.email)).thenReturn(true)
        assertThatThrownBy { underTest.addCustomer(request) }
            .isInstanceOf(DuplicateResourceException::class.java)
            .hasMessageContaining("Email already exists")

        verify(dao, Mockito.never()).insertCustomer(any())
    }

    @Test
    fun deleteCustomer() {
        // Given
        val id = 10
        whenever(dao.existsCustomerWithId(id)).thenReturn(true)
        // When
        underTest.deleteCustomer(id)
        // Then
        verify(dao).deleteCustomerById(id)
    }

    @Test
    fun `deleteCustomer throws exception when customer not found`() {
        // Given
        val id = 10
        whenever(dao.existsCustomerWithId(id)).thenReturn(false)
        // When
        assertThatThrownBy { underTest.deleteCustomer(id) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining("Customer with id $id not found")
        verify(dao, Mockito.never()).deleteCustomerById(id)
    }

    @Test
    fun updateCustomer() {
        val id = 10
        val request = CustomerUpdateRequest("Alex", "alex@gmail.com", 20)
        val customer = Customer(id, "Bob", "bob@gmail.com", 19)
        whenever(dao.selectCustomerById(id)).thenReturn(customer)
        whenever(dao.existsCustomerWithEmail(request.email!!)).thenReturn(false)
        underTest.updateCustomer(id, request)

        val argumentCaptor = argumentCaptor<Customer>()

        verify(dao).updateCustomer(argumentCaptor.capture())
        val capturedCustomer = argumentCaptor.firstValue
        assertThat(capturedCustomer.id).isEqualTo(id)
        assertThat(capturedCustomer.name).isEqualTo(request.name)
        assertThat(capturedCustomer.email).isEqualTo(request.email)
        assertThat(capturedCustomer.age).isEqualTo(request.age)
    }

    @Test
    fun `updateCustomer email already exists`() {
        val id = 10
        val request = CustomerUpdateRequest("Alex", "alex@gmail.com", 20)
        val customer = Customer("Bob", "bob@gmail.com", 19)
        whenever(dao.selectCustomerById(id)).thenReturn(customer)
        whenever(dao.existsCustomerWithEmail(request.email!!)).thenReturn(true)

        assertThatThrownBy { underTest.updateCustomer(id, request) }
            .isInstanceOf(DuplicateResourceException::class.java)
            .hasMessageContaining("Email already exists")
        verify(dao, never()).updateCustomer(customer)
    }

    @Test
    fun `updateCustomer throws exception when customer not found`() {
        val id = 10
        val request = CustomerUpdateRequest("Alex", "alex@gmail.com", 20)
        val customer = Customer("Alex", "alex@gmail.com", 20)
        whenever(dao.selectCustomerById(id)).thenReturn(customer)
        assertThatThrownBy { underTest.updateCustomer(id, request) }
            .isInstanceOf(RequestValidationException::class.java)
            .hasMessageContaining("No data changes found")

        verify(dao, never()).existsCustomerWithEmail(any())
        verify(dao, never()).updateCustomer(any())
    }
}
