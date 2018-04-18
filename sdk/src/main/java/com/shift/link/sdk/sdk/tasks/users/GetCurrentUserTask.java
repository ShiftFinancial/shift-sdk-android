package com.shift.link.sdk.sdk.tasks.users;

import com.shift.link.sdk.api.exceptions.ApiException;
import com.shift.link.sdk.api.vos.datapoints.DataPointList;
import com.shift.link.sdk.api.vos.datapoints.DataPointVo;
import com.shift.link.sdk.api.vos.requests.base.UnauthorizedRequestVo;
import com.shift.link.sdk.api.vos.responses.users.CurrentUserResponseVo;
import com.shift.link.sdk.api.wrappers.ShiftApiWrapper;
import com.shift.link.sdk.sdk.tasks.ShiftApiTask;
import com.shift.link.sdk.sdk.tasks.handlers.ApiResponseHandler;

/**
 * A concrete {@link ShiftApiTask} to retrieve the current user info.
 * @author Adrian
 */
public class GetCurrentUserTask extends ShiftApiTask<Void, Void, DataPointList, UnauthorizedRequestVo> {

    private final boolean mThrowSessionExpiredError;

    /**
     * @see ShiftApiTask#ShiftApiTask
     * @param requestData See {@link ShiftApiTask#ShiftApiTask}.
     * @param apiWrapper See {@link ShiftApiTask#ShiftApiTask}.
     * @param responseHandler See {@link ShiftApiTask#ShiftApiTask}.
     * @param throwSessionExpiredError specify if a {@link me.shift.link.sdk.api.vos.responses.SessionExpiredErrorVo} must be thrown
     */
    public GetCurrentUserTask(UnauthorizedRequestVo requestData, ShiftApiWrapper apiWrapper,
                              ApiResponseHandler responseHandler, boolean throwSessionExpiredError) {

        super(requestData, apiWrapper, responseHandler);
        this.mThrowSessionExpiredError = throwSessionExpiredError;
    }

    /** {@inheritDoc} */
    @Override
    protected DataPointList callApi() throws ApiException {
        CurrentUserResponseVo response = getApiWrapper().getCurrentUser(getRequestData(), mThrowSessionExpiredError);
        DataPointList dataPointList = new DataPointList(DataPointList.ListType.userData);
        for(DataPointVo d : response.userData.data) {
            if(d != null) {
                dataPointList.add(d);
            }
        }

        return dataPointList;
    }
}