package com.newsolution.almhrab;

import android.util.Log;

import java.text.DecimalFormat;

public class Hijri_Cal_Tools {
    public static double e3;
    public static double e4;
    public static int hijriDay;
    public static int hijriMonth;
    public static int hijriYear;
    private static double lat1;
    private static double lat2;
    private static double lon1;
    private static double lon2;
    private static double mday;
    private static double mmonth;
    private static double myear;
    public static double n3;
    public static double n4;
    public static double v1;
    public static double v10;
    public static double v11;
    public static double v12;
    public static double v14;
    public static double v15;
    public static double v16;
    public static double v17;
    public static double v18;
    public static double v2;
    public static double v20;
    public static double v22;
    public static double v23;
    public static double v25;
    public static double v27;
    public static double v28;
    public static double v3;
    public static double v30;
    public static double v31;
    public static double v4;
    public static double v5;
    public static double v6;
    public static double v7;
    public static double v8;
    public static double v9;
    public static double valpha;

    public static void setDay(Double d) {
        Log.i("day before", mday + "");
        mday += d.doubleValue();
        Log.i("day before", mday + "");
        calculation(lat1, lat2, lon1, lon2, myear, mmonth, mday);
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public static void calculation(double lati1, double lati2, double longi1, double longi2, double year, double month, double day) {
        lat1 = lati1;
        lat2 = lati2;
        lon1 = longi1;
        lon2 = longi2;
        mday = day;
        mmonth = month;
        myear = year;
        e3 = longi2 / 60.0d;
        e4 = e3 + longi1;
        n3 = lati2 / 60.0d;
        n4 = n3 + lati1;
        v1 = ((((367.0d * year) - ((double) ((int) (1.75d * (((double) ((int) ((9.0d + month) / 12.0d))) + year))))) + ((double) ((int) (275.0d * (month / 9.0d))))) + day) - 730531.5d;
        v2 = (280.461d + (0.9856474d * v1)) % 360.0d;
        v3 = (357.528d + (0.9856003d * v1)) % 360.0d;
        v4 = (v2 + (1.915d * Math.sin((v3 * 3.141592653589793d) / 180.0d))) + (0.02d * Math.sin(((2.0d * v3) * 3.141592653589793d) / 180.0d));
        v5 = 23.439d - (4.0E-7d * v1);
        v6 = (Math.atan(Math.cos((v5 * 3.141592653589793d) / 180.0d) * Math.tan((v4 * 3.141592653589793d) / 180.0d)) * 180.0d) / 3.141592653589793d;
        v7 = v6 - (360.0d * Math.floor(v6 / 360.0d));
        valpha = v7 + (90.0d * (Math.floor(v4 / 90.0d) - Math.floor(v7 / 90.0d)));
        v8 = (100.46d + (0.985647352d * v1)) - (360.0d * Math.floor((100.46d + (0.985647352d * v1)) / 360.0d));
        v9 = (Math.asin(Math.sin((v5 * 3.141592653589793d) / 180.0d) * Math.sin((v4 * 3.141592653589793d) / 180.0d)) * 180.0d) / 3.141592653589793d;
        v10 = (valpha - v8) - (360.0d * Math.floor((valpha - v8) / 360.0d));
        v11 = v10 - e4;
        v12 = (v11 / 15.0d) + 4.0d;
        v14 = (Math.asin((Math.sin((n4 * 3.141592653589793d) / 180.0d) * Math.sin((v9 * 3.141592653589793d) / 180.0d)) + (Math.cos((n4 * 3.141592653589793d) / 180.0d) * Math.cos((v9 * 3.141592653589793d) / 180.0d))) * 180.0d) / 3.141592653589793d;
        v15 = 90.0d - ((Math.atan(1.0d / (1.0d + (1.0d / Math.tan((v14 * 3.141592653589793d) / 180.0d)))) * 180.0d) / 3.141592653589793d);
        v16 = ((Math.acos((Math.sin(((90.0d - v15) * 3.141592653589793d) / 180.0d) - (Math.sin((v9 * 3.141592653589793d) / 180.0d) * Math.sin((n4 * 3.141592653589793d) / 180.0d))) / (Math.cos((v9 * 3.141592653589793d) / 180.0d) * Math.cos((n4 * 3.141592653589793d) / 180.0d))) * 180.0d) / 3.141592653589793d) / 15.0d;
        v18 = v12 + v16;
        setV20(v12 + v17);
        v22 = (Math.acos((Math.sin(-0.01454441043328608d) - (Math.sin((v9 * 3.141592653589793d) / 180.0d) * Math.sin((n4 * 3.141592653589793d) / 180.0d))) / (Math.cos((v9 * 3.141592653589793d) / 180.0d) * Math.cos((n4 * 3.141592653589793d) / 180.0d))) * 180.0d) / 3.141592653589793d;
        v23 = v12 - (v22 / 15.0d);
        v25 = v12 + (v22 / 15.0d);
        v27 = (Math.acos((Math.sin(-0.3141592653589793d) - (Math.sin((v9 * 3.141592653589793d) / 180.0d) * Math.sin((n4 * 3.141592653589793d) / 180.0d))) / (Math.cos((v9 * 3.141592653589793d) / 180.0d) * Math.cos((n4 * 3.141592653589793d) / 180.0d))) * 180.0d) / 3.141592653589793d;
        v28 = v12 + (v27 / 15.0d);
        v30 = (Math.acos((Math.sin(-0.3141592653589793d) - (Math.sin((v9 * 3.141592653589793d) / 180.0d) * Math.sin((n4 * 3.141592653589793d) / 180.0d))) / (Math.cos((v9 * 3.141592653589793d) / 180.0d) * Math.cos((n4 * 3.141592653589793d) / 180.0d))) * 180.0d) / 3.141592653589793d;
        v31 = v12 - (v30 / 15.0d);
        v12 += 0.08333333333333333d;
        v18 += 0.08333333333333333d;
        v25 += 0.08333333333333333d;
        if (month <= 2.0d) {
            year -= 1.0d;
            month += 12.0d;
        }
        double A = (double) ((int) (year / 100.0d));
        double L = (((double) ((int) (((((double) (((int) (365.25d * (4716.0d + year))) + ((int) (30.6001d * (1.0d + month))))) + day) + ((2.0d - A) + ((double) ((int) (A / 4.0d))))) - 1524.5d))) - 1948440.0d) + 10632.0d;
        double N = (double) ((int) ((L - 1.0d) / 10631.0d));
        L = (L - (10631.0d * N)) + 354.0d;
        double J = (double) ((((int) ((10985.0d - L) / 5316.0d)) * ((int) ((50.0d * L) / 17719.0d))) + (((int) (L / 5670.0d)) * ((int) ((43.0d * L) / 15238.0d))));
        L = ((L - ((double) (((int) ((30.0d - J) / 15.0d)) * ((int) ((17719.0d * J) / 50.0d))))) - ((double) (((int) (J / 16.0d)) * ((int) ((15238.0d * J) / 43.0d))))) + 29.0d;
        hijriMonth = (int) ((24.0d * L) / 709.0d);
        hijriDay = (int) (L - ((double) ((int) ((709.0d * ((double) hijriMonth)) / 24.0d))));
        hijriYear = (int) (((30.0d * N) + J) - 30.0d);
    }

    public static String getTimeHHMM(double decimalTime) {
        int hours = (int) decimalTime;
        int minutes = (int) ((decimalTime - ((double) ((int) decimalTime))) * 60.0d);
        if (((int) ((((decimalTime - ((double) ((int) decimalTime))) * 60.0d) - ((double) ((int) ((decimalTime - ((double) ((int) decimalTime))) * 60.0d)))) * 60.0d)) >= 30) {
            minutes++;
        }
        if (minutes == 60) {
            minutes = 0;
            hours++;
        }
        String timeHHMM;
        if (hours < 10) {
            if (minutes < 10) {
                timeHHMM = "0"+String.valueOf(hours) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = "0"+String.valueOf(hours) + ":" + String.valueOf(minutes);
            }
            return timeHHMM+":00"; //+ "ص";
        } else if (hours > 12) {
            if (minutes < 10) {
                timeHHMM = String.valueOf(hours ) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = String.valueOf(hours ) + ":" + String.valueOf(minutes);
            }
            return timeHHMM +":00";//+ "م";
        } else {
            if (minutes < 10) {
                timeHHMM = String.valueOf(hours) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = String.valueOf(hours) + ":" + String.valueOf(minutes);
            }
            if (hours == 12) {
                return timeHHMM +":00";//+ "م";
            }
            return timeHHMM +":00";//+ "ص";
        }
    }

    public static int getHijriDay() {
        return hijriDay;
    }

    public static int getHijriMonth() {
        return hijriMonth;
    }

    public static int getHijriYear() {
        return hijriYear;
    }

    public static String getDhuhur() {
//        Log.i("////*",v12+"");
        return getTimeHHMM(v12);
    }

    public static String getAsar() {
        return getTimeHHMM(v12 + v16);
    }

    public static String getSunRise() {
        return getTimeHHMM(v23);
    }

    public static String getMagrib() {
        return getTimeHHMM(v25);
    }

    public static String getFajer() {
        return getTimeHHMM(v31);
    }

    public static String getIshaa() {
        return getTimeHHMM(v28);
    }

    public static Double getDhuhurDouble() {
        return Double.valueOf(v12);
    }

    public static Double getAsarDouble() {
        return Double.valueOf(v12 + v16);
    }

    public static Double getSunRiseDouble() {
        return Double.valueOf(v23);
    }

    public static Double getMagribDouble() {
        return Double.valueOf(v25);
    }

    public static Double getFajerDouble() {
        return Double.valueOf(v31);
    }

    public static Double getIshaaDouble() {
        return Double.valueOf(v28);
    }

    public static String getDuha() {
        return getTimeHHMM(v23 + 0.25d);
    }

    public static double getV20() {
        return v20;
    }

    public static void setV20(double n20) {
        v20 = n20;
    }

    public static int[] getTimeHHMMCal(double decimalTime) {
        int hours = (int) decimalTime;
        int minutes = (int) ((decimalTime - ((double) ((int) decimalTime))) * 60.0d);
        if (((int) ((((decimalTime - ((double) ((int) decimalTime))) * 60.0d) - ((double) ((int) ((decimalTime - ((double) ((int) decimalTime))) * 60.0d)))) * 60.0d)) >= 30) {
            minutes++;
        }
        if (minutes == 60) {
            minutes = 0;
            hours++;
        }
        int[] arrayTime = new int[]{hours, minutes};
        Log.i("Hijri", hours + ":" + minutes);
        return arrayTime;
    }

    public static int[] getDhuhurCal() {
        return getTimeHHMMCal(v12);
    }

    public static int[] getAsarCal() {
        return getTimeHHMMCal(v12 + v16);
    }

    public static int[] getSunRiseCal() {
        return getTimeHHMMCal(v23);
    }

    public static int[] getMagribCal() {
        return getTimeHHMMCal(v25);
    }

    public static int[] getFajerCal() {
        return getTimeHHMMCal(v31);
    }

    public static int[] getIshaaCal() {
        return getTimeHHMMCal(v28);
    }

    public static String calDMS(double latlng) {
        String dms = "";
        int degree = (int) latlng;
        double min = latlng - degree;
        Log.i("dms// 1 : ", "" + min);
        int minutes = (int) (min * 60);
        Log.i("dms//: ", "" + minutes);
        dms = degree + ":" + minutes;
        return dms;
    }
}
