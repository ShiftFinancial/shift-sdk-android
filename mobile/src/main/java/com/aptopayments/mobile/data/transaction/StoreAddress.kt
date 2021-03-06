package com.aptopayments.mobile.data.transaction

import com.aptopayments.mobile.data.geo.Country
import java.io.Serializable

data class StoreAddress(
    val address: String?,
    val apUnit: String?,
    val country: Country?,
    val city: String?,
    val region: String?,
    val zip: String?
) : Serializable {
    fun toStringRepresentation(): String {
        return arrayListOf(address, apUnit, city, region, country?.name)
            .asSequence()
            .filterNotNull()
            .joinToString(", ")
    }
}
