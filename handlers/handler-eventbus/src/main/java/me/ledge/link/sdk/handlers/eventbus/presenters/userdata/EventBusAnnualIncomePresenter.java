package me.ledge.link.sdk.handlers.eventbus.presenters.userdata;

import android.support.v7.app.AppCompatActivity;

import me.ledge.link.api.vos.responses.config.ConfigResponseVo;
import me.ledge.link.api.vos.responses.config.EmploymentStatusListResponseVo;
import me.ledge.link.api.vos.responses.config.SalaryFrequenciesListResponseVo;
import me.ledge.link.sdk.ui.presenters.userdata.AnnualIncomePresenter;
import me.ledge.link.sdk.ui.views.userdata.AnnualIncomeView;
import org.greenrobot.eventbus.Subscribe;

/**
 * An {@link AnnualIncomePresenter} that uses the {@link EventBus} to receive API responses.
 * @author Wijnand
 */
public class EventBusAnnualIncomePresenter extends AnnualIncomePresenter {

    /**
     * Creates a new {@link AnnualIncomePresenter} instance.
     * @param activity Activity.
     */
    public EventBusAnnualIncomePresenter(AppCompatActivity activity) {
        super(activity);
    }

    /** {@inheritDoc} */
    @Override
    public void attachView(AnnualIncomeView view) {
        super.attachView(view);
        mResponseHandler.subscribe(this);
    }

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        mResponseHandler.unsubscribe(this);
        super.detachView();
    }

    /**
     * Called when the employment statuses list API response has been received.
     * @param response API response.
     */
    @Subscribe
    public void handleEmploymentStatusesList(ConfigResponseVo response) {
        if (isEmploymentStatusesPresent(response)) {
            setEmploymentStatusesList(response.employmentStatusOpts.data);
        }
    }

    /**
     * Called when the salary frequencies list API response has been received.
     * @param response API response.
     */
    @Subscribe
    public void handleSalaryFrequenciesList(ConfigResponseVo response) {
        if (isSalaryFrequencyPresent(response)) {
            setSalaryFrequenciesList(response.salaryFrequencyOpts.data);
        }
    }

    private boolean isEmploymentStatusesPresent(ConfigResponseVo response) {
        return response!=null && response.employmentStatusOpts!=null;
    }

    private boolean isSalaryFrequencyPresent(ConfigResponseVo response) {
        return response!=null && response.salaryFrequencyOpts!=null;
    }
}
