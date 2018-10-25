package com.shiftpayments.link.sdk.api.vos;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.shiftpayments.link.sdk.api.vos.datapoints.FinancialAccountVo;
import com.shiftpayments.link.sdk.api.vos.requests.financialaccounts.KycStatus;
import com.shiftpayments.link.sdk.api.vos.responses.financialaccounts.MoneyVo;
import com.shiftpayments.link.sdk.api.vos.responses.card.Features;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.shiftpayments.link.sdk.api.vos.Card.FinancialAccountState.ACTIVE;
import static com.shiftpayments.link.sdk.api.vos.Card.FinancialAccountState.CREATED;

/**
 * Created by adrian on 18/01/2017.
 */

public class Card extends FinancialAccountVo {
    public enum CardNetwork {
        VISA,
        MASTERCARD,
        AMEX,
        UNKNOWN
    }

    public enum FinancialAccountState {
        ACTIVE,
        INACTIVE,
        CANCELLED,
        CREATED,
        UNKNOWN
    }

    @SerializedName("card_network")
    public CardNetwork cardNetwork;
    @SerializedName("last_four")
    public String lastFourDigits;
    @SerializedName("card_brand")
    public String cardBrand;
    @SerializedName("card_issuer")
    public String cardIssuer;
    @SerializedName("pan")
    public String PANToken;
    @SerializedName("cvv")
    public String CVVToken;
    @SerializedName("expiration")
    public String expirationDate;
    @SerializedName("state")
    public FinancialAccountState state;
    @SerializedName("kyc_status")
    public KycStatus kycStatus;
    @SerializedName("kyc_reason")
    public String[] kycReason;
    @SerializedName("physical_card_activation_required")
    public Boolean physicalCardActivationRequired;
    @SerializedName("spendable_today")
    public MoneyVo spendableAmount;
    @SerializedName("native_spendable_today")
    public MoneyVo nativeSpendableAmount;
    @SerializedName("features")
    public Features features;

    public Card() {
        super(null, FinancialAccountType.Card, false);
        cardNetwork = null;
        lastFourDigits = null;
        cardBrand = null;
        cardIssuer = null;
        PANToken = null;
        CVVToken = null;
        expirationDate = null;
        state = null;
        kycStatus = null;
        kycReason = null;
        spendableAmount = null;
        nativeSpendableAmount = null;
        physicalCardActivationRequired = null;
        features = null;
    }

    public Card(String accountId, String lastFourDigits, CardNetwork type, String cardBrand, String cardIssuer, String expirationDate,
                String PANToken, String CVVToken, FinancialAccountState state, boolean verified) {
        super(accountId, FinancialAccountType.Card, verified);
        this.cardNetwork = type;
        this.lastFourDigits = lastFourDigits;
        this.cardBrand = cardBrand;
        this.cardIssuer = cardIssuer;
        this.PANToken = PANToken;
        this.CVVToken = CVVToken;
        this.expirationDate = expirationDate;
        this.state = state;
        this.kycStatus = null;
        this.kycReason = null;
    }

    public Card(String accountId, String lastFourDigits, CardNetwork type, String cardBrand, String cardIssuer, String expirationDate,
                String PANToken, String CVVToken, FinancialAccountState state, KycStatus kycStatus, String[] kycReason, 
                MoneyVo spendableAmount, MoneyVo nativeSpendableAmount, boolean physicalCardActivationRequired, Features features, boolean verified) {
        super(accountId, FinancialAccountType.Card, verified);
        this.cardNetwork = type;
        this.lastFourDigits = lastFourDigits;
        this.cardBrand = cardBrand;
        this.cardIssuer = cardIssuer;
        this.PANToken = PANToken;
        this.CVVToken = CVVToken;
        this.expirationDate = expirationDate;
        this.state = state;
        this.kycStatus = kycStatus;
        this.kycReason = kycReason;
        this.spendableAmount = spendableAmount;
        this.nativeSpendableAmount = nativeSpendableAmount;
        this.physicalCardActivationRequired = physicalCardActivationRequired;
        this.features = features;
    }

