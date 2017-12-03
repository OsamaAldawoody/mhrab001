package com.newsolution.almhrab;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by aelde on 12/9/2015.
 */
public class GlobalVars extends Application {
    private String themeId,mosquename,fajr,sunrise,dhohr,asr,maghrib,isha
            ,iqSunrise,iqfajr,iqdhohr,iqasr,iqmaghrib,iqisha,nextPray,lang,city,country;
    ArrayList<String> mats = null;
    ArrayList<String> advs =  null;
    ArrayList<String> acts =  null;

    public int waitTime=2000;

    public int IsData,IsLoginData,mosqueId;
    public Double lat,lng;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/neosansarabic.ttf")//battar  droidkufi_regular droid_sans_arabic neosansarabic //mcs_shafa_normal
                    .setFontAttrId(R.attr.fontPath)
                    .build());
    }

    public void  setLang(String Lang) {
        lang=Lang;
    }
    public String getLang() {
        return lang;
    }

    public void  setmosqueId(int mid) {
        mosqueId=mid;
    }
    public int getmosqueId() {
        return mosqueId;
    }

    public void  setIsLoginData(int v) {
        IsLoginData=v;
    }
    public int getIsLoginData() {
        return IsLoginData;
    }

    public void  setIsData() {
        IsData=1;
    }
    public int getIsData() {
        return IsData;
    }

    public void setMainData(String cmosquename) {
        Log.e("saving Data : ","saving MainData : "+cmosquename);
        mosquename = cmosquename;
    }
    public String getMainData() {
       // Log.e("getting Data : ","getting MainData : "+mosquename);
        return mosquename;
    }
    public void saveLoc(Double la, Double ln, String ci , String co) {
       //Log.e("saving location : ","saving location : "+la+" - "+ln);
        lat = la;
        lng = ln;
        city=ci;
        country=co;
    }
    public String[] getLoc() {
    //    Log.e("getting location : ","getting location : "+lat+" - "+lng+" - "+city+" - "+country);
        String[] location={""+lat,""+lng,city,country};
        return location;
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
    public String[] getPrayTimes() {
       // Log.e("getting Data : ","getting Pray Times");
        String[] prayTimes={fajr,sunrise,dhohr,asr,maghrib,isha};
        return prayTimes;
    }


    public void setMousqeSettings(String ifajr,String iSunrise,String idhohr, String iasr, String imaghrib, String iisha) {
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
        String[] iqamaTimes={iqfajr,iqSunrise,iqdhohr,iqasr,iqmaghrib,iqisha};
        return iqamaTimes;
    }
    public void setNextPray(String npray) {
        //Log.e("saving Data : ","saving NextPray : "+npray);
        nextPray = npray;
    }
    public String getNextPray() {
      //  Log.e("getting Data : ","getting NextPray : "+nextPray);
        return nextPray;
    }

}
