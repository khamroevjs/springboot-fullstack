package com.khamroev.springbootfullstack.customer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.ResultSet

class CustomerRowMapperTest {

    @Test
    fun mapRow() {
        // Given
        val rs = mock<ResultSet>()
        val underTest = CustomerRowMapper()
        val id = 1
        val name = "Alex"
        val email = "alex@gmail.com"
        val age = 20
        whenever(rs.getInt("id")).thenReturn(id)
        whenever(rs.getString("name")).thenReturn(name)
        whenever(rs.getString("email")).thenReturn(email)
        whenever(rs.getInt("age")).thenReturn(age)
        // When
        val actual = underTest.mapRow(rs, 1)
        // Then
        assertEquals(id, actual.id)
        assertEquals(name, actual.name)
        assertEquals(email, actual.email)
        assertEquals(age, actual.age)
    }
}