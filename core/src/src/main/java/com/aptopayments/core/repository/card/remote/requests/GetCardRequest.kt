package com.aptopayments.core.repository.card.remote.requests

import java.io.Serializable

data class GetCardRequest(
        val accountID: String,
        val showDetails: Boolean = false
) : Serializable