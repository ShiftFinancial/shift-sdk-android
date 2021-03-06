package com.aptopayments.mobile.data.card

import java.util.Locale

enum class ProvisioningTokenServiceProvider {
    TOKEN_PROVIDER_VISA, TOKEN_PROVIDER_MASTERCARD, TOKEN_PROVIDER_AMEX;

    companion object {
        fun fromString(str: String) = valueOf(str.toUpperCase(Locale.US))
    }
}
