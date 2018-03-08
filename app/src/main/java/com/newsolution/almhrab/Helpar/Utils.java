package com.newsolution.almhrab.Helpar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.R;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by HSM on 1/16/2015.
 */
public class Utils {
    public static String PREFS = "AlMhrabApp";
    public static final String ENGLISH = "English";
    public static final String ARABIC = "Arabic";
    public static String FONT_NAME_B = "fonts/droidkufi_regular.ttf";
    public static String FONT_NAME_EB = "fonts/droid_kufi_bold.ttf";
    public static String FONT_NAME = "fonts/ae_battar.ttf";
    public static String FONT_LOGO = "fonts/deco_naskh_special.ttf";
    static Typeface font;
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String ARG_FIREBASE_TOKEN = "deviceToken";

    public static String getIqama(String time, String iqama) {
        if (TextUtils.isEmpty(iqama))
            iqama = "15";
//       time=time.replace("ص","").replace("م","");
        String intime[] = time.split(":");
        int hour = Integer.parseInt(intime[0]);
        int minutes = Integer.parseInt(intime[1]);
        int h = hour;
        long m = minutes + Long.parseLong(iqama);

        if (m > 59) {
            m = m - 60;
            h++;
        }

        String Iqama = "";
        if (h < 10)
            Iqama = "0" + h + ":" + m + ":00";
        else
            Iqama = h + ":" + m + ":00";
        // Log.e("pray + iqama = ", time + " -- " + Iqama);
        return Iqama;
    }

