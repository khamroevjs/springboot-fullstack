package com.khamroev.springbootfullstack

import com.github.javafaker.Faker
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.sql.DataSource

@Testcontainers
abstract class AbstractTestcontainers {

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            val flyway = Flyway.configure().dataSource(container.jdbcUrl, container.username, container.password).load()
            flyway.migrate()
        }

        @JvmStatic
        @Container
        protected val container: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15-alpine")
            .withDatabaseName("khamroev-dao-unit-test")
            .withUsername("khamroev")
            .withPassword("password")

        @DynamicPropertySource
        @JvmStatic
        private fun registerDataSourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }

        @JvmStatic
        private fun getDataSource(): DataSource {
            return DataSourceBuilder.create()
                .driverClassName(container.driverClassName)
                .url(container.jdbcUrl)
                .username(container.username)
                .password(container.password)
                .build()
        }

        @JvmStatic
        protected fun getJdbcTemplate(): JdbcTemplate {
            return JdbcTemplate(getDataSource())
        }

        @JvmStatic
        protected val FAKER = Faker()
    }
}