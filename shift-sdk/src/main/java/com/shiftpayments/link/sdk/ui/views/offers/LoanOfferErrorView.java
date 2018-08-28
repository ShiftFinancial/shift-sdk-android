package com.shiftpayments.link.sdk.ui.views.offers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiftpayments.link.sdk.ui.R;
import com.shiftpayments.link.sdk.ui.models.loanapplication.BigButtonModel;
import com.shiftpayments.link.sdk.ui.models.loanapplication.IntermediateLoanApplicationModel;
import com.shiftpayments.link.sdk.ui.storages.UIStorage;

/**
 * Generic loan offer error display.
 * @author Wijnand
 */
public class LoanOfferErrorView extends LinearLayout implements View.OnClickListener {

    /**
     * Callbacks that this View will invoke.
     */
    public interface ViewListener {

        /**
         * Called when the "Get offers" button has been clicked.
         */
        void offersClickHandler();

        /**
         * Called when the big button has been pressed.
         * @param action The action to take.
         */
        void bigButtonClickHandler(int action);
    }

    private ImageView mCloudImage;
    private TextView mExplanationField;
    private TextView mOffersButton;

    private LinearLayout mButtonsHolder;
    private TextView mBigButton;

    private ViewListener mListener;
    private IntermediateLoanApplicationModel mData;

    public LoanOfferErrorView(Context context) {
        super(context);
    }

    public LoanOfferErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Finds all references to child Views.
     */
    private void findAllViews() {
        mCloudImage = findViewById(R.id.iv_cloud);
        mExplanationField = findViewById(R.id.tv_explanation);
        mOffersButton = findViewById(R.id.tv_bttn_get_offers);

        mButtonsHolder = findViewById(R.id.ll_buttons_holder);
        mBigButton = findViewById(R.id.tv_bttn_big);
    }

    /**
     * Sets up all required listeners.
     */
    private void setupListeners() {
        mOffersButton.setOnClickListener(this);
        mBigButton.setOnClickListener(this);
    }

    /** {@inheritDoc} */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findAllViews();
        setupListeners();
        setColors();
    }

    private void setColors() {
        int primaryColor = UIStorage.getInstance().getPrimaryColor();
        mOffersButton.setTextColor(primaryColor);
        mBigButton.setTextColor(primaryColor);
    }

    /** {@inheritDoc} */
    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        int id = view.getId();
        if (id == R.id.tv_bttn_get_offers) {
            mListener.offersClickHandler();
        } else if (id == R.id.tv_bttn_big) {
            mListener.bigButtonClickHandler(mData.getBigButtonModel().getAction());
        }
    }

    /**
     * Stores a new callback listener that this View will invoke.
     * @param listener New callback listener.
     */
    public void setListener(ViewListener listener) {
        mListener = listener;
    }

    /**
     * Displays the latest data.
     * @param data Data to show.
     */
    public void setData(IntermediateLoanApplicationModel data) {
        mData = data;
        BigButtonModel buttonModel = mData.getBigButtonModel();

        mButtonsHolder.setVisibility(GONE);
        mBigButton.setVisibility(GONE);

        mCloudImage.setImageResource(data.getCloudImageResource());
        mExplanationField.setText(data.getExplanationText(getResources()));

        if (buttonModel.isVisible()) {
            mBigButton.setText(buttonModel.getLabelResource());
            mBigButton.setVisibility(VISIBLE);
        } else {
            mButtonsHolder.setVisibility(VISIBLE);

            if (data.showOffersButton()) {
                mOffersButton.setVisibility(VISIBLE);
            } else {
                mOffersButton.setVisibility(GONE);
            }
        }
    }
}