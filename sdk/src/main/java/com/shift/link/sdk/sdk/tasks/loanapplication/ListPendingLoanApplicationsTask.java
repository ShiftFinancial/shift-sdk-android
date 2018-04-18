package com.shift.link.sdk.sdk.tasks.loanapplication;

import com.shift.link.sdk.api.exceptions.ApiException;
import com.shift.link.sdk.api.vos.requests.base.ListRequestVo;
import com.shift.link.sdk.api.vos.responses.loanapplication.LoanApplicationsSummaryListResponseVo;
import com.shift.link.sdk.api.wrappers.ShiftApiWrapper;
import com.shift.link.sdk.sdk.tasks.ShiftApiTask;
import com.shift.link.sdk.sdk.tasks.handlers.ApiResponseHandler;

/**
 * A concrete {@link ShiftApiTask} to list open loan applications.
 * @author Wijnand
 */
public class ListPendingLoanApplicationsTask
        extends ShiftApiTask<Void, Void, LoanApplicationsSummaryListResponseVo, ListRequestVo> {

    /**
     * Creates a new {@link ListPendingLoanApplicationsTask} instance.
     * @param requestData API request data.
     * @param apiWrapper The API wrapper instance to make API calls.
     * @param responseHandler The response handler instance used to publish results.
     */
    public ListPendingLoanApplicationsTask(ListRequestVo requestData, ShiftApiWrapper apiWrapper,
                                           ApiResponseHandler responseHandler) {

        super(requestData, apiWrapper, responseHandler);
    }

    /** {@inheritDoc} */
    @Override
    protected LoanApplicationsSummaryListResponseVo callApi() throws ApiException {
        return getApiWrapper().getPendingLoanApplicationsList(getRequestData());
    }
}