package com.newsolution.almhrab.scheduler;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import com.newsolution.almhrab.Activity.ChoosePriority;
import com.newsolution.almhrab.Activity.ClosePhone;
import com.newsolution.almhrab.Activity.MainActivity;
import com.newsolution.almhrab.Activity.RingAlarmActivity;
import com.newsolution.almhrab.Activity.Splash;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.Constants;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Helpar.WakeLocker;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.newsolution.almhrab.AppConstants.Constants.ALARM_ID;
import static com.newsolution.almhrab.AppConstants.Constants.EXTRA_PRAYER_NAME;
import static com.newsolution.almhrab.AppConstants.Constants.EXTRA_PRAYER_TIME;
import static com.newsolution.almhrab.AppConstants.Constants.FIVE_MINUTES;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class SalaatAlarmReceiver extends WakefulBroadcastReceiver implements Constants {
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    private AlarmManager iqamaAlarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    private PendingIntent iqamaAlarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences(AppConst.PREFS, context.MODE_PRIVATE);
        spedit = sp.edit();
        // BEGIN_INCLUDE(alarm_onreceive)
        /* 
         * If your receiver intent includes extras that need to be passed along to the
         * service, use setComponent() to indicate that the service should handle the
         * receiver's intent. For example:
         * 
         * ComponentName comp = new ComponentName(context.getPackageName(), 
         *      MyService.class.getName());
         *
         * // This intent passed in this call will include the wake lock extra as well as 
         * // the receiver intent contents.
         * startWakefulService(context, (intent.setComponent(comp)));
         * 
         * In this example, we simply create a new intent to deliver to the service.
         * This intent holds an extra identifying the wake lock.
         */
        int action = intent.getIntExtra(EXTRA_ACTION, -1);
        String prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME);
        String prayName = intent.getStringExtra(PRAYER_NAME);
        long prayerTime = intent.getLongExtra(EXTRA_PRAYER_TIME, -1);

        boolean timePassed = (prayerTime != -1 && Math.abs(System.currentTimeMillis() - prayerTime) > FIVE_MINUTES);
        Intent service = new Intent(context, SalaatSchedulingService.class);
        service.putExtra(EXTRA_PRAYER_NAME, prayerName);
        service.putExtra(PRAYER_NAME, prayName);
        if (!timePassed) {
            if (action == 0) {
                spedit.putString("sound", "talib").commit();
                service.putExtra(EXTRA_ACTION, 0);
            } else if (action == 1) {
                service.putExtra(EXTRA_ACTION, 1);
                spedit.putString("sound", "iqama").commit();
            } else if (action == 2) {
                spedit.putString("sound", "phone").commit();
                service.putExtra(EXTRA_ACTION, 2);
                if (isAlarmEnabledForPrayer(context, prayerName)) {
                    Intent newIntent = new Intent(context, ClosePhone.class);
                    Log.d("SalaatAlarmReceiver", "Alarm Receiver Got " + prayerName);
                    newIntent.putExtra(Constants.EXTRA_PRAYER_NAME, prayerName);
                    newIntent.putExtra(Constants.PRAYER_NAME, prayName);
                    newIntent.putExtra(EXTRA_ACTION, 2);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newIntent);
                }
            }
            WakefulBroadcastReceiver.startWakefulService(context, service);
        }
        setAlarm(context);
    }

    // BEGIN_INCLUDE(set_alarm)

    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     *
     * @param context
     */
    public void setAlarm(Context context) {
        sp = context.getSharedPreferences(AppConst.PREFS, context.MODE_PRIVATE);
        spedit = sp.edit();
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SalaatAlarmReceiver.class);

        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.

        int alarmIndex = 0;
//        Log.e("**//sleep", sp.getBoolean("sleep", false)+"");
        Calendar then = Calendar.getInstance(TimeZone.getDefault());
        then.setTimeInMillis(System.currentTimeMillis());
//        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Activity.KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(context.KEYGUARD_SERVICE);
//        if (sp.getBoolean("sleep", false)) {
////            String sleepOn = Utils.addToTime(sp.getString("isha", ""), sp.getInt("sleepOn", 0) + "");
////            String sleepOff = Utils.diffFromTime(sp.getString("suh", ""), sp.getInt("sleepOff", 0) + "");
//            try {
//                Date mToday = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//
//                String curTime = sdf.format(mToday);
//                Date cur = sdf.parse(curTime);
//
//                String curTime1 = df.format(cur);
//                Date userDate = df.parse(curTime1);
//                Log.e("**//curTime", curTime);
//                Log.e("**//cur", cur.toString());
//
//                Date start = df.parse(sp.getString("startTime", userDate + ""));
//                Date end = df.parse(sp.getString("endTime", userDate + ""));
//                Log.e("**//curTime", userDate.toString());
//                Log.e("**//start", start.toString());
//                Log.e("**//end", end.toString());
//                if ((userDate.after(start) || userDate.equals(start)) && userDate.before(end)) {
//                    Log.e("**//result", "falls between start and end , go to screen 1 ");
//                    lock.reenableKeyguard();
//                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 1000);
////                    return;
//                } else {
//                    WakeLocker.acquire(context);
//                    WakeLocker.release();
//                    lock.disableKeyguard();
//                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, (3 * 24 * 60 * 60 * 1000));
//                    Log.e("**//result", "does not fall between start and end , go to screen 2 ");
//                }
//            } catch (ParseException e) {
//
//                e.printStackTrace();
//            }
//        } else {
//            WakeLocker.acquire(context);
//            WakeLocker.release();
//            lock.disableKeyguard();
//        }

