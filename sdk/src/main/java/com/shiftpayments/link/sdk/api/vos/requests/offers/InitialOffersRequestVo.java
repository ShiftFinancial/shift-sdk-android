package com.shiftpayments.link.sdk.api.vos.requests.offers;

import com.shiftpayments.link.sdk.api.utils.offers.LoanCategory;
import com.shiftpayments.link.sdk.api.vos.requests.base.ListRequestVo;
import com.shiftpayments.link.sdk.api.wrappers.ShiftApiWrapper;
import com.shiftpayments.link.sdk.sdk.tasks.ShiftApiTask;
import com.shiftpayments.link.sdk.sdk.tasks.handlers.ApiResponseHandler;
import com.shiftpayments.link.sdk.sdk.tasks.offers.InitialOffersTask;

/**
 * Request data for the first loan offers API call.
 * @author wijnand
 */
public class InitialOffersRequestVo extends ListRequestVo {

    /**
     * Loan amount.
     */
    public Integer loan_amount;

    /**
     * Loan purpose ID, based on the "config/loanPurposes" API response.
     */
    public Integer loan_purpose_id;

    /**
     * Three letter <a href="https://en.wikipedia.org/wiki/ISO_4217#Active_codes">ISO 4217</a> currency code.
     */
    public String currency;

    /**
     * Loan type.
     * @see LoanCategory
     */
    public int loan_category_id = LoanCategory.CONSUMER;

    /**
     * Desired loan length in months.
     */
    public int desired_months = 12;

    @Override
    public ShiftApiTask getApiTask(ShiftApiWrapper shiftApiWrapper, ApiResponseHandler responseHandler) {
        return new InitialOffersTask(this, shiftApiWrapper, responseHandler);
    }
}
