package com.khamroev.springbootfullstack.customer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository("jdbc")
class CustomerJDBCDataAccessService @Autowired constructor(
    private val jdbcTemplate: JdbcTemplate,
    private val customerRowMapper: CustomerRowMapper
) : CustomerDao {
    override fun selectAllCustomers(): List<Customer> {
        val sql = "SELECT id, name, email, age FROM customer"
        return jdbcTemplate.query(sql, customerRowMapper)
    }

    override fun selectCustomerById(id: Int): Customer? {
        val sql = "SELECT id, name, email, age FROM customer WHERE id = ?"
        val rowMapper: (ResultSet, Int) -> Customer = { rs, _ ->
            Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age")
            )
        }
        return jdbcTemplate.query(sql, rowMapper, id).firstOrNull()
    }

    override fun insertCustomer(customer: Customer): Customer {
        val keyHolder = GeneratedKeyHolder()
        val sql = "INSERT INTO customer (name, email, age) VALUES (?, ?, ?)"
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(sql, arrayOf("id"))
            ps.setString(1, customer.name)
            ps.setString(2, customer.email)
            ps.setInt(3, customer.age)
            ps
        }, keyHolder)

        val id = keyHolder.key!!.toInt()
        customer.id = id
        return customer
    }

    override fun existsCustomerWithEmail(email: String): Boolean {
        val sql = "SELECT COUNT(id) AS count FROM customer WHERE email = ?"
        val count = jdbcTemplate.queryForObject(sql, Int::class.java, email)
        return count > 0
    }

    override fun existsCustomerWithId(id: Int): Boolean {
        val sql = "SELECT COUNT(id) FROM customer WHERE id = ?"
        val count = jdbcTemplate.queryForObject(sql, Int::class.java, id)
        return count > 0
    }

    override fun deleteCustomerById(id: Int) {
        val sql = "DELETE FROM customer WHERE id = ?"
        jdbcTemplate.update(sql, id)
    }

    override fun updateCustomer(customer: Customer) {
        val sql = "UPDATE customer SET name = ?, email = ?, age = ? WHERE id = ?"
        jdbcTemplate.update(sql, customer.name, customer.email, customer.age, customer.id)
    }
}