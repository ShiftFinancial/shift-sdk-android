package me.ledge.link.sdk.ui.tests.robolectric.tests.models.loanapplication.details;

import android.content.res.Resources;
import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.models.loanapplication.BigButtonModel;
import me.ledge.link.sdk.ui.models.loanapplication.details.UploadDocsLoanApplicationDetailsModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowContentProvider;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Tests the {@link UploadDocsLoanApplicationDetailsModel} class.
 * @author Wijnand
 */
@RunWith(RobolectricGradleTestRunner.class)
public class UploadDocsLoanApplicationDetailsModelTest {

    private Resources mResources;
    private UploadDocsLoanApplicationDetailsModel mModel;

    /**
     * Sets up each test.
     */
    @Before
    public void setUp() {
        mResources = new ShadowContentProvider().getContext().getResources();
        mModel = new UploadDocsLoanApplicationDetailsModel(null, mResources, null);
    }

    /**
     * Given an instantiated Model.<br />
     * When fetching the loan application status text.<br />
     * Then the correct text should be returned.
     */
    @Test
    public void correctStatusText() {
        Assert.assertThat("Incorrect status text.", mModel.getStatusText(), equalTo("Additional docs required"));
    }

    /**
     * Given an instantiated Model.<br />
     * When fetching the loan application status text color.<br />
     * Then the correct text color should be returned.
     */
    @Test
    public void correctStatusColor() {
        Assert.assertThat("Incorrect status color.", mModel.getStatusColor(),
                equalTo(mResources.getColor(R.color.llsdk_application_pending_borrower_action_color)));
    }

    /**
     * Given an instantiated Model.<br />
     * When fetching the button configuration.<br />
     * Then the correct button configuration should be returned.
     */
    @Test
    public void correctButton() {
        BigButtonModel buttonModel = mModel.getBigButtonModel();

        Assert.assertTrue("Button should be visible.", buttonModel.isVisible());
        Assert.assertThat("Incorrect button text.", buttonModel.getLabelResource(),
                equalTo(R.string.loan_application_button_documents_pending));
        Assert.assertThat("Incorrect button action.", buttonModel.getAction(),
                equalTo(BigButtonModel.Action.UPLOAD_DOCUMENTS));
    }
}
