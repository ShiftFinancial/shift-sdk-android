package com.shiftpayments.link.sdk.wrappers.retrofit.services;

import com.google.gson.JsonObject;
import com.shiftpayments.link.sdk.api.vos.requests.users.AcceptDisclaimerRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.users.DeleteUserRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.users.LoginRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.users.RegisterPushNotificationsRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.users.StartOAuthRequestVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.CreateUserResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.CurrentUserResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.LoginUserResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.OAuthStatusResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.StartOAuthResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.UserResponseVo;
import com.shiftpayments.link.sdk.api.wrappers.ShiftApiWrapper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * User related API calls.
 * @author Wijnand
 */
public interface UserService {
    /**
     * Creates a {@link Call} to create a new user.
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.CREATE_USER_PATH)
    Call<CreateUserResponseVo> createUser(@Body JsonObject data);

    /**
     * Creates a {@link Call} to update an existing user.
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @PUT(ShiftApiWrapper.UPDATE_USER_PATH)
    Call<UserResponseVo> updateUser(@Body JsonObject data);

    /**
     * Creates a {@link Call} to login an existing user.
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.LOGIN_USER_PATH)
    Call<LoginUserResponseVo> loginUser(@Body LoginRequestVo data);

    /**
     * Creates a {@link Call} to get the current user info.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.GET_CURRENT_USER_PATH)
    Call<CurrentUserResponseVo> getCurrentUser();

    /**
     * Creates a {@link Call} to login an existing user.
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.DELETE_USER_PATH)
    Call<Void> deleteUser(@Body DeleteUserRequestVo data);

    /**
     * Creates a {@link Call} to register a push notification token.
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.REGISTER_PUSH_NOTIFICATION_TOKEN_PATH)
    Call<ResponseBody> registerPushNotifications(@Body RegisterPushNotificationsRequestVo data);

    /**
     * Creates a {@link Call} to start oAuth
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.OAUTH_START_PATH)
    Call<StartOAuthResponseVo> startOAuth(@Body StartOAuthRequestVo data);

    /**
     * Creates a {@link Call} to get the status of the given OAuth ID
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.OAUTH_STATUS_PATH)
    Call<OAuthStatusResponseVo> getOAuthStatus(@Path("ID") String id);

    /**
     * Creates a {@link Call} to start oAuth
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.ACCEPT_DISCLAIMER_PATH)
    Call<ResponseBody> acceptDisclaimer(@Body AcceptDisclaimerRequestVo data);
}