package me.ledge.link.sdk.ui.views.financialaccountselector;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devmarvel.creditcardentry.library.CardValidCallback;
import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.vinaygaba.creditcardview.CreditCardView;

import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.utils.KeyboardUtil;
import me.ledge.link.sdk.ui.views.ViewWithToolbar;

/**
 * Displays the add card screen.
 * @author Adrian
 */
public class AddCardView
        extends RelativeLayout
        implements ViewWithToolbar, View.OnClickListener {

    /**
     * Callbacks this {@link View} will invoke.
     */
    public interface ViewListener {
        /**
         * Called when the scan card button has been pressed.
         */
        void scanClickHandler();

        /**
         * Called when the add card button has been pressed.
         */
        void addCardClickHandler();
    }

    private TextView mAddCardButton;
    private TextView mScanCardButton;
    private CreditCardForm mCreditCardForm;
    private CreditCardView mCreditCardView;
    private Toolbar mToolbar;
    private AddCardView.ViewListener mListener;

    /**
     * @see AddCardView#AddCardView
     * @param context See {@link AddCardView#AddCardView}.
     */
    public AddCardView(Context context) {
        this(context, null);
    }

    /**
     * @see AddCardView#AddCardView
     * @param context See {@link AddCardView#AddCardView}.
     * @param attrs See {@link AddCardView#AddCardView}.
     */
    public AddCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /** {@inheritDoc} */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findAllViews();
        setupListeners();
    }

    protected void findAllViews() {
        mAddCardButton = (TextView) findViewById(R.id.tv_add_bttn);
        mScanCardButton = (TextView) findViewById(R.id.tv_scan_bttn);
        mToolbar = (Toolbar) findViewById(R.id.tb_llsdk_toolbar);
        mCreditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);
        mCreditCardView = (CreditCardView) findViewById(R.id.credit_card_view);
        mCreditCardForm.focusCreditCard();
    }

    protected void setupListeners() {
        if (mAddCardButton != null) {
            mAddCardButton.setOnClickListener(this);
        }
        if (mScanCardButton != null) {
            mScanCardButton.setOnClickListener(this);
        }

        mCreditCardForm.setOnCardValidCallback(cardValidCallback);
    }

    CardValidCallback cardValidCallback = new CardValidCallback() {
        @Override
        public void cardValid(CreditCard creditCard) {
            mCreditCardView.setCardNumber(creditCard.getCardNumber());
            mCreditCardView.setExpiryDate(creditCard.getExpDate());
            mCreditCardView.setType(creditCard.getCardType().ordinal());
            KeyboardUtil.hideKeyboard(AddCardView.super.getContext());
        }
    };


    public void setListener(ViewListener listener) {
        mListener = listener;
    }

    /** {@inheritDoc} */
    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        int id = view.getId();
        if (id == R.id.tv_add_bttn) {
            if(mCreditCardForm.isCreditCardValid()) {
                Log.d("ADRIAN","Credit card is valid!");
                mListener.addCardClickHandler();
            }
            else {
                Log.d("ADRIAN","Credit card is invalid!");
            }
        }
        else if (id == R.id.tv_scan_bttn) {
            mListener.scanClickHandler();
        }
    }

    public void setCardName(String name) {
        mCreditCardView.setCardName(name);
    }
}
