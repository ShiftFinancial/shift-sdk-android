package me.ledge.link.api.vos.datapoints;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class VerificationVo {
    public enum VerificationStatus{
        PENDING,
        PASSED,
        FAILED;
    }

    private VerificationStatus mVerificationStatus;
    private String mSecret;
    private String mVerificationId;
    private ArrayList<CredentialVo> mAlternateCredentials;

    public VerificationVo() {
        mVerificationId = null;
        mSecret = null;
        mVerificationStatus = null;
        mAlternateCredentials = null;
    }

    public VerificationVo(String verificationId) {
        mVerificationId = verificationId;
        mSecret = null;
        mVerificationStatus = null;
        mAlternateCredentials = null;
    }

    /**
     * Creates a new {@link VerificationVo} instance.
     */
    public VerificationVo(String verificationId, VerificationStatus status, String secret,
                          ArrayList<CredentialVo> alternateCredentials) {
        mVerificationId = verificationId;
        mSecret = secret;
        mVerificationStatus = status;
        mAlternateCredentials = alternateCredentials;
    }

    public String getVerificationId() {
        return mVerificationId;
    }

    public void setVerificationId(String mVerificationId) {
        this.mVerificationId = mVerificationId;
    }

    public String getSecret() {
        return mSecret;
    }

    public void setSecret(String secret) {
        this.mSecret = secret;
    }

    public ArrayList<CredentialVo> getAlternateEmailCredentials() {
        return getAlternateCredentialsOfType(CredentialVo.CredentialType.email);
    }
    public ArrayList<CredentialVo> getAlternatePhoneCredentials() {
        return getAlternateCredentialsOfType(CredentialVo.CredentialType.phone);
    }

    public ArrayList<CredentialVo> getAlternateCredentialsOfType(CredentialVo.CredentialType type) {
        if(mAlternateCredentials == null) {
            return null;
        }
        ArrayList<CredentialVo> credentialList = new ArrayList<CredentialVo>();
        for(CredentialVo credential : mAlternateCredentials) {
            if (credential.getCredentialType() == type.getType()) {
                credentialList.add(credential);
            }
        }
        if(!credentialList.isEmpty()) {
            return credentialList;
        }
        return null;
    }

    public void setAlternateCredentials(ArrayList<CredentialVo> alternateCredentials) {
        this.mAlternateCredentials = alternateCredentials;
    }

    public void setVerificationStatus(String verificationStatus) {
        try {
            this.mVerificationStatus = VerificationStatus.valueOf(verificationStatus.toUpperCase());
        }
        catch(IllegalArgumentException e) {
            this.mVerificationStatus = null;
        }
    }

    /**
     * @return is DataPoint Verified
     */
    public boolean isVerified() {
        return mVerificationStatus == VerificationStatus.PASSED;
    }

    public JsonObject toJSON() {
        JsonObject gsonObject = new JsonObject();;
        gsonObject.addProperty("secret", mSecret);
        gsonObject.addProperty("verification_id", String.valueOf(mVerificationId));
        return gsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationVo that = (VerificationVo) o;

        if (mVerificationStatus != that.mVerificationStatus) return false;
        if (mSecret != null ? !mSecret.equals(that.mSecret) : that.mSecret != null) return false;
        if (mVerificationId != null ? !mVerificationId.equals(that.mVerificationId) : that.mVerificationId != null)
            return false;
        return mAlternateCredentials != null ? mAlternateCredentials.equals(that.mAlternateCredentials) : that.mAlternateCredentials == null;

    }

    @Override
    public int hashCode() {
        int result = mVerificationStatus != null ? mVerificationStatus.hashCode() : 0;
        result = 31 * result + (mSecret != null ? mSecret.hashCode() : 0);
        result = 31 * result + (mVerificationId != null ? mVerificationId.hashCode() : 0);
        result = 31 * result + (mAlternateCredentials != null ? mAlternateCredentials.hashCode() : 0);
        return result;
    }
}