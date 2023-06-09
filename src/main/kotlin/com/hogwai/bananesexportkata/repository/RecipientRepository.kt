package com.hogwai.bananesexportkata.repository

import com.hogwai.bananesexportkata.model.Recipient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipientRepository : JpaRepository<Recipient, Long> {
    fun existsByNameAndAddressAndPostalCodeAndCityAndCountry(
            name: String,
            address: String,
            postalCode: String,
            city: String,
            country: String
    ): Boolean
}