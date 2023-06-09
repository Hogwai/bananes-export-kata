package com.hogwai.bananesexportkata.service

import com.hogwai.bananesexportkata.model.Order
import com.hogwai.bananesexportkata.repository.OrderRepository
import com.hogwai.bananesexportkata.repository.RecipientRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class OrderService(
        private val orderRepository: OrderRepository,
        private val recipientRepository: RecipientRepository
) {
    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }

    fun getOrderById(id: Long): Order? {
        return orderRepository.findById(id).orElse(null)
    }

    fun existsById(id: Long): Boolean {
        return orderRepository.existsById(id)
    }

    fun getOrdersByRecipient(recipientId: Long): List<Order> {
        val recipient = recipientRepository.findById(recipientId).orElse(null)
        return if (recipient != null) {
            orderRepository.findByRecipient(recipient)
        } else {
            emptyList()
        }
    }

    fun createOrder(order: Order): Order {
        // Calculating price based on quantity
        order.price = order.bananaQuantity * 2.50
        return orderRepository.save(order)
    }

    fun updateOrder(order: Order): Order {
        return this.createOrder(order);
    }

    fun deleteOrder(id: Long) {
        orderRepository.deleteById(id)
    }

    fun validateOrder(order: Order) {
        // Validating delivery date
        requireNotNull(order.deliveryDate) {
            "The order must have a delivery date."
        }

        val minDeliveryDate = LocalDate.now().plusWeeks(1)
        require(order.deliveryDate.isEqual(minDeliveryDate) || order.deliveryDate.isAfter(minDeliveryDate)) {
            "The delivery date must be, at least, one week in the future compared to the current date."
        }

        // Validating banana quantity
        requireNotNull(order.bananaQuantity) {
            "The order must have a quantity of bananas."
        }

        val quantity = order.bananaQuantity
        require(quantity >= 25) {
            "The minimum quantity of bananas is 25 kg."
        }

        require(quantity % 25 == 0) {
            "The quantity must come by boxes (25 kg per box)."
        }

        require(quantity <= 10_000) {
            "An order cannot contain more than 10,000 kg (400 boxes)."
        }
    }
}