package me.ledge.link.sdk.ui.models.userdata;

import me.ledge.link.api.vos.datapoints.PaydayLoan;
import me.ledge.link.api.vos.datapoints.DataPointList;
import me.ledge.link.api.vos.datapoints.DataPointVo;
import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.models.Model;

/**
 * Concrete {@link Model} for the payday loan screen.
 * @author Adrian
 */
public class PaydayLoanModel extends AbstractUserDataModel {

    private PaydayLoan mPaydayLoan;

    public PaydayLoanModel() {
        super();
        init();
    }

    private void init() {
        mPaydayLoan = new PaydayLoan();
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasAllData() {
        return mPaydayLoan.hasUsedPaydayLoan!=null;
    }

    /** {@inheritDoc} */
    @Override
    public int getActivityTitleResource() {
        return R.string.payday_loan_label;
    }

    /** {@inheritDoc} */
    @Override
    public DataPointList getBaseData() {
        DataPointList base = super.getBaseData();
        PaydayLoan basePaydayLoan = (PaydayLoan) base.getUniqueDataPoint(
                DataPointVo.DataPointType.PayDayLoan, new PaydayLoan());
        basePaydayLoan.hasUsedPaydayLoan = this.hasUsedPaydayLoan();
        return base;
    }

    /** {@inheritDoc} */
    @Override
    public void setBaseData(DataPointList base) {
        super.setBaseData(base);
        PaydayLoan basePaydayLoan = (PaydayLoan) base.getUniqueDataPoint(
                DataPointVo.DataPointType.PayDayLoan, null);
        if(basePaydayLoan!=null) {
            setPaydayLoan(basePaydayLoan.hasUsedPaydayLoan);
        }
    }

    public Boolean hasUsedPaydayLoan() {
        return mPaydayLoan.hasUsedPaydayLoan;
    }

    public void setPaydayLoan(Boolean hasUsedPaydayLoan) {
        mPaydayLoan.hasUsedPaydayLoan = hasUsedPaydayLoan;
    }
}