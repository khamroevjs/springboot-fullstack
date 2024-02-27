package com.khamroev.springbootfullstack.customer

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class CustomerRowMapper : RowMapper<Customer> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Customer {
        return Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getInt("age")
        )
    }
}
