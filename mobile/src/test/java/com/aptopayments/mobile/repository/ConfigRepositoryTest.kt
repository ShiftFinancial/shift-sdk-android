package com.aptopayments.mobile.repository

import com.aptopayments.mobile.UnitTest
import com.aptopayments.mobile.data.TestDataProvider
import com.aptopayments.mobile.exception.Failure.NetworkConnection
import com.aptopayments.mobile.extension.shouldBeLeftAndInstanceOf
import com.aptopayments.mobile.functional.right
import com.aptopayments.mobile.network.ListEntity
import com.aptopayments.mobile.network.NetworkHandler
import com.aptopayments.mobile.platform.ErrorHandler
import com.aptopayments.mobile.platform.RequestExecutor
import com.aptopayments.mobile.repository.config.ConfigRepository
import com.aptopayments.mobile.repository.config.remote.ConfigService
import com.aptopayments.mobile.repository.config.remote.entities.CardConfigurationEntity
import com.aptopayments.mobile.repository.config.remote.entities.CardProductSummaryEntity
import com.aptopayments.mobile.repository.config.remote.entities.ContextConfigurationEntity
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response
import kotlin.test.assertEquals

class ConfigRepositoryTest : UnitTest() {

    private lateinit var requestExecutor: RequestExecutor
    private lateinit var sut: ConfigRepository.Network

    @Mock
    private lateinit var networkHandler: NetworkHandler

    @Mock
    private lateinit var service: ConfigService

    @Mock
    private lateinit var getConfigCall: Call<ContextConfigurationEntity>

    @Mock
    private lateinit var getConfigResponse: Response<ContextConfigurationEntity>

    @Mock
    private lateinit var contextConfigurationEntity: ContextConfigurationEntity

    @Mock
    private lateinit var getCardConfigCall: Call<CardConfigurationEntity>

    @Mock
    private lateinit var getCardConfigResponse: Response<CardConfigurationEntity>

    @Mock
    private lateinit var getCardProductsCall: Call<ListEntity<CardProductSummaryEntity>>

    @Mock
    private lateinit var getCardProductsResponse: Response<ListEntity<CardProductSummaryEntity>>

    @Before
    override fun setUp() {
        super.setUp()
        requestExecutor = RequestExecutor(networkHandler, ErrorHandler(mock()))
        startKoin {
            modules(module {
                single { networkHandler }
                single { requestExecutor }
                single { service }
            })
        }
        sut = ConfigRepository.Network(networkHandler, service)
    }

    @Test
    fun `Get context configuration should delegate to the service`() {
        val testContextConfiguration = TestDataProvider.provideContextConfiguration()
        given { networkHandler.isConnected }.willReturn(true)
        given { getConfigResponse.body() }.willReturn(contextConfigurationEntity)
        given { getConfigResponse.isSuccessful }.willReturn(true)
        given { getConfigCall.execute() }.willReturn(getConfigResponse)
        given { service.getContextConfiguration() }.willReturn(getConfigCall)
        given { contextConfigurationEntity.toContextConfiguration() }.willReturn(testContextConfiguration)

        val contextConfig = sut.getContextConfiguration()

        assertEquals(testContextConfiguration.right(), contextConfig)

        verify(service).getContextConfiguration()
    }

    @Test
    fun `Get context configuration should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val result = sut.getContextConfiguration()

        result.shouldBeLeftAndInstanceOf(NetworkConnection::class.java)
        verifyZeroInteractions(service)
    }

    @Test
    fun `Get card configuration should delegate to the service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { getCardConfigResponse.body() }.willReturn(CardConfigurationEntity())
        given { getCardConfigResponse.isSuccessful }.willReturn(true)
        given { getCardConfigCall.execute() }.willReturn(getCardConfigResponse)
        given { service.getCardProduct(cardProductId = "") }.willReturn(getCardConfigCall)

        sut.getCardProduct(cardProductId = "")
        verify(service).getCardProduct(cardProductId = "")
    }

    @Test
    fun `Get card configuration should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val result = sut.getCardProduct(cardProductId = "")

        result.shouldBeLeftAndInstanceOf(NetworkConnection::class.java)
        verifyZeroInteractions(service)
    }

    @Test
    fun `Get card products should delegate to the service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { getCardProductsResponse.body() }.willReturn(ListEntity())
        given { getCardProductsResponse.isSuccessful }.willReturn(true)
        given { getCardProductsCall.execute() }.willReturn(getCardProductsResponse)
        given { service.getCardProducts() }.willReturn(getCardProductsCall)

        sut.getCardProducts()
        verify(service).getCardProducts()
    }

    @Test
    fun `Get card products should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val result = sut.getCardProducts()

        result.shouldBeLeftAndInstanceOf(NetworkConnection::class.java)
        verifyZeroInteractions(service)
    }
}