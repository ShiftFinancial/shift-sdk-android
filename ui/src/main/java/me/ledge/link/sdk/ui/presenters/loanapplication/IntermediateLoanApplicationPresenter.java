package me.ledge.link.sdk.ui.presenters.loanapplication;

import android.support.v7.app.AppCompatActivity;
import me.ledge.link.api.utils.LoanApplicationStatus;
import me.ledge.link.api.vos.responses.loanapplication.LoanApplicationDetailsResponseVo;
import me.ledge.link.sdk.ui.models.loanapplication.IntermediateLoanApplicationModel;
import me.ledge.link.sdk.ui.models.loanapplication.PendingLenderActionModel;
import me.ledge.link.sdk.ui.models.loanapplication.RejectedLoanApplicationModel;
import me.ledge.link.sdk.ui.presenters.ActivityPresenter;
import me.ledge.link.sdk.ui.presenters.Presenter;
import me.ledge.link.sdk.ui.storages.LoanStorage;
import me.ledge.link.sdk.ui.views.loanapplication.IntermediateLoanApplicationView;

/**
 * Concrete {@link Presenter} for a loan application in an intermediate state.
 * @author Wijnand
 */
public class IntermediateLoanApplicationPresenter
        extends ActivityPresenter<IntermediateLoanApplicationModel, IntermediateLoanApplicationView>
        implements Presenter<IntermediateLoanApplicationModel, IntermediateLoanApplicationView> {

    /**
     * Creates a new {@link IntermediateLoanApplicationPresenter} instance.
     * @param activity Activity.
     */
    public IntermediateLoanApplicationPresenter(AppCompatActivity activity) {
        super(activity);
    }

    /** {@inheritDoc} */
    @Override
    public IntermediateLoanApplicationModel createModel() {
        LoanApplicationDetailsResponseVo loanApplication = LoanStorage.getInstance().getCurrentLoanApplication();
        IntermediateLoanApplicationModel model = new RejectedLoanApplicationModel(loanApplication);

        if (loanApplication != null && loanApplication.status == LoanApplicationStatus.PENDING_LENDER_ACTION) {
            model = new PendingLenderActionModel(loanApplication);
        }

        return model;
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(IntermediateLoanApplicationView view) {
        super.attachView(view);
        view.setData(mModel);
    }

    /** {@inheritDoc} */
    @Override
    public void startPreviousActivity() {
        LoanStorage.getInstance().setCurrentLoanApplication(null);
        super.startPreviousActivity();
    }
}