package com.aptopayments.mobile.data.config

import java.util.Locale

enum class UIStatusBarStyle {
    LIGHT, DARK, AUTO;

    companion object {
        fun parseStatusBarStyle(style: String): UIStatusBarStyle {
            return try {
                valueOf(style.toUpperCase(Locale.US))
            } catch (exception: IllegalArgumentException) {
                AUTO
            }
        }
    }
}
