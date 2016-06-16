package me.ledge.link.sdk.sdk.tasks.config;

import me.ledge.link.api.exceptions.ApiException;
import me.ledge.link.api.vos.requests.base.UnauthorizedRequestVo;
import me.ledge.link.api.vos.responses.config.HousingTypeListResponseVo;
import me.ledge.link.api.wrappers.LinkApiWrapper;
import me.ledge.link.sdk.sdk.tasks.LedgeLinkApiTask;
import me.ledge.link.sdk.sdk.tasks.handlers.ApiResponseHandler;

/**
 * A concrete {@link LedgeLinkApiTask} to get the list of housing types.
 * @author wijnand
 */
public class HousingTypeListTask
        extends LedgeLinkApiTask<Void, Void, HousingTypeListResponseVo, UnauthorizedRequestVo> {

    /**
     * Creates a new {@link HousingTypeListTask} instance.
     * @param requestData API request data.
     * @param apiWrapper The API wrapper instance to make API calls.
     * @param responseHandler The response handler instance used to publish results.
     */
    public HousingTypeListTask(UnauthorizedRequestVo requestData, LinkApiWrapper apiWrapper,
            ApiResponseHandler responseHandler) {

        super(requestData, apiWrapper, responseHandler);
    }

    /** {@inheritDoc} */
    @Override
    protected HousingTypeListResponseVo callApi() throws ApiException {
        return getApiWrapper().getHousingTypeList(getRequestData());
    }
}