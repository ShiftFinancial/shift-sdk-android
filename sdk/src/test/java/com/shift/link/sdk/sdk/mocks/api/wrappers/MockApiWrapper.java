package com.shift.link.sdk.sdk.mocks.api.wrappers;

import com.shift.link.sdk.api.exceptions.ApiException;
import com.shift.link.sdk.api.utils.TermUnit;
import com.shift.link.sdk.api.utils.loanapplication.LoanApplicationMethod;
import com.shift.link.sdk.api.vos.Card;
import com.shift.link.sdk.api.vos.datapoints.DataPointList;
import com.shift.link.sdk.api.vos.datapoints.DataPointVo;
import com.shift.link.sdk.api.vos.datapoints.FinancialAccountVo;
import com.shift.link.sdk.api.vos.requests.base.ListRequestVo;
import com.shift.link.sdk.api.vos.requests.base.UnauthorizedRequestVo;
import com.shift.link.sdk.api.vos.requests.dashboard.CreateProjectRequestVo;
import com.shift.link.sdk.api.vos.requests.dashboard.CreateTeamRequestVo;
import com.shift.link.sdk.api.vos.requests.financialaccounts.AddBankAccountRequestVo;
import com.shift.link.sdk.api.vos.requests.financialaccounts.ApplicationAccountRequestVo;
import com.shift.link.sdk.api.vos.requests.financialaccounts.IssueVirtualCardRequestVo;
import com.shift.link.sdk.api.vos.requests.financialaccounts.UpdateFinancialAccountPinRequestVo;
import com.shift.link.sdk.api.vos.requests.financialaccounts.UpdateFinancialAccountRequestVo;
import com.shift.link.sdk.api.vos.requests.offers.InitialOffersRequestVo;
import com.shift.link.sdk.api.vos.requests.users.DeleteUserRequestVo;
import com.shift.link.sdk.api.vos.requests.users.LoginRequestVo;
import com.shift.link.sdk.api.vos.requests.verifications.StartVerificationRequestVo;
import com.shift.link.sdk.api.vos.requests.verifications.VerificationRequestVo;
import com.shift.link.sdk.api.vos.responses.config.ConfigResponseVo;
import com.shift.link.sdk.api.vos.responses.config.ContextConfigResponseVo;
import com.shift.link.sdk.api.vos.responses.config.CreditScoreListResponseVo;
import com.shift.link.sdk.api.vos.responses.config.CreditScoreVo;
import com.shift.link.sdk.api.vos.responses.config.HousingTypeListResponseVo;
import com.shift.link.sdk.api.vos.responses.config.HousingTypeVo;
import com.shift.link.sdk.api.vos.responses.config.IncomeTypeListResponseVo;
import com.shift.link.sdk.api.vos.responses.config.IncomeTypeVo;
import com.shift.link.sdk.api.vos.responses.config.LinkConfigResponseVo;
import com.shift.link.sdk.api.vos.responses.config.LoanPurposeVo;
import com.shift.link.sdk.api.vos.responses.config.LoanPurposesResponseVo;
import com.shift.link.sdk.api.vos.responses.config.RequiredDataPointVo;
import com.shift.link.sdk.api.vos.responses.config.RequiredDataPointsListResponseVo;
import com.shift.link.sdk.api.vos.responses.config.SalaryFrequenciesListResponseVo;
import com.shift.link.sdk.api.vos.responses.config.SalaryFrequencyVo;
import com.shift.link.sdk.api.vos.responses.dashboard.CreateProjectResponseVo;
import com.shift.link.sdk.api.vos.responses.dashboard.CreateTeamResponseVo;
import com.shift.link.sdk.api.vos.responses.financialaccounts.FundingSourceListVo;
import com.shift.link.sdk.api.vos.responses.financialaccounts.FundingSourceVo;
import com.shift.link.sdk.api.vos.responses.financialaccounts.TransactionListResponseVo;
import com.shift.link.sdk.api.vos.responses.financialaccounts.UpdateFinancialAccountPinResponseVo;
import com.shift.link.sdk.api.vos.responses.financialaccounts.UpdateFinancialAccountResponseVo;
import com.shift.link.sdk.api.vos.responses.loanapplication.LoanApplicationDetailsResponseVo;
import com.shift.link.sdk.api.vos.responses.loanapplication.LoanApplicationsSummaryListResponseVo;
import com.shift.link.sdk.api.vos.responses.offers.InitialOffersResponseVo;
import com.shift.link.sdk.api.vos.responses.offers.LenderVo;
import com.shift.link.sdk.api.vos.responses.offers.OfferVo;
import com.shift.link.sdk.api.vos.responses.offers.OffersListVo;
import com.shift.link.sdk.api.vos.responses.offers.TermVo;
import com.shift.link.sdk.api.vos.responses.users.CreateUserResponseVo;
import com.shift.link.sdk.api.vos.responses.users.CurrentUserResponseVo;
import com.shift.link.sdk.api.vos.responses.users.LoginUserResponseVo;
import com.shift.link.sdk.api.vos.responses.users.UserDataListResponseVo;
import com.shift.link.sdk.api.vos.responses.users.UserResponseVo;
import com.shift.link.sdk.api.vos.responses.verifications.FinishVerificationResponseVo;
import com.shift.link.sdk.api.vos.responses.verifications.StartVerificationResponseVo;
import com.shift.link.sdk.api.vos.responses.verifications.VerificationResponseVo;
import com.shift.link.sdk.api.vos.responses.verifications.VerificationStatusResponseVo;
import com.shift.link.sdk.api.wrappers.ShiftApiWrapper;

