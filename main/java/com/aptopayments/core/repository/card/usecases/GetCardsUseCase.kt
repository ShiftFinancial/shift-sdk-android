package com.aptopayments.core.repository.card.usecases

import androidx.annotation.VisibleForTesting
import com.aptopayments.core.data.card.Card
import com.aptopayments.core.interactor.UseCase
import com.aptopayments.core.network.NetworkHandler
import com.aptopayments.core.repository.card.CardRepository
import java.lang.reflect.Modifier

@VisibleForTesting(otherwise = Modifier.PROTECTED)
internal class GetCardsUseCase constructor(
        private val repository: CardRepository,
        networkHandler: NetworkHandler
) : UseCase<List<Card>, Unit>(networkHandler) {
    override fun run(params: Unit) = repository.getCards()
}