    public static String setPhoneAlert(String time, String period) {
        String intime[] = time.split(":");
        int hour = Integer.parseInt(intime[0]);
        int minutes = Integer.parseInt(intime[1]);
        int second = 0;
        String phoneAlert = "";
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Date date = df.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long millSec = calendar.getTimeInMillis();
            long sec = millSec - (Long.parseLong(period) * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", new Locale("en"));
            Calendar c = Calendar.getInstance();
            calendar.setTimeInMillis(sec);
            phoneAlert = formatter.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //   int h = hour;
//        long s = second + Long.parseLong(period);
//        long m = minutes ;
//        long sec;
//        sec=(int)s/60;
//        if (s > 59) {
//            s = s -(sec* 60);
//            m=m+sec;
//        }
//        if (m > 59) {
//            m = m - 60;
//            h++;
//        }
//        String phoneAlert = h + ":" + m+":"+s;
        Log.e("phone alert = ", time + " -- " + phoneAlert);

//        Calendar calendar = Calendar.getInstance();
//        System.out.println("Original = " + calendar.getTime());
//
//        calendar.set(Calendar.HOUR, hour);
//
//        calendar.set(Calendar.MINUTE, minutes);
//
//        calendar.set(Calendar.SECOND, second);
//        System.out.println("Updated  = " + calendar.getTime());
//          DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.US);
//        String phoneAlert ="";
//        phoneAlert=   df.format(calendar.getTime());
//        Log.e("phone alert = ", time + " -- " + phoneAlert);
        return phoneAlert;
    }

    public static String addToTime(String time, String increment) {
        String result = "";
        if (!TextUtils.isEmpty(time)) {
            if (TextUtils.isEmpty(increment))
                increment = "15";
            String intime[] = time.split(":");
            int hour = Integer.parseInt(intime[0]);
            int minutes = Integer.parseInt(intime[1]);
            int h = hour;
            long m = minutes + Long.parseLong(increment);

            if (m > 59) {
                m = m - 60;
                h++;
            }

            if (h < 10)
                result = "0" + h + ":" + m + ":00";
            else
                result = h + ":" + m + ":00";
        }
        return result;
    }

    public static String diffFromTime(String time, String decrement) {
        String phoneAlert = "";
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Date date = df.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long millSec = calendar.getTimeInMillis();
            long sec = millSec - (Long.parseLong(decrement) * 60 * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", new Locale("en"));
            Calendar c = Calendar.getInstance();
            calendar.setTimeInMillis(sec);
            phoneAlert = formatter.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("phone alert = ", time + " -- " + phoneAlert);
        return phoneAlert;
    }

    public static long getTime(Calendar cal, String time) {

        if (!TextUtils.isEmpty(time)) {
            String[] convTime = time.split(":");
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(convTime[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(convTime[1]));
            if (convTime.length > 2)
                cal.set(Calendar.SECOND, Integer.valueOf(convTime[2]));
            else
                cal.set(Calendar.SECOND, 0);

            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal.getTimeInMillis();
    }

    public static long getMillis(String givenTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date mDate = sdf.parse(givenTime);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Date getMonday() {
        Date today = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();

        cal.setTime(today);

        int dow = cal.get(Calendar.DAY_OF_WEEK);

        while (dow != Calendar.FRIDAY) {
            int date = cal.get(Calendar.DATE);

            if (date == 1) {
                int month = cal.get(Calendar.MONTH);

                if (month == Calendar.JANUARY) {
                    month = Calendar.DECEMBER;

                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                } else {
                    month--;
                }

                cal.set(Calendar.MONTH, month);

                date = getMonthLastDate(month, cal.get(Calendar.YEAR));
            } else {
                date--;
            }

            cal.set(Calendar.DATE, date);

            dow = cal.get(Calendar.DAY_OF_WEEK);
        }

        return cal.getTime();
    }

    private static int getMonthLastDate(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;

            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;

            default:    //  Calendar.FEBRUARY
                return year % 4 == 0 ? 29 : 28;
        }
    }

    public static void setAppFont(Context context, String fontName) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), FONT_NAME);
        replaceFont("MONOSPACE", typeface);
        replaceFont("DEFAULT", typeface);
        replaceFont("SERIF", typeface);
        replaceFont("SANS_SERIF", typeface);
    }

    private static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    public static String getFormattedCurrentDate() {
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return df.format(c.getTime());
    }

    private static void replaceFont(String s, Typeface font) {
        try {
            Field field = Typeface.class.getDeclaredField(s);
            field.setAccessible(true);
            field.set(null, font);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void applyFont(final Context context, final View v) {
        font = Typeface.createFromAsset(context.getAssets(), FONT_NAME);
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFont(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(font);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(font);
            } else if (v instanceof RadioButton) {
                ((RadioButton) v).setTypeface(font);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

    public static void applyFontBold(final Context context, final View v) {
        font = Typeface.createFromAsset(context.getAssets(), FONT_NAME_B);
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFont(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(font);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(font);
            } else if (v instanceof RadioButton) {
                ((RadioButton) v).setTypeface(font);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

    public static void applyFontEnBold(final Context context, final View v) {
        font = Typeface.createFromAsset(context.getAssets(), FONT_NAME_EB);
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFont(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(font);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(font);
            } else if (v instanceof RadioButton) {
                ((RadioButton) v).setTypeface(font);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }

    public static void setLightStatusBar(View view, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    public static String getLastName(String userName) {
        String name = userName;

        int start = name.indexOf(' ');
        int end = name.lastIndexOf(' ');

        String firstName = "";
        String middleName = "";
        String lastName = "";

        if (start >= 0) {
            firstName = name.substring(0, start);
            if (end > start)
                middleName = name.substring(start + 1, end);
            lastName = name.substring(end + 1, name.length());
            return lastName;
        } else {
            return name;
        }

    }

    public static String getFirstName(String userName) {
        String name = userName;

        int start = name.indexOf(' ');
        int end = name.lastIndexOf(' ');

        String firstName = "";
        String middleName = "";
        String lastName = "";

        if (start >= 0) {
            firstName = name.substring(0, start);
            if (end > start)
                middleName = name.substring(start + 1, end);
            lastName = name.substring(end + 1, name.length());
            return firstName;
        } else {
            return name;
        }
    }


    public static String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sp = new SimpleDateFormat("dd-MM-yyyy");
        String date = sp.format(c.getTime());
        return date;
    }

    public static long getTimeInMillsecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static long getStartTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date d1 = c.getTime();
        // Log.i("----- st date ",d1+"");
        return d1.getTime();
    }

    public static long getEndTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date d1 = c.getTime();
        //Log.i("----- end date ", d1 + "");
        return d1.getTime();
    }

    public static boolean compareDate(String date2) {
        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sp = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = currentDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dateToday = null;
        Date dateInput = null;
        try {
            dateToday = sdf.parse(date1);
            dateInput = sdf.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean d = dateToday.after(dateInput) || dateToday.equals(dateInput);
        Log.i("/////**- ", d + "");
        return d;
    }

    final static String MONTHS[] =
            new String[]{"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String getDateTime(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String fullDate = buildValueOf(calendar.get(Calendar.DAY_OF_MONTH)) + " " + MONTHS[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR) + " at " +
                buildValueOf(getHour(calendar.get(Calendar.HOUR_OF_DAY))) + ":" + buildValueOf(calendar.get(Calendar.MINUTE)) + " " + time_type(calendar.get(Calendar.HOUR_OF_DAY));
        return fullDate;
    }

    public static String changeServerDateFormat(String myString) throws ParseException {
        SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date dt = sd1.parse(myString);
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String newDate = sd2.format(dt);
//        System.out.println(newDate);
        return newDate;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm", new Locale("en"));
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private static String buildValueOf(int value) {
        if (value >= 10)
            return String.valueOf(value);
        else
            return "0" + String.valueOf(value);
    }

    private static String time_type(int i) {
        if (i > 12)
            return "PM";

        return "AM";
    }

    private static int getHour(int hour) {
        if (hour < 12)
            return hour;
        else
            return hour - 12;
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en"));
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTime() {
        //  Locale locale = new Locale("en");
        // Locale.setDefault(locale);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", new Locale("en"));
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isSaturday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SATURDAY) {
            return true;
        } else return false;
    }

    public static boolean isSunday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            return true;
        } else return false;
    }

    public static boolean isMonday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.MONDAY) {
            return true;
        } else return false;
    }

    public static boolean isTuesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.TUESDAY) {
            return true;
        } else return false;
    }

    public static boolean isWednesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.WEDNESDAY) {
            return true;
        } else return false;
    }

