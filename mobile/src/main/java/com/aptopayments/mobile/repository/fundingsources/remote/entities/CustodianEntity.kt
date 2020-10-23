package com.aptopayments.mobile.repository.fundingsources.remote.entities

import com.aptopayments.mobile.data.card.Custodian
import com.google.gson.annotations.SerializedName

internal class CustodianEntity(

    @SerializedName("name")
    var name: String = "",

    @SerializedName("logo")
    var logo: String = "",

    @SerializedName("custodian_type")
    var type: String = "",

    @SerializedName("id")
    var id: String = ""
) {
    fun toCustodian() = Custodian(
        name = name,
        logo = logo,
        type = type,
        id = id
    )
}