import java.util.HashMap;

/**
 * Mock implementation of the {@link ShiftApiWrapper} interface.
 * @author Wijnand
 */
public class MockApiWrapper implements ShiftApiWrapper {

    public static final String TOKEN = "bearer_token";

    public static final String LENDER_ONE_NAME = "More money";
    public static final String LENDER_ONE_LARGE_IMAGE
            = "http://cdn-media-1.lifehack.org/wp-content/files/2016/01/04125044/10-Simple-Things-You-Can-Do-To-Earn-More-Money.jpg";
    public static final long OFFER_ONE_AMOUNT = 5555;
    public static final float OFFER_ONE_INTEREST = 19.9f;
    public static final float OFFER_ONE_PAYMENT = 123.45f;
    public static final String OFFER_ONE_APPLICATION_URL = "http://www.moremoney.com/";
    public static final int EXPECTED_INCOME_TYPE = 1;
    public static final int EXPECTED_SALARY_FREQUENCY = 2;

    private String mDeveloperKey;
    private String mDevice;
    private String mBearerToken;
    private String mProjectToken;
    private String mEndPoint;
    private String mVgsEndPoint;

    @Override
    public String getDeveloperKey() {
        return mDeveloperKey;
    }

    /** {@inheritDoc} */
    @Override
    public void setBaseRequestData(String developerKey, String device, boolean isCertificatePinningEnabled, boolean trustSelfSignedCerts) {
        mDeveloperKey = developerKey;
        mDevice = device;
    }

    /** {@inheritDoc} */
    @Override
    public void setBearerToken(String token) {
        mBearerToken = token;
    }

    @Override
    public String getBearerToken() {
        return mBearerToken;
    }

    /** {@inheritDoc} */
    @Override
    public void setDeveloperKey(String developerKey) {
        mDeveloperKey = developerKey;
    }

    @Override
    public void setProjectToken(String projectToken) { mProjectToken = projectToken; }

    @Override
    public String getProjectToken() {
        return mProjectToken;
    }

    /** {@inheritDoc} */
    @Override
    public String getApiEndPoint() {
        return mEndPoint;
    }

    @Override
    public void setApiEndPoint(String endPoint, boolean isCertificatePinningEnabled, boolean trustSelfSignedCerts) {
        mEndPoint = endPoint;
    }

    @Override
    public String getVgsEndPoint() {
        return mVgsEndPoint;
    }

    @Override
    public void setVgsEndPoint(String endPoint) {
        mVgsEndPoint = endPoint;
    }

    /** {@inheritDoc} */
    public HashMap<String, String> getHTTPHeaders() {
        HashMap<String, String> response = new HashMap<>();
        return response;
    }

    /** {@inheritDoc} */
    @Override
    public LinkConfigResponseVo getLinkConfig(UnauthorizedRequestVo requestData) {
        LinkConfigResponseVo response = new LinkConfigResponseVo();
        response.loanPurposesList = new LoanPurposesResponseVo();
        response.loanPurposesList.data = new LoanPurposeVo[0];

        response.userRequiredData = new RequiredDataPointsListResponseVo();
        response.userRequiredData.data = new RequiredDataPointVo[0];

        return response;
    }

