package com.aptopayments.core.repository.fundingsources

import com.aptopayments.core.data.fundingsources.Balance
import com.aptopayments.core.exception.Failure
import com.aptopayments.core.functional.Either
import com.aptopayments.core.network.ListEntity
import com.aptopayments.core.network.NetworkHandler
import com.aptopayments.core.platform.BaseRepository
import com.aptopayments.core.repository.fundingsources.local.BalanceLocalDao
import com.aptopayments.core.repository.fundingsources.local.entities.BalanceLocalEntity
import com.aptopayments.core.repository.fundingsources.remote.FundingSourcesService
import com.aptopayments.core.repository.fundingsources.remote.entities.BalanceEntity
import javax.inject.Inject

internal interface FundingSourceRepository : BaseRepository {

    fun getFundingSources(accountId: String, refresh: Boolean): Either<Failure, List<Balance>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: FundingSourcesService,
                        private val balanceLocalDao: BalanceLocalDao
    ) : BaseRepository.BaseRepositoryImpl(),
            FundingSourceRepository {

        override fun getFundingSources(accountId: String, refresh: Boolean): Either<Failure, List<Balance>> {
            if (refresh) {
                return getFundingSourcesFromRemoteAPI(accountId)
            }
            else {
                balanceLocalDao.getBalances()?.let { localBalances ->
                    return Either.Right(localBalances.map { it.toBalance() })
                } ?: return getFundingSourcesFromRemoteAPI(accountId)
            }
        }

        private fun getFundingSourcesFromRemoteAPI(accountId: String): Either<Failure, List<Balance>> {
            return when (networkHandler.isConnected) {
                true -> {
                    request(service.getFundingSources(accountId), { listEntity: ListEntity<BalanceEntity> ->
                        val balanceList = listEntity.data?.map {
                            it.toBalance()
                        } ?: emptyList()
                        balanceLocalDao.clearBalanceCache()
                        balanceLocalDao.saveBalances(balanceList.map { BalanceLocalEntity.fromBalance(it) })
                        balanceList
                    }, ListEntity())
                }
                false, null -> Either.Left(Failure.NetworkConnection)
            }
        }
    }
}