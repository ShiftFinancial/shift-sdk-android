package com.shiftpayments.link.sdk.ui.views.card;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiftpayments.link.sdk.ui.R;

/**
 * Displays a single adjustment
 * @author Adrian
 */
public class AdjustmentView extends FrameLayout {

    private TextView mDescriptionField;
    private TextView mId;
    private TextView mExchangeRate;
    private TextView mAmount;

    /**
     * @see CardView#CardView
     * @param context See {@link CardView#CardView}.
     */
    public AdjustmentView(Context context) {
        super(context);
    }

    /**
     * @see CardView#CardView
     * @param context See {@link CardView#CardView}.
     * @param attrs See {@link CardView#CardView}.
     */
    public AdjustmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** {@inheritDoc} */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findAllViews();
    }

    /**
     * Finds all references to child Views.
     */
    private void findAllViews() {
        mDescriptionField = (TextView) findViewById(R.id.tv_transfer_description);
        mId = (TextView) findViewById(R.id.tv_transfer_id);
        mExchangeRate = (TextView) findViewById(R.id.tv_transfer_exchange_rate);
        mAmount = (TextView) findViewById(R.id.tv_transfer_amount);
    }

    public void setDescription(String description) {
        mDescriptionField.setText(description);
    }

    public void setId(String id) {
        mId.setText(id);
    }

    public void setExchangeRate(String exchangeRate) {
        mExchangeRate.setText(exchangeRate);
    }

    public void setAmount(String amount) {
        mAmount.setText(amount);
    }
}