    /** {@inheritDoc} */
    @Override
    public ContextConfigResponseVo getUserConfig(UnauthorizedRequestVo requestData) {
        ConfigResponseVo response = new ConfigResponseVo();
        response.housingTypeOpts = new HousingTypeListResponseVo();
        response.housingTypeOpts.data = new HousingTypeVo[0];
        response.salaryFrequencyOpts = new SalaryFrequenciesListResponseVo();
        response.salaryFrequencyOpts.data = new SalaryFrequencyVo[1];
        SalaryFrequencyVo salaryFrequencyVo = new SalaryFrequencyVo();
        salaryFrequencyVo.salary_frequency_id = EXPECTED_SALARY_FREQUENCY;
        response.salaryFrequencyOpts.data[0] = salaryFrequencyVo;
        response.incomeTypeOpts = new IncomeTypeListResponseVo();
        response.incomeTypeOpts.data = new IncomeTypeVo[1];
        IncomeTypeVo incomeTypeVo = new IncomeTypeVo();
        incomeTypeVo.income_type_id = EXPECTED_INCOME_TYPE;
        response.incomeTypeOpts.data[0] = incomeTypeVo;
        response.creditScoreOpts = new CreditScoreListResponseVo();
        response.creditScoreOpts.data = new CreditScoreVo[0];
        ContextConfigResponseVo configResponseVo = new ContextConfigResponseVo();
        configResponseVo.projectConfiguration = response;
        return configResponseVo;
    }

    /** {@inheritDoc} */
    @Override
    public CreateUserResponseVo createUser(DataPointList requestData) throws ApiException {
        CreateUserResponseVo response = new CreateUserResponseVo();
        response.user_token = TOKEN;
        return response;
    }

    /** {@inheritDoc} */
    @Override
    public UserResponseVo updateUser(DataPointList requestData) throws ApiException {
        UserResponseVo response = new UserResponseVo();
        return response;
    }

    @Override
    public LoginUserResponseVo loginUser(LoginRequestVo loginRequestVo) throws ApiException {
        LoginUserResponseVo response = new LoginUserResponseVo();
        response.user_token = TOKEN;
        return response;
    }

    @Override
    public CurrentUserResponseVo getCurrentUser(UnauthorizedRequestVo unauthorizedRequestVo, boolean throwSessionExpiredError) throws ApiException {
        CurrentUserResponseVo response = new CurrentUserResponseVo();
        response.userData = new UserDataListResponseVo();
        response.userData.data = new DataPointVo[0];
        return response;
    }

    /** {@inheritDoc} */
    @Override
    public InitialOffersResponseVo getInitialOffers(InitialOffersRequestVo requestData) {
        OfferVo offerOne = new OfferVo();
        offerOne.id = "1";
        offerOne.loan_amount = OFFER_ONE_AMOUNT;
        offerOne.interest_rate = OFFER_ONE_INTEREST;
        offerOne.payment_amount = OFFER_ONE_PAYMENT;
        offerOne.application_method = LoanApplicationMethod.WEB;
        offerOne.application_url = OFFER_ONE_APPLICATION_URL;
        offerOne.term = new TermVo();
        offerOne.term.unit = TermUnit.WEEK;
        offerOne.term.duration = 3;
        offerOne.lender = new LenderVo();
        offerOne.lender.lender_name = LENDER_ONE_NAME;
        offerOne.lender.large_image = LENDER_ONE_LARGE_IMAGE;

        OfferVo offerTwo = new OfferVo();
        offerTwo.id = "2";

        OfferVo[] rawOffers = new OfferVo[]{ offerOne, offerTwo };

        OffersListVo offerList = new OffersListVo();
        offerList.data = rawOffers;
        offerList.page = 1;
        offerList.rows = rawOffers.length;
        offerList.total_count = rawOffers.length;
        offerList.has_more = false;

        InitialOffersResponseVo response = new InitialOffersResponseVo();
        response.offer_request_id = "69";
        response.offers = offerList;

        return response;
    }

