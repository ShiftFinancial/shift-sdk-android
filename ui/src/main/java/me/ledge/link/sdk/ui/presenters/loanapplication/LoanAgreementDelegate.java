package me.ledge.link.sdk.ui.presenters.loanapplication;

import me.ledge.link.sdk.ui.models.loanapplication.LoanAgreementModel;

/**
 * Delegation interface for the the loan agreement screen.
 *
 * @author Adrian
 */
public interface LoanAgreementDelegate {

    void loanAgreementShowNext(LoanAgreementModel model);
    void loanAgreementShowPrevious(LoanAgreementModel model);
}
