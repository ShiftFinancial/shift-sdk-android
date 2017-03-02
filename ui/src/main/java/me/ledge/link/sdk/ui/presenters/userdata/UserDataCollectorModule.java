package me.ledge.link.sdk.ui.presenters.userdata;

import android.app.Activity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java8.util.concurrent.CompletableFuture;
import me.ledge.link.api.vos.ApiErrorVo;
import me.ledge.link.api.vos.DataPointList;
import me.ledge.link.api.vos.DataPointVo;
import me.ledge.link.api.vos.responses.config.RequiredDataPointVo;
import me.ledge.link.api.vos.responses.config.RequiredDataPointsListResponseVo;
import me.ledge.link.api.vos.responses.users.CreateUserResponseVo;
import me.ledge.link.sdk.sdk.LedgeLinkSdk;
import me.ledge.link.sdk.sdk.storages.ConfigStorage;
import me.ledge.link.sdk.ui.Command;
import me.ledge.link.sdk.ui.LedgeBaseModule;
import me.ledge.link.sdk.ui.LedgeLinkUi;
import me.ledge.link.sdk.ui.activities.MvpActivity;
import me.ledge.link.sdk.ui.activities.userdata.AddressActivity;
import me.ledge.link.sdk.ui.activities.userdata.AnnualIncomeActivity;
import me.ledge.link.sdk.ui.activities.userdata.CreditScoreActivity;
import me.ledge.link.sdk.ui.activities.userdata.IdentityVerificationActivity;
import me.ledge.link.sdk.ui.activities.userdata.MonthlyIncomeActivity;
import me.ledge.link.sdk.ui.activities.userdata.PersonalInformationActivity;
import me.ledge.link.sdk.ui.activities.verification.EmailVerificationActivity;
import me.ledge.link.sdk.ui.activities.verification.PhoneVerificationActivity;
import me.ledge.link.sdk.ui.presenters.verification.EmailVerificationDelegate;
import me.ledge.link.sdk.ui.presenters.verification.PhoneVerificationDelegate;
import me.ledge.link.sdk.ui.storages.SharedPreferencesStorage;
import me.ledge.link.sdk.ui.storages.UserStorage;

/**
 * Created by adrian on 29/12/2016.
 */

