package com.aptopayments.core.extension

import java.net.URL

internal fun parseURL(value: String): URL? {
    if (value.isEmpty()) { return null }
    return URL(value)
}
