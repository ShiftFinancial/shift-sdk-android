package me.ledge.link.sdk.sdk.tasks.config;

import me.ledge.link.api.exceptions.ApiException;
import me.ledge.link.api.vos.requests.base.UnauthorizedRequestVo;
import me.ledge.link.api.vos.responses.config.DisclaimerResponseVo;
import me.ledge.link.api.wrappers.LinkApiWrapper;
import me.ledge.link.sdk.sdk.tasks.LedgeLinkApiTask;
import me.ledge.link.sdk.sdk.tasks.handlers.ApiResponseHandler;

/**
 * TODO: Class documentation.
 * @author Wijnand
 */
public class LinkDisclaimerTask extends LedgeLinkApiTask<Void, Void, DisclaimerResponseVo, UnauthorizedRequestVo> {

    /**
     * Creates a new {@link LinkDisclaimerTask} instance.
     * @param requestData API request data.
     * @param wrapper The API wrapper instance to make API calls.
     * @param handler The response handler instance used to publish results.
     */
    public LinkDisclaimerTask(UnauthorizedRequestVo requestData, LinkApiWrapper wrapper, ApiResponseHandler handler) {
        super(requestData, wrapper, handler);
    }

    /** {@inheritDoc} */
    @Override
    protected DisclaimerResponseVo callApi() throws ApiException {
        return getApiWrapper().getLinkDisclaimer(getRequestData());
    }
}