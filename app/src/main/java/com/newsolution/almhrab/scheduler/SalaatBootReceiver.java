package com.newsolution.almhrab.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.newsolution.almhrab.Helpar.Utils;


/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class SalaatBootReceiver extends BroadcastReceiver {
    SalaatAlarmReceiver salaatAlarm = new SalaatAlarmReceiver();
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        sp = context.getSharedPreferences(Utils.PREFS, context.MODE_PRIVATE);
        spedit = sp.edit();
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                salaatAlarm.setAlarm(context);


        } else if (action.equals("android.intent.action.TIMEZONE_CHANGED") ||
                action.equals("android.intent.action.TIME_SET") ||
                action.equals("android.intent.action.MY_PACKAGE_REPLACED")) {
            // Our location could have changed, which means time calculations may be different
            // now so cancel the alarm and set it again.
                salaatAlarm.cancelAlarm(context);
                salaatAlarm.setAlarm(context);

        }
    }
}
//END_INCLUDE(autostart)
