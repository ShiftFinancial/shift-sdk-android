package me.ledge.link.sdk.ui.presenters.financialaccountselector;


import android.support.v7.app.AppCompatActivity;

import me.ledge.link.sdk.ui.models.financialaccountselector.AddBankAccountModel;
import me.ledge.link.sdk.ui.presenters.ActivityPresenter;
import me.ledge.link.sdk.ui.presenters.Presenter;
import me.ledge.link.sdk.ui.views.financialaccountselector.AddBankAccountView;


/**
 * Concrete {@link Presenter} for the plaid webview.
 * @author Adrian
 */
public class AddBankAccountPresenter
    extends ActivityPresenter<AddBankAccountModel, AddBankAccountView>
    implements Presenter<AddBankAccountModel, AddBankAccountView> {

    private AddBankAccountDelegate mDelegate;

    /**
     * Creates a new {@link ActivityPresenter} instance.
     *
     * @param activity Activity.
     */
    public AddBankAccountPresenter(AppCompatActivity activity, AddBankAccountDelegate delegate) {
        super(activity);
        mDelegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(AddBankAccountView view) {
        super.attachView(view);
    }

    @Override
    public void onBack() {
        mDelegate.addBankAccountOnBackPressed();
    }

    @Override
    public AddBankAccountModel createModel() {
        return new AddBankAccountModel();
    }

    public void publicTokenReceived(String token) {
        mDelegate.bankAccountLinked(token);
    }

}
