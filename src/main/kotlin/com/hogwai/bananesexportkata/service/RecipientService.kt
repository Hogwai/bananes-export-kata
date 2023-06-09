package com.hogwai.bananesexportkata.service

import com.hogwai.bananesexportkata.model.Recipient
import com.hogwai.bananesexportkata.repository.RecipientRepository
import org.springframework.stereotype.Service

@Service
class RecipientService(private val recipientRepository: RecipientRepository) {
    fun getAllRecipients(): List<Recipient> {
        return recipientRepository.findAll()
    }

    fun getRecipientById(id: Long): Recipient? {
        return recipientRepository.findById(id).orElse(null)
    }

    fun createRecipient(recipient: Recipient): Recipient {
        this.validateRecipient(recipient)
        return recipientRepository.save(recipient)
    }

    fun updateRecipient(recipient: Recipient): Recipient {
        return recipientRepository.save(recipient)
    }

    fun deleteRecipient(id: Long) {
        recipientRepository.deleteById(id)
    }

    fun validateRecipient(recipient: Recipient) {
        require(recipient.name.isNotBlank() &&
                recipient.address.isNotBlank() &&
                recipient.postalCode.isNotBlank() &&
                recipient.city.isNotBlank() &&
                recipient.country.isNotBlank()
        ) {
            "A recipient must have a name, an address, a postal code, a city and a country."
        }

        require(!recipientRepository.existsByNameAndAddressAndPostalCodeAndCityAndCountry(
                recipient.name,
                recipient.address,
                recipient.postalCode,
                recipient.city,
                recipient.country
        )) {
            "A recipient with the same information already exists."
        }
    }
}