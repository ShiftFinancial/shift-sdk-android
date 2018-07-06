package com.shiftpayments.link.sdk.ui.workflow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import com.shiftpayments.link.sdk.api.vos.responses.ApiErrorVo;
import com.shiftpayments.link.sdk.api.vos.responses.SessionExpiredErrorVo;
import com.shiftpayments.link.sdk.ui.R;
import com.shiftpayments.link.sdk.ui.ShiftPlatform;
import com.shiftpayments.link.sdk.ui.presenters.BaseDelegate;
import com.shiftpayments.link.sdk.ui.utils.ApiErrorUtil;

import java.lang.ref.SoftReference;

/**
 * Created by adrian on 29/12/2016.
 */

public abstract class ShiftBaseModule implements NavigationCommand, BaseDelegate {

    private Activity mActivity;
    public Command onBack;
    public Command onFinish;
    private ProgressDialog mProgressDialog;

    public ShiftBaseModule(Activity activity, Command onFinish, Command onBack) {
        mActivity = activity;
        this.onFinish = onFinish;
        this.onBack = onBack;
    }

    public abstract void initialModuleSetup();

    protected void startModule(ShiftBaseModule module) {
        SoftReference<ShiftBaseModule> moduleSoftReference = new SoftReference<>(module);
        ModuleManager.getInstance().setModule(moduleSoftReference);
        module.initialModuleSetup();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    /**
     * Starts another activity.
     * @param activity The Activity to start.
     */
    @Override
    public void startActivity(Class activity) {
        if (activity != null) {
            mActivity.startActivity(getStartIntent(activity));
        }
        mActivity.finish();
    }

    /**
     * @param activity The Activity to start.
     * @return The {@link Intent} to use to start the next Activity.
     */
    private Intent getStartIntent(Class activity) {
        return new Intent(mActivity, activity);
    }

    protected void showLoading(boolean show) {
        if(mActivity.isFinishing())
        {
            return;
        }
        mActivity.runOnUiThread(() -> {
            if (show) {
                mProgressDialog = ProgressDialog.show(mActivity,null,null);
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mProgressDialog.setContentView(R.layout.include_rl_loading_transparent);
            } else {
                if(mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    protected void showError(ApiErrorVo error) {
        ApiErrorUtil.showErrorMessage(error, getActivity());
    }

    public void handleSessionExpiredError(SessionExpiredErrorVo error) {
        ApiErrorUtil.showErrorMessage(error, mActivity);
        ShiftPlatform.clearUserToken(mActivity);
    }
}
