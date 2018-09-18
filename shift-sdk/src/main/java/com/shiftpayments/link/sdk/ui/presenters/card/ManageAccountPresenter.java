package com.shiftpayments.link.sdk.ui.presenters.card;

import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.shiftpayments.link.sdk.ui.R;
import com.shiftpayments.link.sdk.ui.activities.card.ManageAccountActivity;
import com.shiftpayments.link.sdk.ui.models.card.ManageAccountModel;
import com.shiftpayments.link.sdk.ui.presenters.BasePresenter;
import com.shiftpayments.link.sdk.ui.presenters.Presenter;
import com.shiftpayments.link.sdk.ui.storages.UIStorage;
import com.shiftpayments.link.sdk.ui.utils.SendEmailUtil;
import com.shiftpayments.link.sdk.ui.views.card.ManageAccountView;

/**
 * Concrete {@link Presenter} for the manage account screen.
 * @author Adrian
 */
public class ManageAccountPresenter
        extends BasePresenter<ManageAccountModel, ManageAccountView>
        implements Presenter<ManageAccountModel, ManageAccountView>, ManageAccountView.ViewListener {

    private ManageAccountActivity mActivity;
    private ManageAccountDelegate mDelegate;

    public ManageAccountPresenter(ManageAccountActivity activity, ManageAccountDelegate delegate) {
        mActivity = activity;
        mDelegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(ManageAccountView view) {
        super.attachView(view);
        view.setViewListener(this);
        mActivity.setSupportActionBar(mView.getToolbar());
        mActivity.getSupportActionBar().setTitle(mActivity.getResources().getString(R.string.account_management_title));
    }

    @Override
    public ManageAccountModel createModel() {
        return new ManageAccountModel();
    }

    @Override
    public void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        String alertTitle = mActivity.getString(R.string.sign_out);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(UIStorage.getInstance().getTextPrimaryColor());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(alertTitle);
        spannableStringBuilder.setSpan(
                foregroundColorSpan,
                0,
                alertTitle.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        builder.setTitle(spannableStringBuilder);

        String alertMessage = mActivity.getString(R.string.account_management_dialog_message);
        foregroundColorSpan = new ForegroundColorSpan(UIStorage.getInstance().getTextSecondaryColor());
        spannableStringBuilder = new SpannableStringBuilder(alertMessage);
        spannableStringBuilder.setSpan(
                foregroundColorSpan,
                0,
                alertMessage.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        builder.setMessage(spannableStringBuilder);

        builder.setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
        builder.setPositiveButton("YES", (dialog, id) -> {
            mResponseHandler.unsubscribe(this);
            mActivity.finish();
            mDelegate.onSignOut();
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(UIStorage.getInstance().getTextPrimaryColor());
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UIStorage.getInstance().getUiPrimaryColor());
        });
        dialog.show();
    }

    @Override
    public void contactSupport() {
        new SendEmailUtil(UIStorage.getInstance().getContextConfig().supportEmailAddress).execute(mActivity);
    }

    @Override
    public void onBack() {
        mActivity.onBackPressed();
    }
}
