package com.khamroev.springbootfullstack.customer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController @Autowired constructor(private val service: CustomerService) {

    @GetMapping
    fun getCustomers(): List<Customer> {
        return service.getAllCustomers()
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable(value = "id") id: Int): Customer {
        return service.getCustomer(id)
    }

    @PostMapping
    fun registerCustomer(@RequestBody request: CustomerRegistrationRequest): Customer {
        return service.addCustomer(request)
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable(value = "id") id: Int) {
        service.deleteCustomer(id)
    }

    @PutMapping("/{id}")
    fun updateCustomer(@PathVariable(value = "id") id: Int, @RequestBody request: CustomerUpdateRequest) {
        service.updateCustomer(id, request)
    }
}