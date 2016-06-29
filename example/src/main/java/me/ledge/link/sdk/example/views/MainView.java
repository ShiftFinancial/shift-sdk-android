package me.ledge.link.sdk.example.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import me.ledge.link.sdk.example.R;
import me.ledge.link.sdk.ui.vos.IdDescriptionPairDisplayVo;
import me.ledge.link.sdk.ui.widgets.HintArrayAdapter;

/**
 * Displays the main View.
 * @author Wijnand
 */
public class MainView extends RelativeLayout implements View.OnClickListener {

    /**
     * Callbacks this View will invoke.
     */
    public interface ViewListener {
        /**
         * Called when the "Get loan offers" button has been pressed.
         */
        void offersClickedHandler();

        /**
         * Called when the "Fill all" button has been pressed.
         */
        void fillAllClickedHandler();

        /**
         * Called when the "Clear all" button has been pressed.
         */
        void clearAllClickedHandler();
    }

    private Button mOffersButton;
    private Button mFillButton;
    private Button mClearButton;

    private EditText mLoanAmountField;
    private Spinner mLoanPurposeSpinner;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private EditText mPhoneField;
    private EditText mAddressField;
    private EditText mApartmentField;
    private EditText mCityField;
    private EditText mStateField;
    private EditText mZipField;
    private EditText mIncomeField;
    private RelativeLayout mLoadingOverlay;

    private ViewListener mListener;

    /**
     * @see ScrollView#ScrollView
     * @param context See {@link ScrollView#ScrollView}.
     */
    public MainView(Context context) {
        super(context);
    }

