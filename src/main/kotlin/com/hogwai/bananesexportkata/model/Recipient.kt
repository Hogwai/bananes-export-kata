package com.hogwai.bananesexportkata.model

import io.swagger.v3.oas.annotations.Hidden
import jakarta.persistence.*

@Entity
@Table(name = "recipient")
data class Recipient(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Hidden
        var id: Long = 0,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var address: String,

        @Column(nullable = false)
        var postalCode: String,

        @Column(nullable = false)
        var city: String,

        @Column(nullable = false)
        var country: String
)
