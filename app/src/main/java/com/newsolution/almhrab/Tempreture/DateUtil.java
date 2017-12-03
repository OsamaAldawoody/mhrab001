package com.newsolution.almhrab.Tempreture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static final int CS_sRGB = 1000;
    public static Date GetUTCTime() {
        return new Date(new Date().getTime() - ((long) ((GetTimeZone() * 60) * CS_sRGB)));
    }

    public static Date DateAddHours(Date date, double hour) {
        return new Date(date.getTime() + ((long) ((int) (((hour * 60.0d) * 60.0d) * 1000.0d))));
    }

    public static String ToString(Date date, String format) {
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Date ToData(String strData, String format) {
        try {
            return new SimpleDateFormat(format).parse(strData);
        } catch (Exception e) {
            return null;
        }
    }

    public static String TimeSpanString(Date data1, Date date2) {
        try {
            int s = (int) (Math.abs(data1.getTime() - date2.getTime()) / 1000);
            int day = s / 86400;
            int hour = (s - (((day * 24) * 60) * 60)) / 3600;
            int min = ((s - (((day * 24) * 60) * 60)) - ((hour * 60) * 60)) / 60;
            int se = ((s - (((day * 24) * 60) * 60)) - ((hour * 60) * 60)) - (min * 60);
            if (day > 0) {
                return day + ":" + StringUtil.PadLeft(hour + "", 2) + ":" + StringUtil.PadLeft(min + "", 2) + ":" + StringUtil.PadLeft(se + "", 2);
            }
            return StringUtil.PadLeft(hour + "", 2) + ":" + StringUtil.PadLeft(min + "", 2) + ":" + StringUtil.PadLeft(se + "", 2);
        } catch (Exception e) {
            return "";
        }
    }

    public static int GetTimeZone() {
        try {
            return ((TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings()) / 60) / CS_sRGB;
        } catch (Exception e) {
            return 0;
        }
    }
}
