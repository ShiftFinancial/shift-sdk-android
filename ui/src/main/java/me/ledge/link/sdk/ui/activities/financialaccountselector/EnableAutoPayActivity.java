package me.ledge.link.sdk.ui.activities.financialaccountselector;

import android.view.View;

import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.activities.MvpActivity;
import me.ledge.link.sdk.ui.models.financialaccountselector.EnableAutoPayModel;
import me.ledge.link.sdk.ui.presenters.financialaccountselector.EnableAutoPayDelegate;
import me.ledge.link.sdk.ui.presenters.financialaccountselector.EnableAutoPayPresenter;
import me.ledge.link.sdk.ui.presenters.userdata.BaseDelegate;
import me.ledge.link.sdk.ui.views.financialaccountselector.EnableAutoPayView;


/**
 * Wires up the MVP pattern for the screen that checks if auto-pay should be enabled.
 * @author Adrian
 */
public class EnableAutoPayActivity
        extends MvpActivity<EnableAutoPayModel, EnableAutoPayView, EnableAutoPayPresenter> {

    /** {@inheritDoc} */
    @Override
    protected EnableAutoPayView createView() {
        return (EnableAutoPayView) View.inflate(this, R.layout.act_enable_auto_pay, null);
    }

    /** {@inheritDoc} */
    @Override
    protected EnableAutoPayPresenter createPresenter(BaseDelegate delegate) {
        if(delegate instanceof EnableAutoPayDelegate) {
            return new EnableAutoPayPresenter(this, (EnableAutoPayDelegate) delegate);
        }
        else {
            throw new NullPointerException("Received Module does not implement EnableAutoPayDelegate!");
        }
    }
}
