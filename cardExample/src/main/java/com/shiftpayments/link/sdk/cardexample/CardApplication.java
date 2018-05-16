package com.shiftpayments.link.sdk.cardexample;

import android.support.multidex.MultiDexApplication;

import io.branch.referral.Branch;

public class CardApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
    }
}