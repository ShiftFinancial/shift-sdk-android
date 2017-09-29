package me.ledge.link.sdk.ui.models.userdata;

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import me.ledge.link.api.vos.datapoints.DataPointList;
import me.ledge.link.api.vos.datapoints.DataPointVo;
import me.ledge.link.api.vos.datapoints.PhoneNumberVo;
import me.ledge.link.sdk.ui.R;
import me.ledge.link.sdk.ui.models.Model;
import me.ledge.link.sdk.ui.utils.PhoneHelperUtil;


/**
 * Concrete {@link Model} for the phone screen.
 * @author Adrian
 */

public class PhoneModel extends AbstractUserDataModel implements UserDataModel {

    private PhoneNumberVo mPhone;

    /**
     * Creates a new {@link PhoneModel} instance.
     */

    public PhoneModel() {
        init();
    }

    /**
     * Initializes this class.
     */
    protected void init() {
        mPhone = new PhoneNumberVo();
    }

    /** {@inheritDoc} */
    @Override
    public int getActivityTitleResource() {
        return R.string.phone_title;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasAllData() {
        return hasPhone();
    }

    /** {@inheritDoc} */
    @Override
    public DataPointList getBaseData() {
        DataPointList base = super.getBaseData();

        if(hasPhone()) {
            PhoneNumberVo phoneNumber = (PhoneNumberVo) base.getUniqueDataPoint(
                    DataPointVo.DataPointType.Phone, new PhoneNumberVo());
            phoneNumber.phoneNumber = mPhone.phoneNumber;
        }

        return base;
    }

    /** {@inheritDoc} */
    @Override
    public void setBaseData(DataPointList base) {
        super.setBaseData(base);

        PhoneNumberVo phoneNumber = (PhoneNumberVo) base.getUniqueDataPoint(
                DataPointVo.DataPointType.Phone, null);
        if(phoneNumber!=null) {
            setPhone(phoneNumber);
        }
    }

    /**
     * @return Phone number.
     */
    public PhoneNumber getPhone() {
        return mPhone.phoneNumber;
    }

    public void setPhone(PhoneNumberVo phoneNumber) {
        if(phoneNumber != null) {
            setPhone(phoneNumber.phoneNumber);
        }
    }

    /**
     * Parses and stores a valid phone number.
     * @param phone Raw phone number.
     */
    public void setPhone(String phone) {
        PhoneNumber number = PhoneHelperUtil.parsePhone(phone);
        setPhone(number);
    }

    /**
     * Stores a valid phone number.
     * @param number Phone number object.
     */
    public void setPhone(PhoneNumber number) {
        if (number != null && PhoneHelperUtil.isValidNumber(number)) {
            mPhone.phoneNumber = number;
        } else {
            mPhone.phoneNumber = null;
        }
    }

    /**
     * @return Whether a phone number has been set.
     */
    public boolean hasPhone() {
        return mPhone.phoneNumber != null;
    }
}
