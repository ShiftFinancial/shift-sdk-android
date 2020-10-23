package com.aptopayments.mobile.exception

import com.aptopayments.mobile.exception.Failure.FeatureFailure
import com.aptopayments.mobile.extension.localized
import org.json.JSONObject

private const val UNKNOWN_SESSION = 3030
private const val SESSION_EXPIRED = 3031
private const val INVALID_SESSION = 3032
private const val EMPTY_SESSION = 3033
private const val LOGIN_ERROR_INVALID_CREDENTIALS = 90028
private const val LOGIN_ERROR_UNVERIFIED_DATAPOINTS = 90032
private const val SHIFT_CARD_ACTIVATE_ERROR = 90173
private const val SHIFT_CARD_ENABLE_ERROR = 90174
private const val SHIFT_CARD_DISABLE_ERROR = 90175
private const val PRIMARY_FUNDING_SOURCE_NOT_FOUND = 90197
private const val SHIFT_ACTIVATE_PHYSICAL_CARD_ERROR = 90206
private const val WRONG_PHYSICAL_CARD_ACTIVATION_CODE = 90207
private const val TOO_MANY_PHYSICAL_CARD_ACTIVATION_ATTEMPTS = 90208
private const val PHYSICAL_CARD_ALREADY_ACTIVATED = 90209
private const val PHYSICAL_CARD_ACTIVATION_NOT_SUPPORTED = 90210
private const val INVALID_PHYSICAL_CARD_ACTIVATION_CODE = 90211
private const val REVOKED_TOKEN = 90251
private const val INPUT_PHONE_REQUIRED = 200013
private const val INPUT_PHONE_INVALID = 200014
private const val INPUT_PHONE_NOT_ALLOWED = 200015
private const val SIGNUP_NOT_ALLOWED = 200016
private const val FIRST_NAME_REQUIRED = 200017
private const val FIRST_NAME_INVALID = 200018
private const val LAST_NAME_REQUIRED = 200019
private const val LAST_NAME_INVALID = 200020
private const val EMAIL_INVALID = 200023
private const val EMAIL_NOT_ALLOWED = 200024
private const val DOB_REQUIRED = 200025
private const val DOB_TOO_YOUNG = 200026
private const val ID_DOCUMENT_INVALID = 200027
private const val ADDRESS_INVALID = 200028
private const val POSTAL_CODE_INVALID = 200029
private const val LOCALITY_INVALID = 200030
private const val REGION_INVALID = 200031
private const val COUNTRY_INVALID = 200032
private const val DOB_INVALID = 200035
private const val CARD_ALREADY_ISSUED = 200036
private const val BALANCE_VALIDATIONS_EMAIL_SENDS_DISABLED = 200040
private const val BALANCE_VALIDATIONS_INSUFFICIENT_APPLICATION_LIMIT = 200041
private const val CAN_NOT_SEND_SMS = 9213
private const val INVALID_PHONE_NUMBER = 9214
private const val UNREACHABLE_PHONE_NUMBER = 9215
private const val INVALID_CALLED_PHONE_NUMBER = 9216
private const val INSUFFICIENT_FUNDS = 90196
private const val STATEMENT_URL_NOT_GENERATED = 200043
private const val STATEMENT_NOT_UPLOADED = 200044
private const val STATEMENT_URL_NOT_GENERATED2 = 200045
private const val STATEMENT_GENERATING_ERROR = 200051
private const val UNDEFINED_MESSAGE = "error.transport.undefined"

