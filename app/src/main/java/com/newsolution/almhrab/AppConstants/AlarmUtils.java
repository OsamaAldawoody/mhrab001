package com.newsolution.almhrab.AppConstants;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.ArrayAdapter;

import com.newsolution.almhrab.R;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class AlarmUtils {
    public static Uri getRandomRingtone(Context context) {
        Uri alert;
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(4);
        int count = ringtoneManager.getCursor().getCount();
        int attempts = 0;
        do {
            alert = ringtoneManager.getRingtoneUri(((int) Math.random()) * (count + 1));
            if (alert != null) {
                break;
            }
            attempts++;
        } while (attempts < 5);
        return alert;
    }

    public static Uri getAlarmRingtoneUri() {
        Uri alert = RingtoneManager.getDefaultUri(4);
        if (alert != null) {
            return alert;
        }
        alert = RingtoneManager.getDefaultUri(2);
        if (alert == null) {
            return RingtoneManager.getDefaultUri(1);
        }
        return alert;
    }

    public static int getAlarmVolumeFromPercentage(AudioManager audioManager, int audioStream, float percentage) {
        return (int) Math.ceil(((double) audioManager.getStreamMaxVolume(audioStream)) * (((double) percentage) / 100.0d));
    }

    public static Map<String, Uri> getRingtones(Activity activity) {
        RingtoneManager manager = new RingtoneManager(activity);
        manager.setType(1);
        Cursor cursor = manager.getCursor();
        Map<String, Uri> list = new LinkedHashMap();
        while (cursor.moveToNext()) {
            list.put(cursor.getString(1), manager.getRingtoneUri(cursor.getPosition()));
        }
        return list;
    }

//    public static void getRingtonesDialog(Activity activity, Collection<String> items, int selected, OnClickListener itemClickListener, OnClickListener okClickListener, OnClickListener cancelClickListener) {
//        Builder builderSingle = new Builder(activity);
//        builderSingle.setTitle("Select Ringtone");
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(activity, 17367058);
//        arrayAdapter.addAll(items);
//        builderSingle.setNegativeButton(activity.getString(R.string.cancel_delete), cancelClickListener);
//        builderSingle.setPositiveButton(activity.getString(R.string.ok), okClickListener);
//        builderSingle.setSingleChoiceItems(arrayAdapter, selected, itemClickListener);
//        builderSingle.setCancelable(false);
//        builderSingle.show();
//    }
}
