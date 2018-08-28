package com.shiftpayments.link.sdk.ui.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.shiftpayments.link.sdk.api.vos.responses.NoConnectionErrorVo;
import com.shiftpayments.link.sdk.api.vos.responses.SystemMaintenanceVo;
import com.shiftpayments.link.sdk.sdk.ShiftLinkSdk;
import com.shiftpayments.link.sdk.ui.models.ActivityModel;
import com.shiftpayments.link.sdk.ui.models.Model;
import com.shiftpayments.link.sdk.ui.presenters.BaseDelegate;
import com.shiftpayments.link.sdk.ui.presenters.Presenter;
import com.shiftpayments.link.sdk.ui.storages.UIStorage;
import com.shiftpayments.link.sdk.ui.views.ViewWithToolbar;
import com.shiftpayments.link.sdk.ui.workflow.ModuleManager;

import org.greenrobot.eventbus.Subscribe;


/**
 * Generic MVP Activity, wires up the MVP parts.
 *
 * @param <M> {@link Model} type.
 * @param <V> {@link View} type.
 * @param <P> {@link Presenter} type.
 *
 * @author Wijnand
 */
public abstract class FragmentMvpActivity<M extends ActivityModel, V extends View, P extends Presenter<M, V>>
        extends AppCompatActivity {

    protected V mView;
    protected P mPresenter;

    /**
     * @return New {@link View} instance.
     */
    protected abstract V createView();

    /**
     * @return New {@link Presenter} instance.
     */
    protected abstract P createPresenter(BaseDelegate delegate);

    /**
     * Creates the {@link View} and {@link Presenter} and wires everything.<br/>
     * Assumes that the {@link Presenter} will instantiate the {@link Model}.<br/>
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = createView();
        setContentView(mView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(UIStorage.getInstance().getStatusBarColor());
        }
        mPresenter = createPresenter(ModuleManager.getInstance().getCurrentModule());
        mPresenter.attachView(mView);
        if(mView instanceof ViewWithToolbar) {
            Toolbar toolbar = ((ViewWithToolbar) mView).getToolbar();
            toolbar.setBackgroundDrawable(new ColorDrawable(UIStorage.getInstance().getPrimaryColor()));
            toolbar.setTitleTextColor(UIStorage.getInstance().getTextTopbarColor());
        }
    }

    /**
     * Detaches the {@link View} from the {@link Presenter}. Avoids memory leaks.<br/>
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShiftLinkSdk.getResponseHandler().subscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShiftLinkSdk.getResponseHandler().unsubscribe(this);
    }

    /**
     * Called when there is no internet connection
     * @param error Error
     */
    @Subscribe
    public void handleResponse(NoConnectionErrorVo error) {
        this.startActivity(new Intent(this, NoConnectionActivity.class));
    }

    /**
     * Called when the backend is down for maintenance
     * @param error Error
     */
    @Subscribe
    public void handleResponse(SystemMaintenanceVo error) {
        this.startActivity(new Intent(this, SystemMaintenanceActivity.class));
    }
}