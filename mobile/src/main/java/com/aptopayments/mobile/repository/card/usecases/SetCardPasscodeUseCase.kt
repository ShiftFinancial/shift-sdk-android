package com.aptopayments.mobile.repository.card.usecases

import com.aptopayments.mobile.interactor.UseCase
import com.aptopayments.mobile.network.NetworkHandler
import com.aptopayments.mobile.repository.card.CardRepository

internal class SetCardPasscodeUseCase(
    private val repository: CardRepository,
    networkHandler: NetworkHandler
) : UseCase<Unit, SetCardPasscodeUseCase.Params>(networkHandler) {
    override fun run(params: Params) =
        repository.setCardPasscode(
            cardId = params.cardId,
            passcode = params.passcode,
            verificationId = params.verificationId
        )

    data class Params(val cardId: String, val passcode: String, val verificationId: String?)
}