    /**
     * @see ScrollView#ScrollView
     * @param context See {@link ScrollView#ScrollView}.
     * @param attrs See {@link ScrollView#ScrollView}.
     */
    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** {@inheritDoc} */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findAllViews();
        setUpListeners();
    }

    /**
     * Finds all references to {@link View}s.
     */
    private void findAllViews() {
        mOffersButton = (Button) findViewById(R.id.bttn_get_offers);
        mFillButton = (Button) findViewById(R.id.bttn_fill_all);
        mClearButton = (Button) findViewById(R.id.bttn_clear_all);

        mLoanAmountField = (EditText) findViewById(R.id.et_loan_amount);
        mLoanPurposeSpinner = (Spinner) findViewById(R.id.sp_loan_purpose);
        mFirstNameField = (EditText) findViewById(R.id.et_first_name);
        mLastNameField = (EditText) findViewById(R.id.et_last_name);
        mEmailField = (EditText) findViewById(R.id.et_email);
        mPhoneField = (EditText) findViewById(R.id.et_phone);
        mAddressField = (EditText) findViewById(R.id.et_address);
        mApartmentField = (EditText) findViewById(R.id.et_apartment_number);
        mCityField = (EditText) findViewById(R.id.et_city);
        mStateField = (EditText) findViewById(R.id.et_state);
        mZipField = (EditText) findViewById(R.id.et_zip_code);
        mIncomeField = (EditText) findViewById(R.id.et_income);
        mLoadingOverlay = (RelativeLayout) findViewById(R.id.rl_loading_overlay);
    }

    /**
     * Sets up all relevant callback listeners.
     */
    private void setUpListeners() {
        mOffersButton.setOnClickListener(this);
        mFillButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.bttn_get_offers:
                mListener.offersClickedHandler();
                break;
            case R.id.bttn_fill_all:
                mListener.fillAllClickedHandler();
                break;
            case R.id.bttn_clear_all:
                mListener.clearAllClickedHandler();
                break;
            default:
                // Do nothing.
                break;
        }
    }

    /**
     * Stores a new {@link ViewListener}.
     * @param listener The new listener.
     */
    public void setViewListener(ViewListener listener) {
        mListener = listener;
    }

    /**
     * Stores a new {@link HintArrayAdapter} for the {@link Spinner} to use.
     * @param adapter New {@link HintArrayAdapter}.
     */
    public void setPurposeAdapter(HintArrayAdapter<IdDescriptionPairDisplayVo> adapter) {
        mLoanPurposeSpinner.setAdapter(adapter);
    }

    /**
     * Changes the loading overlay visibility.
     * @param show Whether the loading overlay should be shown.
     */
    public void showLoading(boolean show) {
        if (show) {
            mLoadingOverlay.setVisibility(VISIBLE);
        } else {
            mLoadingOverlay.setVisibility(GONE);
        }
    }

    /**
     * @return Loan amount.
     */
    public String getLoanAmount() {
        return mLoanAmountField.getText().toString();
    }

    /**
     * Shows a new loan amount.
     * @param amount New loan amount.
     */
    public void setLoanAmount(String amount) {
        mLoanAmountField.setText(amount);
    }

    /**
     * @return Loan purpose.
     */
    public IdDescriptionPairDisplayVo getLoanPurpose() {
        return (IdDescriptionPairDisplayVo) mLoanPurposeSpinner.getSelectedItem();
    }

    /**
     * Resets the loan purpose Spinner.
     */
    public void setLoanPurpose(int index) {
        mLoanPurposeSpinner.setSelection(index);
    }

    /**
     * @return First name.
     */
    public String getFirstName() {
        return mFirstNameField.getText().toString();
    }

    /**
     * Shows a new first name.
     * @param name New first name.
     */
    public void setFirstName(String name) {
        mFirstNameField.setText(name);
    }

    /**
     * @return Last name.
     */
    public String getLastName() {
        return mLastNameField.getText().toString();
    }

    /**
     * Shows a new last name.
     * @param name New last name.
     */
    public void setLastName(String name) {
        mLastNameField.setText(name);
    }

    /**
     * @return Email address.
     */
    public String getEmail() {
        return mEmailField.getText().toString();
    }

    /**
     * Shows a new email address.
     * @param email New email.
     */
    public void setEmail(String email) {
        mEmailField.setText(email);
    }

    /**
     * @return Phone number.
     */
    public String getPhoneNumber() {
        return mPhoneField.getText().toString();
    }

    /**
     * Shows a new phone number.
     * @param phone New phone number.
     */
    public void setPhoneNumber(String phone) {
        mPhoneField.setText(phone);
    }

    /**
     * @return Address.
     */
    public String getAddress() {
        return mAddressField.getText().toString();
    }

    /**
     * Shows a new address.
     * @param address New address.
     */
    public void setAddress(String address) {
        mAddressField.setText(address);
    }

    /**
     * @return Apartment number.
     */
    public String getApartmentNumber() {
        return mApartmentField.getText().toString();
    }

    /**
     * Shows a new apartment number.
     * @param number New apartment number.
     */
    public void setApartmentNumber(String number) {
        mApartmentField.setText(number);
    }

    /**
     * @return City.
     */
    public String getCity() {
        return mCityField.getText().toString();
    }

    /**
     * Shows a new city.
     * @param city New city.
     */
    public void setCity(String city) {
        mCityField.setText(city);
    }

    /**
     * @return State.
     */
    public String getState() {
        return mStateField.getText().toString();
    }

    /**
     * Shows a new state.
     * @param state New state.
     */
    public void setState(String state) {
        mStateField.setText(state);
    }

    /**
     * @return Zip code.
     */
    public String getZipCode() {
        return mZipField.getText().toString();
    }

    /**
     * Shows a new ZIP code.
     * @param zip New ZIP code.
     */
    public void setZipCode(String zip) {
        mZipField.setText(zip);
    }

    /**
     * @return Income.
     */
    public String getIncome() {
        return mIncomeField.getText().toString();
    }

    /**
     * Shows a new income.
     * @param income New income.
     */
    public void setIncome(String income) {
        mIncomeField.setText(income);
    }
}
