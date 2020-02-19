package com.aptopayments.core.repository.config.remote.entities

import com.aptopayments.core.data.config.Branding
import com.google.gson.annotations.SerializedName

internal data class BrandingEntity(
    @SerializedName("light")
    val light: ProjectBrandingEntity = ProjectBrandingEntity(),

    @SerializedName("dark")
    val dark: ProjectBrandingEntity = ProjectBrandingEntity()
) {
    fun toBranding() = Branding(light.toProjectBranding(), dark.toProjectBranding())
}
