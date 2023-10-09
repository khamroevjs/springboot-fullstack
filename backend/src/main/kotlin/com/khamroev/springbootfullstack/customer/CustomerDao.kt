package com.khamroev.springbootfullstack.customer

interface CustomerDao {

    fun selectAllCustomers(): List<Customer>
    fun selectCustomerById(id: Int): Customer?
    fun insertCustomer(customer: Customer): Customer
    fun existsCustomerWithEmail(email: String): Boolean
    fun existsCustomerWithId(id: Int): Boolean
    fun deleteCustomerById(id: Int)
    fun updateCustomer(customer: Customer)
}