package com.shiftpayments.link.sdk.ui.workflow;

import android.app.Activity;

import com.shiftpayments.link.sdk.ui.activities.ActionNotSupportedActivity;

/**
 * Created by adrian on 09/11/2017.
 */

public class ActionNotSupportedModule extends ShiftBaseModule {

    public ActionNotSupportedModule(Activity activity, Command onFinish, Command onBack) {
        super(activity, onFinish, onBack);
    }

    @Override
    public void initialModuleSetup() {
        startActivity(ActionNotSupportedActivity.class);
    }

}