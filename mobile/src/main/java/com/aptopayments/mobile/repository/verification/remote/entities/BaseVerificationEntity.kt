package com.aptopayments.mobile.repository.verification.remote.entities

import com.aptopayments.mobile.data.user.VerificationStatus
import java.util.Locale

internal interface BaseVerificationEntity {
    val verificationType: String
    val verificationId: String
    val status: String

    fun parseStatus(status: String): VerificationStatus {
        return try {
            VerificationStatus.valueOf(status.toUpperCase(Locale.US))
        } catch (exception: Throwable) {
            VerificationStatus.PENDING
        }
    }
}