package com.shiftpayments.link.sdk.wrappers.retrofit.services;

import com.google.gson.JsonObject;
import com.shiftpayments.link.sdk.api.vos.Card;
import com.shiftpayments.link.sdk.api.vos.datapoints.FinancialAccountVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.ActivatePhysicalCardRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.AddBalanceRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.AddBankAccountRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.IssueVirtualCardRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.SetBalanceStoreRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.SetFundingSourceRequestVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.UpdateFinancialAccountPinRequestVo;
import com.shiftpayments.link.sdk.api.vos.responses.cardapplication.SetBalanceStoreResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.ActivateFinancialAccountResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.ActivatePhysicalCardResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.BalanceListVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.BalanceVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.DisableFinancialAccountResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.EnableFinancialAccountResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.TransactionListResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.UpdateFinancialAccountPinResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.UserDataListResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.verifications.VerificationStatusResponseVo;
import com.shiftpayments.link.sdk.api.wrappers.ShiftApiWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Financial accounts related API calls.
 * @author Wijnand
 */
public interface FinancialAccountService {
    /**
     * Creates a {@link Call} to add a bank account
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.FINANCIAL_ACCOUNTS_PATH)
    Call<VerificationStatusResponseVo> addBankAccount(@Body AddBankAccountRequestVo data);

    /**
     * Creates a {@link Call} to add a credit/debit card.
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.FINANCIAL_ACCOUNTS_PATH)
    Call<Card> addCard(@Body JsonObject data);

    /**
     * Creates a {@link Call} to add a virtual card.
     * @param data Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.ISSUE_CARD_PATH)
    Call<Card> issueVirtualCard(@Body IssueVirtualCardRequestVo data);

    /**
     * Creates a {@link Call} to get the user's financial accounts.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.FINANCIAL_ACCOUNTS_PATH)
    Call<UserDataListResponseVo> getFinancialAccounts();

    /**
     * Creates a {@link Call} to get a specific financial account.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.FINANCIAL_ACCOUNT_PATH)
    Call<FinancialAccountVo> getFinancialAccount(@Path("account_id") String accountId);

    /** Creates a {@link Call} to change pin card.
     * @param pin Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.FINANCIAL_ACCOUNT_PIN_PATH)
    Call<UpdateFinancialAccountPinResponseVo> updateFinancialAccountPin(@Path("account_id") String accountId, @Body UpdateFinancialAccountPinRequestVo pin);

    /** Creates a {@link Call} to activate a card.
     * @param accountId Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.FINANCIAL_ACCOUNT_ACTIVATE_PATH)
    Call<ActivateFinancialAccountResponseVo> activateFinancialAccount(@Path("account_id") String accountId);

    /** Creates a {@link Call} to activate a physical card.
     * @param accountId Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.ACTIVATE_PHYSICAL_CARD_PATH)
    Call<ActivatePhysicalCardResponseVo> activatePhysicalCard(@Path("account_id") String accountId, @Body ActivatePhysicalCardRequestVo code);

    /** Creates a {@link Call} to enable a card.
     * @param accountId Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.FINANCIAL_ACCOUNT_ENABLE_PATH)
    Call<EnableFinancialAccountResponseVo> enableFinancialAccount(@Path("account_id") String accountId);

    /** Creates a {@link Call} to disable a card.
     * @param accountId Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.FINANCIAL_ACCOUNT_DISABLE_PATH)
    Call<DisableFinancialAccountResponseVo> disableFinancialAccount(@Path("account_id") String accountId);

    /**
     * Creates a {@link Call} to get the transaction list of a specific financial account.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.FINANCIAL_ACCOUNT_TRANSACTIONS_PATH)
    Call<TransactionListResponseVo> getTransactions(@Path("account_id") String accountId, @Query("rows") int rows, @Query("last_transaction_id") String lastTransactionId);

    /**
     * Creates a {@link Call} to get a the funding source of a specific financial account.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.FINANCIAL_ACCOUNT_BALANCE_PATH)
    Call<BalanceVo> getFundingSource(@Path("account_id") String accountId);

    /**
     * Creates a {@link Call} to get the user's funding sources.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.USER_BALANCES_PATH)
    Call<BalanceListVo> getUserFundingSources(@Path("account_id") String accountId);

    /** Creates a {@link Call} to set the user's funding source
     * @param accountId Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.FINANCIAL_ACCOUNT_BALANCE_PATH)
    Call<BalanceVo> setFundingSource(@Path("account_id") String accountId, @Body SetFundingSourceRequestVo request);


    /** Creates a {@link Call} to set the balance store
     * @param request Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.SET_BALANCE_STORE_PATH)
    Call<SetBalanceStoreResponseVo> setBalanceStore(@Path("application_id") String applicationId, @Body SetBalanceStoreRequestVo request);

    /** Creates a {@link Call} to add a new balance
     * @param request Mandatory request data.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.USER_BALANCES_PATH)
    Call<BalanceVo> addBalanceStore(@Path("account_id") String accountId, @Body AddBalanceRequestVo request);
}
