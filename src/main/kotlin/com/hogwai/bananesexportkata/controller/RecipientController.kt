package com.hogwai.bananesexportkata.controller

import com.hogwai.bananesexportkata.model.Recipient
import com.hogwai.bananesexportkata.service.RecipientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/recipient")
class RecipientController(private val recipientService: RecipientService) {

    @GetMapping
    fun getAllRecipients(): ResponseEntity<Any> {
        return try {
            val recipients = recipientService.getAllRecipients()
            ResponseEntity.ok(recipients)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting all recipients: ${e.message}")
        }
    }

    @GetMapping("/{id}")
    fun getRecipientById(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val recipient = recipientService.getRecipientById(id)
            if (recipient != null) {
                ResponseEntity.ok(recipient)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while getting the recipient $id: ${e.message}")
        }
    }

    @PostMapping
    fun createRecipient(@RequestBody recipient: Recipient): ResponseEntity<Any> {
        return try {
            val createdRecipient = recipientService.createRecipient(recipient)
            ResponseEntity.status(HttpStatus.CREATED).body("This recipient has been created: $createdRecipient")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating the recipient: ${e.message}")
        }
    }

    @PutMapping("/{id}")
    fun updateRecipient(@PathVariable id: Long, @RequestBody recipient: Recipient): ResponseEntity<Any> {
        return try {
            val existingRecipient = recipientService.getRecipientById(id)
            if (existingRecipient != null) {
                recipient.id = existingRecipient.id
                val updatedRecipient = recipientService.updateRecipient(recipient)
                ResponseEntity.ok("This recipient has been updated: $updatedRecipient")
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while updating the recipient $id: ${e.message}")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteRecipient(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val existingRecipient = recipientService.getRecipientById(id)
            if (existingRecipient != null) {
                recipientService.deleteRecipient(id)
                ResponseEntity.ok("This recipient has been deleted: $existingRecipient")
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("The recipient $id has not been found.")
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while deleting the recipient $id: ${e.message}")
        }
    }
}