package me.ledge.link.sdk.sdk.tasks.users;

import me.ledge.link.api.exceptions.ApiException;
import me.ledge.link.api.vos.requests.users.CreateUserRequestVo;
import me.ledge.link.api.vos.responses.users.CreateUserResponseVo;
import me.ledge.link.api.wrappers.LinkApiWrapper;
import me.ledge.link.sdk.sdk.tasks.LedgeLinkApiTask;
import me.ledge.link.sdk.sdk.tasks.handlers.ApiResponseHandler;

/**
 * A concrete {@link LedgeLinkApiTask} to create a new user.
 * @author Wijnand
 */
public class CreateUserTask extends LedgeLinkApiTask<Void, Void, CreateUserResponseVo, CreateUserRequestVo> {

    /**
     * @see LedgeLinkApiTask#LedgeLinkApiTask
     * @param requestData See {@link LedgeLinkApiTask#LedgeLinkApiTask}.
     * @param apiWrapper See {@link LedgeLinkApiTask#LedgeLinkApiTask}.
     * @param responseHandler See {@link LedgeLinkApiTask#LedgeLinkApiTask}.
     */
    public CreateUserTask(CreateUserRequestVo requestData, LinkApiWrapper apiWrapper,
            ApiResponseHandler responseHandler) {

        super(requestData, apiWrapper, responseHandler);
    }

    /** {@inheritDoc} */
    @Override
    protected CreateUserResponseVo callApi() throws ApiException {
        return getApiWrapper().createUser(getRequestData());
    }
}
