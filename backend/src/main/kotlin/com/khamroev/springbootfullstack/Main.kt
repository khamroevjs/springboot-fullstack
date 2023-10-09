package com.khamroev.springbootfullstack

import com.khamroev.springbootfullstack.customer.Customer
import com.khamroev.springbootfullstack.customer.CustomerRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Main {
    @Bean
    fun runner(customerRepository: CustomerRepository): CommandLineRunner {
        return CommandLineRunner {
            // TODO("Add data faker")
            val customers = listOf(
                Customer(1, "Alex", "alex@gmail.com", 21),
                Customer(2, "Jamila", "jamila@gmail.com", 19),
                Customer(3, "John", "john@gmail.com", 25)
            )
            customerRepository.saveAll(customers)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Main>(*args)
        }
    }
}
