package me.ledge.link.sdk.ui.presenters.link;

import android.app.Activity;
import android.widget.Toast;

import java8.util.concurrent.CompletableFuture;
import me.ledge.link.api.vos.responses.config.ConfigResponseVo;
import me.ledge.link.api.vos.responses.workflow.ActionVo;
import me.ledge.link.api.vos.responses.workflow.GenericMessageConfigurationVo;
import me.ledge.link.sdk.sdk.storages.ConfigStorage;
import me.ledge.link.sdk.ui.presenters.loanapplication.LoanApplicationModule;
import me.ledge.link.sdk.ui.presenters.showgenericmessage.ShowGenericMessageModule;
import me.ledge.link.sdk.ui.presenters.userdata.UserDataCollectorModule;
import me.ledge.link.sdk.ui.storages.UIStorage;
import me.ledge.link.sdk.ui.workflow.LedgeBaseModule;

/**
 * Created by adrian on 29/12/2016.
 */

public class LinkModule extends LedgeBaseModule {

    public LinkModule(Activity activity) {
        super(activity);
    }
    private boolean mUserHasAllRequiredData;
    private boolean mShowWelcomeScreen;
    private ActionVo mWelcomeScreenAction;

    @Override
    public void initialModuleSetup() {
        mUserHasAllRequiredData = false;
        CompletableFuture
                .supplyAsync(()-> UIStorage.getInstance().getContextConfig())
                .exceptionally(ex -> {
                    showError(ex.getMessage());
                    return null;
                })
                .thenAccept(this::projectConfigRetrieved);
    }

    private void showWelcomeScreen() {
        GenericMessageConfigurationVo actionConfig = (GenericMessageConfigurationVo) mWelcomeScreenAction.configuration;
        ShowGenericMessageModule mShowGenericMessageModule = ShowGenericMessageModule.getInstance(this.getActivity(), actionConfig);
        mShowGenericMessageModule.onFinish = this::showOrSkipLoanInfo;
        mShowGenericMessageModule.onBack = this::showHomeActivity;
        startModule(mShowGenericMessageModule);
    }

    private void showOrSkipLoanInfo() {
        if (isLoanInfoRequired()) {
            showLoanInfo();
        } else {
            skipLoanInfo();
        }
    }

    private void showLoanInfoOrBack() {
        if (isLoanInfoRequired()) {
            showLoanInfo();
        } else {
            if(mShowWelcomeScreen) {
                showWelcomeScreen();
            }
            else {
                showHomeActivity();
            }
        }
    }

    private boolean isLoanInfoRequired() {
        boolean shouldSkipLoanAmount = ConfigStorage.getInstance().getSkipLoanAmount();
        boolean shouldSkipLoanPurpose = ConfigStorage.getInstance().getSkipLoanPurpose();
        return !shouldSkipLoanAmount || !shouldSkipLoanPurpose;
    }

    private void showLoanInfo() {
        LoanInfoModule loanInfoModule = LoanInfoModule.getInstance(this.getActivity());
        loanInfoModule.userHasAllRequiredData = mUserHasAllRequiredData;
        if(mUserHasAllRequiredData) {
            loanInfoModule.onGetOffers = this::showOffersList;
            loanInfoModule.onFinish = this::showOffersList;
            loanInfoModule.onUpdateProfile = () -> startUserDataCollectorModule(true);
        }
        else {
            loanInfoModule.onGetOffers = null;
            loanInfoModule.onFinish = this::showUserDataCollector;
        }
        if(mShowWelcomeScreen) {
            loanInfoModule.onBack = this::showWelcomeScreen;
        }
        else {
            loanInfoModule.onBack = this::showHomeActivity;
        }
        startModule(loanInfoModule);
    }

    private void skipLoanInfo() {
        if(mUserHasAllRequiredData) {
            showOffersList();
        }
        else {
            showUserDataCollector();
        }
    }

    private void showUserDataCollector() {
        startUserDataCollectorModule(false);
    }

    private void startUserDataCollectorModule(boolean updateProfile) {
        UserDataCollectorModule userDataCollectorModule = UserDataCollectorModule.getInstance(this.getActivity());
        userDataCollectorModule.onUserHasAllRequiredData = null;
        userDataCollectorModule.onFinish = this::showOffersList;
        userDataCollectorModule.onBack = this::showLoanInfoOrBack;
        userDataCollectorModule.isUpdatingProfile = updateProfile;
        startModule(userDataCollectorModule);
    }

    private void showOffersList() {
        mUserHasAllRequiredData = true;
        LoanApplicationModule loanApplicationModule = LoanApplicationModule.getInstance(this.getActivity());
        loanApplicationModule.onUpdateUserProfile = () -> startUserDataCollectorModule(true);
        loanApplicationModule.onBack = this::showOrSkipLoanInfo;
        startModule(loanApplicationModule);
    }

    private void showHomeActivity() {
        Activity currentActivity = this.getActivity();
        currentActivity.finish();
        currentActivity.startActivity(currentActivity.getIntent());
    }

    private void askDataCollectorIfUserHasAllRequiredData() {
        UserDataCollectorModule userDataCollectorModule = UserDataCollectorModule.getInstance(this.getActivity());
        userDataCollectorModule.onFinish = this::showOrSkipWelcomeScreen;
        userDataCollectorModule.onUserDoesNotHaveAllRequiredData = () -> {
            mUserHasAllRequiredData = false;
            showOrSkipWelcomeScreen();
        };
        userDataCollectorModule.onUserHasAllRequiredData = () -> {
            mUserHasAllRequiredData = true;
            showOrSkipWelcomeScreen();
        };
        startModule(userDataCollectorModule);
    }

    private void showOrSkipWelcomeScreen() {
        if(mShowWelcomeScreen) {
            showWelcomeScreen();
        }
        else {
            showOrSkipLoanInfo();
        }
    }

    private void projectConfigRetrieved(ConfigResponseVo configResponseVo) {
        mShowWelcomeScreen = (configResponseVo.welcomeScreenAction.status != 0);
        if(mShowWelcomeScreen) {
            mWelcomeScreenAction = configResponseVo.welcomeScreenAction;
        }
        askDataCollectorIfUserHasAllRequiredData();
    }

    private void showError(String errorMessage) {
        if(!errorMessage.isEmpty()) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