public class UserDataCollectorModule extends LedgeBaseModule implements PhoneVerificationDelegate,
        EmailVerificationDelegate, IdentityVerificationDelegate, AddressDelegate,
        AnnualIncomeDelegate, MonthlyIncomeDelegate, CreditScoreDelegate, PersonalInformationDelegate {

    private static UserDataCollectorModule instance;
    public Command onFinish;
    public Command onBack;
    private LinkedList mRequiredDataPointList;
    private ArrayList<Class<? extends MvpActivity>> mRequiredActivities;

    public static synchronized  UserDataCollectorModule getInstance(Activity activity) {
        if (instance == null) {
            instance = new UserDataCollectorModule(activity);
        }
        return instance;
    }

    private UserDataCollectorModule(Activity activity) {
        super(activity);
        mRequiredDataPointList = null;
        mRequiredActivities = new ArrayList<>();
    }

    @Override
    public void initialModuleSetup() {
        LedgeLinkSdk.getResponseHandler().unsubscribe(this);
        LedgeLinkSdk.getResponseHandler().subscribe(this);
        CompletableFuture
                .supplyAsync(()-> ConfigStorage.getInstance().getRequiredUserData())
                .exceptionally(ex -> {
                    startActivity(PersonalInformationActivity.class);
                    return null;
                })
                .thenAccept(this::parseRequiredData);
    }

    /**
     * Called when the get current user info response has been received.
     * @param userInfo API response.
     */
    @Subscribe
    public void handleResponse(DataPointList userInfo) {
        LedgeLinkSdk.getResponseHandler().unsubscribe(this);
        UserStorage.getInstance().setUserData(userInfo);
        compareRequiredDataPointsWithCurrent(userInfo);
    }

    /**
     * Called when the login response has been received.
     * @param response API response.
     */
    @Subscribe
    public void handleToken(CreateUserResponseVo response) {
        LedgeLinkSdk.getResponseHandler().unsubscribe(this);
        if (response != null) {
            UserStorage.getInstance().setBearerToken(response.user_token);
            SharedPreferencesStorage.storeUserToken(getActivity(), response.user_token);
            stopModule();
        }
    }

    /**
     * Called when an API error has been received.
     * @param error API error.
     */
    @Subscribe
    public void handleApiError(ApiErrorVo error) {
        LedgeLinkSdk.getResponseHandler().unsubscribe(this);
        startActivity(PersonalInformationActivity.class);
    }

    @Override
    public void phoneVerificationSucceeded(DataPointVo phone) {
        if(phone.getVerification().getAlternateEmailCredentials() != null) {
            startActivity(EmailVerificationActivity.class);
        }
        else {
            startActivity(getActivityAtPosition(PhoneVerificationActivity.class, 1));
        }
    }

    @Override
    public void phoneVerificationOnBackPressed() {
        startActivity(getActivityAtPosition(PhoneVerificationActivity.class, -1));
    }

    @Override
    public void emailVerificationSucceeded() {
        if(isCurrentPhoneVerified()) {
            LedgeLinkSdk.getResponseHandler().unsubscribe(this);
            LedgeLinkSdk.getResponseHandler().subscribe(this);
            LedgeLinkUi.loginUser(getLoginData());
        }
        else {
            Class nextActivity = getActivityAtPosition(EmailVerificationActivity.class, 1);
            if(nextActivity != null) {
                startActivity(nextActivity);
            }
            else {
                stopModule();
            }
        }
    }

    @Override
    public void emailOnBackPressed() {
        startActivity(mRequiredActivities.get(0));
    }

    @Override
    public void identityVerificationSucceeded() {
        stopModule();
    }

    @Override
    public void identityVerificationOnBackPressed() {
        startActivity(getActivityAtPosition(IdentityVerificationActivity.class, -1));
    }

    @Override
    public void addressStored() {
        startActivity(getActivityAtPosition(AddressActivity.class, 1));
    }

    @Override
    public void addressOnBackPressed() {
        startActivity(mRequiredActivities.get(0));
    }

    @Override
    public void annualIncomeStored() {
        startActivity(getActivityAtPosition(AnnualIncomeActivity.class, 1));
    }

    @Override
    public void annualIncomeOnBackPressed() {
        startActivity(getActivityAtPosition(AnnualIncomeActivity.class, -1));
    }

    @Override
    public void monthlyIncomeStored() {
        startActivity(getActivityAtPosition(MonthlyIncomeActivity.class, 1));
    }

    @Override
    public void monthlyIncomeOnBackPressed() {
        startActivity(getActivityAtPosition(MonthlyIncomeActivity.class, -1));
    }

    @Override
    public void creditScoreStored() {
        startActivity(getActivityAtPosition(CreditScoreActivity.class, 1));
    }

    @Override
    public void creditScoreOnBackPressed() {
        startActivity(getActivityAtPosition(CreditScoreActivity.class, -1));
    }

    @Override
    public void personalInformationStored() {
        if(isPhoneVerificationRequired() && !isCurrentPhoneVerified()) {
            startActivity(PhoneVerificationActivity.class);
        }
        else {
            startActivity(AddressActivity.class);
        }
    }

    @Override
    public void personalInformationOnBackPressed() {
        onBack.execute();
    }

    private void parseRequiredData(RequiredDataPointsListResponseVo requiredDataPointsList) {
        mRequiredDataPointList = new LinkedList<>(Arrays.asList(requiredDataPointsList.data));

        CompletableFuture
                .supplyAsync(()-> ConfigStorage.getInstance().getPOSMode())
                .exceptionally(ex -> {
                    startActivity(PersonalInformationActivity.class);
                    return null;
                })
                .thenAccept(this::getCurrentUserOrContinue);
    }

    private void getCurrentUserOrContinue(boolean isPOSMode) {
        String userToken = SharedPreferencesStorage.getUserToken(super.getActivity(), isPOSMode);
        if(!isPOSMode && userToken != null) {
            LedgeLinkUi.getApiWrapper().setBearerToken(userToken);
            LedgeLinkUi.getCurrentUser();
        }
        else {
            compareRequiredDataPointsWithCurrent(new DataPointList());
        }
    }

    private void compareRequiredDataPointsWithCurrent(DataPointList currentDataPointList) {
        HashMap<DataPointVo.DataPointType, List<DataPointVo>> currentDataPointMap = currentDataPointList.getDataPoints();

        for(DataPointVo.DataPointType currentType : currentDataPointMap.keySet()) {
            for(Iterator<RequiredDataPointVo> listIterator = mRequiredDataPointList.iterator(); listIterator.hasNext();) {
                RequiredDataPointVo requiredDataPointVo = listIterator.next();
                // TODO: this will be refactored on BE side (instead of int -> String)
                if(requiredDataPointVo.type == currentType.ordinal()+1) {
                    if(!requiredDataPointVo.verificationRequired || currentDataPointMap.get(currentType).get(0).isVerified()) {
                        listIterator.remove();
                    }
                }
            }
        }

        fillRequiredActivitiesList();
        startModule();
    }

    private void startModule() {
        if(mRequiredActivities.isEmpty()) {
            stopModule();
        }
        else {
            startActivity(mRequiredActivities.get(0));
        }
    }

    private void stopModule() {
        LedgeLinkSdk.getResponseHandler().unsubscribe(this);
        onFinish.execute();
    }

    private void fillRequiredActivitiesList() {
        if(!mRequiredDataPointList.isEmpty()) {
            for (RequiredDataPointVo requiredDataPointVo : (Iterable<RequiredDataPointVo>) mRequiredDataPointList) {
                if(requiredDataPointVo.type == 1) {
                    addRequiredActivity(PersonalInformationActivity.class);
                }
                if(requiredDataPointVo.type == 2) {
                    addRequiredActivity(PersonalInformationActivity.class);
                    if(requiredDataPointVo.verificationRequired) {
                        addRequiredActivity(PhoneVerificationActivity.class);
                    }
                }
                if(requiredDataPointVo.type == 3) {
                    addRequiredActivity(PersonalInformationActivity.class);
                    if(requiredDataPointVo.verificationRequired) {
                        addRequiredActivity(EmailVerificationActivity.class);
                    }
                }
                if(requiredDataPointVo.type == 6 || requiredDataPointVo.type == 7) {
                    addRequiredActivity(AddressActivity.class);
                }
                if(requiredDataPointVo.type == 8) {
                    addRequiredActivity(AnnualIncomeActivity.class);
                }
                if(requiredDataPointVo.type == 9) {
                    addRequiredActivity(AnnualIncomeActivity.class);
                    addRequiredActivity(MonthlyIncomeActivity.class);
                }
                if(requiredDataPointVo.type == 10) {
                    addRequiredActivity(CreditScoreActivity.class);
                }
            }
            addRequiredActivity(IdentityVerificationActivity.class);
        }
    }

    private void addRequiredActivity(Class requiredActivity) {
        if(!mRequiredActivities.contains(requiredActivity)) {
            mRequiredActivities.add(requiredActivity);
        }
    }

    private Class getActivityAtPosition(Class currentActivity, int positionOffset) {
        Class result = null;
        int currentIndex = mRequiredActivities.indexOf(currentActivity);
        if(currentIndex != -1) {
            int targetIndex = currentIndex + positionOffset;
            if (targetIndex >= 0 && targetIndex < mRequiredActivities.size()) {
                result = mRequiredActivities.get(targetIndex);
            }
        }

        return result;
    }

    private boolean isPhoneVerificationRequired() {
        boolean isPhoneVerificationRequired = false;
        for (RequiredDataPointVo requiredDataPointVo : (Iterable<RequiredDataPointVo>) mRequiredDataPointList) {
            if (requiredDataPointVo.type == 2 && requiredDataPointVo.verificationRequired) {
                isPhoneVerificationRequired = true;
            }
        }
        return isPhoneVerificationRequired;
    }

    private boolean isCurrentPhoneVerified() {
        DataPointList currentData = UserStorage.getInstance().getUserData();
        DataPointVo currentPhone = currentData.getUniqueDataPoint(DataPointVo.DataPointType.PhoneNumber, null);
        return currentPhone.isVerified();
    }

    public DataPointList getLoginData() {
        DataPointList base = UserStorage.getInstance().getUserData();
        DataPointVo.Email emailAddress = (DataPointVo.Email) base.getUniqueDataPoint(
                DataPointVo.DataPointType.Email, new DataPointVo.Email());
        DataPointVo.PhoneNumber phoneNumber = (DataPointVo.PhoneNumber) base.getUniqueDataPoint(
                DataPointVo.DataPointType.PhoneNumber, new DataPointVo.PhoneNumber());

        DataPointList data = new DataPointList();
        data.add(emailAddress);
        data.add(phoneNumber);
        return data;
    }
}