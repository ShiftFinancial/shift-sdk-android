package com.shiftpayments.link.sdk.ui.tests.robolectric.tests.presenters.userdata;

import android.support.v7.app.AppCompatActivity;

import com.shiftpayments.link.sdk.api.vos.datapoints.DataPointList;
import com.shiftpayments.link.sdk.api.vos.datapoints.DataPointVo;
import com.shiftpayments.link.sdk.api.vos.datapoints.Income;
import com.shiftpayments.link.sdk.ui.BuildConfig;
import com.shiftpayments.link.sdk.ui.mocks.presenters.userdata.MockAnnualIncomePresenter;
import com.shiftpayments.link.sdk.ui.mocks.presenters.userdata.MockUserDataCollectorModule;
import com.shiftpayments.link.sdk.ui.mocks.views.userdata.MockAnnualIncomeView;
import com.shiftpayments.link.sdk.ui.presenters.userdata.AnnualIncomePresenter;
import com.shiftpayments.link.sdk.ui.presenters.userdata.UserDataCollectorModule;
import com.shiftpayments.link.sdk.ui.storages.UserStorage;
import com.shiftpayments.link.sdk.ui.tests.robolectric.LibraryProjectTestRunner;
import com.shiftpayments.link.sdk.ui.views.userdata.AnnualIncomeView;
import com.shiftpayments.link.sdk.ui.workflow.ModuleManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.lang.ref.SoftReference;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(LibraryProjectTestRunner.class)
@Config(constants = BuildConfig.class)
public class AnnualIncomePresenterTest {

    private static final long EXPECTED_INCOME = 90000;
    private static final int TEST_MAX_INCOME = 100000;
    private static final int EXPECTED_MULTIPLIER = 1000;

    private AnnualIncomePresenter mPresenter;
    private AnnualIncomeView mView;

    @Before
    public void setUp() {
        AppCompatActivity mActivity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        UserDataCollectorModule userDataCollectorModule = UserDataCollectorModule.getInstance(mActivity, null, null);
        ModuleManager.getInstance().setModule(new SoftReference<>(userDataCollectorModule));
        mPresenter = new MockAnnualIncomePresenter(mActivity, new MockUserDataCollectorModule(mActivity));
        mView = new MockAnnualIncomeView(mActivity);
        mPresenter.attachView(mView);
    }

    @Test
    public void absoluteAnnualGrossIncomeStoredInModel() {
        ((MockAnnualIncomePresenter) mPresenter).setModelMaxIncome(TEST_MAX_INCOME);
        ((MockAnnualIncomePresenter) mPresenter).setMultiplier(EXPECTED_MULTIPLIER);
        mView.setIncome(EXPECTED_INCOME / EXPECTED_MULTIPLIER);
        mPresenter.nextClickHandler();

        DataPointList userData = UserStorage.getInstance().getUserData();
        Assert.assertThat("User data should not be empty.", userData, not(nullValue()));
        Income income = (Income) userData.getUniqueDataPoint(
                DataPointVo.DataPointType.Income, null);
        Assert.assertThat("Incorrect annual gross income.", income.annualGrossIncome, equalTo(EXPECTED_INCOME));
    }

    /**
     * Cleans up after each test.
     */
    @After
    public void tearDown() {
        mPresenter.detachView();
        UserStorage.getInstance().setUserData(null);
    }

}