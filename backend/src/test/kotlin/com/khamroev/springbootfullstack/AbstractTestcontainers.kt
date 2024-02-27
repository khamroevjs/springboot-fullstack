package com.khamroev.springbootfullstack

import com.github.javafaker.Faker
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
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
            Flyway.configure().dataSource(container.jdbcUrl, container.username, container.password).load().migrate()
        }

        @JvmStatic
        @Container
        @ServiceConnection
        protected val container: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15-alpine")
            .withDatabaseName("khamroev-dao-unit-test")
            .withUsername("khamroev")
            .withPassword("1234")

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

    @Test
    fun `can start postgres DB`() {
        assertThat(container.isRunning).isTrue()
        assertThat(container.isCreated).isTrue()
    }
}
