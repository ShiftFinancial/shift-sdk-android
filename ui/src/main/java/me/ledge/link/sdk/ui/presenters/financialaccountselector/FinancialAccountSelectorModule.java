package me.ledge.link.sdk.ui.presenters.financialaccountselector;

import android.app.Activity;

import me.ledge.link.sdk.ui.Command;
import me.ledge.link.sdk.ui.LedgeBaseModule;
import me.ledge.link.sdk.ui.activities.financialaccountselector.AddCardActivity;
import me.ledge.link.sdk.ui.activities.financialaccountselector.AddFinancialAccountListActivity;

/**
 * Created by adrian on 29/12/2016.
 */

public class FinancialAccountSelectorModule extends LedgeBaseModule implements AddFinancialAccountListDelegate, AddCardDelegate {

    private static FinancialAccountSelectorModule instance;
    public Command onBack;

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

        // TODO: if user has accounts, showSelectFinancialAccountListSelector()
        showAddFinancialAccountListSelector();
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
    public void cardAdded() {

    }

    @Override
    public void addCardOnBackPressed() {
        showAddFinancialAccountListSelector();
    }

    private void showAddFinancialAccountListSelector() {
        startActivity(AddFinancialAccountListActivity.class);
    }
}