//    LinkedHashMap<String, String> prayerTimes = PrayTime.getPrayerTimes(context, alarmIndex, lat, lng, PrayTime.TIME_24);
        Map<String, String> prayerTimes = new HashMap<>();
        prayerTimes.put(context.getString(R.string.pn1), sp.getString("suh", ""));
        prayerTimes.put(context.getString(R.string.pn2), sp.getString("sun", ""));
        prayerTimes.put(context.getString(R.string.pn3), sp.getString("duh", ""));
        prayerTimes.put(context.getString(R.string.pn4), sp.getString("asr", ""));
        prayerTimes.put(context.getString(R.string.pn5), sp.getString("magrib", ""));
        prayerTimes.put(context.getString(R.string.pn6), sp.getString("isha", ""));
        prayerTimes.put(context.getString(R.string.qn1), sp.getString("iqsuh", ""));
        prayerTimes.put(context.getString(R.string.qn2), sp.getString("iqsun", ""));
        prayerTimes.put(context.getString(R.string.qn3), sp.getString("iqduh", ""));
        prayerTimes.put(context.getString(R.string.qn4), sp.getString("iqasr", ""));
        prayerTimes.put(context.getString(R.string.qn5), sp.getString("iqmagrib", ""));
        prayerTimes.put(context.getString(R.string.qn6), sp.getString("iqisha", ""));
        prayerTimes.put(context.getString(R.string.pa), sp.getString("phoneAlert", ""));

        prayerTimes = sortByFloatValue(prayerTimes);
        for (Map.Entry<String, String> entry : prayerTimes.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }
        List<String> prayerNames = new ArrayList<>(prayerTimes.keySet());

        boolean nextAlarmFound = false;
        String nameOfPrayerFound = null;
        for (String prayer : prayerNames) {
            if (!isAlarmEnabledForPrayer(context, prayer)) {
                continue;
            }
//           Log.i("999", isAlarmEnabledForPrayer(context, prayer) + "");
            then = getCalendarFromPrayerTime(then, prayerTimes.get(prayer));

            if (then.after(now)) {//وقت الصلاة بعد الان
                // this is the alarm to set
                nameOfPrayerFound = prayer;
                Log.i("999", nameOfPrayerFound);
                nextAlarmFound = true;
                break;
            }
        }

        if (!nextAlarmFound) {
            for (String prayer : prayerNames) {
                if (!isAlarmEnabledForPrayer(context, prayer)) {
                    continue;
                }

                then = getCalendarFromPrayerTime(then, prayerTimes.get(prayer));

                if (then.before(now)) {//وقت الصلاة قبل الان
                    // this is the next day.
                    nameOfPrayerFound = prayer;
                    Log.i("999", " 88" + nameOfPrayerFound);
                    nextAlarmFound = true;
                    then.add(Calendar.DAY_OF_YEAR, 1);
//                    updatePrayTimes(then.YEAR, then.MONTH, then.DAY_OF_MONTH);
//                    Log.i("999887: ",then.DAY_OF_YEAR+"");
                    break;
                }
            }
        }

        if (!nextAlarmFound) {
            return; //something went wrong, abort!
        }

        nameOfPrayerFound = getPrayerNameFromIndex(context, getPrayerIndexFromName(context, nameOfPrayerFound));
        String formatString = "%1$s";
        String text = "";
        if (nameOfPrayerFound.equals(context.getString(R.string.pn1))
                || nameOfPrayerFound.equals(context.getString(R.string.pn3))
                || nameOfPrayerFound.equals(context.getString(R.string.pn4))
                || nameOfPrayerFound.equals(context.getString(R.string.pn5))
                || nameOfPrayerFound.equals(context.getString(R.string.pn6))) {
            spedit.putString("sound", "talib").commit();
            intent.putExtra(EXTRA_ACTION, 0);
            text = String.format(formatString, context.getString(R.string.notification_body, nameOfPrayerFound));
        } else if (nameOfPrayerFound.equals(context.getString(R.string.qn1))
                || nameOfPrayerFound.equals(context.getString(R.string.qn3))
                || nameOfPrayerFound.equals(context.getString(R.string.qn4))
                || nameOfPrayerFound.equals(context.getString(R.string.qn5))
                || nameOfPrayerFound.equals(context.getString(R.string.qn6))) {
            intent.putExtra(EXTRA_ACTION, 1);
            spedit.putString("sound", "iqama").commit();
            text = String.format(formatString, context.getString(R.string.notification_body1, nameOfPrayerFound));
        } else if (nameOfPrayerFound.equals(context.getString(R.string.pa))) {
            spedit.putString("sound", "phone").commit();
            text = String.format(formatString, context.getString(R.string.pa1));
            intent.putExtra(EXTRA_ACTION, 2);
        }
        intent.putExtra(EXTRA_PRAYER_NAME, text);
        intent.putExtra(PRAYER_NAME, nameOfPrayerFound);
        intent.putExtra(EXTRA_PRAYER_TIME, then.getTimeInMillis());
        alarmIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            //lollipop_mr1 is 22, this is only 23 and above
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), alarmIntent);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //JB_MR2 is 18, this is only 19 and above.
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), alarmIntent);
        } else {
            //available since api1
            alarmMgr.set(AlarmManager.RTC_WAKEUP, then.getTimeInMillis(), alarmIntent);
        }
        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, SalaatBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private static Map<String, String> sortByFloatValue(Map<String, String> unsortMap) {
        List<Map.Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        /// Loop the sorted list and put it into a new insertion order Map
        /// LinkedHashMap
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /**
     * Cancels the alarm.
     *
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr == null) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        if (alarmMgr != null) {
            if (alarmIntent == null) {
                Intent intent = new Intent(context, SalaatAlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            }
            alarmMgr.cancel(alarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, SalaatBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelIqamaAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (iqamaAlarmMgr == null) {
            iqamaAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        if (iqamaAlarmMgr != null) {
            if (iqamaAlarmIntent == null) {
                Intent intent = new Intent(context, SalaatAlarmReceiver.class);
                iqamaAlarmIntent = PendingIntent.getBroadcast(context, IQAMA_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            }
            iqamaAlarmMgr.cancel(iqamaAlarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, SalaatBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)

    private int getPrayerIndexFromName(Context context, String prayerName) {
//    String name = prayerName.toLowerCase();
//    char index = name.charAt(0);
        if (prayerName.equalsIgnoreCase(context.getString(R.string.pn1))) {
            return 0;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.pn3))) {
            return 1;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.pn4))) {
            return 2;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.pn5))) {
            return 3;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.pn6))) {
            return 4;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.qn1))) {
            return 5;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.qn3))) {
            return 6;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.qn4))) {
            return 7;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.qn5))) {
            return 8;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.qn6))) {
            return 9;
        } else if (prayerName.equalsIgnoreCase(context.getString(R.string.pa))) {
            return 10;
        } else
            return -1;
    }

    private String getPrayerNameFromIndex(Context context, int prayerIndex) {
        String prayerName = "";
        switch (prayerIndex) {
            case 0:
                prayerName = context.getString(R.string.pn1);
                break;
            case 1:
                prayerName = context.getString(R.string.pn3);
                break;
            case 2:
                prayerName = context.getString(R.string.pn4);
                break;
            case 3:
                prayerName = context.getString(R.string.pn5);
                break;
            case 4:
                prayerName = context.getString(R.string.pn6);
                break;
            case 5:
                prayerName = context.getString(R.string.qn1);
                break;
            case 6:
                prayerName = context.getString(R.string.qn3);
                break;
            case 7:
                prayerName = context.getString(R.string.qn4);
                break;
            case 8:
                prayerName = context.getString(R.string.qn5);
                break;
            case 9:
                prayerName = context.getString(R.string.qn6);
                break;
            case 10:
                prayerName = context.getString(R.string.pa);
                break;

        }
        return prayerName;
    }

    private String getIqamaNameFromIndex(Context context, int iqamaIndex) {
        String iqamaName = null;
        switch (iqamaIndex) {
            case 0:
                iqamaName = context.getString(R.string.qn1);
                break;
            case 1:
                iqamaName = context.getString(R.string.qn3);
                break;
            case 2:
                iqamaName = context.getString(R.string.qn4);
                break;
            case 3:
                iqamaName = context.getString(R.string.qn5);
                break;
            case 4:
                iqamaName = context.getString(R.string.qn6);
                break;
        }
        return iqamaName;
    }

    private boolean isAlarmEnabledForPrayer(Context context, String prayer) {
        if (isFriday()) {
            if (prayer.equalsIgnoreCase(context.getString(R.string.pn3)))
                return false;
            if (prayer.equalsIgnoreCase(context.getString(R.string.qn3)))
                return false;
            else if (prayer.equalsIgnoreCase(context.getString(R.string.pa))) {
                return sp.getBoolean("close_screen", true);
            } else return true;
        } else {
            if (prayer.equalsIgnoreCase(context.getString(R.string.pa))) {
                return sp.getBoolean("close_screen", true);
            } else return true;
        }
    }

    private boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.FRIDAY) {
            Log.e("****/ friday/", " " + true);
            return true;
        } else return false;
    }