    public Card(Card c) {
        super(c.mAccountId, FinancialAccountType.Card, c.isVerified());
        this.cardNetwork = c.cardNetwork;
        this.lastFourDigits = c.lastFourDigits;
        this.cardBrand = c.cardBrand;
        this.cardIssuer = c.cardIssuer;
        this.PANToken = c.PANToken;
        this.CVVToken = c.CVVToken;
        this.expirationDate = c.expirationDate;
        this.state = c.state;
        this.kycStatus = c.kycStatus;
        this.kycReason = c.kycReason;
        this.spendableAmount = c.spendableAmount;
        this.nativeSpendableAmount = c.nativeSpendableAmount;
        this.physicalCardActivationRequired = c.physicalCardActivationRequired;
        this.features = c.features;
    }

    @Override
    public JsonObject toJSON() {
        JsonObject gsonObject = super.toJSON();
        gsonObject.addProperty("type", "card");
        gsonObject.addProperty("card_network", cardNetwork != null ? cardNetwork.name() : "");
        gsonObject.addProperty("card_brand", cardBrand);
        gsonObject.addProperty("card_issuer", cardIssuer);
        gsonObject.addProperty("state", state != null ? state.name() : "");
        gsonObject.addProperty("pan", PANToken);
        gsonObject.addProperty("cvv", CVVToken);
        gsonObject.addProperty("last_four", lastFourDigits);
        gsonObject.addProperty("expiration", getAPIFormatExpirationDate(expirationDate));
        return gsonObject;
    }

    public boolean isCardActivated() {
        return ((state != null) && (state == ACTIVE));
    }

    public boolean isCardCreated() {
        return ((state != null) && (state == CREATED));
    }

    private String getAPIFormatExpirationDate(String expirationDate) {
        final String CARD_EXPIRATION_FORMAT = "MM/yy";
        final String API_FORMAT = "yyyy-MM";

        String formattedExpirationDate;
        final SimpleDateFormat dateFormat = new SimpleDateFormat(CARD_EXPIRATION_FORMAT, java.util.Locale.getDefault());
        try {
            Date date = dateFormat.parse(expirationDate);
            dateFormat.applyPattern(API_FORMAT);
            formattedExpirationDate = dateFormat.format(date);
        } catch (ParseException e) {
            formattedExpirationDate = null;
        }
        return formattedExpirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        Card card = (Card) o;

        if (cardNetwork != card.cardNetwork) return false;
        if (lastFourDigits != null ? !lastFourDigits.equals(card.lastFourDigits) : card.lastFourDigits != null)
            return false;
        if (PANToken != null ? !PANToken.equals(card.PANToken) : card.PANToken != null)
            return false;
        if (CVVToken != null ? !CVVToken.equals(card.CVVToken) : card.CVVToken != null)
            return false;
        if (cardNetwork != null ? !cardNetwork.equals(card.cardNetwork) : card.cardNetwork != null)
            return false;
        if (cardBrand != null ? !cardBrand.equals(card.cardBrand) : card.cardBrand != null)
            return false;
        if (cardIssuer != null ? !cardIssuer.equals(card.cardIssuer) : card.cardIssuer != null)
            return false;
        if (state != null ? !state.equals(card.state) : card.state != null)
            return false;
        return expirationDate != null ? expirationDate.equals(card.expirationDate) : card.expirationDate == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode()+(cardNetwork != null ? cardNetwork.hashCode() : 0);
        result = 31 * result + (lastFourDigits != null ? lastFourDigits.hashCode() : 0);
        result = 31 * result + (PANToken != null ? PANToken.hashCode() : 0);
        result = 31 * result + (CVVToken != null ? CVVToken.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (cardNetwork != null ? cardNetwork.hashCode() : 0);
        result = 31 * result + (cardBrand != null ? cardBrand.hashCode() : 0);
        result = 31 * result + (cardIssuer != null ? cardIssuer.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
