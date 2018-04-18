package com.shift.link.sdk.ui.presenters.loanapplication;

import android.support.v7.app.AppCompatActivity;

import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.shift.link.sdk.api.utils.loanapplication.LoanApplicationStatus;
import com.shift.link.sdk.api.vos.responses.config.LoanProductListVo;
import com.shift.link.sdk.api.vos.responses.loanapplication.LoanApplicationDetailsResponseVo;
import com.shift.link.sdk.api.vos.responses.offers.OfferVo;
import com.shift.link.sdk.sdk.storages.ConfigStorage;
import com.shift.link.sdk.ui.R;
import com.shift.link.sdk.ui.ShiftPlatform;
import com.shift.link.sdk.ui.models.loanapplication.LoanAgreementModel;
import com.shift.link.sdk.ui.presenters.ActivityPresenter;
import com.shift.link.sdk.ui.presenters.Presenter;
import com.shift.link.sdk.ui.storages.LoanStorage;
import com.shift.link.sdk.ui.utils.LoadingSpinnerManager;
import com.shift.link.sdk.ui.views.loanapplication.LoanAgreementView;

import java8.util.concurrent.CompletableFuture;
import me.ledge.common.fragments.dialogs.NotificationDialogFragment;

/**
 * Concrete {@link Presenter} for the loan agreement.
 * @author Wijnand
 */
public class LoanAgreementPresenter
        extends ActivityPresenter<LoanAgreementModel, LoanAgreementView>
        implements Presenter<LoanAgreementModel, LoanAgreementView>, LoanAgreementView.ViewListener {

    private NotificationDialogFragment mNotificationDialog;
    private LoanAgreementDelegate mDelegate;
    private LoadingSpinnerManager mLoadingSpinnerManager;

    /**
     * Creates a new {@link LoanAgreementPresenter} instance.
     * @param activity Activity.
     */
    public LoanAgreementPresenter(AppCompatActivity activity, LoanAgreementDelegate delegate) {
        super(activity);
        mDelegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public LoanAgreementModel createModel() {
        LoanApplicationDetailsResponseVo application = LoanStorage.getInstance().getCurrentLoanApplication();
        OfferVo offer = null;

        if (application != null) {
            offer = application.offer;
        }

        return new LoanAgreementModel(offer, ShiftPlatform.getImageLoader());
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(LoanAgreementView view) {
        super.attachView(view);
        CompletableFuture
                .supplyAsync(()-> ConfigStorage.getInstance().getLoanProducts())
                .thenAccept(this::eSignDisclaimersListRetrieved);
        mView.setViewListener(this);
        mLoadingSpinnerManager = new LoadingSpinnerManager(mView);
        mLoadingSpinnerManager.showLoading(false);
        mView.updateBottomButton(false);
        mView.setData(mModel);

        mNotificationDialog = new NotificationDialogFragment();
        mNotificationDialog.setTitle(mActivity.getString(R.string.loan_agreement_terms_dialog_title));
        mNotificationDialog.setMessage(mActivity.getString(R.string.loan_agreement_terms_dialog_message));
    }

    private void eSignDisclaimersListRetrieved(LoanProductListVo loanProductListVo) {
        /*if (loanProductListVo == null) {
            return;
        }

        String lineBreak = "<br />";
        String partnerDivider = "<br /><br />";
        StringBuilder eSignDisclaimer = new StringBuilder();
        StringBuilder eSignConsentDisclaimer = new StringBuilder();

        for(LoanProductVo loanProduct : loanProductListVo.data) {
            if (!TextUtils.isEmpty(loanProduct.eSignDisclaimer)) {
                eSignDisclaimer.append(loanProduct.eSignDisclaimer.replaceAll("\\r?\\n", lineBreak));
            }
            if (!TextUtils.isEmpty(loanProduct.eSignConsentDisclaimer)) {
                eSignConsentDisclaimer.append(loanProduct.eSignConsentDisclaimer.replaceAll("\\r?\\n", lineBreak));
            }
            eSignDisclaimer.append(partnerDivider);
            eSignConsentDisclaimer.append(partnerDivider);
        }

        mActivity.runOnUiThread(() -> {
            mView.setDisclaimer(eSignDisclaimer.substring(0, eSignDisclaimer.length() - partnerDivider.length()));
            mView.setConsentDisclaimer(eSignConsentDisclaimer.substring(0, eSignConsentDisclaimer.length() - partnerDivider.length()));
        });*/
    }

    @Override
    public void onBack() {
        mDelegate.loanAgreementShowPrevious(mModel);
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        mView.setViewListener(null);
        super.detachView();
    }

    /** {@inheritDoc} */
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (mView == null) {
            return;
        }

        boolean fullyScrolled = false;
        if (scrollY >= mView.getMaxScroll()) {
            fullyScrolled = true;
        }

        mView.updateBottomButton(fullyScrolled);
    }

    /** {@inheritDoc} */
    @Override
    public void onDownMotionEvent() { /* Do nothing. */ }

    /** {@inheritDoc} */
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) { /* Do nothing. */ }

    /** {@inheritDoc} */
    @Override
    public void confirmClickHandler() {
        if (mView.getCurrentScroll() >= mView.getMaxScroll()) {
            if (mView.hasAcceptedTerms()) {
                // Faking data hard!
                LoanApplicationDetailsResponseVo application = LoanStorage.getInstance().getCurrentLoanApplication();
                application.status = LoanApplicationStatus.LOAN_APPROVED;
                application.status_message = String.format(
                        "Your loan with %s is being funded.", application.offer.lender.lender_name);

                mDelegate.loanAgreementShowNext(mModel);
            } else {
                mNotificationDialog.show(mActivity.getFragmentManager(), NotificationDialogFragment.DIALOG_TAG);
            }
        } else {
            mView.scrollToBottom();
        }
    }
}