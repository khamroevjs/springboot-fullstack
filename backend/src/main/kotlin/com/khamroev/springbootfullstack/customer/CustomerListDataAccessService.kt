package com.khamroev.springbootfullstack.customer

import org.springframework.stereotype.Repository

@Repository("list")
class CustomerListDataAccessService : CustomerDao {
    private val customers = mutableListOf(
        Customer(1, "Alex", "alex@gmail.com", 21),
        Customer(2, "Jamila", "jamila@gmail.com", 19)
    )

    override fun selectAllCustomers(): List<Customer> {
        return customers
    }

    override fun selectCustomerById(id: Int): Customer? {
        return customers.firstOrNull { it.id == id }
    }

    override fun insertCustomer(customer: Customer): Customer {
        customers.add(customer)
        return customer
    }

    override fun existsCustomerWithEmail(email: String): Boolean {
        return customers.any { it.email == email }
    }

    override fun existsCustomerWithId(id: Int): Boolean {
        return customers.any { it.id == id }
    }

    override fun deleteCustomerById(id: Int) {
        customers.removeIf { it.id == id }
    }

    override fun updateCustomer(customer: Customer) {
        deleteCustomerById(customer.id)
        insertCustomer(customer)
    }
}
