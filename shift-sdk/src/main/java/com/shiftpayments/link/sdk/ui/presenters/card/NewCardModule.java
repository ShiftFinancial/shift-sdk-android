package com.shiftpayments.link.sdk.ui.presenters.card;

import android.app.Activity;

import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.OAuthCredentialVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.SetBalanceStoreRequestVo;
import com.shiftpayments.link.sdk.api.vos.responses.ApiEmptyResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.ApiErrorVo;
import com.shiftpayments.link.sdk.api.vos.responses.cardapplication.CardApplicationResponseVo;
import com.shiftpayments.link.sdk.api.vos.responses.cardconfig.DisclaimerConfiguration;
import com.shiftpayments.link.sdk.api.vos.responses.config.DataPointGroupVo;
import com.shiftpayments.link.sdk.api.vos.responses.config.RequiredDataPointVo;
import com.shiftpayments.link.sdk.api.vos.responses.workflow.CallToActionVo;
import com.shiftpayments.link.sdk.api.vos.responses.workflow.CollectUserDataActionConfigurationVo;
import com.shiftpayments.link.sdk.api.vos.responses.workflow.UserDataCollectorConfigurationVo;
import com.shiftpayments.link.sdk.sdk.ShiftLinkSdk;
import com.shiftpayments.link.sdk.sdk.storages.ConfigStorage;
import com.shiftpayments.link.sdk.ui.R;
import com.shiftpayments.link.sdk.ui.ShiftPlatform;
import com.shiftpayments.link.sdk.ui.presenters.custodianselector.CustodianSelectorDelegate;
import com.shiftpayments.link.sdk.ui.presenters.custodianselector.CustodianSelectorModule;
import com.shiftpayments.link.sdk.ui.presenters.showdisclaimer.showgenericmessage.ShowDisclaimerModule;
import com.shiftpayments.link.sdk.ui.presenters.userdata.UserDataCollectorModule;
import com.shiftpayments.link.sdk.ui.storages.CardStorage;
import com.shiftpayments.link.sdk.ui.vos.ApplicationVo;
import com.shiftpayments.link.sdk.ui.workflow.ActionNotSupportedModule;
import com.shiftpayments.link.sdk.ui.workflow.Command;
import com.shiftpayments.link.sdk.ui.workflow.ModuleManager;
import com.shiftpayments.link.sdk.ui.workflow.ShiftBaseModule;
import com.shiftpayments.link.sdk.ui.workflow.WorkflowModule;
import com.shiftpayments.link.sdk.ui.workflow.WorkflowObjectStatusInterface;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shiftpayments.link.sdk.api.utils.workflow.WorkflowActionType.COLLECT_USER_DATA;
import static com.shiftpayments.link.sdk.api.utils.workflow.WorkflowActionType.ISSUE_CARD;
import static com.shiftpayments.link.sdk.api.utils.workflow.WorkflowActionType.SELECT_BALANCE_STORE;
import static com.shiftpayments.link.sdk.api.utils.workflow.WorkflowActionType.SHOW_DISCLAIMER;

public class NewCardModule extends WorkflowModule implements CustodianSelectorDelegate {

    public NewCardModule(Activity activity,
                         WorkflowObjectStatusInterface getWorkflowObjectStatus, Command onFinish,
                         Command onBack) {
        super(activity, getWorkflowObjectStatus, onFinish, onBack);
    }

    @Override
    public void initialModuleSetup() {
        ShiftLinkSdk.getResponseHandler().subscribe(this);
        ShiftPlatform.createCardApplication(ConfigStorage.getInstance().getCardConfig().cardProduct.id);
    }

    @Override
    public void startNextModule() {
        mWorkFlowObject = getWorkflowObjectStatus.getApplication(mWorkFlowObject);
        startAppropriateModule();
    }

    /**
     * Called when create card application has been received.
     * @param application Card Application.
     */
    @Subscribe
    public void handleApplication(CardApplicationResponseVo application) {
        ShiftLinkSdk.getResponseHandler().unsubscribe(this);
        ApplicationVo cardApplication = new ApplicationVo(application.id, application.nextAction, application.workflowObjectId);
        CardStorage.getInstance().setApplication(cardApplication);
        mWorkFlowObject = cardApplication;
        startAppropriateModule();
    }

