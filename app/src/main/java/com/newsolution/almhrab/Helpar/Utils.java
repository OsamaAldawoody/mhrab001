package com.newsolution.almhrab.Helpar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class Utils {
    public static String PREFS = "Mhrab";
    public static final String LASTUPDATE = "last_update";
    public static final String ENGLISH = "English";
    public static final String DeviceNo = "DeviceNo";

    private final static String[] iMONTHS = {"", "محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخر", "رجب",
            "شعبان", "رمضان", "شوال", "ذو القعدة", " ذو الحجة"};

    public static String getIqama(String time, String iqama) {
        if (TextUtils.isEmpty(iqama))
            iqama = "15";
        String[] intime = time.split(":");
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
        return Iqama;
    }

    public static String setPhoneAlert(String time, String period) {
        String phoneAlert = "";
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Date date = df.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long millSec = calendar.getTimeInMillis();
            long sec = millSec - (Long.parseLong(period) * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            Calendar c = Calendar.getInstance();
            calendar.setTimeInMillis(sec);
            phoneAlert = formatter.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
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

    public static void applyFont(final Context context, final View v) {
        String FONT_NAME = "fonts/ae_battar.ttf";
        Typeface font = Typeface.createFromAsset(context.getAssets(), FONT_NAME);
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
        }
    }


    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isSaturday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SATURDAY;
    }

    public static boolean isSunday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SUNDAY;
    }

    public static boolean isMonday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.MONDAY;
    }

    public static boolean isTuesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.TUESDAY;
    }

    public static boolean isWednesday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.WEDNESDAY;
    }

    public static boolean isThursday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.THURSDAY;
    }

    public static boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.FRIDAY;
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


    public static boolean isOnline(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean compareDates(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.ENGLISH);
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
            } else {
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
        System.out.println("Date2 *" + d2);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
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
            } else {
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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
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
        toast.show();
    }

    public static String getName(String name) {
        String[] username = name.split("@");
        return username[0];
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (activity.getCurrentFocus() != null)
            if (in != null) {
                in.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
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
        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);

        int iDayN = hd.date1();
        String normalDate = wdNames[iDayN] + " | " + (int) day + " " + MonthNames[(int) month] + " " + (int) year
                + " | ";
        int hijriDiff = act.getSharedPreferences(Utils.PREFS, Context.MODE_PRIVATE)
                .getInt("hijriDiff", 0);
        int[] dt = HijriCalendar.getSimpleDate(today, hijriDiff);
        String hijriDate = dt[1] + " " + iMONTHS[dt[2]] + " " + dt[3] + act.getString(R.string.mt);
        return normalDate + hijriDate;
    }

    public static String writeIslamicDate1(Activity act, DateHigri hd) {

        String[] MonthNames = {act.getString(R.string.em1), act.getString(R.string.em2), act.getString(R.string.em3),
                act.getString(R.string.em4), act.getString(R.string.em5), act.getString(R.string.em6), act.getString(R.string.em7),
                act.getString(R.string.em8), act.getString(R.string.em9), act.getString(R.string.em10), act.getString(R.string.em11)
                , act.getString(R.string.em12)};
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH);
        double year = today.get(Calendar.YEAR);
        int hijriDiff = act.getSharedPreferences(Utils.PREFS, act.MODE_PRIVATE)
                .getInt("hijriDiff", 0);
        int[] dt = HijriCalendar.getSimpleDate(today, hijriDiff);
        String hijriDate = dt[1] + " " + iMONTHS[dt[2]] + " " + dt[3] + act.getString(R.string.mt);
        String normalDate = (int) day + " " + MonthNames[(int) month] + " " + (int) year;
        return hijriDate + " - " + normalDate;
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
        int iDayN = hd.date1();
        return wdNames[iDayN] + "  " + (int) day + " " + MonthNames[(int) month]
                + " " + (int) year + " " + act.getString(R.string.mt1);
    }

    public static String writeHDate(Activity act, DateHigri hd) {
        Calendar today = Calendar.getInstance();
        int hijriDiff = act.getSharedPreferences(Utils.PREFS, act.MODE_PRIVATE)
                .getInt("hijriDiff", 0);
        int[] dt = HijriCalendar.getSimpleDate(today, hijriDiff);

        return dt[1] + " " + iMONTHS[dt[2]] + " " + dt[3] + act.getString(R.string.mt);
    }

    public static String convertToEng(String value) {
        return (((((((((((value).replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3"))
                .replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7"))
                .replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
    }

    public static CharSequence padText(CharSequence text, TextPaint paint, int width) {
        Rect textbounds = new Rect();
        paint.getTextBounds(text.toString(), 0, text.length(), textbounds);

        if (textbounds.width() > width) {
            return text;
        }

        String workaroundString = "a a";
        Rect spacebounds = new Rect();
        paint.getTextBounds(workaroundString, 0, workaroundString.length(), spacebounds);

        Rect abounds = new Rect();
        paint.getTextBounds(new char[]{
                'a'
        }, 0, 1, abounds);

        float spaceWidth = spacebounds.width() - (abounds.width() * 2);


        int amountOfSpacesNeeded = (int) Math.ceil((width - textbounds.width()) / spaceWidth);

        return amountOfSpacesNeeded > 0 ? padRight(text.toString(), text.toString().length()
                + amountOfSpacesNeeded) : text;
    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static void setImage(Activity activity, String masjedImg, final AppCompatImageView imageView) {
        Glide.with(activity).load(Uri.parse(masjedImg))
                .override(100, 100).listener(new RequestListener<Uri, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                imageView.setImageResource(R.drawable.ic_mosque);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }

}
