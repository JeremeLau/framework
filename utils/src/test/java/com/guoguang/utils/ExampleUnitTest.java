package com.guoguang.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.guoguang.utils.phonenum.PhoneNumUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
        try {
            String isPhoneValid = PhoneNumUtil.getCarrier("15251191011", "");
            System.out.println(isPhoneValid);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
    }
}