    @Override
    public void onTokensRetrieved(String accessToken, String refreshToken) {
        ShiftLinkSdk.getResponseHandler().subscribe(this);
        OAuthCredentialVo coinbaseCredentials = new OAuthCredentialVo(accessToken, refreshToken);
        SetBalanceStoreRequestVo setBalanceStoreRequest = new SetBalanceStoreRequestVo("coinbase", coinbaseCredentials);
        ShiftPlatform.setBalanceStore(CardStorage.getInstance().getApplication().applicationId, setBalanceStoreRequest);
    }

    @Subscribe
    public void handleResponse(ApiEmptyResponseVo response) {
        ShiftLinkSdk.getResponseHandler().unsubscribe(this);
        super.onFinish.execute();
    }

    /**
     * Called when an API error has been received.
     * @param error API error.
     */
    @Subscribe
    public void handleApiError(ApiErrorVo error) {
        ShiftLinkSdk.getResponseHandler().unsubscribe(this);
        super.showError(error);
    }

    private void startAppropriateModule() {
        switch (mWorkFlowObject.nextAction.actionType) {
            case COLLECT_USER_DATA:
                startUserDataCollector();
                break;
            case SELECT_BALANCE_STORE:
                startCustodianModule();
                break;
            case ISSUE_CARD:
                issueVirtualCard();
                break;
            case SHOW_DISCLAIMER:
                showDisclaimer();
                break;
            default:
                new ActionNotSupportedModule(getActivity(), this.onFinish, this.onBack).initialModuleSetup();
        }
    }

    private void startUserDataCollector() {
        CollectUserDataActionConfigurationVo configuration = (CollectUserDataActionConfigurationVo) mWorkFlowObject.nextAction.configuration;
        List<RequiredDataPointVo> requiredDataPointList = new ArrayList<>();
        for(DataPointGroupVo dataPointGroup : configuration.requiredDataPointsList.data) {
            requiredDataPointList.addAll(Arrays.asList(dataPointGroup.datapoints.data));
        }
        ConfigStorage.getInstance().setRequiredUserData(requiredDataPointList.toArray(new RequiredDataPointVo[0]));
        UserDataCollectorModule userDataCollectorModule = UserDataCollectorModule.getInstance(getActivity(), this::startNextModule, super.onBack);
        UserDataCollectorConfigurationVo config = new UserDataCollectorConfigurationVo(getActivity().getString(R.string.id_verification_title_issue_card), new CallToActionVo(getActivity().getString(R.string.id_verification_next_button_issue_card)));
        ConfigStorage.getInstance().setUserDataCollectorConfig(config);
        startModule(userDataCollectorModule);
    }

    private void startCustodianModule() {
        Command onFinish = ()->{
            setCurrentModule();
            this.startNextModule();
        };
        setCurrentModule();
        CustodianSelectorModule custodianSelectorModule = CustodianSelectorModule.getInstance(this.getActivity(), this, onFinish, super.onBack);
        startModule(custodianSelectorModule);
    }

    private void issueVirtualCard() {
        setCurrentModule();
        IssueVirtualCardModule issueVirtualCardModule = new IssueVirtualCardModule(getActivity(), this.onFinish, this::onIssueVirtualCardBackPressed);
        startModule(issueVirtualCardModule);
    }

    private void onIssueVirtualCardBackPressed() {
        SoftReference<ShiftBaseModule> userDataCollectorModule = new SoftReference<>(UserDataCollectorModule.getInstance(getActivity(), this.onFinish, super.onBack));
        ModuleManager.getInstance().setModule(userDataCollectorModule);
    }

    private void setCurrentModule() {
        SoftReference<ShiftBaseModule> moduleSoftReference = new SoftReference<>(this);
        ModuleManager.getInstance().setModule(moduleSoftReference);
    }

    private void showDisclaimer() {
        Command onFinishCallback = ()->{
            setCurrentModule();
            this.startNextModule();
        };
        ShowDisclaimerModule disclaimerModule = ShowDisclaimerModule.getInstance(getActivity(),
                onFinishCallback, onBack, (DisclaimerConfiguration) mWorkFlowObject.nextAction.configuration,
                mWorkFlowObject.workflowObjectId, mWorkFlowObject.nextAction.actionId);
        disclaimerModule.initialModuleSetup();
    }
}