//    private boolean isFriday() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
//        Calendar cal = getNextFridayDate();
//        String friday = dateFormat.format(cal.getTime());
//        Calendar calendar=Calendar.getInstance();
//        String today = dateFormat.format(calendar.getTime());
//                Log.e("****/ friday/", " " + friday);
//                Log.e("****/ today/", " " + today);
//        if (today.equals(friday)){
//            return  true;
//        }
//        return  false;
//    }
//    private static Calendar getNextFridayDate() {
//        Calendar calendar = Calendar.getInstance();
//        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
//        int days = Calendar.FRIDAY - weekday;
//        if (days <= 0) {
//            days += 7;
//        }
//        calendar.add(Calendar.DAY_OF_YEAR, days);
//        return calendar;
//    }
//    private boolean isAlarmEnabledForPrayer(Context context, String prayer) {
//        if (prayer.equalsIgnoreCase(context.getString(R.string.pn2)) || prayer.equalsIgnoreCase(context.getString(R.string.qn2))) {
//            return false;
//        }
//        if (prayer.equalsIgnoreCase(context.getString(R.string.pn1)))
//            return sp.getBoolean("notif_onF", true);
//        if ( prayer.equalsIgnoreCase(context.getString(R.string.pn3)))
//            return sp.getBoolean("notif_onD", true);
//        if (prayer.equalsIgnoreCase(context.getString(R.string.pn4)))
//            return sp.getBoolean("notif_onA", true);
//        if ( prayer.equalsIgnoreCase(context.getString(R.string.pn5)))
//            return sp.getBoolean("notif_onM", true);
//        if ( prayer.equalsIgnoreCase(context.getString(R.string.pn6)))
//            return sp.getBoolean("notif_onI", true);

