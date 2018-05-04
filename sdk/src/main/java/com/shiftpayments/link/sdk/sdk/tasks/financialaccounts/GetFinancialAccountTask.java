package com.shiftpayments.link.sdk.sdk.tasks.financialaccounts;

import com.shiftpayments.link.sdk.api.exceptions.ApiException;
import com.shiftpayments.link.sdk.api.vos.datapoints.FinancialAccountVo;
import com.shiftpayments.link.sdk.api.wrappers.ShiftApiWrapper;
import com.shiftpayments.link.sdk.sdk.tasks.ShiftApiTask;
import com.shiftpayments.link.sdk.sdk.tasks.handlers.ApiResponseHandler;

/**
 * A concrete {@link ShiftApiTask} to retrieve the current user's financial accounts
 * @author Adrian
 */
public class GetFinancialAccountTask extends ShiftApiTask<Void, Void, FinancialAccountVo, String> {

    /**
     * @see ShiftApiTask#ShiftApiTask
     * @param requestData See {@link ShiftApiTask#ShiftApiTask}.
     * @param apiWrapper See {@link ShiftApiTask#ShiftApiTask}.
     * @param responseHandler See {@link ShiftApiTask#ShiftApiTask}.
     */
    public GetFinancialAccountTask(String requestData, ShiftApiWrapper apiWrapper,
                                   ApiResponseHandler responseHandler) {

        super(requestData, apiWrapper, responseHandler);
    }

    /** {@inheritDoc} */
    @Override
    protected FinancialAccountVo callApi() throws ApiException {
        return getApiWrapper().getFinancialAccount(getRequestData());
    }
}
