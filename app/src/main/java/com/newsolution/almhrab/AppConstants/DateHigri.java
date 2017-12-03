package com.newsolution.almhrab.AppConstants;

import com.newsolution.almhrab.R;

import java.util.Calendar;

public class DateHigri {


    static double gmod(double n,double  m) {
        return ((n % m) + m) % m;
    }

   public static double[] kuwaiticalendar(int hijriDiff, boolean adjust) {
        Calendar today = Calendar.getInstance();
        int adj=0;
        if(adjust){
            adj=0;
        }else{
            adj=1;
        }

        if (adjust) {
            int adjustmili = 1000 * 60 * 60 * 24 * adj;
            long todaymili = today.getTimeInMillis() + adjustmili;
            today.setTimeInMillis(todaymili);
        }
       double day =( today.get(Calendar.DAY_OF_MONTH))+hijriDiff;
        double  month = today.get(Calendar.MONTH);
        double  year = today.get(Calendar.YEAR);

        double  m = month + 1;
        double  y = year;
        if (m < 3) {
            y -= 1;
            m += 12;
        }

        double a = Math.floor(y / 100.);
        double b = 2 - a + Math.floor(a / 4.);

        if (y < 1583)
            b = 0;
        if (y == 1582) {
            if (m > 10)
                b = -10;
            if (m == 10) {
                b = 0;
                if (day > 4)
                    b = -10;
            }
        }

        double jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day
                + b - 1524;

        b = 0;
        if (jd > 2299160) {
            a = Math.floor((jd - 1867216.25) / 36524.25);
            b = 1 + a - Math.floor(a / 4.);
        }
        double bb = jd + b + 1524;
        double cc = Math.floor((bb - 122.1) / 365.25);
        double dd = Math.floor(365.25 * cc);
        double ee = Math.floor((bb - dd) / 30.6001);
        day = (bb - dd) - Math.floor(30.6001 * ee);
        month = ee - 1;
        if (ee > 13) {
            cc += 1;
            month = ee - 13;
        }
        year = cc - 4716;

        double wd = gmod(jd + 1, 7) + 1;

        double iyear = 10631. / 30.;
        double epochastro = 1948084;
        double epochcivil = 1948085;

        double shift1 = 8.01 / 60.;

        double z = jd - epochastro;
        double cyc = Math.floor(z / 10631.);
        z = z - 10631 * cyc;
        double j = Math.floor((z - shift1) / iyear);
        double iy = 30 * cyc + j;
        z = z - Math.floor(j * iyear + shift1);
        double im = Math.floor((z + 28.5001) / 29.5);
        if (im == 13)
            im = 12;
        double id = z - Math.floor(29.5001 * im - 29);

        double[]  myRes = new double[8];

        myRes[0] = day; // calculated day (CE)
        myRes[1] = month - 1; // calculated month (CE)
        myRes[2] = year; // calculated year (CE)
        myRes[3] = jd - 1; // julian day number
        myRes[4] = wd - 1; // weekday number
        myRes[5] = id; // islamic date
        myRes[6] = im - 1; // islamic month
        myRes[7] = iy; // islamic year

        return myRes;
    }



    public static String writeIslamicDate() {
        String[] wdNames = {String.valueOf(R.string.sun), String.valueOf(R.string.mon), String.valueOf(R.string.tus), String.valueOf(R.string.wes)
                , String.valueOf(R.string.ths), String.valueOf(R.string.fri), String.valueOf(R.string.sat)};
        String[] iMonthNames = {String.valueOf(R.string.am1), String.valueOf(R.string.am2), String.valueOf(R.string.am3),
                String.valueOf(R.string.am4), String.valueOf(R.string.am5), String.valueOf(R.string.am6), String.valueOf(R.string.am7),
                String.valueOf(R.string.am8), String.valueOf(R.string.am9), String.valueOf(R.string.am10), String.valueOf(R.string.am11)
                , String.valueOf(R.string.am12)};
        String[] MonthNames = {String.valueOf(R.string.em1), String.valueOf(R.string.em2), String.valueOf(R.string.em3),
                String.valueOf(R.string.em4), String.valueOf(R.string.em5), String.valueOf(R.string.em6), String.valueOf(R.string.em7),
                String.valueOf(R.string.em8), String.valueOf(R.string.em9), String.valueOf(R.string.em10), String.valueOf(R.string.em11)
                , String.valueOf(R.string.em12)};
        boolean dayTest=true;
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double  month = today.get(Calendar.MONTH);
        double  year = today.get(Calendar.YEAR);
        double[] iDate = kuwaiticalendar(0,dayTest);
       // String outputIslamicDate = wdNames[(int) iDate[4]] + " " + (int)day + " " +MonthNames[(int) month] + " " + (int)year + " م " + (int)iDate[5] + " "+ iMonthNames[(int) iDate[6]] + " " + (int)iDate[7] + " هـ ";
       String outputIslamicDate = wdNames[(int) iDate[4]] + " | " + (int)day + " " +MonthNames[(int) month] + " " + (int)year + " | " + (int)iDate[5] + " "+ iMonthNames[(int) iDate[6]] + " " +
               (int)iDate[7] + " هـ ";

        return outputIslamicDate;
    }


    public static int date1() {
        String[] wdNames = {"الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس",
                "الجمعة", "السبت"};
        boolean dayTest=true;
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double[] iDate = kuwaiticalendar(0,dayTest);
        int outputIslamicDate = (int) iDate[4];
        return outputIslamicDate;
    }
    static int date2() {
        String[] MonthNames = {"يناير", "فبراير", "مارس",
                "أبريل", "مايو", "يونيو", "يوليو",
                "أغسطس", "سبمتمبر", "أكتوبر", "نوفمبر", "ديسمبر"};
        boolean dayTest=true;
        Calendar today = Calendar.getInstance();
        double  month = today.get(Calendar.MONTH);
        int outputIslamicDate =  (int) month;
        return outputIslamicDate;
    }
    public static String date3() {
        boolean dayTest=true;
        Calendar today = Calendar.getInstance();
        double[] iDate = kuwaiticalendar(0,dayTest);
        double  year = today.get(Calendar.YEAR);
        String outputIslamicDate = (int)iDate[5]  + " " +  (int)year ;
        return outputIslamicDate;
    }
    public static int date4() {
        String[] iMonthNames = {"محرم", "صفر", "ربيع الأول",
                "ربيع الآخر", "جمادى الأولى", "جمادة الآخرة", "رجب",
                "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"};
        boolean dayTest=true;
        Calendar today = Calendar.getInstance();
        double[] iDate = kuwaiticalendar(0,dayTest);
        int outputIslamicDate = (int) iDate[6];
        return outputIslamicDate;
    }
    public static String date5() {
        boolean dayTest=true;
        Calendar today = Calendar.getInstance();
        double[] iDate = kuwaiticalendar(0,dayTest);
        String outputIslamicDate = (int)iDate[7] + "";
        return outputIslamicDate;
    }







}