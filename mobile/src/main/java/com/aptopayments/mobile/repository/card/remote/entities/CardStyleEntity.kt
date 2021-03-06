package com.aptopayments.mobile.repository.card.remote.entities

import com.aptopayments.mobile.data.card.CardStyle
import com.aptopayments.mobile.extension.ColorParserImpl
import com.google.gson.annotations.SerializedName
import java.net.MalformedURLException
import java.net.URL

internal const val DEFAULT_TEXT_CARD_COLOR = "FFFFFF"

internal data class CardStyleEntity(

    @SerializedName("background")
    val background: CardBackgroundStyleEntity?,

    @SerializedName("text_color")
    val textColor: String? = DEFAULT_TEXT_CARD_COLOR,

    @SerializedName("balance_selector_asset")
    val balanceSelectorAsset: String? = null
) {
    fun toCardStyle(): CardStyle? {
        val colorParser = ColorParserImpl()

        return background?.let {
            it.toCardBackgroundStyle()?.let { cardBackgroundStyle ->
                CardStyle(
                    background = cardBackgroundStyle,
                    textColor = colorParser.fromHexString(textColor, DEFAULT_TEXT_CARD_COLOR),
                    balanceSelectorAsset = parseAssetUrl(balanceSelectorAsset)
                )
            }
        }
    }

    private fun parseAssetUrl(url: String?): URL? = url?.let {
        try {
            URL(it)
        } catch (e: MalformedURLException) {
            null
        }
    }

    companion object {
        fun from(cardStyle: CardStyle?): CardStyleEntity? {
            return cardStyle?.let {
                CardStyleEntity(
                    background = CardBackgroundStyleEntity.from(cardStyle.background),
                    textColor = cardStyle.textColor?.toString(16),
                    balanceSelectorAsset = cardStyle.balanceSelectorAsset.toString()
                )
            }
        }
    }
}
