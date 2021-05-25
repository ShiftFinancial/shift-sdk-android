package com.aptopayments.mobile.di

import android.app.Application
import android.content.Context
import com.aptopayments.mobile.db.DataBaseProvider
import com.aptopayments.mobile.network.ConnectivityChecker
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class KoinModulesTest : KoinTest {

    private val app: Application = mock()
    private val context: Context = mock() {
        on { applicationContext } doReturn app
    }
    private val dbProvider: DataBaseProvider = mock()

    private val mockedAndroidContext = module {
        single { context }
        single { app }
        single(override = true) { dbProvider }
        single(override = true) { mock<ConnectivityChecker>() }
    }

    @Test
    fun checkAllModules() = checkModules {
        modules(applicationModule, repositoryModule, useCasesModule, mockedAndroidContext)
    }
}
