package com.newsolution.almhrab.scheduler;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.newsolution.almhrab.Activity.MainActivity;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.Constants;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.R;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SalaatSchedulingService extends IntentService implements Constants, MediaPlayer.OnCompletionListener {
    private boolean isAlarmEnabledForPrayer=true;

    public SalaatSchedulingService() {
        super("SchedulingService");
    }

    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    // The Google home page URL from which the app fetches content.
    // You can find a list of other Google domains with possible doodles here:
    // http://en.wikipedia.org/wiki/List_of_Google_domains
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    long[] pattern = {1200, 1200, 1200, 1200, 1200, 1200, 1200, 1200, 1200};
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    MediaPlayer mp;

    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        int action = intent.getIntExtra(EXTRA_ACTION, -1);
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTimeInMillis(System.currentTimeMillis());
        String formatString = "%2$tk:%2$tM %1$s";
        mp = new MediaPlayer();
        String prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME);
        String prayName = intent.getStringExtra(PRAYER_NAME);
        isAlarmEnabledForPrayer=isAlarmEnabledForPrayer(getApplicationContext(), prayName);
        if (action == 0) {
            if (!isAlarmEnabledForPrayer)
           sendNotification(String.format(formatString, prayerName, now),  prayerName);
            else {
                if (TextUtils.isEmpty(sp.getString("uriAthan",""))) {
//                    spedit.putString("sound", "talib").commit();
                PlaySound.play(getBaseContext(), "talib");
                }else {
                    Log.i("****",sp.getString("uriAthan","")+" **-");
                    PlaySound.playSDCard(getBaseContext(),sp.getString("uriAthan",""),"talib");
                }
                try {
                    mp.setOnCompletionListener(this);
                } catch (Exception e) {
                    e.getMessage();
                    mp = null;
                }
            }
        } else if (action == 1) {
            if (!isAlarmEnabledForPrayer)
                sendNotification(String.format(formatString, prayerName, now),  prayerName);
            else {
                if (TextUtils.isEmpty(sp.getString("uriIqama",""))) {
//                    spedit.putString("sound", "iqama").commit();
                    PlaySound.play(getBaseContext(),"iqama");
                }else {
//                    spedit.putString("sound", "iqama").commit();
                    PlaySound.playSDCard(getBaseContext(),sp.getString("uriIqama",""),"iqama");
                }

            try {
                mp.setOnCompletionListener(this);
            } catch (Exception e) {
                e.getMessage();
                mp = null;
            }}
        }   else if (action == 2) {
            if (isAlarmEnabledForPrayer){
                spedit.putString("sound","phone").commit();
            if (sp.getBoolean("close_voice", true)){
                PlaySound.play(getBaseContext(),"phone");
            try {
                mp.setOnCompletionListener(this);
            } catch (Exception e) {
                e.getMessage();
                mp = null;
            }
        }}
        }
//        Log.i("****:action= ",action+" sound:"+sp.getString("sound"," ll"));

        // Release the wake lock provided by the BroadcastReceiver.
        SalaatAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }

    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String title, String msg) {

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setAutoCancel(true)
                       // .setVibrate(pattern)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true);
            Uri sound = null;
              sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound);
            mBuilder.setSound(sound);
        mBuilder.setContentIntent(contentIntent);
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            Uri path = Uri.parse("android.resource://"+getPackageName()+"/raw/sound.mp3");
//            RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(),
//                    RingtoneManager.TYPE_NOTIFICATION, path);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), path);
//            r.play();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        mp = null;
    }
    private boolean isAlarmEnabledForPrayer(Context context, String prayer) {
        if (prayer.equalsIgnoreCase(context.getString(R.string.pn2)) || prayer.equalsIgnoreCase(context.getString(R.string.qn2))) {
            return false;
        }
        if (prayer.equalsIgnoreCase(context.getString(R.string.pn1)))
            return sp.getBoolean("notif_onF", true);
        if ( prayer.equalsIgnoreCase(context.getString(R.string.pn3)))
            return sp.getBoolean("notif_onD", true);
        if (prayer.equalsIgnoreCase(context.getString(R.string.pn4)))
            return sp.getBoolean("notif_onA", true);
        if ( prayer.equalsIgnoreCase(context.getString(R.string.pn5)))
            return sp.getBoolean("notif_onM", true);
        if ( prayer.equalsIgnoreCase(context.getString(R.string.pn6)))
            return sp.getBoolean("notif_onI", true);

        else if (prayer.equalsIgnoreCase(context.getString(R.string.qn1)))
            return sp.getBoolean("iqama_notif_onF", true);
        if (prayer.equalsIgnoreCase(context.getString(R.string.qn3)))
            return sp.getBoolean("iqama_notif_onD", true);
        if ( prayer.equalsIgnoreCase(context.getString(R.string.qn4)))
            return sp.getBoolean("iqama_notif_onA", true);
        if ( prayer.equalsIgnoreCase(context.getString(R.string.qn5)))
            return sp.getBoolean("iqama_notif_onM", true);
        if ( prayer.equalsIgnoreCase(context.getString(R.string.qn6)))
            return sp.getBoolean("iqama_notif_onI", true);
        else if (prayer.equalsIgnoreCase(context.getString(R.string.pa))) {
            return sp.getBoolean("close_screen", true);
        } else return true;
    }

}
