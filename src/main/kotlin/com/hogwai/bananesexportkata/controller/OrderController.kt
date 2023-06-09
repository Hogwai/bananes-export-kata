package com.hogwai.bananesexportkata.controller

import com.hogwai.bananesexportkata.model.Order
import com.hogwai.bananesexportkata.request.OrderRequest
import com.hogwai.bananesexportkata.service.OrderService
import com.hogwai.bananesexportkata.service.RecipientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
class OrderController(
        private val orderService: OrderService,
        private val recipientService: RecipientService
) {

    @GetMapping
    fun getAllOrders(): ResponseEntity<Any> {
        return try {
            val orders = orderService.getAllOrders()
            ResponseEntity.ok(orders)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting all orders: ${e.message}")
        }
    }

    @GetMapping("/recipient/{recipientId}")
    fun getOrdersByRecipient(@PathVariable recipientId: Long): ResponseEntity<Any> {
        return try {
            val recipient = recipientService.getRecipientById(recipientId)
            if (recipient != null) {
                val orders = orderService.getOrdersByRecipient(recipientId)
                ResponseEntity.ok(orders)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The recipient $recipientId does not exist.")
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting orders for the recipient $recipientId: ${e.message}")
        }
    }

    @PostMapping
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<Any> {
        return try {
            val recipient = recipientService.getRecipientById(orderRequest.recipientId)
            if (recipient == null) {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The recipient ${orderRequest.recipientId} of the order has been found.")
            } else {
                val newOrder = Order(recipient, orderRequest.deliveryDate, orderRequest.bananaQuantity)
                orderService.validateOrder(newOrder)
                val createdOrder = orderService.createOrder(newOrder)
                ResponseEntity.status(HttpStatus.CREATED).body("The order has been created: $createdOrder")
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating an order: ${e.message}")
        }
    }

    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id: Long, @RequestBody orderToBeUpdated: OrderRequest): ResponseEntity<Any> {
        return try {
            val recipient = recipientService.getRecipientById(orderToBeUpdated.recipientId)
            if (recipient == null) {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The recipient ${orderToBeUpdated.recipientId} of the order has not been found.")
            } else if (orderService.existsById(id)) {
                val orderToUpdate = Order(recipient, orderToBeUpdated.deliveryDate, orderToBeUpdated.bananaQuantity)
                orderService.validateOrder(orderToUpdate)
                val updatedOrder = orderService.updateOrder(orderToUpdate)
                ResponseEntity.ok("The order has been updated: $updatedOrder")
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("The order $id to update has not been found.")
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while updating the order $id: ${e.message}")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            if (orderService.existsById(id)) {
                orderService.deleteOrder(id)
                ResponseEntity.ok("The order $id has been deleted.")
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The order $id has not been found")
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while deleting the order $id: ${e.message}")
        }
    }
}