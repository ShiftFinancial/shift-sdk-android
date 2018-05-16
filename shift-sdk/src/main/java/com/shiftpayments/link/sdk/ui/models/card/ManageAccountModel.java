package com.shiftpayments.link.sdk.ui.models.card;

import android.content.res.Resources;

import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.FundingSourceVo;
import com.shiftpayments.link.sdk.ui.models.Model;
import com.shiftpayments.link.sdk.ui.storages.CardStorage;

import java.util.ArrayList;

import me.ledge.common.utils.PagedList;

/**
 * Concrete {@link Model} for managing an account.
 * @author Adrian
 */
public class ManageAccountModel implements Model {

    private PagedList<FundingSourceModel> mFundingSources;
    private FundingSourceModel mSelectedFundingSource;

    /**
     * Creates a new {@link ManageAccountModel} instance.
     */
    public ManageAccountModel() {
        mFundingSources = new PagedList<>();
    }

    /**
     * Adds a list of funding sources.
     * @param fundingSources List of funding sources.
     */
    public void addFundingSources(Resources resources, FundingSourceVo[] fundingSources) {
        ArrayList<FundingSourceModel> newFundingSources = new ArrayList<>(fundingSources.length);
        for(FundingSourceVo fundingSource : fundingSources) {
            boolean isSelected = false;
            if(CardStorage.getInstance().hasFundingSourceId() && fundingSource.id != null) {
                isSelected = CardStorage.getInstance().getFundingSourceId().equals(fundingSource.id);
            }
            FundingSourceModel fundingSourceModel = new FundingSourceModel(fundingSource, resources, isSelected);
            newFundingSources.add(fundingSourceModel);
            if(isSelected) {
                mSelectedFundingSource = fundingSourceModel;
            }
        }

        mFundingSources.addAll(newFundingSources);
    }

    public PagedList<FundingSourceModel> getFundingSources() {
        return mFundingSources;
    }

    public String getSpendableAmount() {
        if(mSelectedFundingSource != null) {
            return mSelectedFundingSource.getSpendableAmount();
        }
        return "";
    }

    public void setSelectedFundingSource(String id) {
        ArrayList<FundingSourceModel> fundingSources = new ArrayList<>(mFundingSources.getList());
        for(FundingSourceModel fundingSource : fundingSources) {
            if(fundingSource.getFundingSourceId().equals(id)) {
                mSelectedFundingSource = fundingSource;
            }
        }
    }
}