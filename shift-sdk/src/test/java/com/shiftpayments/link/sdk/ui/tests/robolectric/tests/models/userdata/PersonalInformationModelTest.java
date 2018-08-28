package com.shiftpayments.link.sdk.ui.tests.robolectric.tests.models.userdata;

import android.text.TextUtils;

import com.shiftpayments.link.sdk.api.vos.datapoints.DataPointList;
import com.shiftpayments.link.sdk.api.vos.datapoints.DataPointVo;
import com.shiftpayments.link.sdk.api.vos.datapoints.Email;
import com.shiftpayments.link.sdk.api.vos.datapoints.PersonalName;
import com.shiftpayments.link.sdk.ui.R;
import com.shiftpayments.link.sdk.ui.mocks.answers.textutils.IsEmptyAnswer;
import com.shiftpayments.link.sdk.ui.models.userdata.PersonalInformationModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests the {@link PersonalInformationModel} class.
 * @author Wijnand
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ TextUtils.class })
public class PersonalInformationModelTest {

    private static final String EXPECTED_FIRST_NAME = "Michael";
    private static final String EXPECTED_LAST_NAME = "Bluth";
    private static final String EXPECTED_EMAIL = "michael@bluthcompany.com";

    private PersonalInformationModel mModel;

    /**
     * Sets up each test.
     */
    @Before
    public void setUp() {
        mockStatic(TextUtils.class);
        when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new IsEmptyAnswer());

        mModel = new PersonalInformationModel();
    }

    /**
     * Given an empty Model.<br />
     * When fetching the title resource ID.<br />
     * Then the correct ID should be returned.
     */
    @Test
    public void hasCorrectTitleResource() {
        Assert.assertThat("Incorrect resource ID.",
                mModel.getActivityTitleResource(),
                equalTo(R.string.personal_info_label));
    }

    /**
     * Given an empty Model.<br />
     * When setting base data with valid first name, last name, email and phone number.<br />
     * Then the Model should contain the same values as the base data.
     */
    @Test
    public void allDataIsSetFromBaseData() {
        DataPointList base = new DataPointList();
        PersonalName baseName = new PersonalName(EXPECTED_FIRST_NAME, EXPECTED_LAST_NAME, false, false);
        Email baseEmail = new Email(EXPECTED_EMAIL, false, false);
        base.add(baseName);
        base.add(baseEmail);

        mModel.setBaseData(base);

        Assert.assertThat("Incorrect first name.", mModel.getFirstName(), equalTo(baseName.firstName));
        Assert.assertThat("Incorrect last name.", mModel.getLastName(), equalTo(baseName.lastName));
        Assert.assertThat("Incorrect email address.", mModel.getEmail(), equalTo(baseEmail.email));
        Assert.assertTrue("All data should be set.", mModel.hasValidData());
    }

    /**
     * Given a Model with all data set.<br />
     * When fetching the base data.<br />
     * Then the base data should contain the same values as the Model.
     */
    @Test
    public void baseDataIsUpdated() {
        mModel.setBaseData(new DataPointList());

        mModel.setFirstName(EXPECTED_FIRST_NAME);
        mModel.setLastName(EXPECTED_LAST_NAME);
        mModel.setEmail(EXPECTED_EMAIL);

        DataPointList base = mModel.getBaseData();
        PersonalName baseName = (PersonalName) base.getUniqueDataPoint(
                DataPointVo.DataPointType.PersonalName, new PersonalName());
        Email baseEmail = (Email) base.getUniqueDataPoint(
                DataPointVo.DataPointType.Email, new Email());

        Assert.assertThat("Incorrect first name.", baseName.firstName, equalTo(mModel.getFirstName()));
        Assert.assertThat("Incorrect last name.", baseName.lastName, equalTo(mModel.getLastName()));
        Assert.assertThat("Incorrect email address.", baseEmail.email, equalTo(mModel.getEmail()));
        Assert.assertTrue("All data should be set.", mModel.hasValidData());
    }

    /**
     * Given an empty Model.<br />
     * When trying to store a valid first name.<br />
     * Then the first name should be stored.
     */
    @Test
    public void validFirstNameIsStored() {
        mModel.setFirstName(EXPECTED_FIRST_NAME);
        Assert.assertTrue("First name should be stored.", mModel.hasFirstName());
        Assert.assertThat("Incorrect first name.", mModel.getFirstName(), equalTo(EXPECTED_FIRST_NAME));
        Assert.assertFalse("There should be missing data.", mModel.hasValidData());
    }

    /**
     * Given an empty Model.<br />
     * When trying to store a invalid first name.<br />
     * Then the first name should not be stored.
     */
    @Test
    public void invalidFirstNameIsNotStored() {
        mModel.setFirstName("");
        Assert.assertFalse("First name should NOT be stored.", mModel.hasFirstName());
    }

    /**
     * Given an empty Model.<br />
     * When trying to store a valid last name.<br />
     * Then the last name should be stored.
     */
    @Test
    public void validLastNameIsStored() {
        mModel.setLastName(EXPECTED_LAST_NAME);
        Assert.assertTrue("Last name should be stored.", mModel.hasLastName());
        Assert.assertThat("Incorrect last name.", mModel.getLastName(), equalTo(EXPECTED_LAST_NAME));
        Assert.assertFalse("There should be missing data.", mModel.hasValidData());
    }

    /**
     * Given an empty Model.<br />
     * When trying to store a invalid last name.<br />
     * Then the last name should not be stored.
     */
    @Test
    public void invalidLastNameIsNotStored() {
        mModel.setLastName("");
        Assert.assertFalse("Last name should NOT be stored.", mModel.hasLastName());
    }

    /**
     * Given an empty Model.<br />
     * When trying to store a valid email address.<br />
     * Then the email address should be stored.
     */
    @Test
    public void validEmailIsStored() {
        mModel.setEmail(EXPECTED_EMAIL);
        Assert.assertTrue("Email should be stored.", mModel.hasEmail());
        Assert.assertThat("Incorrect email.", mModel.getEmail(), equalTo(EXPECTED_EMAIL));
        Assert.assertFalse("There should be missing data.", mModel.hasValidData());
    }

    /**
     * Given an empty Model.<br />
     * When trying to store a invalid email address.<br />
     * Then the email address should not be stored.
     */
    @Test
    public void invalidEmailIsNotStored() {
        mModel.setEmail("www.michaelbluthcompany.com");
        Assert.assertFalse("Email should NOT be stored.", mModel.hasEmail());
    }

}