    /** {@inheritDoc} */
    @Override
    public OffersListVo getMoreOffers(String offerRequestId, ListRequestVo requestData) throws ApiException {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public LoanApplicationDetailsResponseVo createLoanApplication(String offerId) {
        LoanApplicationDetailsResponseVo application = new LoanApplicationDetailsResponseVo();
        application.status = "";

        return application;
    }

    @Override
    public LoanApplicationsSummaryListResponseVo getPendingLoanApplicationsList(ListRequestVo listRequestVo) throws ApiException {
        return new LoanApplicationsSummaryListResponseVo();
    }

    // TODO: add unit tests for verification tasks
    @Override
    public StartVerificationResponseVo startVerification(StartVerificationRequestVo startVerificationRequestVo) throws ApiException {
        StartVerificationResponseVo response = new StartVerificationResponseVo();
        response.status = "";
        response.type = "";
        response.verification_id = "";
        return response;
    }

    @Override
    public FinishVerificationResponseVo completeVerification(VerificationRequestVo verificationRequestVo, String verificationId) throws ApiException {
        FinishVerificationResponseVo response = new FinishVerificationResponseVo();
        response.status = "";
        response.type = "";
        return response;
    }

    @Override
    public VerificationStatusResponseVo getVerificationStatus(String verificationID) throws ApiException {
        VerificationStatusResponseVo response = new VerificationStatusResponseVo();
        response.status = "";
        response.type = "";
        response.verification_id = verificationID;
        return response;
    }

    @Override
    public VerificationResponseVo restartVerification(String s) throws ApiException {
        VerificationResponseVo response = new VerificationResponseVo();
        response.status = "";
        response.type = "";
        return response;
    }

    @Override
    public VerificationStatusResponseVo addBankAccount(AddBankAccountRequestVo data) throws ApiException {
        VerificationStatusResponseVo response = new VerificationStatusResponseVo();
        response.status = "";
        response.type = "";
        response.verification_id = "1234";
        return response;
    }

    @Override
    public Card addCard(Card card) throws ApiException {
        card.mAccountId = "1234";
        return card;
    }

    @Override
    public Card issueVirtualCard(IssueVirtualCardRequestVo issueVirtualCardRequestVo) throws ApiException {
        Card response = new Card();
        response.cardNetwork = Card.CardNetwork.VISA;
        return response;
    }

    @Override
    public UserDataListResponseVo getFinancialAccounts(UnauthorizedRequestVo unauthorizedRequestVo) throws ApiException {
        UserDataListResponseVo response = new UserDataListResponseVo();
        response.data = new DataPointVo[0];
        return response;
    }

    @Override
    public FinancialAccountVo getFinancialAccount(String s) throws ApiException {
        return new FinancialAccountVo("", FinancialAccountVo.FinancialAccountType.Card, false);
    }

    @Override
    public LoanApplicationDetailsResponseVo getApplicationStatus(String s) throws ApiException {
        LoanApplicationDetailsResponseVo application = new LoanApplicationDetailsResponseVo();
        application.status = "";

        return application;
    }

    @Override
    public LoanApplicationDetailsResponseVo setApplicationAccount(ApplicationAccountRequestVo applicationAccountRequestVo, String s) throws ApiException {
        return new LoanApplicationDetailsResponseVo();
    }

    @Override
    public void deleteUser(DeleteUserRequestVo deleteUserRequestVo) throws ApiException {
    }

    @Override
    public CreateTeamResponseVo createTeam(CreateTeamRequestVo createTeamRequestVo) throws ApiException {
        CreateTeamResponseVo createTeamResponseVo = new CreateTeamResponseVo();
        createTeamResponseVo.team_id = "1234";
        return createTeamResponseVo;
    }

    @Override
    public void deleteTeam(String s) throws ApiException {

    }

    @Override
    public CreateProjectResponseVo createProject(CreateProjectRequestVo createProjectRequestVo, String s) throws ApiException {
        CreateProjectResponseVo createProjectResponseVo = new CreateProjectResponseVo();
        createProjectResponseVo.project_id = "1234";
        return createProjectResponseVo;
    }

    @Override
    public void deleteProject(String s, String s1) throws ApiException {
    }

    @Override
    public UpdateFinancialAccountResponseVo updateFinancialAccount(String accountId, UpdateFinancialAccountRequestVo requestData) throws ApiException {
        return null;
    }

    @Override
    public UpdateFinancialAccountPinResponseVo updateFinancialAccountPin(String s, UpdateFinancialAccountPinRequestVo requestData) throws ApiException {
        return null;
    }

    @Override
    public TransactionListResponseVo getFinancialAccountTransactions(String s, int i, String s1) throws ApiException {
        return null;
    }

    @Override
    public FundingSourceVo getFinancialAccountFundingSource(String accountId) throws ApiException {
        return null;
    }

    @Override
    public FundingSourceListVo getUserFundingSources(UnauthorizedRequestVo requestData) throws ApiException {
        return null;
    }
}