private const val INVALID_PAYMENT_SOURCE_CARD_TYPE = 200058
private const val INVALID_PAYMENT_SOURCE_CARD_NUMBER = 200059
private const val INVALID_PAYMENT_SOURCE_CARD_CVV = 200060
private const val INVALID_PAYMENT_SOURCE_CARD_EXPIRATION = 200061
private const val INVALID_PAYMENT_SOURCE_CARD_POSTAL_CODE = 200062
private const val INVALID_PAYMENT_SOURCE_CARD_ADDRESS = 200063
private const val INVALID_PAYMENT_SOURCE_CARD_ENTITY = 200064
private const val INVALID_PAYMENT_SOURCE_AMOUNT = 200065
private const val INVALID_PAYMENT_SOURCE_CURRENCY = 200066
private const val PAYMENT_SOURCE_ADD_LIMIT = 200067
private const val INVALID_PAYMENT_SOURCE_CARD_NETWORK = 200068

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    fun errorMessage(): String = getErrorKey().localized()

    open fun getErrorKey() = ""

    object NetworkConnection : Failure() {
        override fun getErrorKey() = "no_network_description"
    }

    object MaintenanceMode : Failure() {
        override fun getErrorKey() = "maintenance_description"
    }

    object DeprecatedSDK : Failure()
    object UserSessionExpired : Failure() {
        override fun getErrorKey() = "session_expired_error"
    }

    class ServerError(val code: Int?, private val message: String? = null) : Failure() {

        fun hasUndefinedKey() = getErrorKey() == UNDEFINED_MESSAGE

        fun isErrorBalanceValidationsEmailSendsDisabled() = code == BALANCE_VALIDATIONS_EMAIL_SENDS_DISABLED

        fun isErrorBalanceValidationsInsufficientApplicationLimit() =
            code == BALANCE_VALIDATIONS_INSUFFICIENT_APPLICATION_LIMIT

        fun isErrorInsufficientFunds() = code == INSUFFICIENT_FUNDS

        override fun getErrorKey(): String {
            return when (this.code) {
                UNKNOWN_SESSION, INVALID_SESSION -> "error.transport.invalid_session"
                SESSION_EXPIRED -> "error.transport.session_expired"
                EMPTY_SESSION -> "error.transport.empty_session"
                LOGIN_ERROR_INVALID_CREDENTIALS -> "error.transport.login_error_invalid_credentials"
                LOGIN_ERROR_UNVERIFIED_DATAPOINTS -> "error.transport.login_error_unverified_datapoints"
                SHIFT_CARD_ACTIVATE_ERROR -> "error.transport.shift_card_activate_error"
                SHIFT_CARD_ENABLE_ERROR -> "error.transport.shift_card_enable_error"
                SHIFT_CARD_DISABLE_ERROR -> "error.transport.shift_card_disable_error"
                PRIMARY_FUNDING_SOURCE_NOT_FOUND -> "error.transport.primary_funding_source_not_found"
                SHIFT_ACTIVATE_PHYSICAL_CARD_ERROR, PHYSICAL_CARD_ACTIVATION_NOT_SUPPORTED -> "error.transport.physical_card_activation_not_supported"
                WRONG_PHYSICAL_CARD_ACTIVATION_CODE, INVALID_PHYSICAL_CARD_ACTIVATION_CODE -> "error.transport.wrong_physical_card_activation_code"
                REVOKED_TOKEN -> "issue_card.issue_card.error.token_revoked"
                TOO_MANY_PHYSICAL_CARD_ACTIVATION_ATTEMPTS -> "error.transport.too_many_physical_card_activation_attempts"
                PHYSICAL_CARD_ALREADY_ACTIVATED -> "error.transport.physical_card_already_activated"
                INPUT_PHONE_REQUIRED -> "issue_card.issue_card.error.required_phone"
                INPUT_PHONE_INVALID -> "issue_card.issue_card.error.invalid_phone"
                INPUT_PHONE_NOT_ALLOWED -> "issue_card.issue_card.error.not_allowed_phone"
                SIGNUP_NOT_ALLOWED -> "issue_card.issue_card.error.signup_not_allowed"
                FIRST_NAME_REQUIRED -> "issue_card.issue_card.error.required_first_name"
                FIRST_NAME_INVALID -> "issue_card.issue_card.error.invalid_first_name"
                LAST_NAME_REQUIRED -> "issue_card.issue_card.error.required_last_name"
                LAST_NAME_INVALID -> "issue_card.issue_card.error.invalid_last_name"
                EMAIL_INVALID -> "issue_card.issue_card.error.invalid_email"
                EMAIL_NOT_ALLOWED -> "issue_card.issue_card.error.not_allowed_email"
                DOB_REQUIRED -> "issue_card.issue_card.error.required_dob"
                DOB_TOO_YOUNG -> "issue_card.issue_card.error.dob_too_young"
                ID_DOCUMENT_INVALID -> "issue_card.issue_card.error.invalid_id_document"
                ADDRESS_INVALID -> "issue_card.issue_card.error.invalid_address"
                POSTAL_CODE_INVALID -> "issue_card.issue_card.error.invalid_postal_code"
                LOCALITY_INVALID -> "issue_card.issue_card.error.invalid_locality"
                REGION_INVALID -> "issue_card.issue_card.error.invalid_region"
                COUNTRY_INVALID -> "issue_card.issue_card.error.invalid_country"
                CARD_ALREADY_ISSUED -> "issue_card.issue_card.error.card_already_issued"
                DOB_INVALID -> "issue_card.issue_card.error.invalid_dob"
                BALANCE_VALIDATIONS_EMAIL_SENDS_DISABLED -> "select_balance_store.login.error_email_sends_disabled.message"
                BALANCE_VALIDATIONS_INSUFFICIENT_APPLICATION_LIMIT -> "select_balance_store.login.error_insufficient_application_limit.message"
                CAN_NOT_SEND_SMS -> "auth.input_phone.error.can_not_send_sms"
                INVALID_PHONE_NUMBER -> "auth.input_phone.error.invalid_phone_number"
                UNREACHABLE_PHONE_NUMBER -> "auth.input_phone.error.unreachable_phone_number"
                INVALID_CALLED_PHONE_NUMBER -> "auth.input_phone.error.invalid_called_phone_number"
                STATEMENT_URL_NOT_GENERATED, STATEMENT_NOT_UPLOADED, STATEMENT_URL_NOT_GENERATED2,
                STATEMENT_GENERATING_ERROR -> "monthly_statements.list.error_generating_report.message"
                INVALID_PAYMENT_SOURCE_CARD_NETWORK, INVALID_PAYMENT_SOURCE_CARD_TYPE, INVALID_PAYMENT_SOURCE_CARD_ENTITY -> "load_funds_add_card_error_message"
                INVALID_PAYMENT_SOURCE_CARD_NUMBER -> "load_funds_add_card_error_number"
                INVALID_PAYMENT_SOURCE_CARD_CVV -> "load_funds_add_card_error_cvv"
                INVALID_PAYMENT_SOURCE_CARD_EXPIRATION -> "load_funds_add_card_error_expiration"
                INVALID_PAYMENT_SOURCE_CARD_POSTAL_CODE -> "load_funds_add_card_error_postal_code"
                INVALID_PAYMENT_SOURCE_CARD_ADDRESS -> "load_funds_add_card_error_address"
                INVALID_PAYMENT_SOURCE_AMOUNT -> "load_funds_add_money_error_limit"
                INVALID_PAYMENT_SOURCE_CURRENCY -> "load_funds_add_money_error_message"
                PAYMENT_SOURCE_ADD_LIMIT -> "load_funds_add_card_error_limit"
                else -> UNDEFINED_MESSAGE
            }
        }

        fun isOauthTokenRevokedError() = code == REVOKED_TOKEN

        fun toJSonObject(): JSONObject {
            val errorKey = getErrorKey()
            val rawCode = if (errorKey == UNDEFINED_MESSAGE) "" else (code?.toString() ?: "")

            val json = JSONObject()
                .put("code", code)
                .put("message", errorKey.localized())
                .put("raw_code", rawCode)

            addErrorTracking255CharactersRestriction(json, message)
            return json
        }

        private fun addErrorTracking255CharactersRestriction(json: JSONObject, message: String?) {
            if (!message.isNullOrEmpty()) {
                message.chunked(255).take(7).forEachIndexed { index, chunk -> json.put("reason$index", chunk) }
            }
        }
    }

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure(private val message: String = "", private val title: String = "") : Failure() {
        override fun getErrorKey() = message
        fun getErrorTitleKey() = title
    }
}