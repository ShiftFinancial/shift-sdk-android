package me.ledge.link.sdk.ui.models.loanapplication;

import android.content.res.Resources;

import java.util.LinkedList;

import me.ledge.link.api.vos.datapoints.DataPointList;
import me.ledge.link.api.vos.responses.config.RequiredDataPointVo;
import me.ledge.link.api.vos.responses.offers.OfferVo;
import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.images.GenericImageLoader;
import me.ledge.link.sdk.ui.models.ActivityModel;
import me.ledge.link.sdk.ui.models.Model;
import me.ledge.link.sdk.ui.storages.LinkStorage;
import me.ledge.link.sdk.ui.storages.UserStorage;
import me.ledge.link.sdk.ui.vos.LoanDataVo;

/**
 * Loan summary {@link Model}.
 * @author Adrian
 */
public class LoanApplicationSummaryModel extends LoanAgreementModel implements ActivityModel, Model {

    private LinkedList<RequiredDataPointVo> mRequiredDataPointsList;

    /**
     * Creates a new {@link LoanApplicationSummaryModel} instance.
     * @param offer The selected loan offer.
     * @param imageLoader Image loader.
     */
    public LoanApplicationSummaryModel(OfferVo offer, GenericImageLoader imageLoader) {
        super(offer, imageLoader);

    }

    /** {@inheritDoc} */
    @Override
    public int getActivityTitleResource() {
        return R.string.loan_confirmation_label;
    }

    public void setRequiredData(LinkedList<RequiredDataPointVo> requiredDataPointsList) {
        mRequiredDataPointsList = requiredDataPointsList;
    }

    public LinkedList<RequiredDataPointVo> getRequiredData() {
        return mRequiredDataPointsList;
    }

    public DataPointList getApplicationInfo() {
        return UserStorage.getInstance().getUserData();
    }

    public String getLoanPurpose() {
        LoanDataVo loanData = LinkStorage.getInstance().getLoanData();
        return loanData.loanPurpose.toString();

    }

    public String getLoanPurposeLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_loan_purpose);
    }

    public String getFirstNameLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_first_name);
    }

    public String getLastNameLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_last_name);
    }

    public String getPhoneLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_phone);
    }

    public String getEmailLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_email);
    }

    public String getBirthdayLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_birthday);
    }

    public String getSsnLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_ssn);
    }

    public String getAddressLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_address);
    }

    public String getAptUnitLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_apt_unit);
    }

    public String getZipCodeLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_zip_code);
    }

    public String getCityLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_city);
    }

    public String getStateLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_state);
    }

    public String getHousingStatusLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_housing_status);
    }

    public String getEmploymentStatusLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_employment_status);
    }

    public String getSalaryFrequencyLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_salary_frequency);
    }

    public String getAnnualIncomeLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_annual_pretax_income);
    }

    public String getMonthlyIncomeLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_monthly_net_income);
    }

    public String getCreditScoreLabel(Resources resources) {
        return resources.getString(R.string.loan_application_summary_credit_score);
    }
}