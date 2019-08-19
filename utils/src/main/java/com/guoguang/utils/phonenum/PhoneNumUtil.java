package com.guoguang.utils.phonenum;

import android.text.TextUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

/**
 * Author: Created by jereme on 2018/12/29
 * E-main: liuqx@guoguang.com.cn
 */
public class PhoneNumUtil {

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
    private static PhoneNumberOfflineGeocoder geoCoder = PhoneNumberOfflineGeocoder.getInstance();
    private static String REGION = "CN";
    /**
     * 根据区号判断是否是正确的电话号码
     * @param phoneNumber :带国家码的电话号码
     * return ：true 合法  false：不合法
     */
    public static boolean isPhoneNumberValid(String phoneNumber, String region) throws NumberParseException {
        if (TextUtils.isEmpty(region)) {
            region = REGION;
        }
        Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, region);
        return phoneNumberUtil.isValidNumber(numberProto);
    }

    /**
     * 根据国家代码和手机号  判断手机运营商
     * @param phoneNumber   phoneNumber
     * @return
     */
    public static String getCarrier(String phoneNumber, String region) throws NumberParseException {
        if (TextUtils.isEmpty(region)) {
            region = REGION;
        }
        Phonenumber.PhoneNumber referencePhoneNumber = phoneNumberUtil.parse(phoneNumber, region);
        return carrierMapper.getNameForNumber(referencePhoneNumber, Locale.CHINA);
    }

    /**
     *
     * @Description: 根据国家代码和手机号  手机归属地
     * @param @param phoneNumber
     * @param @return    参数
     * @throws
     */
    public static String getGeo(String phoneNumber, String region) throws NumberParseException {
        if (TextUtils.isEmpty(region)) {
            region = REGION;
        }
        Phonenumber.PhoneNumber referencePhoneNumber = phoneNumberUtil.parse(phoneNumber, region);
        //手机号码归属城市 referenceRegion
        return geoCoder.getDescriptionForNumber(referencePhoneNumber, Locale.CHINA);
    }
}
