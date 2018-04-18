package com.shift.link.sdk.ui.presenters.financialaccountselector;

import android.support.v7.app.AppCompatActivity;

import com.shift.link.sdk.api.vos.datapoints.DataPointList;
import com.shift.link.sdk.api.vos.datapoints.DataPointVo;
import com.shift.link.sdk.api.vos.responses.ApiErrorVo;
import com.shift.link.sdk.ui.ShiftPlatform;
import com.shift.link.sdk.ui.models.financialaccountselector.IntermediateFinancialAccountListModel;
import com.shift.link.sdk.ui.presenters.ActivityPresenter;
import com.shift.link.sdk.ui.presenters.Presenter;
import com.shift.link.sdk.ui.utils.LoadingSpinnerManager;
import com.shift.link.sdk.ui.views.financialaccountselector.IntermediateFinancialAccountListView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Concrete {@link Presenter} for the intermediate screen during financial accounts loading
 * @author Adrian
 */
public class IntermediateFinancialAccountListPresenter
        extends ActivityPresenter<IntermediateFinancialAccountListModel, IntermediateFinancialAccountListView>
        implements Presenter<IntermediateFinancialAccountListModel, IntermediateFinancialAccountListView>  {


    private IntermediateFinancialAccountListDelegate mDelegate;
    private LoadingSpinnerManager mLoadingSpinnerManager;
    /**
     * Creates a new {@link IntermediateFinancialAccountListPresenter} instance.
     * @param activity Activity.
     */
    public IntermediateFinancialAccountListPresenter(AppCompatActivity activity, IntermediateFinancialAccountListDelegate delegate) {
        super(activity);
        mDelegate = delegate;
        ShiftPlatform.getFinancialAccounts();
    }

    @Override
    public IntermediateFinancialAccountListModel createModel() {
        return new IntermediateFinancialAccountListModel();
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(IntermediateFinancialAccountListView view) {
        super.attachView(view);
        mResponseHandler.subscribe(this);
        mLoadingSpinnerManager = new LoadingSpinnerManager(mView);
        mLoadingSpinnerManager.showLoading(true);
    }

    @Override
    public void onBack() {
        if(mLoadingSpinnerManager.isLoading()) {
            return;
        }
        mResponseHandler.unsubscribe(this);
        mDelegate.onIntermediateFinancialAccountListBackPressed();
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        mResponseHandler.unsubscribe(this);
        super.detachView();
    }

    /**
     * Called when the get current user info response has been received.
     * @param userInfo API response.
     */
    @Subscribe
    public void handleResponse(DataPointList userInfo) {
        mResponseHandler.unsubscribe(this);
        if (mView != null) {
            mLoadingSpinnerManager.showLoading(false);
        }
        if(userInfo.getDataPointsOf(DataPointVo.DataPointType.FinancialAccount) == null) {
            mDelegate.noFinancialAccountsReceived();
        }
        else {
            mDelegate.financialAccountsReceived(userInfo);
        }
    }

    /**
     * Called when an API error has been received.
     * @param error API error.
     */
    @Subscribe
    public void handleApiError(ApiErrorVo error) {
        mResponseHandler.unsubscribe(this);
        if (mView != null) {
            mLoadingSpinnerManager.showLoading(false);
        }
        mDelegate.noFinancialAccountsReceived();
    }
}