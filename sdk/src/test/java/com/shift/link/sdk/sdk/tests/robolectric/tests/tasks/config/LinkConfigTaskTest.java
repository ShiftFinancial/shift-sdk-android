package com.shift.link.sdk.sdk.tests.robolectric.tests.tasks.config;

import com.shift.link.sdk.api.vos.requests.base.UnauthorizedRequestVo;
import com.shift.link.sdk.api.vos.responses.config.LinkConfigResponseVo;
import com.shift.link.sdk.sdk.mocks.api.wrappers.MockApiWrapper;
import com.shift.link.sdk.sdk.mocks.sdk.tasks.handlers.MockResponseHandler;
import com.shift.link.sdk.sdk.tasks.config.LinkConfigTask;
import com.shift.link.sdk.sdk.utils.tasks.RoboLinkApiTaskWrapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;

public class LinkConfigTaskTest {

    private RoboLinkApiTaskWrapper<LinkConfigResponseVo, UnauthorizedRequestVo> mTask;

    @Before
    public void setUp() {
        mTask = new RoboLinkApiTaskWrapper<>(new LinkConfigTask(
                new UnauthorizedRequestVo(), new MockApiWrapper(), new MockResponseHandler()
        ));
    }

    @Test
    public void apiResponseIsNotEmpty() {
        Assert.assertThat("Result should not be empty.", mTask.execute(), not(nullValue()));
    }
}