//         else if (prayer.equalsIgnoreCase(context.getString(R.string.qn1)))
//            return sp.getBoolean("iqama_notif_onF", true);
//        if (prayer.equalsIgnoreCase(context.getString(R.string.qn3)))
//            return sp.getBoolean("iqama_notif_onD", true);
//        if ( prayer.equalsIgnoreCase(context.getString(R.string.qn4)))
//            return sp.getBoolean("iqama_notif_onA", true);
//        if ( prayer.equalsIgnoreCase(context.getString(R.string.qn5)))
//            return sp.getBoolean("iqama_notif_onM", true);
//        if ( prayer.equalsIgnoreCase(context.getString(R.string.qn6)))
//            return sp.getBoolean("iqama_notif_onI", true);
//         else if (prayer.equalsIgnoreCase(context.getString(R.string.pa))) {
//            return sp.getBoolean("close_screen", true);
//        } else return true;
//    }

    private Calendar getCalendarFromPrayerTime(Calendar cal, String prayerTime) {
        if (!TextUtils.isEmpty(prayerTime)) {
            String[] time = prayerTime.split(":");
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(time[1]));
            if (time.length > 2)
                cal.set(Calendar.SECOND, Integer.valueOf(time[2]));
            else
                cal.set(Calendar.SECOND, 0);

            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal;

    }

    private String[] calculate(int year, int month, int day) {

        int lat1 = sp.getInt("lat1", -1);
        int lat2 = sp.getInt("lat2", -1);
        int long1 = sp.getInt("long1", -1);
        int long2 = sp.getInt("long2", -1);
        Hijri_Cal_Tools.calculation((double) lat1, (double) lat2, (double) long1, (double) long2,
                year, month, day);
//        Log.i("init()", lat1 + "," + lat2 + "," + long1 + "," + long2 + "," + year + "," +
//                month + "," + day);
        String cfajr = Hijri_Cal_Tools.getFajer();
        String csunrise = Hijri_Cal_Tools.getSunRise();
        String cdhohr = Hijri_Cal_Tools.getDhuhur();
        String casr = Hijri_Cal_Tools.getAsar();
        String cmaghrib = Hijri_Cal_Tools.getMagrib();
        String cisha = Hijri_Cal_Tools.getIshaa();
        String[] prayTimes = {cfajr, csunrise, cdhohr, casr, cmaghrib, cisha};
        return prayTimes;
    }

}
