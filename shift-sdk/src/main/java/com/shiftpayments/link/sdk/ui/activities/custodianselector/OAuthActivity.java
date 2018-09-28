package com.shiftpayments.link.sdk.ui.activities.custodianselector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.shiftpayments.link.sdk.api.vos.responses.ApiErrorVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.OAuthStatusResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.users.StartOAuthResponseVo;
import com.shiftpayments.link.sdk.sdk.ShiftSdk;
import com.shiftpayments.link.sdk.ui.ShiftPlatform;
import com.shiftpayments.link.sdk.ui.activities.BaseActivity;
import com.shiftpayments.link.sdk.ui.presenters.custodianselector.OAuthDelegate;
import com.shiftpayments.link.sdk.ui.workflow.ModuleManager;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by adrian on 20/02/2018.
 */

public class OAuthActivity extends BaseActivity {

    private OAuthDelegate mCurrentModule;
    private Thread mGetStatusThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ModuleManager.getInstance().getCurrentModule() instanceof OAuthDelegate) {
            mCurrentModule = (OAuthDelegate) ModuleManager.getInstance().getCurrentModule();
        } else {
            throw new NullPointerException("Received Module does not implement OAuthDelegate!");
        }
        ShiftSdk.getResponseHandler().subscribe(this);
        ShiftPlatform.startOAuth(mCurrentModule.getProvider());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGetStatusThread.interrupt();
        ShiftSdk.getResponseHandler().unsubscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // This activity will listen for the OAuth status response in background
        ShiftSdk.getResponseHandler().subscribe(this);
    }


    /**
     * Called when the start oAuth response has been received.
     * @param response API response.
     */
    @Subscribe
    public void handleResponse(StartOAuthResponseVo response) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.url));
        startActivity(browserIntent);
        getStatusPeriodically(response.id);
    }

    /**
     * Called when the start oAuth response has been received.
     * @param response API response.
     */
    @Subscribe
    public void handleResponse(OAuthStatusResponseVo response) {
        if(response.status.equals("passed")) {
            mGetStatusThread.interrupt();
            mCurrentModule.onOauthPassed(response);
            this.finish();
        }
    }

    /**
     * Called when an API error has been received.
     * @param error API error.
     */
    @Subscribe
    public void handleApiError(ApiErrorVo error) {
        mGetStatusThread.interrupt();
        mCurrentModule.onOAuthError(error);
    }

    private void getStatusPeriodically(String id) {
        Runnable getStatus = () -> ShiftPlatform.getOAuthStatus(id);
        mGetStatusThread = new Thread(() -> {
            try {
                while(true) {
                    Thread.sleep(3000);
                    getStatus.run();
                }
            } catch (InterruptedException e) {
                return;
            }
        });
        mGetStatusThread.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.finish();
    }
}
