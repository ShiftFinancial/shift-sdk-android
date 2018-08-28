package com.shiftpayments.link.sdk.ui.presenters.userdata;

import android.support.v7.app.AppCompatActivity;

import com.shiftpayments.link.sdk.api.vos.responses.config.ConfigResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.config.CreditScoreVo;
import com.shiftpayments.link.sdk.ui.models.userdata.CreditScoreModel;
import com.shiftpayments.link.sdk.ui.storages.UIStorage;
import com.shiftpayments.link.sdk.ui.views.userdata.CreditScoreView;

/**
 * Concrete {@link Presenter} for the credit score screen.
 * @author wijnand
 */
public class CreditScorePresenter
        extends UserDataPresenter<CreditScoreModel, CreditScoreView>
        implements CreditScoreView.ViewListener {

    private CreditScoreDelegate mDelegate;

    /**
     * Creates a new {@link CreditScorePresenter} instance.
     * @param activity Activity.
     */
    public CreditScorePresenter(AppCompatActivity activity, CreditScoreDelegate delegate) {
        super(activity);
        mDelegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public CreditScoreModel createModel() {
        return new CreditScoreModel();
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(CreditScoreView view) {
        super.attachView(view);
        showCreditScoreOptions(UIStorage.getInstance().getContextConfig());
        mView.setListener(this);
    }

    @Override
    public void onBack() {
        mDelegate.creditScoreOnBackPressed();
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        mView.setListener(null);
        super.detachView();
    }

    /** {@inheritDoc} */
    @Override
    public void nextClickHandler() {
        mModel.setCreditScoreRange(mView.getScoreRangeId());
        mView.showError(!mModel.hasValidData());

        if (mModel.hasValidData()) {
            saveData();
            mDelegate.creditScoreStored();
        }
    }

    private void showCreditScoreOptions(ConfigResponseVo config) {
        CreditScoreVo[] typesList = config.creditScoreOpts.data;
        if (typesList != null) {
            mView.makeRadioButtons(typesList);
            mView.setColors();
            mView.setScoreRangeId(mModel.getCreditScoreRange());
        }
    }
}