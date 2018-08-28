package com.shiftpayments.link.sdk.ui.presenters.userdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.shiftpayments.link.sdk.api.vos.datapoints.DataPointVo;
import com.shiftpayments.link.sdk.api.vos.responses.config.RequiredDataPointVo;
import com.shiftpayments.link.sdk.api.vos.responses.workflow.UserDataCollectorConfigurationVo;
import com.shiftpayments.link.sdk.ui.R;
import com.shiftpayments.link.sdk.ui.models.userdata.IdentityVerificationModel;
import com.shiftpayments.link.sdk.ui.presenters.Presenter;
import com.shiftpayments.link.sdk.ui.storages.UIStorage;
import com.shiftpayments.link.sdk.ui.utils.ResourceUtil;
import com.shiftpayments.link.sdk.ui.views.userdata.IdentityVerificationView;
import com.shiftpayments.link.sdk.ui.workflow.ModuleManager;

import java.util.Arrays;

/**
 * Concrete {@link Presenter} for the ID verification screen.
 * @author Wijnand
 */
public class IdentityVerificationPresenter
        extends UserDataPresenter<IdentityVerificationModel, IdentityVerificationView>
        implements Presenter<IdentityVerificationModel, IdentityVerificationView>,
        IdentityVerificationView.ViewListener {

    private IdentityVerificationDelegate mDelegate;
    private boolean mIsSSNRequired;
    private boolean mIsSSNNotAvailableAllowed;
    private boolean mIsBirthdayRequired;
    private UserDataCollectorConfigurationVo mCallToActionConfig;

    /**
     * Creates a new {@link IdentityVerificationPresenter} instance.
     */
    public IdentityVerificationPresenter(AppCompatActivity activity, IdentityVerificationDelegate delegate) {
        super(activity);
        mDelegate = delegate;
        UserDataCollectorModule module = (UserDataCollectorModule) ModuleManager.getInstance().getCurrentModule();

        mIsSSNRequired = false;
        mIsSSNNotAvailableAllowed = false;
        for (RequiredDataPointVo requiredDataPointVo : module.mRequiredDataPointList) {
            if(requiredDataPointVo.type.equals(DataPointVo.DataPointType.SSN)) {
                mIsSSNRequired = true;
                mIsSSNNotAvailableAllowed = requiredDataPointVo.notSpecifiedAllowed;
            }
        }

        mIsBirthdayRequired = module.mRequiredDataPointList.contains(new RequiredDataPointVo(DataPointVo.DataPointType.BirthDate));
        mCallToActionConfig = module.getCallToActionConfig();
    }

    /**
     * @param activity The {@link Activity} that will be hosting the date picker.
     * @return Resource ID of the theme to use with for the birthday date picker.
     */
    private int getBirthdayDialogThemeId(Activity activity) {
        return new ResourceUtil().getResourceIdForAttribute(activity, R.attr.llsdk_userData_datePickerTheme);
    }

    /**
     * @param activity The {@link Activity} containing the attribute values.
     * @return Resource ID of the color to use for the progress bar.
     */
    private int getProgressBarColor(Activity activity) {
        return new ResourceUtil().getResourceIdForAttribute(activity, R.attr.llsdk_userData_progressColor);
    }

    /** {@inheritDoc} */
    @Override
    public IdentityVerificationModel createModel() {
        return new IdentityVerificationModel();
    }

    /** {@inheritDoc} */
    @Override
    protected void populateModelFromStorage() {
        mModel.setExpectedSSNLength(mActivity.getResources().getInteger(R.integer.ssn_length));
        mModel.setMinimumAge(mActivity.getResources().getInteger(R.integer.min_age));
        super.populateModelFromStorage();
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(IdentityVerificationView view) {
        super.attachView(view);
        mView.setListener(this);

        mView.showBirthday(mIsBirthdayRequired);
        if(mIsBirthdayRequired && mModel.hasValidBirthday()) {
            String month = mActivity.getResources().getStringArray(R.array.months_of_year)[mModel.getBirthdateMonth()];
            mView.setBirthdayMonth(month);
            mView.setBirthdayDay(mModel.getBirthdateDay());
            mView.setBirthdayYear(mModel.getBirthdateYear());
        }

        mView.showSSN(mIsSSNRequired);
        mView.showSSNNotAvailableCheckbox(mIsSSNNotAvailableAllowed);
        if(mIsSSNRequired && mModel.hasValidSsn()) {
            mView.setSSN(mModel.getSocialSecurityNumber());
        }

        int progressColor = getProgressBarColor(mActivity);
        if (progressColor != 0) {
            mView.setProgressColor(progressColor);
        }

        if(mCallToActionConfig != null) {
            mView.setButtonText(mCallToActionConfig.callToAction.title.toUpperCase());
            mActivity.getSupportActionBar().setTitle(mCallToActionConfig.title);
        }

        if(((UserDataCollectorModule) ModuleManager.getInstance().getCurrentModule()).isUpdatingProfile) {
            if(mIsSSNRequired && mView.getSocialSecurityNumber().isEmpty()) {
                mView.setMaskedSSN();
            }
        }
    }

    @Override
    public void onBack() {
        mDelegate.identityVerificationOnBackPressed();
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        mResponseHandler.unsubscribe(this);
        mView.setListener(null);
        super.detachView();
    }

    @Override
    public void ssnCheckBoxClickHandler() {
        mView.enableSSNField(!mView.isSSNCheckboxChecked());
        mView.updateSocialSecurityError(false, 0);
    }

    @Override
    public void monthClickHandler() {
        showMonthPicker();
    }

    /** {@inheritDoc} */
    @Override
    public void nextClickHandler() {
        // Validate input.
        if(mView.isSSNCheckboxChecked()) {
            mIsSSNRequired = false;
            mModel.setSocialSecurityNotAvailable(true);
        }

        if(mIsBirthdayRequired) {
            int day = mView.getBirthdayDay().isEmpty() ? 0 : Integer.valueOf(mView.getBirthdayDay());
            int year = mView.getBirthdayYear().isEmpty() ? 0 : Integer.valueOf(mView.getBirthdayYear());
            String[] months = mActivity.getResources().getStringArray(R.array.months_of_year);
            int month = Arrays.asList(months).indexOf(mView.getBirthdayMonth());
            mModel.setBirthday(year, month, day);
            mView.updateBirthdayError(!mModel.hasValidBirthday(), mModel.getBirthdayErrorString());
        }
        if(mIsSSNRequired && userHasUpdatedSSN()) {
            mModel.setSocialSecurityNumber(mView.getSocialSecurityNumber());
            mView.updateSocialSecurityError(!mModel.hasValidSsn(), mModel.getSsnErrorString());
        }

        if(mIsSSNRequired && mIsBirthdayRequired) {
            if(mModel.hasValidData() || ((UserDataCollectorModule) ModuleManager.getInstance().getCurrentModule()).isUpdatingProfile
                    && !userHasUpdatedSSN() && mModel.hasValidBirthday()) {
                saveDataAndExit();
            }
        }
        else if(mIsBirthdayRequired) {
            if(mModel.hasValidBirthday()) {
                saveDataAndExit();
            }
        }
        else if(mIsSSNRequired){
            if(mModel.hasValidSsn() || ((UserDataCollectorModule) ModuleManager.getInstance().getCurrentModule()).isUpdatingProfile
                    && !userHasUpdatedSSN()) {
                saveDataAndExit();
            }
        }
        else {
            saveDataAndExit();
        }
    }

    private boolean userHasUpdatedSSN() {
        return (!mView.getSocialSecurityNumber().equals(mModel.getSocialSecurityNumber()) &&
                !mView.isSSNMasked()) ||
                (!((UserDataCollectorModule) ModuleManager.getInstance().getCurrentModule()).isUpdatingProfile
                && mView.getSocialSecurityNumber()!=null);
    }

    private void saveDataAndExit() {
        super.saveData();
        mDelegate.identityVerificationSucceeded();
    }

    private void showMonthPicker() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mActivity, android.R.layout.select_dialog_singlechoice);
        String[] monthsOfYear = mActivity.getResources().getStringArray(R.array.months_of_year);
        arrayAdapter.addAll(monthsOfYear);

        AlertDialog dialog = new AlertDialog.Builder(mActivity)
            .setTitle(R.string.birthdate_label_month)
            .setPositiveButton(android.R.string.ok, null)
            .setAdapter(arrayAdapter, null)
            .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> dialog.dismiss());
        });
        dialog.getListView().setOnItemClickListener(
                (parent, view, position, id) -> {
                    for(int i=0; i<parent.getChildCount(); i++) {
                        CheckedTextView child = (CheckedTextView) parent.getChildAt(i);
                        if(child.isChecked()) {
                            child.toggle();
                        }
                    }
                    CheckedTextView checkedTextView = (CheckedTextView) view;
                    Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(mActivity, R.drawable.abc_btn_radio_material));
                    DrawableCompat.setTintList(drawable, UIStorage.getInstance().getRadioButtonColors());
                    checkedTextView.setCheckMarkDrawable(drawable);
                    checkedTextView.toggle();
                    mView.setBirthdayMonth(arrayAdapter.getItem(position));
                });
        dialog.show();
    }
}