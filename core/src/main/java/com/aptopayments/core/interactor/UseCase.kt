package com.aptopayments.core.interactor

import androidx.annotation.VisibleForTesting
import com.aptopayments.core.exception.Failure
import com.aptopayments.core.functional.Either
import com.aptopayments.core.network.NetworkHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * and will post the result in the UI thread.
 */
@VisibleForTesting(otherwise = Modifier.PROTECTED)
internal abstract class UseCase<out Type, in Params> (val networkHandler: NetworkHandler) where Type : Any {

    abstract fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        GlobalScope.launch {
            runUseCase(params) { result ->
                GlobalScope.launch(Dispatchers.Main) { onResult(result) }
            }
        }
    }

    private fun runUseCase(params: Params, onResult: (Either<Failure, Type>) -> Unit) {
        run(params).either({ failure ->
            when (failure) {
                is Failure.NetworkConnection -> {
                    networkHandler.subscribeNetworkReachabilityListener(this) { available ->
                        if (available) {
                            networkHandler.unsubscribeNetworkReachabilityListener(this)
                            runUseCase(params, onResult)
                        }
                    }
                    networkHandler.networkNotReachable()
                }
                is Failure.MaintenanceMode -> {
                    networkHandler.subscribeMaintenanceListener(this) { available ->
                        if (available) {
                            networkHandler.unsubscribeMaintenanceListener(this)
                            runUseCase(params, onResult)
                        }
                    }
                    networkHandler.maintenanceModeDetected()
                }
                is Failure.DeprecatedSDK -> { networkHandler.deprecatedSdkDetected() }
                else -> onResult(Either.Left(failure))
            }
        }, { value ->
            onResult(Either.Right(value))
        })
    }
}
