package me.ledge.link.sdk.ui.models.userdata;

import me.ledge.link.api.utils.CreditScoreRange;
import me.ledge.link.api.vos.DataPointList;
import me.ledge.link.api.vos.DataPointVo;
import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.models.Model;

/**
 * Concrete {@link Model} for the credit score screen.
 * @author wijnand
 */
public class CreditScoreModel extends AbstractUserDataModel {

    public static final int DEFAULT_CREDIT_INDICATION = 0;

    private DataPointVo.CreditScore mCreditScore;

    /**
     * Creates a new {@link CreditScoreModel} instance.
     */
    public CreditScoreModel() {
        super();
        init();
    }

    /**
     * Initializes this class.
     */
    private void init() {
        mCreditScore = new DataPointVo.CreditScore(DEFAULT_CREDIT_INDICATION, false);
    }

    /**
     * @param indication The credit score range to check.
     * @return Whether this is a valid credit score range.
     */
    private boolean isValidRange(int indication) {
        return indication >= CreditScoreRange.EXCELLENT && indication <= CreditScoreRange.POOR;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasAllData() {
        return isValidRange(mCreditScore.creditScoreRange);
    }

    /** {@inheritDoc} */
    @Override
    public int getActivityTitleResource() {
        return R.string.credit_score_label;
    }

    /** {@inheritDoc} */
    @Override
    public DataPointList getBaseData() {
        DataPointList base = super.getBaseData();
        DataPointVo.CreditScore baseCreditScore = (DataPointVo.CreditScore) base.getUniqueDataPoint(
                DataPointVo.DataPointType.CreditScore, new DataPointVo.CreditScore());
        baseCreditScore.creditScoreRange = getCreditScoreRange();
        return base;
    }

    /** {@inheritDoc} */
    @Override
    public void setBaseData(DataPointList base) {
        super.setBaseData(base);
        DataPointVo.CreditScore baseCreditScore = (DataPointVo.CreditScore) base.getUniqueDataPoint(
                DataPointVo.DataPointType.CreditScore, new DataPointVo.CreditScore());

        setCreditScoreRange(baseCreditScore.creditScoreRange);
    }

    /**
     * @return Credit score range.
     * @see CreditScoreRange
     */
    public int getCreditScoreRange() {
        return mCreditScore.creditScoreRange;
    }

    /**
     * Stores a new credit score range.
     * @param indication New range.
     * @see CreditScoreRange
     */
    public void setCreditScoreRange(int indication) {
        if (isValidRange(indication)) {
            mCreditScore.creditScoreRange = indication;
        } else {
            mCreditScore.creditScoreRange = DEFAULT_CREDIT_INDICATION;
        }
    }
}
