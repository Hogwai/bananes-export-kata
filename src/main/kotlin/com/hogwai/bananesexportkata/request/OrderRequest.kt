package com.hogwai.bananesexportkata.request

import jakarta.persistence.Column
import java.time.LocalDate

data class OrderRequest(
        @Column(nullable = false)
        var recipientId: Long,

        @Column(nullable = false)
        var deliveryDate: LocalDate,

        @Column(nullable = false)
        var bananaQuantity: Int
)
