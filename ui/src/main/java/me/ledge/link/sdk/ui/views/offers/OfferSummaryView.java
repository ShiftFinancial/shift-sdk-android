package me.ledge.link.sdk.ui.views.offers;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import me.ledge.common.views.RowView;
import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.models.offers.OfferSummaryModel;
import me.ledge.link.sdk.ui.storages.UIStorage;

/**
 * Displays an offer summary.
 * @author wijnand
 */
public class OfferSummaryView extends CardView implements RowView<OfferSummaryModel>, View.OnClickListener {

    /**
     * Callbacks that this View will invoke.
     */
    public interface ViewListener {
        /**
         * Called when the "apply now" button has been clicked.
         * @param offer The associated loan offer.
         */
        void applyClickHandler(OfferSummaryModel offer);

        void moreInfoClickHandler(OfferSummaryModel offer);
    }

    private TextView mLenderNameField;
    private ImageView mLenderLogo;

    private TextView mInterestField;
    private TextView mAmountField;
    private TextView mMonthlyPaymentField;
    private TextView mMoreInfoButton;
    private TextView mApplyButton;

    private ViewListener mListener;
    private OfferSummaryModel mData;

    /**
     * @see CardView#CardView
     * @param context See {@link CardView#CardView}.
     */
    public OfferSummaryView(Context context) {
        super(context);
    }

    /**
     * @see CardView#CardView
     * @param context See {@link CardView#CardView}.
     * @param attrs See {@link CardView#CardView}.
     */
    public OfferSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @see CardView#CardView
     * @param context See {@link CardView#CardView}.
     * @param attrs See {@link CardView#CardView}.
     * @param defStyleAttr See {@link CardView#CardView}.
     */
    public OfferSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Locates all child Views.
     */
    private void findAllViews() {
        mLenderNameField = (TextView) findViewById(R.id.tv_lender);
        mLenderLogo = (ImageView) findViewById(R.id.iv_lender_logo);

        mInterestField = (TextView) findViewById(R.id.tv_interest);
        mAmountField = (TextView) findViewById(R.id.tv_amount);
        mMonthlyPaymentField = (TextView) findViewById(R.id.tv_monthly);
        mMoreInfoButton = (TextView) findViewById(R.id.tv_bttn_more_info);
        mApplyButton = (TextView) findViewById(R.id.tv_apply);

    }

    /**
     * Sets up child View callback listeners.
     */
    private void setUpListeners() {
        mApplyButton.setOnClickListener(this);
        mMoreInfoButton.setOnClickListener(this);
    }

    /**
     * Updates all fields with the offer data OR empty Strings when no data is set.
     */
    private void updateAllFields() {
        mMoreInfoButton.setVisibility(GONE);

        if (mData == null) {
            mLenderNameField.setText("");
            mLenderLogo.setImageResource(android.R.color.transparent);
            mInterestField.setText("");
            mAmountField.setText("");
            mMonthlyPaymentField.setText("");
        } else {
            mLenderNameField.setText(mData.getLenderName());
            mInterestField.setText(mData.getInterestText());
            mAmountField.setText(mData.getAmountText());
            mMonthlyPaymentField.setText(mData.getMonthlyPaymentText());

            if (mData.hasImageLoader() && mData.getLenderImage() != null) {
                mLenderLogo.setVisibility(VISIBLE);
                mData.getImageLoader().load(mData.getLenderImage(), mLenderLogo);
            } else {
                mLenderLogo.setVisibility(GONE);
            }

            if (mData.hasDisclaimer()) {
                mMoreInfoButton.setVisibility(VISIBLE);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findAllViews();
        setUpListeners();
        setColors();
    }

    private void setColors() {
        int color = UIStorage.getInstance().getPrimaryColor();
        mApplyButton.setTextColor(color);
    }

    /** {@inheritDoc} */
    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        int id = view.getId();
        if (id == R.id.tv_apply) {
            mListener.applyClickHandler(mData);
        } else if (id == R.id.tv_bttn_more_info) {
            mListener.moreInfoClickHandler(mData);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setData(OfferSummaryModel data) {
        mData = data;
        updateAllFields();
    }

    /** {@inheritDoc} */
    @Override
    public void reset() {
        if (mData != null && mData.hasImageLoader() && mData.getLenderImage() != null) {
            mData.getImageLoader().cancel(mData.getLenderImage(), mLenderLogo);
        }

        mData = null;
        updateAllFields();
    }

    /**
     * Stores a new callback listener that this View will invoke.
     * @param listener New callback listener.
     */
    public void setListener(ViewListener listener) {
        mListener = listener;
    }
}
