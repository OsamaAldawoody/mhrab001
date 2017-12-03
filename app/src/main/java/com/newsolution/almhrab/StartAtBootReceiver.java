package com.newsolution.almhrab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.newsolution.almhrab.Activity.MainActivity;
import com.newsolution.almhrab.Activity.Splash;

/**
 * Created by hp on 10/22/2017.
 */

public class StartAtBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent activityIntent = new Intent(context, Splash.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
    }
}
