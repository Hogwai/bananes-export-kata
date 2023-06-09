package com.hogwai.bananesexportkata.service

import com.hogwai.bananesexportkata.model.Order
import com.hogwai.bananesexportkata.model.Recipient
import com.hogwai.bananesexportkata.repository.OrderRepository
import com.hogwai.bananesexportkata.repository.RecipientRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDate
import java.util.*

class OrderServiceTest {

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var recipientRepository: RecipientRepository

    private lateinit var orderService: OrderService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        orderService = OrderService(orderRepository, recipientRepository)
    }

    @Test
    fun createOrder_ValidOrder_CreatesAndReturnsOrder() {
        // Given
        val recipient = Recipient(1L, "John Doe", "123 Street", "12345", "City", "Country")
        val deliveryDate = LocalDate.now()
        val bananaQuantity = 50
        val price = 125.0
        val order = Order(recipient, deliveryDate, bananaQuantity)
        `when`(orderRepository.save(order)).thenReturn(order.copy(id = 1L, price = price))

        // When
        val createdOrder = orderService.createOrder(order)

        // Then
        assertNotNull(createdOrder)
        assertEquals(1L, createdOrder.id)
        assertEquals(recipient, createdOrder.recipient)
        assertEquals(deliveryDate, createdOrder.deliveryDate)
        assertEquals(bananaQuantity, createdOrder.bananaQuantity)
        assertEquals(price, createdOrder.price)
    }

    @Test
    fun getOrderById_ExistingOrderId_ReturnsOrder() {
        // Given
        val orderId = 1L
        val recipient = Recipient(1L, "John Doe", "123 Street", "12345", "City", "Country")
        val deliveryDate = LocalDate.now()
        val bananaQuantity = 50
        val price = 125.0
        val order = Order(orderId, recipient, deliveryDate, bananaQuantity, price)
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(order))

        // When
        val retrievedOrder = orderService.getOrderById(orderId)

        // Then
        assertNotNull(retrievedOrder)
        assertEquals(order, retrievedOrder)
    }

    @Test
    fun getAllOrders_ReturnsAllOrders() {
        // Given
        val orders = listOf(
                Order(1L, Recipient(1L, "John Doe", "123 Street", "12345", "City", "Country"),
                        LocalDate.now(), 50, 125.0),
                Order(2L, Recipient(2L, "Jane Smith", "456 Avenue", "67890", "City", "Country"),
                        LocalDate.now(), 100, 250.0)
        )
        `when`(orderRepository.findAll()).thenReturn(orders)

        // When
        val allOrders = orderService.getAllOrders()

        // Then
        assertEquals(orders, allOrders)
    }

    @Test
    fun existsById_ExistingOrderId_ReturnsTrue() {
        // Given
        val orderId = 1L
        `when`(orderRepository.existsById(orderId)).thenReturn(true)

        // When
        val exists = orderService.existsById(orderId)

        // Then
        assertTrue(exists)
    }

    @Test
    fun existsById_NonExistingOrderId_ReturnsFalse() {
        // Given
        val orderId = 1L
        `when`(orderRepository.existsById(orderId)).thenReturn(false)

        // When
        val exists = orderService.existsById(orderId)

        // Then
        assertFalse(exists)
    }

    @Test
    fun getOrdersByRecipient_ExistingRecipientId_ReturnsOrdersForRecipient() {
        // Given
        val recipientId = 1L
        val recipient = Recipient(recipientId, "John Doe", "123 Street", "12345", "City", "Country")
        val orders = listOf(
                Order(1L, recipient, LocalDate.now(), 50, 125.0),
                Order(2L, recipient, LocalDate.now(), 100, 250.0)
        )
        `when`(recipientRepository.findById(recipientId)).thenReturn(Optional.of(recipient))
        `when`(orderRepository.findByRecipient(recipient)).thenReturn(orders)

        // When
        val recipientOrders = orderService.getOrdersByRecipient(recipientId)

        // Then
        assertEquals(orders, recipientOrders)
    }

    @Test
    fun updateOrder_ValidOrder_ReturnsUpdatedOrder() {
        // Given
        val orderId = 1L
        val recipient = Recipient(1L, "John Doe", "123 Street", "12345", "City", "Country")
        val order = Order(orderId, recipient, LocalDate.now(), 50, 125.0)
        val updatedOrder = Order(orderId, recipient, LocalDate.now(), 75, 187.5)
        Mockito.`when`(orderRepository.save(order)).thenReturn(updatedOrder)

        // When
        val result = orderService.updateOrder(order)

        // Then
        assertNotNull(result)
        assertEquals(updatedOrder, result)
    }

    @Test
    fun deleteOrder_ExistingOrderId_DeletesOrder() {
        // Given
        val orderId = 1L

        // When
        orderService.deleteOrder(orderId)

        // Then
        Mockito.verify(orderRepository, Mockito.times(1)).deleteById(orderId)
    }

    @Test
    fun validateOrder_ValidOrder_NoExceptionsThrown() {
        // Given
        val recipient = Recipient(1L, "John Doe", "123 Street", "12345", "City", "Country")
        val order = Order(recipient, LocalDate.now().plusWeeks(1), 50)

        // When/Then - No exceptions should be thrown
        assertDoesNotThrow { orderService.validateOrder(order) }
    }
}
