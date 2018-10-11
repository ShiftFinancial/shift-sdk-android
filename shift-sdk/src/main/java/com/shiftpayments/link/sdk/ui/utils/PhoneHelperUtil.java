package com.shiftpayments.link.sdk.ui.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.NATIONAL;

/**
 * Phone number helper class.
 * @author Adrian
 */
public class PhoneHelperUtil {
    private static PhoneNumberUtil mPhoneUtil = PhoneNumberUtil.getInstance();

    public static String formatPhone(Phonenumber.PhoneNumber phoneNumber) {
        return mPhoneUtil.format(phoneNumber, NATIONAL);
    }

    public static String formatPhone(Phonenumber.PhoneNumber phoneNumber, boolean addDialCode) {
        String formattedPhone = formatPhone(phoneNumber);
        if(addDialCode) {
            formattedPhone = "+" + phoneNumber.getCountryCode() + " " + formattedPhone;
        }

        return formattedPhone;
    }

    public static boolean isValidNumber(Phonenumber.PhoneNumber number) {
        return  mPhoneUtil.isValidNumber(number);
    }

    public static Phonenumber.PhoneNumber parsePhone(String phoneAsString, String countryCode) {
        try {
            return mPhoneUtil.parse(phoneAsString, countryCode);
        } catch (NumberParseException e) {
            return null;
        }
    }

}
