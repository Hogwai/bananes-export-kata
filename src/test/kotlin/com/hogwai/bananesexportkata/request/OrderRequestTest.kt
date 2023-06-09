package com.hogwai.bananesexportkata.request

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class OrderRequestTest {

    @Test
    fun createOrderRequest_ValidParameters_Success() {
        // Given
        val recipientId = 1L
        val deliveryDate = LocalDate.now()
        val bananaQuantity = 50

        // When
        val orderRequest = OrderRequest(recipientId, deliveryDate, bananaQuantity)

        // Then
        assertEquals(recipientId, orderRequest.recipientId)
        assertEquals(deliveryDate, orderRequest.deliveryDate)
        assertEquals(bananaQuantity, orderRequest.bananaQuantity)
    }

}
