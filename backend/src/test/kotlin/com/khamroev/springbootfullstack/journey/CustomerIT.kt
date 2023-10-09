package com.khamroev.springbootfullstack.journey

import com.github.javafaker.Faker
import com.khamroev.springbootfullstack.AbstractTestcontainers
import com.khamroev.springbootfullstack.customer.Customer
import com.khamroev.springbootfullstack.customer.CustomerRegistrationRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIT : AbstractTestcontainers() {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    companion object {
        private const val CUSTOMER_URI = "/api/v1/customers"
        private val RANDOM = Random()
    }

    @Test
    fun `can register a customer`() {
        // create registration request
        val faker = Faker()
        val fakerName = faker.name()
        val name = fakerName.fullName()
        val email = fakerName.lastName() + UUID.randomUUID() + "@foobarhello123.com"
        val age = RANDOM.nextInt(1, 100)
        val request = CustomerRegistrationRequest(name, email, age)

        // send a post request
        val registeredCustomer = webTestClient.post()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CustomerRegistrationRequest::class.java)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer::class.java)
            .returnResult()
            .responseBody

        // get customer by id
        val actual = webTestClient.get()
            .uri("$CUSTOMER_URI/{id}", registeredCustomer?.id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer::class.java)
            .returnResult()
            .responseBody

        assertThat(actual?.id).isEqualTo(registeredCustomer?.id)
        assertThat(actual?.name).isEqualTo(name)
        assertThat(actual?.email).isEqualTo(email)
        assertThat(actual?.age).isEqualTo(age)
    }

    @Test
    fun `can delete customer`() {
        // create registration request
        val faker = Faker()
        val fakerName = faker.name()
        val name = fakerName.fullName()
        val email = fakerName.lastName() + UUID.randomUUID() + "@foobarhello123.com"
        val age = RANDOM.nextInt(1, 100)
        val request = CustomerRegistrationRequest(name, email, age)

        // send a post request
        val registeredCustomer = webTestClient.post()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CustomerRegistrationRequest::class.java)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer::class.java)
            .returnResult()
            .responseBody

        // delete customer
        webTestClient.delete()
            .uri("$CUSTOMER_URI/{id}", registeredCustomer?.id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()

        // get customer by id
        webTestClient.get()
            .uri("$CUSTOMER_URI/{id}", registeredCustomer?.id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `can update customer`() {
        // create registration request
        val faker = Faker()
        val fakerName = faker.name()
        val name = fakerName.fullName()
        val email = fakerName.lastName() + UUID.randomUUID() + "@foobarhello123.com"
        val age = RANDOM.nextInt(1, 100)
        val request = CustomerRegistrationRequest(name, email, age)

        // send a post request
        val registeredCustomer = webTestClient.post()
            .uri(CUSTOMER_URI)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), CustomerRegistrationRequest::class.java)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer::class.java)
            .returnResult()
            .responseBody

        val newName  = fakerName.fullName()
        val newEmail = fakerName.lastName() + UUID.randomUUID() + "@foobarhello123.com"
        val newAge = RANDOM.nextInt(1, 100)
        val updateRequest = CustomerRegistrationRequest(newName, newEmail, newAge)

        // update customer
        webTestClient.put()
            .uri("$CUSTOMER_URI/{id}", registeredCustomer?.id)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(updateRequest), CustomerRegistrationRequest::class.java)
            .exchange()
            .expectStatus()
            .isOk()

        // get customer by id
        val actual = webTestClient.get()
            .uri("$CUSTOMER_URI/{id}", registeredCustomer?.id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Customer::class.java)
            .returnResult()
            .responseBody

        assertThat(actual?.id).isEqualTo(registeredCustomer?.id)
        assertThat(actual?.name).isEqualTo(newName)
        assertThat(actual?.email).isEqualTo(newEmail)
        assertThat(actual?.age).isEqualTo(newAge)
    }
}