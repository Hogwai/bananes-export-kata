package com.hogwai.bananesexportkata.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hogwai.bananesexportkata.model.Recipient
import com.hogwai.bananesexportkata.service.RecipientService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class RecipientControllerTest {

    @Mock
    private lateinit var recipientService: RecipientService

    private lateinit var mockMvc: MockMvc

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockMvc = MockMvcBuilders.standaloneSetup(RecipientController(recipientService)).build()
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
        `when`(recipientService.getAllRecipients()).thenReturn(recipientList)

        // When/Then
        mockMvc.perform(get("/recipient"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Jean"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Jeanne"))
    }

    @Test
    fun getRecipientById_ExistingId_ReturnsRecipient() {
        // Given
        val recipientId = 1L
        val recipient = createRecipientWithId(1)
        `when`(recipientService.getRecipientById(recipientId)).thenReturn(recipient)

        // When/Then
        mockMvc.perform(get("/recipient/{id}", recipientId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Jean"))
    }

    @Test
    fun getRecipientById_NonExistingId_ReturnsNotFound() {
        // Given
        val recipientId = 1L
        `when`(recipientService.getRecipientById(recipientId)).thenReturn(null)

        // When/Then
        mockMvc.perform(get("/recipient/{id}", recipientId))
            .andExpect(status().isNotFound)
    }

    @Test
    fun createRecipient_ValidRecipient_ReturnsCreatedRecipient() {
        // Given
        val recipient = createRecipientWithId(1)
        `when`(recipientService.createRecipient(recipient)).thenReturn(recipient)

        // When/Then
        mockMvc.perform(
            post("/recipient")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipient))
        )
            .andExpect(status().isCreated)
            .andExpect(content().string("This recipient has been created: $recipient"))
    }

    @Test
    fun updateRecipient_ExistingId_ValidRecipient_ReturnsUpdatedRecipient() {
        // Given
        val recipientId = 1L
        val existingRecipient = createRecipientWithId(1)
        val updatedRecipient = Recipient(recipientId, "John D'eau", "rue de l'Honneur", "24130", "La Force", "France")
        `when`(recipientService.getRecipientById(recipientId)).thenReturn(existingRecipient)
        `when`(recipientService.updateRecipient(updatedRecipient)).thenReturn(updatedRecipient)

        // When/Then
        mockMvc.perform(
            put("/recipient/{id}", recipientId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedRecipient))
        ).andExpect(status().isOk)
            .andExpect(content().string("This recipient has been updated: $updatedRecipient"))
    }

    @Test
    fun updateRecipient_NonExistingId_ReturnsNotFound() {
        // Given
        val recipientId = 1L
        val recipient = createRecipientWithId(1)
        `when`(recipientService.getRecipientById(recipientId)).thenReturn(null)

        // When/Then
        mockMvc.perform(
            put("/recipient/{id}", recipientId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipient))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun deleteRecipient_ExistingId_ReturnsDeletedRecipient() {
        // Given
        val recipientId = 1L
        val existingRecipient = createRecipientWithId(1)
        `when`(recipientService.getRecipientById(recipientId)).thenReturn(existingRecipient)

        // When/Then
        mockMvc.perform(delete("/recipient/{id}", recipientId))
            .andExpect(status().isOk)
            .andExpect(content().string("This recipient has been deleted: $existingRecipient"))
    }

    @Test
    fun deleteRecipient_NonExistingId_ReturnsNotFound() {
        // Given
        val recipientId = 1L
        `when`(recipientService.getRecipientById(recipientId)).thenReturn(null)

        // When/Then
        mockMvc.perform(delete("/recipient/{id}", recipientId))
            .andExpect(status().isNotFound)
            .andExpect(content().string("The recipient $recipientId has not been found."))
    }
}
