package com.aptopayments.mobile.repository.stats.remote.entities

import com.aptopayments.mobile.data.stats.CategorySpending
import com.aptopayments.mobile.data.stats.MonthlySpending
import com.aptopayments.mobile.network.ListEntity
import com.google.gson.annotations.SerializedName

internal data class MonthlySpendingEntity(

    @SerializedName("prev_spending_exists")
    val prevSpendingExists: Boolean = false,

    @SerializedName("next_spending_exists")
    val nextSpendingExists: Boolean = false,

    @SerializedName("spending")
    val spending: ListEntity<CategorySpendingEntity>? = null

) {
    fun toMonthlySpending() = MonthlySpending(
        prevSpendingExists = prevSpendingExists,
        nextSpendingExists = nextSpendingExists,
        spending = parseSpending()
    )

    private fun parseSpending(): List<CategorySpending> {
        return spending?.data?.let {
            it.map { categorySpending -> categorySpending.toCategorySpending() }
        } ?: emptyList()
    }
}
