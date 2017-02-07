package me.ledge.link.sdk.ui.presenters.financialaccountselector;

import android.app.Activity;

import me.ledge.link.api.vos.Card;
import me.ledge.link.api.vos.DataPointList;
import me.ledge.link.api.vos.FinancialAccountVo;
import me.ledge.link.api.vos.requests.financialaccounts.AddBankAccountRequestVo;
import me.ledge.link.sdk.ui.Command;
import me.ledge.link.sdk.ui.LedgeBaseModule;
import me.ledge.link.sdk.ui.LedgeLinkUi;
import me.ledge.link.sdk.ui.activities.financialaccountselector.AddBankAccountActivity;
import me.ledge.link.sdk.ui.activities.financialaccountselector.AddCardActivity;
import me.ledge.link.sdk.ui.activities.financialaccountselector.AddFinancialAccountListActivity;
import me.ledge.link.sdk.ui.activities.financialaccountselector.EnableAutoPayActivity;
import me.ledge.link.sdk.ui.activities.financialaccountselector.IntermediateFinancialAccountListActivity;
import me.ledge.link.sdk.ui.activities.financialaccountselector.SelectFinancialAccountListActivity;
import me.ledge.link.sdk.ui.models.financialaccountselector.SelectFinancialAccountModel;
import me.ledge.link.sdk.ui.storages.UserStorage;

/**
 * Created by adrian on 29/12/2016.
 */

public class FinancialAccountSelectorModule extends LedgeBaseModule
        implements AddFinancialAccountListDelegate, AddCardDelegate, AddBankAccountDelegate,
        SelectFinancialAccountListDelegate, IntermediateFinancialAccountListDelegate, EnableAutoPayDelegate {

    private static FinancialAccountSelectorModule instance;
    public Command onBack;

    private FinancialAccountVo selectedFinancialAccount;

    public static synchronized FinancialAccountSelectorModule getInstance(Activity activity) {
        if (instance == null) {
            instance = new FinancialAccountSelectorModule(activity);
        }
        return instance;
    }

    private FinancialAccountSelectorModule(Activity activity) {
        super(activity);
    }

    @Override
    public void initialModuleSetup() {
        startActivity(IntermediateFinancialAccountListActivity.class);
    }

    @Override
    public void addFinancialAccountListOnBackPressed() {
        onBack.execute();
    }

    @Override
    public void addCard() {
        startActivity(AddCardActivity.class);
    }

    @Override
    public void addBankAccount() {
        startActivity(AddBankAccountActivity.class);
    }

    @Override
    public void virtualCardIssued() {
        //TODO receive virtual card from API
        //setSelectedFinancialAccount(card);
        startActivity(EnableAutoPayActivity.class);
    }

    @Override
    public void cardAdded(Card card) {
        LedgeLinkUi.addCard(card);
        setSelectedFinancialAccount(card);
        startActivity(EnableAutoPayActivity.class);
    }

    @Override
    public void addCardOnBackPressed() {
        showAddFinancialAccountListSelector();
    }

    @Override
    public void addBankAccountOnBackPressed() {
        showAddFinancialAccountListSelector();
    }

    @Override
    public void bankAccountLinked(String token) {
        AddBankAccountRequestVo request = new AddBankAccountRequestVo();
        request.publicToken = token;
        LedgeLinkUi.addBankAccount(request);
        startActivity(IntermediateFinancialAccountListActivity.class);
    }

    @Override
    public void selectFinancialAccountListOnBackPressed() {
        onBack.execute();
    }

    @Override
    public void addAccountPressed() {
        showAddFinancialAccountListSelector();
    }

    @Override
    public void accountSelected(SelectFinancialAccountModel model) {
        setSelectedFinancialAccount(model.getFinancialAccount());
        startActivity(EnableAutoPayActivity.class);
    }

    @Override
    public void onIntermediateFinancialAccountListBackPressed() {
        onBack.execute();
    }

    @Override
    public void noFinancialAccountsReceived() {
        showAddFinancialAccountListSelector();
    }

    @Override
    public void financialAccountsReceived(DataPointList userData) {
        UserStorage.getInstance().setUserData(userData);
        showSelectFinancialAccountListSelector();
    }

    private void showAddFinancialAccountListSelector() {
        startActivity(AddFinancialAccountListActivity.class);
    }

    private void showSelectFinancialAccountListSelector() {
        startActivity(SelectFinancialAccountListActivity.class);
    }

    @Override
    public void autoPayEnabled() {

    }

    @Override
    public void autoPayOnBackPressed() {
        onBack.execute();
    }

    @Override
    public FinancialAccountVo getFinancialAccount() {
        return this.getSelectedFinancialAccount();
    }

    public FinancialAccountVo getSelectedFinancialAccount() {
        return selectedFinancialAccount;
    }

    public void setSelectedFinancialAccount(FinancialAccountVo selectedFinancialAccount) {
        this.selectedFinancialAccount = selectedFinancialAccount;
    }
}