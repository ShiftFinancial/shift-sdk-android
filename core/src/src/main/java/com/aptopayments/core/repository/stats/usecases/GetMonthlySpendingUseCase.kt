package com.aptopayments.core.repository.stats.usecases

import com.aptopayments.core.data.stats.MonthlySpending
import com.aptopayments.core.interactor.UseCase
import com.aptopayments.core.network.NetworkHandler
import com.aptopayments.core.repository.stats.StatsRepository

internal class GetMonthlySpendingUseCase constructor(
        private val repository: StatsRepository,
        networkHandler: NetworkHandler
) : UseCase<MonthlySpending, GetMonthlySpendingUseCase.Params>(networkHandler) {
    override fun run(params: Params) = repository.getMonthlySpending(
            params.cardId, params.month, params.year)

    data class Params (
            val cardId: String,
            val month: String,
            val year: String
    )
}
