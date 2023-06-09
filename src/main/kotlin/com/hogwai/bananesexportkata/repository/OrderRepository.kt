package com.hogwai.bananesexportkata.repository

import com.hogwai.bananesexportkata.model.Order
import com.hogwai.bananesexportkata.model.Recipient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.recipient = :recipient")
    fun findByRecipient(recipient: Recipient): List<Order>
}