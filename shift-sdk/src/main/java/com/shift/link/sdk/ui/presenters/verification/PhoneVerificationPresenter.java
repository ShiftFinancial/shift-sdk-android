package com.shift.link.sdk.ui.presenters.verification;

import android.support.v7.app.AppCompatActivity;

import com.shift.link.sdk.api.vos.datapoints.DataPointVo;
import com.shift.link.sdk.api.vos.datapoints.PhoneNumberVo;
import com.shift.link.sdk.api.vos.datapoints.VerificationVo;
import com.shift.link.sdk.api.vos.responses.ApiErrorVo;
import com.shift.link.sdk.api.vos.responses.verifications.FinishVerificationResponseVo;
import com.shift.link.sdk.api.vos.responses.verifications.VerificationResponseVo;
import com.shift.link.sdk.ui.R;
import com.shift.link.sdk.ui.ShiftPlatform;
import com.shift.link.sdk.ui.models.verification.PhoneVerificationModel;
import com.shift.link.sdk.ui.presenters.Presenter;
import com.shift.link.sdk.ui.presenters.userdata.UserDataPresenter;
import com.shift.link.sdk.ui.utils.LoadingSpinnerManager;
import com.shift.link.sdk.ui.utils.PhoneHelperUtil;
import com.shift.link.sdk.ui.views.verification.PhoneVerificationView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Concrete {@link Presenter} for the phone verification screen.
 * @author Adrian
 */
public class PhoneVerificationPresenter
        extends UserDataPresenter<PhoneVerificationModel, PhoneVerificationView>
        implements PhoneVerificationView.ViewListener {

    private PhoneVerificationDelegate mDelegate;
    private LoadingSpinnerManager mLoadingSpinnerManager;
    /**
     * Creates a new {@link PhoneVerificationPresenter} instance.
     * @param activity Activity.
     */
    public PhoneVerificationPresenter(AppCompatActivity activity, PhoneVerificationDelegate delegate) {
        super(activity);
        mDelegate = delegate;
        ShiftPlatform.startVerification(mModel.getPhoneVerificationRequest());
    }

    /** {@inheritDoc} */
    @Override
    public PhoneVerificationModel createModel() {
        return new PhoneVerificationModel();
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(PhoneVerificationView view) {
        super.attachView(view);
        mActivity.setTitle(this.getTitle());
        mView.setListener(this);
        mLoadingSpinnerManager = new LoadingSpinnerManager(mView);
        mLoadingSpinnerManager.showLoading(false);
        mResponseHandler.subscribe(this);
    }

    @Override
    public void onBack() {
        mDelegate.phoneVerificationOnBackPressed();
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        mResponseHandler.unsubscribe(this);
        mView.setListener(null);
        super.detachView();
    }

    /** {@inheritDoc} */
    @Override
    public void nextClickHandler() {
        mLoadingSpinnerManager.showLoading(true);
        // Store data.
        mModel.setVerificationCode(mView.getVerificationCode());

        if (mModel.hasAllData()) {
            ShiftPlatform.completeVerification(mModel.getVerificationRequest(), mModel.getVerificationId());
        }
    }

    private String getTitle() {
        PhoneNumberVo phoneNumber = (PhoneNumberVo) mModel.getBaseData().
                getUniqueDataPoint(DataPointVo.DataPointType.Phone, new PhoneNumberVo());
        return PhoneHelperUtil.formatPhone(phoneNumber.phoneNumber);
    }

    /**
     * Called when the restart verification API response has been received.
     * @param response API response.
     */
    @Subscribe
    public void handleResponse(VerificationResponseVo response) {
        if (response != null) {
            PhoneNumberVo phone = mModel.getPhoneFromBaseData();
            if(phone.hasVerification()) {
                phone.getVerification().setVerificationId(response.verification_id);
                phone.getVerification().setVerificationType(response.verification_type);
            }
            else{
                phone.setVerification(new VerificationVo(response.verification_id, response.verification_type));
            }
        }
    }

    /**
     * Called when the finish phone verification API response has been received.
     * @param response API response.
     */
    @Subscribe
    public void handleResponse(FinishVerificationResponseVo response) {
        mLoadingSpinnerManager.showLoading(false);
        if (response != null) {
            PhoneNumberVo phone = mModel.getPhoneFromBaseData();
            phone.getVerification().setVerificationStatus(response.status);
            if(!phone.getVerification().isVerified()) {
                displayWrongCodeMessage();
            }
            else {
                mDelegate.phoneVerificationSucceeded(response);
            }
        }
    }

    /**
     * Called when an API error has been received.
     * @param error API error.
     */
    @Subscribe
    public void handleApiError(ApiErrorVo error) {
        setApiError(error);
    }

    @Override
    public void resendClickHandler() {
        ShiftPlatform.restartVerification(mModel.getVerificationId());
        mView.displaySentMessage(mActivity.getString(R.string.phone_verification_resent));
    }

    private void displayWrongCodeMessage() {
        mView.displayErrorMessage(mActivity.getString(R.string.phone_verification_error));
        mView.clearPinView();
    }
}