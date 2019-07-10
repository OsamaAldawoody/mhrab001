package com.newsolution.almhrab.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import org.json.JSONObject;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChoosePriority extends Activity {
    private Activity activity;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private ProgressBar progress;
    public static String LOG_TAG = "//mhrab";
    private String serverTime;
    public String cfajr, csunrise, cdhohr, casr, cmaghrib, cisha;
    public Calendar today = Calendar.getInstance();
    double day = today.get(Calendar.DAY_OF_MONTH);
    double month = today.get(Calendar.MONTH) + 1;
    double year = today.get(Calendar.YEAR);
    DBOperations DBO;
    private int cityId;
    String[] prayTimes;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/neosansarabic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_choose_priority);
        activity = this;
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        DBO = new DBOperations(activity);
        findViews();
    }

    private void findViews() {
        TextView tvًWith = (TextView) findViewById(R.id.tvًWith);
        TextView tvًWithout = (TextView) findViewById(R.id.tvًWithout);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B57C2F"), android.graphics.PorterDuff.Mode.MULTIPLY);
        tvًWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spedit.putInt("priority", 1).commit();
                syncData();
            }
        });
        tvًWithout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spedit.putInt("priority", 2).commit();
                DBOperations db = new DBOperations(activity);
                db.open();
                boolean ifExist = db.ifSettingsExists();
                db.close();
                if (ifExist)
                    go();
                else
                    Utils.showAlert(activity, getString(R.string.alert), getString(R.string.alert_msg));
            }
        });
    }

    private void syncData() {
        if (Utils.isOnline(activity)) {
            progress.setVisibility(View.VISIBLE);
            new UpdateAsync().execute();
        } else {
            progress.setVisibility(View.GONE);
            Utils.showCustomToast(activity, getString(R.string.no_internet));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateAsync extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            int id = sp.getInt("masjedId", -1);
            String GUID = sp.getString("masjedGUID", "");
            String lastUpdate = sp.getString(Utils.LASTUPDATE, "20170701000000");
            String DeviceNo = sp.getString(Utils.DeviceNo, "");
            JSONObject result = WS.syncData(id, GUID, lastUpdate, DeviceNo);

            Log.i(LOG_TAG, "Sync service Return data : " + result.toString());

            if (result.optBoolean("Status")) {
                serverTime = result.optLong("ResultNumber ") + "";
                return WS.InsertDataToDB(1, activity, result);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            Log.d(LOG_TAG, "End of Sync Service");
            if (status) {
                spedit.putString(Utils.LASTUPDATE, serverTime).commit();
                spedit.putBoolean("isFirstLunch", false).commit();
                Log.d(LOG_TAG, "End of Sync Service Successfuly");
                getPrayerTimes();

            } else if (sp.getBoolean("isFirstLunch", true)) {
                Utils.showCustomToast(activity, getString(R.string.error));
            } else {
                go();
            }
        }

    }

    private String[] calculate() {
        DBO.open();
        City city = DBO.getCityById(cityId);
        DBO.close();
        double lat1 = sp.getInt("lat1", city.getLat1());
        double lat2 = sp.getInt("lat2", city.getLat2());
        double long1 = sp.getInt("long1", city.getLon1());
        double long2 = sp.getInt("long2", city.getLon2());
        Hijri_Cal_Tools.calculation((double) lat1, (double) lat2, (double) long1, (double) long2,
                year, month, day);

        cfajr = Hijri_Cal_Tools.getFajer();
        csunrise = Hijri_Cal_Tools.getSunRise();
        cdhohr = Hijri_Cal_Tools.getDhuhur();
        casr = Hijri_Cal_Tools.getAsar();
        cmaghrib = Hijri_Cal_Tools.getMagrib();
        cisha = Hijri_Cal_Tools.getIshaa();
        return new String[]{cfajr, csunrise, cdhohr, casr, cmaghrib, cisha};
    }

    public void getPrayerTimes() {
        try {
            cityId = sp.getInt("cityId", 1);
            prayTimes = calculate();
            if (prayTimes.length > 0) {
                cfajr = prayTimes[0];
                csunrise = prayTimes[1];
                cdhohr = prayTimes[2];
                casr = prayTimes[3];
                cmaghrib = prayTimes[4];
                cisha = prayTimes[5];
                spedit.putString("suh", cfajr).commit();
                spedit.putString("sun", csunrise).commit();
                spedit.putString("duh", cdhohr).commit();
                spedit.putString("asr", casr).commit();
                spedit.putString("magrib", cmaghrib).commit();
                spedit.putString("isha", cisha).commit();
            }
            go();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void go() {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
