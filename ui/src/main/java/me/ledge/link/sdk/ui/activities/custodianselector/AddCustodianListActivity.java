package me.ledge.link.sdk.ui.activities.custodianselector;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.coinbase.android.sdk.OAuth;
import com.coinbase.api.entity.OAuthTokensResponse;
import com.coinbase.api.exception.UnauthorizedException;

import java.io.IOException;

import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.activities.MvpActivity;
import me.ledge.link.sdk.ui.models.custodianselector.AddCustodianListModel;
import me.ledge.link.sdk.ui.presenters.custodianselector.AddCustodianListDelegate;
import me.ledge.link.sdk.ui.presenters.custodianselector.AddCustodianListPresenter;
import me.ledge.link.sdk.ui.presenters.userdata.BaseDelegate;
import me.ledge.link.sdk.ui.views.custodianselector.AddCustodianListView;


/**
 * Wires up the MVP pattern for the screen that shows the custodians list.
 * @author Adrian
 */
public class AddCustodianListActivity
        extends MvpActivity<AddCustodianListModel, AddCustodianListView, AddCustodianListPresenter> {

    /** {@inheritDoc} */
    @Override
    protected AddCustodianListView createView() {
        return (AddCustodianListView) View.inflate(this, R.layout.act_add_custodian, null);
    }

    /** {@inheritDoc} */
    @Override
    protected AddCustodianListPresenter createPresenter(BaseDelegate delegate) {
        if(delegate instanceof AddCustodianListDelegate) {
            return new AddCustodianListPresenter(this, (AddCustodianListDelegate) delegate);
        }
        else {
            throw new NullPointerException("Received Module does not implement AddCustodianListDelegate!");
        }
    }
}