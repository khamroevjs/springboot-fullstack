package com.khamroev.springbootfullstack.customer

import jakarta.persistence.*

@Entity
@Table(
    name = "customer", uniqueConstraints = [UniqueConstraint(
        name = "customer_email_key",
        columnNames = ["email"]
    )]
)
class Customer(
    @Id
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    var id: Int,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var email: String,
    @Column(nullable = false)
    var age: Int
) {
    constructor(name: String, email: String, age: Int) : this(0, name, email, age)
}