    public static boolean isThursday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.THURSDAY) {
            return true;
        } else return false;
    }

    public static boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.FRIDAY) {
            return true;
        } else return false;
    }

    public static String generatePassword() {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(40, secureRandom).toString(32);
    }

    public static CharSequence converteTimestamp(String mileSegundos) {
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();
    }

    public static int random4() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 1000 + r.nextInt(1000));
    }

    public static int random2() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10 + r.nextInt(10));
    }

    public static int calAge(String age) {

        String[] x = age.split("-");
        Log.i("////,", x[0]);
        int year = Integer.parseInt(x[2]);
        int month = Integer.parseInt(x[1]);
        int day = Integer.parseInt(x[0]);
        Calendar cal = Calendar.getInstance();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;

    }

    public static int random9() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 100000000 + r.nextInt(100000000));
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static void setupUI(final Activity activity, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        hideSoftKeyboard(activity);
                    } catch (Exception e) {
                    }
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(activity, innerView);
            }
        }
    }


    public static boolean isOnline(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean compareDates(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1 " + sdf.format(date1));
            System.out.println("Date2 " + sdf.format(date2));
            System.out.println();
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            } else {// if(date1.equals(date2)){
                System.out.println("Date1 is equal Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public static boolean compareDate(String d1, String d2) {
        System.out.println("Date1 * " + d1);
        System.out.println("Date2 *" +d2);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            System.out.println("Date1 " + sdf.format(date1));
            System.out.println("Date2 " + sdf.format(date2));
            System.out.println();
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            } else {// if(date1.equals(date2)){
                System.out.println("Date1 is equal Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return true;
        }
    }
    public static boolean compareTimes(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1 " + sdf.format(date1));
            System.out.println("Date2 " + sdf.format(date2));
            System.out.println();
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            } else {// if(date1.equals(date2)){
                System.out.println("Date1 is equal Date2");
                return false;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public static void showCustomToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
//        View view = toast.getView();
//        view.setBackgroundColor(Color.RED);
//        TextView text = (TextView) view.findViewById(android.R.id.message);
        /*here you can do anything with text*/
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

//    public static void showAlert(Context context, String title, String msg) {
//        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
//                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create().show();
//    }

    public static String listToCsv(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public static String getName(String name) {
        String[] username = name.split("@");
        return username[0];
    }


//    public static void call(Context context, String str) {
//        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + str)));
//
//    }

    public static void sendEmail(Context context, String email) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        //i.putExtra(Intent.EXTRA_SUBJECT, subject);
        //i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openWeb(Context context, String url) {
        Uri uri = null;
        try {
            uri = Uri.parse(url);
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void setStrictMode() {
        // Activate StrictMode
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                // alternatively .detectAll() for all detectable problems
                .penaltyLog()
                .penaltyDeath()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                // alternatively .detectAll() for all detectable problems
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    public static String getHtml(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><HEAD><meta charset=\"UTF-8\" /></HEAD><body dir=\"rtl\">");
        sb.append(str);
        sb.append("</body></HTML>");
        Log.d("FORMATTED HTML", sb.toString());
        return sb.toString();
    }

    public static double calDistance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(deltaLat / 2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon / 2), 2)));
        return radius * angle;
    }

    public static double distanceKM(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1.60934);

    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void getFacebookHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("Hash key:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

//        editdrop2.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Utils.hideSoftKeyboard(activity);
//
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View popupView = inflater.inflate(R.layout.drop_down_list, null);
//            RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.recycler_view1);
//            DropDownCitiesAdapter dropDownCitiesAdapter = new DropDownCitiesAdapter(regionList,
//                    new DropDownCitiesAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            region = regionList.get(position).getName();
//                            editdrop2.setText(region);
//                            popupWindow.dismiss();
//                        }
//                    });
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(dropDownCitiesAdapter);
//
//            int width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.7);
//            popupWindow = new PopupWindow(popupView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
//            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                popupWindow.setElevation(18f);
//            }
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.setFocusable(true);
//            popupWindow.showAsDropDown(v);
//        }
//
//    });

    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackbar(View view, Activity context, int string) {
        Snackbar.make(view, context.getString(string), Snackbar.LENGTH_LONG).show();
    }

//    public static void showSnackbar(View view, String message, String action, View.OnClickListener listener) {
//        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(action, listener).show();
//    }


    public static void hideKeypadOntouchOutEdittext(final Activity activity, View view) {
    /*called onCreate*/
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                hideKeypadOntouchOutEdittext(activity, innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (activity.getCurrentFocus() != null)
            in.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isNetworkConnected(Context context) {
        boolean HaveConnectedWifi = false;
        boolean HaveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    HaveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    HaveConnectedMobile = true;
        }


        return HaveConnectedWifi || HaveConnectedMobile;
    }

    public static int getColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return activity.getColor(color);

        } else {
            return activity.getResources().getColor(color);
        }


    }

    public static void showAlert(Activity context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    public static String writeIslamicDate(Activity act, DateHigri hd) {
        String[] wdNames = {act.getString(R.string.sun), act.getString(R.string.mon), act.getString(R.string.tus),
                act.getString(R.string.wes)
                , act.getString(R.string.ths), act.getString(R.string.fri), act.getString(R.string.sat)};
        String[] iMonthNames = {"محرم","صفر","ربيع الأول",
                "ربيع الثاني", "جمادى الأولى", "جمادى الآخر", "رجب",
                "شعبان","شعبان", "شوال", "ذو القعدة"
                , " ذو الحجة"};
        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        boolean dayTest = true;
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);
        double[] iDate = hd.kuwaiticalendar(act.getSharedPreferences(AppConst.PREFS, act.MODE_PRIVATE)
                .getInt("hijriDiff", 0), dayTest);
        int iDayN = hd.date1();
        // String outputIslamicDate = wdNames[(int) iDate[4]] + " " + (int)day + " " +MonthNames[(int) month] + " " + (int)year + " م " + (int)iDate[5] + " "+ iMonthNames[(int) iDate[6]] + " " + (int)iDate[7] + " هـ ";
        String outputIslamicDate = wdNames[iDayN] + " | " + (int) day + " " + MonthNames[(int) month] + " " + (int) year
                + " | " + (int) iDate[5] + " " + iMonthNames[(int) iDate[6]] + " " +
                (int) iDate[7] + " " + act.getString(R.string.mt);

        return outputIslamicDate;
    }

    public static String writeIslamicDate1(Activity act, DateHigri hd) {
        String[] wdNames = {act.getString(R.string.sun), act.getString(R.string.mon), act.getString(R.string.tus),
                act.getString(R.string.wes)
                , act.getString(R.string.ths), act.getString(R.string.fri), act.getString(R.string.sat)};
        String[] iMonthNames = {"محرم","صفر","ربيع الأول",
                "ربيع الثاني", "جمادى الأولى", "جمادى الآخر", "رجب",
                "شعبان","شعبان", "شوال", "ذو القعدة"
                , " ذو الحجة"};

        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        boolean dayTest = true;
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);
        double[] iDate = hd.kuwaiticalendar(act.getSharedPreferences(AppConst.PREFS, act.MODE_PRIVATE)
                .getInt("hijriDiff", 0), dayTest);
        int iDayN = hd.date1();
        // String outputIslamicDate = wdNames[(int) iDate[4]] + " " + (int)day + " " +MonthNames[(int) month] + " " + (int)year + " م " + (int)iDate[5] + " "+ iMonthNames[(int) iDate[6]] + " " + (int)iDate[7] + " هـ ";
        String outputIslamicDate = " "+(int) iDate[5] + " " + iMonthNames[(int) iDate[6]] + " " +
                (int) iDate[7] + " " + act.getString(R.string.mt) + " - " + (int) day + " " + MonthNames[(int) month] + " " + (int) year;

        return outputIslamicDate;
    }
    public static String writeMDate(Activity act, DateHigri hd) {
        String[] wdNames = {act.getString(R.string.sun), act.getString(R.string.mon), act.getString(R.string.tus),
                act.getString(R.string.wes)
                , act.getString(R.string.ths), act.getString(R.string.fri), act.getString(R.string.sat)};

        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        boolean dayTest = true;
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);
        double[] iDate = hd.kuwaiticalendar(act.getSharedPreferences(AppConst.PREFS, act.MODE_PRIVATE)
                .getInt("hijriDiff", 0), dayTest);
        int iDayN = hd.date1();
        String outputIslamicDate = wdNames[iDayN] + "  " + (int) day + " " + MonthNames[(int) month]
                + " " + (int) year+ " " + act.getString(R.string.mt1) ;
        return outputIslamicDate;
    }
    public static String writeHDate(Activity act, DateHigri hd) {
        String[] iMonthNames = {"محرم","صفر","ربيع الأول",
                "ربيع الثاني", "جمادى الأولى", "جمادى الآخر", "رجب",
                "شعبان","شعبان", "شوال", "ذو القعدة"
                , " ذو الحجة"};

        boolean dayTest = true;
        double[] iDate = hd.kuwaiticalendar(act.getSharedPreferences(AppConst.PREFS, act.MODE_PRIVATE)
                .getInt("hijriDiff", 0), dayTest);
        String outputIslamicDate = (int) iDate[5] + " " + iMonthNames[(int) iDate[6]] + " " +
                (int) iDate[7] + " " + act.getString(R.string.mt) ;

        return outputIslamicDate;
    }


}
