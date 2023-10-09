package com.khamroev.springbootfullstack.customer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository("jpa")
class CustomerJPADataAccessService @Autowired constructor(private val repository: CustomerRepository) :
    CustomerDao {

    override fun selectAllCustomers(): List<Customer> {
        return repository.findAll()
    }

    override fun selectCustomerById(id: Int): Customer? {
        return repository.findById(id).orElse(null)
    }

    override fun insertCustomer(customer: Customer): Customer {
        return repository.save(customer)
    }

    override fun existsCustomerWithEmail(email: String): Boolean {
        return repository.existsByEmail(email)
    }

    override fun existsCustomerWithId(id: Int): Boolean {
        return repository.existsById(id)
    }

    override fun deleteCustomerById(id: Int) {
        repository.deleteById(id)
    }

    override fun updateCustomer(customer: Customer) {
        repository.save(customer)
    }
}