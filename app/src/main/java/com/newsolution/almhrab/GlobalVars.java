package com.newsolution.almhrab;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by aelde on 12/9/2015.
 */
public class GlobalVars extends Application {
    private String fajr, sunrise, dhohr, asr, maghrib, isha,
            iqSunrise, iqfajr, iqdhohr, iqasr, iqmaghrib, iqisha, nextPray, lang, city, country;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/neosansarabic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public void setPrayTimes(String cfajr, String csunrise, String cdhohr, String casr, String cmaghrib, String cisha) {
        //  Log.e("saving Data : ","saving Pray Times");
        fajr = cfajr;
        sunrise = csunrise;
        dhohr = cdhohr;
        asr = casr;
        maghrib = cmaghrib;
        isha = cisha;
    }


    public void setMousqeSettings(String ifajr, String iSunrise, String idhohr, String iasr, String imaghrib, String iisha) {
//        Log.e("saving Data : ","saving MousqeSittings");
        iqfajr = ifajr;
        iqSunrise = iSunrise;
        iqdhohr = idhohr;
        iqasr = iasr;
        iqmaghrib = imaghrib;
        iqisha = iisha;
    }

    public String[] getMousqeSettings() {
//        Log.e("getting Data : ","getting MousqeSittings");
        String[] iqamaTimes = {iqfajr, iqSunrise, iqdhohr, iqasr, iqmaghrib, iqisha};
        return iqamaTimes;
    }

    public void setNextPray(String npray) {
        //Log.e("saving Data : ","saving NextPray : "+npray);
        nextPray = npray;
    }

}
