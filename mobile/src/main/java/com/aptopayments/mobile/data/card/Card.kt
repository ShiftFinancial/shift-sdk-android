package com.aptopayments.mobile.data.card

import java.io.Serializable
import java.util.Locale

data class Card(
    val accountID: String,
    val cardProductID: String?,
    val cardNetwork: CardNetwork,
    val lastFourDigits: String,
    val cardBrand: String?,
    val cardIssuer: String?,
    val state: CardState,
    val isWaitlisted: Boolean?,
    val cardStyle: CardStyle?,
    val kycStatus: KycStatus?,
    val kycReason: List<String>?,
    val orderedStatus: OrderedStatus,
    val spendableAmount: Money?,
    val nativeSpendableAmount: Money?,
    val cardHolder: String,
    val features: Features?,
    val metadata: String?
) : Serializable {

    enum class CardState {
        ACTIVE,
        INACTIVE,
        CANCELLED,
        CREATED,
        UNKNOWN
    }

    enum class CardNetwork {
        VISA,
        MASTERCARD,
        AMEX,
        DISCOVER,
        TEST,
        UNKNOWN;

        companion object {
            fun fromString(value: String): CardNetwork {
                return try {
                    valueOf(value.toUpperCase(Locale.US))
                } catch (e: IllegalArgumentException) {
                    UNKNOWN
                }
            }
        }
    }

    enum class OrderedStatus {
        NOT_APPLICABLE,
        AVAILABLE,
        ORDERED,
        RECEIVED,
        UNKNOWN
    }
}
