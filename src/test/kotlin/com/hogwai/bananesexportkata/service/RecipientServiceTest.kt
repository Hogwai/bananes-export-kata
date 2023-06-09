package com.hogwai.bananesexportkata.service

import com.hogwai.bananesexportkata.model.Recipient
import com.hogwai.bananesexportkata.repository.RecipientRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

class RecipientServiceTest {

    @Mock
    private lateinit var recipientRepository: RecipientRepository

    private lateinit var recipientService: RecipientService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        recipientService = RecipientService(recipientRepository)
    }

    private fun createRecipientWithId(
        id: Long
    ): Recipient {
        return Recipient(id, "Jean", "rue de la Dernière Pluie", "87160", "Arnac-la-Poste", "France")
    }

    @Test
    fun getAllRecipients_ReturnsRecipients_Success() {
        val recipient = createRecipientWithId(1)
        val recipient2 = createRecipientWithId(2)
        recipient2.name = "Jeanne"
        // Given
        val recipientList = listOf(
            recipient,
            recipient2
        )
        `when`(recipientRepository.findAll()).thenReturn(recipientList)

        // When
        val recipients = recipientService.getAllRecipients()

        // Then
        assertEquals(recipientList.size, recipients.size)
        assertEquals(recipientList, recipients)
    }

    @Test
    fun getRecipientById_ExistingId_ReturnsRecipient() {
        // Given
        val recipientId = 1L
        val recipient = createRecipientWithId(1)
        `when`(recipientRepository.findById(recipientId)).thenReturn(Optional.of(recipient))

        // When
        val result = recipientService.getRecipientById(recipientId)

        // Then
        assertNotNull(result)
        assertEquals(recipient, result)
    }

    @Test
    fun getRecipientById_NonExistingId_ReturnsNull() {
        // Given
        val recipientId = 1L
        `when`(recipientRepository.findById(recipientId)).thenReturn(Optional.empty())

        // When
        val result = recipientService.getRecipientById(recipientId)

        // Then
        assertEquals(null, result)
    }

    @Test
    fun createRecipient_ValidRecipient_ReturnsCreatedRecipient() {
        // Given
        val recipient = createRecipientWithId(1)
        `when`(recipientRepository.save(recipient)).thenReturn(recipient)

        // When
        val result = recipientService.createRecipient(recipient)

        // Then
        assertNotNull(result)
        assertEquals(recipient, result)
    }

    @Test
    fun updateRecipient_ValidRecipient_ReturnsUpdatedRecipient() {
        // Given
        val recipient = createRecipientWithId(1)
        `when`(recipientRepository.save(recipient)).thenReturn(recipient)

        // When
        val result = recipientService.updateRecipient(recipient)

        // Then
        assertNotNull(result)
        assertEquals(recipient, result)
    }

    @Test
    fun deleteRecipient_ExistingId_DeletesRecipient() {
        // Given
        val recipientId = 1L

        // When
        recipientService.deleteRecipient(recipientId)

        // Then
        // Verify that the deleteById method is called
        verify(recipientRepository, times(1)).deleteById(recipientId)
    }

    @Test
    fun validateRecipient_ValidRecipient_NoExceptionThrown() {
        // Given
        val recipient = createRecipientWithId(1)

        // When/Then - No exception should be thrown
        recipientService.validateRecipient(recipient)
    }

    @Test
    fun validateRecipient_InvalidRecipient_ThrowsException() {
        // Given
        val recipient = createRecipientWithId(1)
        recipient.name = ""

        // When/Then - ValidateRecipient should throw an exception
        assertThrows(IllegalArgumentException::class.java) {
            recipientService.validateRecipient(recipient)
        }
    }
}
