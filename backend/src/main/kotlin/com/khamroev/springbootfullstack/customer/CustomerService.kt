package com.khamroev.springbootfullstack.customer

import com.khamroev.springbootfullstack.exception.DuplicateResourceException
import com.khamroev.springbootfullstack.exception.RequestValidationException
import com.khamroev.springbootfullstack.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CustomerService @Autowired constructor(@Qualifier("jdbc") private val dao: CustomerDao) {
    fun getAllCustomers(): List<Customer> {
        return dao.selectAllCustomers()
    }

    fun getCustomer(id: Int): Customer {
        return dao.selectCustomerById(id) ?: throw ResourceNotFoundException("Customer with id $id not found")
    }

    fun addCustomer(request: CustomerRegistrationRequest): Customer {
        if (dao.existsCustomerWithEmail(request.email)) {
            throw DuplicateResourceException("Email already exists")
        }

        val customer = Customer(request.name, request.email, request.age)
        return dao.insertCustomer(customer)
    }

    fun deleteCustomer(id: Int) {
        if (!dao.existsCustomerWithId(id)) {
            throw ResourceNotFoundException("Customer with id $id not found")
        }
        dao.deleteCustomerById(id)
    }

    fun updateCustomer(id: Int, request: CustomerUpdateRequest) {
        val customer = getCustomer(id)
        var changes = false
        if (request.name != null && request.name != customer.name) {
            changes = true
            customer.name = request.name
        }

        if (request.age != null && request.age != customer.age) {
            changes = true
            customer.age = request.age
        }

        if (request.email != null && request.email != customer.email) {
            changes = true
            if (dao.existsCustomerWithEmail(request.email)) {
                throw DuplicateResourceException("Email already exists")
            }
            customer.email = request.email
        }

        if (!changes) {
            throw RequestValidationException("No data changes found")
        }

        dao.updateCustomer(customer)
    }
}