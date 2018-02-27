package com.newsolution.almhrab.scheduler;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.Helpar.WakeLocker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by hp on 10/1/2016.
 */
public class SleepService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    SharedPreferences sp;
    SharedPreferences.Editor spedit;
    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("**//service ", "on");
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        startRepeatingTask();


        return START_STICKY;
    }

    private void startRepeatingTask() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

// This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Calendar now = Calendar.getInstance(TimeZone.getDefault());
                now.setTimeInMillis(System.currentTimeMillis());
                Calendar then = Calendar.getInstance(TimeZone.getDefault());
                then.setTimeInMillis(System.currentTimeMillis());
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
                if (sp.getBoolean("sleep", false)) {
                    Log.e("**//service ", "on start");
                    try {
                        Date mToday = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",new Locale("en"));
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",new Locale("en"));

                        String curTime = sdf.format(mToday);
                        Date cur = sdf.parse(curTime);
                        Date date = new Date();
                        Calendar calendarCurrent = Calendar.getInstance();
                        calendarCurrent.setTime(date);
//                        calendarCurrent.set(Calendar.HOUR_OF_DAY, cur.getHours());
//                        calendarCurrent.set(Calendar.MINUTE, cur.getMinutes());
//                        calendarCurrent.set(Calendar.SECOND, 0);// for 0 sec
                        // System.out.println("***:calendarEnd added "+calendarCurrent.getTime());
                        Log.e("**//calendarCurrent ",""+calendarCurrent.getTime());


                        String curTime1 = df.format(calendarCurrent.getTime());
                        Date userDate = df.parse(curTime1);
                        Date start = df.parse(sp.getString("startTime", userDate + ""));
                        Date end = df.parse(sp.getString("endTime", userDate + ""));
                        Log.e("**//curTime", userDate.toString());
                        Log.e("**//start", start.toString());
                        Log.e("**//end", end.toString());
                        if ((userDate.after(start) || userDate.equals(start)) && userDate.before(end)) {
                            Log.e("**//result", "falls between start and end , sleep ");
                            lock.reenableKeyguard();
                            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 1000);
//                    return;
                        } else {
                            WakeLocker.acquire(getApplicationContext());
                            WakeLocker.release();
                            lock.disableKeyguard();
                            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, (3 * 24 * 60 * 60 * 1000));
                            Log.e("**//result", "does not fall between start and end , wake ");
                        }
                    } catch (Exception e) {
                        Log.e("**//exception", e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.e("**//result", "sleep off");
                    WakeLocker.acquire(getApplicationContext());
                    WakeLocker.release();
                    lock.disableKeyguard();
                }
            }
        }, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

}
