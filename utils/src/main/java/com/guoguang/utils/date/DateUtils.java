package com.guoguang.utils.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author: Created by jereme on 2018/7/23
 * E-main: liuqx@guoguang.com.cn
 */
public class DateUtils {

    //  星期
    private static String[] week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    //  农历月份
    private static String[] lunarMonth = {"正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月", "腊月"};
    //  农历日
    private static String[] lunarDay = {"初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};
    //  农历年
    /*01 甲子 11 甲戌 21 甲申 31 甲午 41 甲辰 51 甲寅
02 乙丑 12 乙亥 22 乙酉 32 乙未 42 乙巳 52 乙卯
03 丙寅 13 丙子 23 丙戌 33 丙申 43 丙午 53 丙辰
04 丁卯 14 丁丑 24 丁亥 34 丁酉 44 丁未 54 丁巳
05 戊辰 15 戊寅 25 戊子 35 戊戌 45 戊申 55 戊午
06 己巳 16 己卯 26 己丑 36 己亥 46 己酉 56 己未
07 庚午 17 庚辰 27 庚寅 37 庚子 47 庚戌 57 庚申
08 辛未 18 辛巳 28 辛卯 38 辛丑 48 辛亥 58 辛酉
09 壬申 19 壬午 29 壬辰 39 壬寅 49 壬子 59 壬戌
10 癸酉 20 癸未 30 癸巳 40 癸卯 50 癸丑 60 癸亥*/
    private static String[] lunarYear = {"甲子年", "乙丑年", "丙寅年", "丁卯年", "戊辰年", "己巳年", "庚午年", "辛未年", "壬申年", "癸酉年",
            "甲戌年", "乙亥年", "丙子年", "丁丑年", "戊寅年", "己卯年", "庚辰年", "辛巳年", "壬午年", "癸未年",
            "甲申年", "乙酉年", "丙戌年", "丁亥年", "戊子年", "己丑年", "庚寅年", "辛卯年", "壬辰年", "癸巳年",
            "甲午年", "乙未年", "丙申年", "丁酉年", "戊戌年", "己亥年", "庚子年", "辛丑年", "壬寅年", "癸卯年",
            "甲辰年", "乙巳年", "丙午年", "丁未年", "戊申年", "己酉年", "庚戌年", "辛亥年", "壬子年", "癸丑年",
            "甲寅年", "乙卯年", "丙辰年", "丁巳年", "戊午年", "己未年", "庚申年", "辛酉年", "壬戌年", "癸亥年"
    };

    /**
     * 获得当天time点时间戳
     */
    public static long getSignTime(int time) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, time);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() / 1000);
    }

    /**
     * 时间段格式化 hh:mm:ss 用来做倒计时
     */
    public static String timeFormat(long time) {
        int hours = (int) time / 3600;
        String hourStr;
        if (hours < 10) {
            hourStr = "0" + hours;

        } else {
            hourStr = hours + "";
        }
        int min = (int) (time - hours * 3600) / 60;
        String minStr;
        if (min < 10) {
            minStr = "0" + min;

        } else {
            minStr = min + "";
        }
        int second = (int) (time - (time / 60) * 60);
        String secondStr;
        if (second < 10) {
            secondStr = "0" + second;

        } else {
            secondStr = second + "";
        }

        return (hourStr + ":" + minStr + ":" + secondStr);
    }

    /**
     * 获取年月日 格式yyyy-MM-dd
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 获取年、月 格式 yyyy-MM
     *
     * @return
     */
    public static String getMonth() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String getMonthAndDay() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当月日期
     * @return Day of month
     */
    public static String getDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day+"";
    }

    /**
     * 获取星期几
     */
    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return week[dayOfWeek - 1];
    }

    /**
     * 获取农历年份
     * @return
     */
    public static String getLunarYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int[] lunarDate = LunarCalendar.solarToLunar(year, month, day);
        return lunarYear[(lunarDate[0] + 2697) % 60 - 1];
    }

    /**
     * 获取农历月份
     * @return
     */
    public static String getLunarMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int[] lunarDate = LunarCalendar.solarToLunar(year, month, day);
        return lunarMonth[lunarDate[1] - 1];
    }

    /**
     * 获取农历日
     * @return
     */
    public static String getLunarDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int[] lunarDate = LunarCalendar.solarToLunar(year, month, day);
        return lunarDay[lunarDate[2] - 1];
    }
}
