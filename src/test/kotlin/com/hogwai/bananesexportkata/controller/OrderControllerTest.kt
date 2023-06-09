package com.hogwai.bananesexportkata.controller

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.hogwai.bananesexportkata.model.Order
import com.hogwai.bananesexportkata.model.Recipient
import com.hogwai.bananesexportkata.request.OrderRequest
import com.hogwai.bananesexportkata.service.OrderService
import com.hogwai.bananesexportkata.service.RecipientService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate


class OrderControllerTest {

    @Mock
    private lateinit var orderService: OrderService

    @Mock
    private lateinit var recipientService: RecipientService

    @InjectMocks
    private lateinit var orderController: OrderController

    private lateinit var mockMvc: MockMvc

    private val objectMapper = JsonMapper.builder().addModule(JavaTimeModule()).build()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build()
    }

    private fun createRecipientWithId(
        id: Long
    ): Recipient {
        return Recipient(id, "Jean", "rue de la Bécane", "80190", "Y", "France")
    }

    private fun <T> any() : T {
        return org.mockito.ArgumentMatchers.any()
    }

    @Test
    fun testGetAllOrders() {
        val order1 = Order(1L, createRecipientWithId(1), LocalDate.now(), 10, 25.0)
        val order2 = Order(2L, createRecipientWithId(1), LocalDate.now(), 5, 12.5)
        val orders = listOf(order1, order2)

        `when`(orderService.getAllOrders()).thenReturn(orders)

        mockMvc.perform(get("/order"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(orders)))
    }

    @Test
    fun testGetOrdersByRecipient() {
        val recipientId = 1L
        val order1 = Order(1L, createRecipientWithId(1), LocalDate.now(), 10, 25.0)
        val order2 = Order(2L, createRecipientWithId(1), LocalDate.now(), 5, 12.5)
        val orders = listOf(order1, order2)

        `when`(recipientService.getRecipientById(recipientId)).thenReturn(createRecipientWithId(1))
        `when`(orderService.getOrdersByRecipient(recipientId)).thenReturn(orders)

        mockMvc.perform(get("/order/recipient/{recipientId}", recipientId))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(orders)))
    }

    @Test
    fun testCreateOrder() {
        val orderRequest = OrderRequest(1L, LocalDate.now(), 10)
        val newOrder = Order(null, createRecipientWithId(1), orderRequest.deliveryDate, orderRequest.bananaQuantity, null)
        val createdOrder = Order(1L, createRecipientWithId(1), orderRequest.deliveryDate, orderRequest.bananaQuantity, null)

        `when`(recipientService.getRecipientById(orderRequest.recipientId)).thenReturn(createRecipientWithId(1))
        doNothing().`when`(orderService).validateOrder(newOrder)
        `when`(orderService.createOrder(newOrder)).thenReturn(createdOrder)

        mockMvc.perform(post("/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isCreated)
            .andExpect(content().string("The order has been created: $createdOrder"))
    }

    @Test
    fun testUpdateOrder() {
        val orderId = 1L
        val orderRequest = OrderRequest(1L, LocalDate.now(), 25)
        val updatedOrder = Order(orderId, createRecipientWithId(1), orderRequest.deliveryDate, 50, null)

        `when`(recipientService.getRecipientById(orderRequest.recipientId)).thenReturn(createRecipientWithId(1))
        `when`(orderService.existsById(orderId)).thenReturn(true)
        doNothing().`when`(orderService).validateOrder(updatedOrder)
        doReturn(updatedOrder).`when`(orderService).updateOrder(any())

        mockMvc.perform(put("/order/{id}", orderId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isOk)
            .andExpect(content().string("The order has been updated: $updatedOrder"))
    }

    @Test
    fun testDeleteOrder() {
        val orderId = 1L

        `when`(orderService.existsById(orderId)).thenReturn(true)

        mockMvc.perform(delete("/order/{id}", orderId))
            .andExpect(status().isOk)
            .andExpect(content().string("The order $orderId has been deleted."))
    }
}
