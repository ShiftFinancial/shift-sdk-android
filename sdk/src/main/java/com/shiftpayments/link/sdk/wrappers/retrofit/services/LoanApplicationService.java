package com.shiftpayments.link.sdk.wrappers.retrofit.services;

import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.ApplicationAccountRequestVo;
import com.shiftpayments.link.sdk.api.vos.responses.loanapplication.LoanApplicationDetailsResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.loanapplication.LoanApplicationsSummaryListResponseVo;
import com.shiftpayments.link.sdk.api.wrappers.ShiftApiWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Loan application related API calls.
 * @author Wijnand
 */
public interface LoanApplicationService {

    /**
     * Creates a {@link Call} to create a loan application.
     * @param offerId The offer to apply to.
     * @return API call to execute.
     */
    @POST(ShiftApiWrapper.CREATE_LOAN_APPLICATION_PATH)
    Call<LoanApplicationDetailsResponseVo> createLoanApplication(@Path("offer_id") String offerId);

    /**
     * Creates a {@link Call} to get the list of open loan applications.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.LIST_LOAN_APPLICATIONS_PATH)
    Call<LoanApplicationsSummaryListResponseVo> getLoanApplicationsSummaryList();

    /**
     * Creates a {@link Call} to get the application status.
     * @return API call to execute.
     */
    @GET(ShiftApiWrapper.APPLICATION_STATUS_PATH)
    Call<LoanApplicationDetailsResponseVo> getApplicationStatus(@Path("application_id") String applicationId);

    /**
     * Creates a {@link Call} to link an account to a loan application.
     * @param data Mandatory request data.
     * @param applicationId The application where the account will be linked to.
     * @return API call to execute.
     */
    @PUT(ShiftApiWrapper.APPLICATION_ACCOUNT_PATH)
    Call<LoanApplicationDetailsResponseVo> setApplicationAccount(@Body ApplicationAccountRequestVo data,
                                                                 @Path("application_id") String applicationId);
}