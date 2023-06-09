package com.hogwai.bananesexportkata.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "order")
data class Order(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "recipient_id", nullable = false)
        var recipient: Recipient,

        @Column(nullable = false)
        var deliveryDate: LocalDate,

        @Column(nullable = false)
        var bananaQuantity: Int,

        var price: Double?
) {
    constructor(
            recipient: Recipient,
            deliveryDate: LocalDate,
            quantity: Int
    ) : this(null, recipient, deliveryDate, quantity, null)
}
