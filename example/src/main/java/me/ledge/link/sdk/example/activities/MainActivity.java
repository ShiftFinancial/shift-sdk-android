package me.ledge.link.sdk.example.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import me.ledge.common.utils.android.AndroidUtils;
import me.ledge.link.api.vos.responses.config.LoanPurposeVo;
import me.ledge.link.api.wrappers.LinkApiWrapper;
import me.ledge.link.api.wrappers.retrofit.two.RetrofitTwoLinkApiWrapper;
import me.ledge.link.sdk.example.R;
import me.ledge.link.sdk.example.views.MainView;
import me.ledge.link.sdk.handlers.eventbus.utils.EventBusHandlerConfigurator;
import me.ledge.link.sdk.imageloaders.volley.VolleyImageLoader;
import me.ledge.link.sdk.ui.LedgeLinkUi;
import me.ledge.link.sdk.ui.utils.HandlerConfigurator;
import me.ledge.link.sdk.ui.vos.LoanPurposeDisplayVo;
import me.ledge.link.sdk.ui.vos.UserDataVo;

/**
 * Main display.
 * @author Wijnand
 */
public class MainActivity extends AppCompatActivity implements MainView.ViewListener {

    private MainView mView;

    /**
     * @param source String source to parse to an integer.
     * @return Parsed integer.
     */
    private int parseIntSafely(String source) {
        int result;

        try {
            result = Integer.parseInt(source);
        } catch (NumberFormatException nfe) {
            result = 0;
        }

        return result;
    }

    /**
     * @return Pre-fill data for the Ledge Line SDK to use.
     */
    private UserDataVo createStartData() {
        UserDataVo data = new UserDataVo();
        boolean dataSet = false;

        if (hasValue(mView.getLoanAmount())) {
            data.loanAmount = parseIntSafely(mView.getLoanAmount());
            dataSet = true;
        }
        if (hasValue(mView.getLoanPurpose())) {
            LoanPurposeVo purpose = new LoanPurposeVo();
            purpose.loan_purpose_id = parseIntSafely(mView.getLoanPurpose());
            data.loanPurpose = new LoanPurposeDisplayVo(purpose);

            dataSet = true;
        }
        if (hasValue(mView.getFirstName())) {
            data.firstName = mView.getFirstName();
            dataSet = true;
        }
        if (hasValue(mView.getLastName())) {
            data.lastName = mView.getLastName();
            dataSet = true;
        }
        if (hasValue(mView.getEmail())) {
            data.emailAddress = mView.getEmail();
            dataSet = true;
        }
        if (hasValue(mView.getPhoneNumber())) {
            try {
                data.phoneNumber = PhoneNumberUtil.getInstance().parse(mView.getPhoneNumber(), "US");
                dataSet = true;
            } catch (NumberParseException npe) {
                // Do nothing.
            }
        }
        if (hasValue(mView.getAddress())) {
            data.address = mView.getAddress();
            dataSet = true;
        }
        if (hasValue(mView.getApartmentNumber())) {
            data.apartmentNumber = mView.getApartmentNumber();
            dataSet = true;
        }
        if (hasValue(mView.getCity())) {
            data.city = mView.getCity();
            dataSet = true;
        }
        if (hasValue(mView.getState())) {
            data.state = mView.getState();
            dataSet = true;
        }
        if (hasValue(mView.getZipCode())) {
            data.zip = mView.getZipCode();
            dataSet = true;
        }
        if (hasValue(mView.getIncome())) {
            data.income = parseIntSafely(mView.getIncome());
            dataSet = true;
        }

        if (!dataSet) {
            data = null;
        }

        return data;
    }

    /**
     * @param fieldValue Field value to check.
     * @return Whether the field has a value set.
     */
    private boolean hasValue(String fieldValue) {
        return !TextUtils.isEmpty(fieldValue);
    }

    /**
     * Sets up the Ledge Link SDK.
     */
    private void setupLedgeLink() {
        AndroidUtils utils = new AndroidUtils();
        HandlerConfigurator configurator = new EventBusHandlerConfigurator();

        LinkApiWrapper apiWrapper = new RetrofitTwoLinkApiWrapper();
        apiWrapper.setBaseRequestData(
                Long.parseLong(getString(R.string.ledge_link_developer_id)),
                utils.getDeviceSummary());

        LedgeLinkUi.setApiWrapper(apiWrapper);

        LedgeLinkUi.setImageLoader(new VolleyImageLoader(this));
        LedgeLinkUi.setResponseHandler(configurator.getResponseHandler());
        LedgeLinkUi.setProcessOrder(configurator.getProcessOrder());
    }

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLedgeLink();

        mView = (MainView) View.inflate(this, R.layout.act_main, null);
        mView.setViewListener(this);

        setContentView(mView);
    }

    /** {@inheritDoc} */
    @Override
    public void offersClickedHandler() {
        LedgeLinkUi.startProcess(this, createStartData());
    }

    /** {@inheritDoc} */
    @Override
    public void fillAllClickedHandler() {
        mView.setLoanAmount(getString(R.string.data_michael_loan_amount));
        mView.setLoanPurpose(getString(R.string.data_michael_loan_purpose));
        mView.setFirstName(getString(R.string.data_michael_first_name));
        mView.setLastName(getString(R.string.data_michael_last_name));
        mView.setEmail(getString(R.string.data_michael_email));
        mView.setPhoneNumber(getString(R.string.data_michael_phone));
        mView.setAddress(getString(R.string.data_michael_address));
        mView.setCity(getString(R.string.data_michael_city));
        mView.setState(getString(R.string.data_michael_state));
        mView.setZipCode(getString(R.string.data_michael_zip));
        mView.setIncome(getString(R.string.data_michael_income));
    }

    /** {@inheritDoc} */
    @Override
    public void clearAllClickedHandler() {
        mView.setLoanAmount("");
        mView.setFirstName("");
        mView.setLastName("");
        mView.setEmail("");
        mView.setPhoneNumber("");
        mView.setAddress("");
        mView.setApartmentNumber("");
        mView.setCity("");
        mView.setState("");
        mView.setZipCode("");
        mView.setIncome("");
    }
}
