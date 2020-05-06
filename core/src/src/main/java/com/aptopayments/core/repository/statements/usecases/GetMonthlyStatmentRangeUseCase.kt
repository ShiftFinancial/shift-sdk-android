package com.aptopayments.core.repository.statements.usecases

import com.aptopayments.core.data.statements.MonthlyStatementPeriod
import com.aptopayments.core.interactor.UseCase
import com.aptopayments.core.network.NetworkHandler
import com.aptopayments.core.repository.statements.MonthlyStatementRepository

internal class GetMonthlyStatementPeriodUseCase constructor(
    private val repository: MonthlyStatementRepository,
    networkHandler: NetworkHandler
) : UseCase<MonthlyStatementPeriod, Unit>(networkHandler) {

    override fun run(params: Unit) = repository.getMonthlyStatementPeriod()

}
