package com.newsolution.almhrab.scheduler;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.newsolution.almhrab.scheduler.SleepService;

/**
 * Created by hp on 2/7/2018.
 */

public class SleepBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.e("**//Broadcast ","on");
        Intent intentS = new Intent(context, SleepService.class);
        context.startService(intentS);

    }
}
