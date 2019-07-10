package com.newsolution.almhrab.scheduler;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Helpar.WakeLocker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SleepService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    SharedPreferences sp;
    SharedPreferences.Editor spedit;
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        startRepeatingTask();


        return START_STICKY;
    }

    private void startRepeatingTask() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Calendar now = Calendar.getInstance(TimeZone.getDefault());
                now.setTimeInMillis(System.currentTimeMillis());
                Calendar then = Calendar.getInstance(TimeZone.getDefault());
                then.setTimeInMillis(System.currentTimeMillis());
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
                if (sp.getBoolean("sleep", false)) {
                    try {
                        Date mToday = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);

                        String curTime = sdf.format(mToday);
                        Date cur = sdf.parse(curTime);
                        Date date = new Date();
                        Calendar calendarCurrent = Calendar.getInstance();
                        calendarCurrent.setTime(date);

                        Log.e("**//calendarCurrent ", "" + calendarCurrent.getTime());


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
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
    }


}
