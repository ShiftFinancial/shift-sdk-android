package com.aptopayments.mobile.repository.user.remote

import com.aptopayments.mobile.data.user.DataPointList
import com.aptopayments.mobile.network.ApiCatalog
import com.aptopayments.mobile.platform.BaseService
import com.aptopayments.mobile.repository.user.remote.entities.NotificationPreferencesEntity
import com.aptopayments.mobile.repository.user.remote.entities.UserEntity
import com.aptopayments.mobile.repository.user.remote.requests.*
import retrofit2.Call
import java.net.URLEncoder

private const val CHARSET = "UTF_8"

internal class UserService constructor(apiCatalog: ApiCatalog) : BaseService() {

    private val userApi by lazy { apiCatalog.api().create(UserApi::class.java) }

    fun createUser(userData: DataPointList, custodianUid: String?, metadata: String?): Call<UserEntity> {
        val request = CreateUserDataRequest.from(userData, custodianUid, metadata)
        return userApi.createUser(request = request)
    }

    fun updateUser(userData: DataPointList): Call<UserEntity> {
        val request = UserDataRequest.from(userData)
        return userApi.updateUser(request = request)
    }

    fun loginUser(loginUserRequest: LoginUserRequest): Call<UserEntity> =
        userApi.loginExistingUser(request = loginUserRequest)

    fun registerPushDevice(pushDeviceRequest: PushDeviceRequest): Call<Unit> =
        userApi.registerPushDevice(request = pushDeviceRequest)

    fun unregisterPushDevice(userToken: String, pushToken: String): Call<Unit> =
        userApi.unregisterPushDevice(
            userToken = authorizationHeader(userToken),
            pushToken = URLEncoder.encode(pushToken, CHARSET)
        )

    fun getNotificationPreferences(): Call<NotificationPreferencesEntity> = userApi.getNotificationPreferences()

    fun updateNotificationPreferences(notificationPreferencesRequest: NotificationPreferencesRequest): Call<NotificationPreferencesEntity> =
        userApi.updateNotificationPreferences(request = notificationPreferencesRequest)

    private fun authorizationHeader(userToken: String) = "Bearer $userToken"
}