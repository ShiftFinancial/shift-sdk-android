package com.aptopayments.core.interactor

import android.app.Activity
import com.aptopayments.core.UnitTest
import com.aptopayments.core.functional.Either.Right
import com.aptopayments.core.network.NetworkHandler
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Test

class UseCaseTest : UnitTest() {

    private val TYPE_TEST = "Test"
    private val TYPE_PARAM = "ParamTest"

    private val useCase = MyUseCase()

    @Test fun `running use case should return 'Either' of use case type`() {
        val params = MyParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }

        result shouldEqual Right(MyType(TYPE_TEST))
    }

    data class MyType(val name: String)
    data class MyParams(val name: String)

    private inner class MyUseCase : UseCase<MyType, MyParams>(NetworkHandler(Activity())) {
        override fun run(params: MyParams) = Right(MyType(TYPE_TEST))
    }

}
