package com.shift.link.sdk.ui.models.financialaccountselector;

import com.shift.link.sdk.api.vos.datapoints.FinancialAccountVo;

/**
 * Information about a financial account.
 * @author Adrian
 */
public interface SelectFinancialAccountModel {

    /**
     * @return Icon resource ID.
     */
    int getIconResourceId();

    /**
     * @return Description.
     */
    String getDescription();

    /**
     * @return The financial account selected by the user.
     */
    FinancialAccountVo getFinancialAccount();

}