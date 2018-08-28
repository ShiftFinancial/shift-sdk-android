package com.shiftpayments.link.sdk.ui.presenters.loanapplication;

import android.content.res.Resources;

import com.shiftpayments.link.sdk.api.utils.loanapplication.LoanApplicationActionId;
import com.shiftpayments.link.sdk.api.utils.loanapplication.LoanApplicationStatus;
import com.shiftpayments.link.sdk.api.vos.responses.loanapplication.LoanApplicationDetailsResponseVo;
import com.shiftpayments.link.sdk.ui.ShiftPlatform;
import com.shiftpayments.link.sdk.ui.models.loanapplication.details.LoanApplicationDetailsModel;
import com.shiftpayments.link.sdk.ui.models.loanapplication.details.PendingLenderActionLoanApplicationDetailsModel;
import com.shiftpayments.link.sdk.ui.presenters.BasePresenter;
import com.shiftpayments.link.sdk.ui.presenters.Presenter;
import com.shiftpayments.link.sdk.ui.views.loanapplication.LoanApplicationDetailsView;

/**
 * Concrete {@link Presenter} for an open loan application.
 * @author Wijnand
 */
public class LoanApplicationDetailsPresenter
        extends BasePresenter<LoanApplicationDetailsModel, LoanApplicationDetailsView>
        implements Presenter<LoanApplicationDetailsModel, LoanApplicationDetailsView>,
        LoanApplicationDetailsView.ViewListener {

    private final Resources mResources;
    private final LoanApplicationDetailsResponseVo mRawApplication;

    /**
     * Creates a new {@link LoanApplicationDetailsPresenter} instance.
     * @param resources Android application resources.
     * @param application Loan application details.
     */
    public LoanApplicationDetailsPresenter(Resources resources, LoanApplicationDetailsResponseVo application) {
        super();
        mResources = resources;
        mRawApplication = application;
    }

    /**
     * Generates the {@link LoanApplicationDetailsModel} based on the {@link LoanApplicationActionId}.
     * @param application Loan application.
     * @return New Model OR null when the criteria aren't met.
     */
    private LoanApplicationDetailsModel createPendingBorrowerActionModel(LoanApplicationDetailsResponseVo application) {
        /*if (application == null || application.required_actions == null || application.required_actions.data == null
                || application.required_actions.data[0] == null) {

            return null;
        }

        LoanApplicationDetailsModel model = null;

        switch (application.required_actions.data[0].action) {
            case LoanApplicationActionId.AGREE_TERMS:
                model = new ApprovedLoanApplicationDetailsModel(application, mResources, ShiftPlatform.getImageLoader());
                break;
            case LoanApplicationActionId.UPLOAD_PHOTO_ID:
            case LoanApplicationActionId.UPLOAD_BANK_STATEMENT:
            case LoanApplicationActionId.UPLOAD_PROOF_OF_ADDRESS:
            case LoanApplicationActionId.UNKNOWN:
                model = new UploadDocsLoanApplicationDetailsModel(
                        application, mResources, ShiftPlatform.getImageLoader());
                break;
            case LoanApplicationActionId.FINISH_APPLICATION_EXTERNAL:
                model = new FinishExternalLoanApplicationDetailsModel(
                        application, mResources, ShiftPlatform.getImageLoader());
                break;
            default:
                // Do nothing.
                break;
        }

        return model;*/
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public LoanApplicationDetailsModel createModel() {
        if (mResources == null || mRawApplication == null) {
            return null;
        }

        LoanApplicationDetailsModel model = null;

        switch (mRawApplication.status) {
            case LoanApplicationStatus.PENDING_BORROWER_ACTION:
                model = createPendingBorrowerActionModel(mRawApplication);
                break;
            case LoanApplicationStatus.PENDING_LENDER_ACTION:
            case LoanApplicationStatus.APPLICATION_RECEIVED:
                model = new PendingLenderActionLoanApplicationDetailsModel(
                        mRawApplication, mResources, ShiftPlatform.getImageLoader());
                break;
            default:
                // Do nothing.
                break;
        }

        return model;
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(LoanApplicationDetailsView view) {
        super.attachView(view);

        if (mModel == null) {
            mModel = createModel();
        }

        mView.setData(mModel);
        mView.setListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        mView.setListener(null);
        mView.setData(null);
        super.detachView();
    }

    /** {@inheritDoc} */
    @Override
    public void bigButtonClickHandler(int action) { /* TODO */ }
}