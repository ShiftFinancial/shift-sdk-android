package com.aptopayments.mobile.data.card

private const val CODE_COUNTRY_UNSUPPORTED = 90191
private const val CODE_REGION_UNSUPPORTED = 90192
private const val CODE_ADDRESS_UNVERIFIED = 90193
private const val CODE_CURRENCY_UNSUPPORTED = 90194
private const val CODE_CANNOT_CAPTURE_FUNDS = 90195
private const val CODE_INSUFFICIENT_FUNDS = 90196
private const val CODE_BALANCE_NOT_FOUND = 90214
private const val CODE_ACCESS_TOKEN_INVALID = 90215
private const val CODE_SCOPES_REQUIRED = 90216
private const val CODE_LEGAL_NAME_MISSING = 90222
private const val CODE_DATE_OF_BIRTH_MISSING = 90223
private const val CODE_DATE_OF_BIRTH_ERROR = 90224
private const val CODE_ADDRESS_MISSING = 90225
private const val CODE_EMAIL_MISSING = 90226
private const val CODE_EMAIL_ERROR = 90227
private const val CODE_BALANCE_VALIDATIONS_EMAIL_SENDS_DISABLED = 200040
private const val CODE_BALANCE_VALIDATIONS_INSUFFICIENT_APPLICATION_LIMIT = 200041
private const val CODE_IDENTITY_NOT_VERIFIED = 200046

enum class SelectBalanceStoreError {
    COUNTRY_UNSUPPORTED,
    REGION_UNSUPPORTED,
    ADDRESS_UNVERIFIED,
    CURRENCY_UNSUPPORTED,
    CANNOT_CAPTURE_FUNDS,
    INSUFFICIENT_FUNDS,
    BALANCE_NOT_FOUND,
    ACCESS_TOKEN_INVALID,
    SCOPES_REQUIRED,
    LEGAL_NAME_MISSING,
    DATE_OF_BIRTH_MISSING,
    DATE_OF_BIRTH_ERROR,
    ADDRESS_MISSING,
    EMAIL_MISSING,
    EMAIL_ERROR,
    BALANCE_VALIDATIONS_EMAIL_SENDS_DISABLED,
    BALANCE_VALIDATIONS_INSUFFICIENT_APPLICATION_LIMIT,
    IDENTITY_NOT_VERIFIED,
    UNKNOWN;

    companion object {
        fun parseError(errorCode: Int?): SelectBalanceStoreError {
            return when (errorCode) {
                CODE_COUNTRY_UNSUPPORTED -> COUNTRY_UNSUPPORTED
                CODE_REGION_UNSUPPORTED -> REGION_UNSUPPORTED
                CODE_ADDRESS_UNVERIFIED -> ADDRESS_UNVERIFIED
                CODE_CURRENCY_UNSUPPORTED -> CURRENCY_UNSUPPORTED
                CODE_CANNOT_CAPTURE_FUNDS -> CANNOT_CAPTURE_FUNDS
                CODE_INSUFFICIENT_FUNDS -> INSUFFICIENT_FUNDS
                CODE_BALANCE_NOT_FOUND -> BALANCE_NOT_FOUND
                CODE_ACCESS_TOKEN_INVALID -> ACCESS_TOKEN_INVALID
                CODE_SCOPES_REQUIRED -> SCOPES_REQUIRED
                CODE_LEGAL_NAME_MISSING -> LEGAL_NAME_MISSING
                CODE_DATE_OF_BIRTH_MISSING -> DATE_OF_BIRTH_MISSING
                CODE_DATE_OF_BIRTH_ERROR -> DATE_OF_BIRTH_ERROR
                CODE_ADDRESS_MISSING -> ADDRESS_MISSING
                CODE_EMAIL_MISSING -> EMAIL_MISSING
                CODE_EMAIL_ERROR -> EMAIL_ERROR
                CODE_BALANCE_VALIDATIONS_EMAIL_SENDS_DISABLED -> BALANCE_VALIDATIONS_EMAIL_SENDS_DISABLED
                CODE_BALANCE_VALIDATIONS_INSUFFICIENT_APPLICATION_LIMIT -> BALANCE_VALIDATIONS_INSUFFICIENT_APPLICATION_LIMIT
                CODE_IDENTITY_NOT_VERIFIED -> IDENTITY_NOT_VERIFIED
                else -> UNKNOWN
            }
        }
    }
}
