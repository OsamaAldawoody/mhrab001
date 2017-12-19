package com.newsolution.almhrab.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.newsolution.almhrab.AppConfig;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash extends Activity {

    private Location myLocation = null;
    private boolean gpsPrayTimes = false;
    private Activity activity;
    public String cfajr, csunrise, cdhohr, casr, cmaghrib, cisha;
    public String nextPray;
    public Calendar today = Calendar.getInstance();
    double day = today.get(Calendar.DAY_OF_MONTH);
    double month = today.get(Calendar.MONTH) + 1;
    double year = today.get(Calendar.YEAR);
    private double long1, long2;
    private double lat1, lat2;
    DBOperations DBO;
    private int cityId;
    GlobalVars globalVariable;
    String[] prayTimes;
    String[] iqamaTimes;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    public static String LOG_TAG = "//*mhrab";
    private String serverTime;
    private ProgressBar progress;
    private String[] mosquSettings;
    private OptionSiteClass settings;
    private boolean isFajrEkamaIsTime, isDhuhrEkamaIsTime, isAlShrouqEkamaIsTime, ishaEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;
    public static long ConnectTimeout = 30000;
    public static long ExitedTime = 10;
    public static long ScanRunTime = 60000;
    private int REQUEST_PERMISSIONS = 100;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B57C2F"), android.graphics.PorterDuff.Mode.MULTIPLY);
        activity = this;
        DBO = new DBOperations(this);
        DBO.createDatabase();
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        globalVariable = (GlobalVars) getApplicationContext();
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
//        String dms = Hijri_Cal_Tools.calDMS(58.51861);
//        Log.i("dms : ", dms);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
//        Toast.makeText(activity, "width= " + width + " : height= " + height, Toast.LENGTH_LONG).show();

        String deviceId = getDeviceId(getApplicationContext());//tManager.getDeviceId();
        spedit.putString(AppConst.DeviceNo, deviceId).commit();
//        Log.i("/////* DeviceNo", deviceId);
//        startApp();
        settingPermission();
//        askForPermissions(new String[]{
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                        android.Manifest.permission.READ_PHONE_STATE,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                        android.Manifest.permission.WRITE_SETTINGS,
////                        android.Manifest.permission.DISABLE_KEYGUARD,
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE
//                },
//                REQUEST_PERMISSIONS);

    }
    public void settingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }else  askForPermissions(new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        android.Manifest.permission.WRITE_SETTINGS,
//                        android.Manifest.permission.DISABLE_KEYGUARD,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSIONS);
        }else  askForPermissions(new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.RECORD_AUDIO,
//                        android.Manifest.permission.WRITE_SETTINGS,
//                        android.Manifest.permission.DISABLE_KEYGUARD,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                },
                REQUEST_PERMISSIONS);

    }
    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
//          Log.i("***9 "," "+permissionsToRequest.size());
            ActivityCompat.requestPermissions(activity,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        } else isBluetooth4();
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                new AlertDialog.Builder(
                        activity)
                        .setTitle(getString(R.string.alert))
                        .setCancelable(false)
                        .setMessage(getString(R.string.permission_rationale))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                askForPermissions(new String[]{
                                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                android.Manifest.permission.READ_PHONE_STATE,
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                android.Manifest.permission.CAMERA,
                                                android.Manifest.permission.RECORD_AUDIO,
//                                                android.Manifest.permission.WRITE_SETTINGS,
//                                                android.Manifest.permission.DISABLE_KEYGUARD,
                                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                                        },
                                        REQUEST_PERMISSIONS);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_delete), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        }).create().show();
            } else isBluetooth4();
        }
    }

    public void isBluetooth4() {
        if (!getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this, getString(R.string.lan_6), 0).show();
            finish();
        }
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.lan_7), 0).show();
            finish();
        }
        if (mBluetoothAdapter.isEnabled()) {
            isNotificationEnabled();
        } else {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1) {
            return;
        }
        if (resultCode == -1) {
//            Toast.makeText(this, getString(R.string.lan_8), 0).show();
            isNotificationEnabled();
        } else if (resultCode == 0) {
            Toast.makeText(this, getString(R.string.lan_9), 0).show();
            finish();
         } else if (requestCode == 200&&resultCode==RESULT_OK) {
            askForPermissions(new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.RECORD_AUDIO,
//                        android.Manifest.permission.WRITE_SETTINGS,
//                        android.Manifest.permission.DISABLE_KEYGUARD,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSIONS);
        } else if (requestCode == 200&&resultCode==RESULT_CANCELED) {
            finish();
        }
        }

    public void isNotificationEnabled() {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService("appops");
            ApplicationInfo appInfo = getApplicationInfo();
            String pkg = getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", new Class[]{Integer.TYPE, Integer.TYPE, String.class});
                int value = ((Integer) appOpsClass.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue();
                if (((Integer) checkOpNoThrowMethod.invoke(appOpsManager, new Object[]{Integer.valueOf(value), Integer.valueOf(uid), pkg})).intValue() == 0) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
            }
//        } else if (Build.VERSION.SDK_INT <= 17) {
//            result = false;
        } else {
            result = true;
        }
        if (result) {
            CheckAppConfig();
        } else {
            new AlertDialog.Builder(this).setTitle(R.string.lan_2).setMessage(R.string.lan_194)
                    .setPositiveButton(R.string.lan_79, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent("android.settings.SETTINGS"));
                            finish();
                        }
                    }).setCancelable(false).show();
        }
    }

    public void CheckAppConfig() {
        if (AppConfig.IsExistSDCard() && AppConfig.IsReadWritePermission()) {
            startApp();
        } else {
            new AlertDialog.Builder(this).setTitle(getString(R.string.lan_2))
                    .setMessage(getString(R.string.lan_10))
                    .setPositiveButton(getString(R.string.lan_79), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false).show();
        }
    }

    public String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId != null) {
            return androidId;
        } else {
            return android.os.Build.SERIAL;
        }
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
//                spedit.putInt("masjedId",-1).commit();
                if (sp.getInt("masjedId", -1) != -1) {
                    getPrayerTimes();
                } else {
                    intent = new Intent(activity, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }

    private String[] calculate() {
        DBO.open();
        City city = DBO.getCityById(cityId);
        settings = DBO.getSettings();
        globalVariable.setMousqeSettings(settings.getFajrEkama() + "", settings.getAlShrouqEkama() + "",
                settings.getDhuhrEkama() + "", settings.getAsrEkama() + "", settings.getMagribEkama() + "",
                settings.getIshaEkama() + "");
        mosquSettings = globalVariable.getMousqeSettings();
        spedit.putString("ifajer", mosquSettings[0]).commit();
        spedit.putString("ishroq", mosquSettings[1]).commit();
        spedit.putString("idhor", mosquSettings[2]).commit();
        spedit.putString("iaser", mosquSettings[3]).commit();
        spedit.putString("imagrib", mosquSettings[4]).commit();
        spedit.putString("iisha", mosquSettings[5]).commit();
        spedit.putInt("hijriDiff", settings.getDateHijri()).commit();

        //        lat1 = city.getLat1();
//        lat2 = city.getLat2();
//        long1 = city.getLon1();
//        long2 = city.getLon2();
        DBO.close();
        lat1 = sp.getInt("lat1", city.getLat1());
        lat2 = sp.getInt("lat2", city.getLat2());
        long1 = sp.getInt("long1", city.getLon1());
        long2 = sp.getInt("long2", city.getLon2());
        Hijri_Cal_Tools.calculation((double) lat1, (double) lat2, (double) long1, (double) long2,
                year, month, day);
//        Log.i("init()", lat1 + "," + lat2 + "," + long1 + "," + long2 + "," + year + "," +
//                month + "," + day);
        cfajr = Hijri_Cal_Tools.getFajer();
        csunrise = Hijri_Cal_Tools.getSunRise();
        cdhohr = Hijri_Cal_Tools.getDhuhur();
        casr = Hijri_Cal_Tools.getAsar();
        cmaghrib = Hijri_Cal_Tools.getMagrib();
        cisha = Hijri_Cal_Tools.getIshaa();
        String[] prayTimes = {cfajr, csunrise, cdhohr, casr, cmaghrib, cisha};
        return prayTimes;
    }

    public void getPrayerTimes() {
        Context context;
        boolean isName = false;
        try {
            cityId = sp.getInt("cityId", 1);
            prayTimes = calculate();
            if (prayTimes.length > 0) {
                isName = true;
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
                isFajrEkamaIsTime = settings.isFajrEkamaIsTime();
                isAlShrouqEkamaIsTime = settings.isAlShrouqEkamaIsTime();
                isDhuhrEkamaIsTime = settings.isDhuhrEkamaIsTime();
                isAsrEkamaIsTime = settings.isAsrEkamaIsTime();
                isMagribEkamaIsTime = settings.isMagribEkamaIsTime();
                ishaEkamaIsTime = settings.ishaEkamaIsTime();
                if (isFajrEkamaIsTime)
                    spedit.putString("iqsuh", settings.getFajrEkamaTime() + ":00").commit();
                else
                    spedit.putString("iqsuh", Utils.getIqama(cfajr, mosquSettings[0])).commit();
                if (isAlShrouqEkamaIsTime)
                    spedit.putString("iqsun", settings.getAlShrouqEkamaTime() + ":00").commit();
                else
                    spedit.putString("iqsun", Utils.getIqama(csunrise, mosquSettings[1])).commit();
//                spedit.putString("iqsun", csunrise).commit();
                if (isDhuhrEkamaIsTime)
                    spedit.putString("iqduh", settings.getDhuhrEkamaTime() + ":00").commit();
                else
                    spedit.putString("iqduh", Utils.getIqama(cdhohr, mosquSettings[2])).commit();
                if (isAsrEkamaIsTime)
                    spedit.putString("iqasr", settings.getAsrEkamaTime() + ":00").commit();
                else
                    spedit.putString("iqasr", Utils.getIqama(casr, mosquSettings[3])).commit();
                if (isMagribEkamaIsTime)
                    spedit.putString("iqmagrib", settings.getMagribEkamaTime() + ":00").commit();
                else
                    spedit.putString("iqmagrib", Utils.getIqama(cmaghrib, mosquSettings[4])).commit();
                if (ishaEkamaIsTime)
                    spedit.putString("iqisha", settings.getIshaEkamaTime() + ":00").commit();
                else
                    spedit.putString("iqisha", Utils.getIqama(cisha, mosquSettings[5])).commit();

                spedit.putInt("hijriDiff", settings.getDateHijri()).commit();

//                Log.e("+++", prayTimes[1]  +" : "+sp.getString("iqsun",""));
//                Log.e("+++", prayTimes[2]  +" : "+sp.getString("iqduh",""));
//                Log.e("+++", prayTimes[3]  +" : "+sp.getString("iqasr",""));
//                Log.e("+++", prayTimes[4]  +" : "+sp.getString("iamagrib",""));
//                Log.e("+++", prayTimes[5]  +" : "+sp.getString("iqisha",""));

            }
            if (isName) {
                globalVariable.setPrayTimes(cfajr, csunrise, cdhohr, casr, cmaghrib, cisha);
                checkNextPray();
                if (sp.getBoolean("isFirstLunch", true)) {
                    if (sp.getInt("priority", 0) == 0) {
                        Intent intent = new Intent(activity, ChoosePriority.class);
                        startActivity(intent);
                        finish();
                    } else if (sp.getInt("priority", 0) == 1) {
                        if (Utils.isOnline(activity)) {
//                        spedit.putInt("priority", 1).commit();
                            getPriority();
                        } else {
//                        Log.e("eeeee+", " 7775555");
                            Utils.showAlert(activity, getString(R.string.alert), getString(R.string.alert_msg));
                        }
                    } else if (sp.getInt("priority", 0) == 2) {
                        new AlertDialog.Builder(activity).setTitle(getString(R.string.alert))
                                .setMessage(getString(R.string.alert_msg))
                                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(activity, ChoosePriority.class);
                                        startActivity(intent);
                                        finish();
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
//                        Utils.showAlert(activity, getString(R.string.alert), getString(R.string.alert_msg));
                    }
                } else {
                    getPriority();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPriority() {
//        Log.e("+++", sp.getInt("priority", 0) + " 5555");
        if (sp.getInt("priority", 0) == 0) {
            Intent intent = new Intent(activity, ChoosePriority.class);
            startActivity(intent);
            finish();
        } else if (sp.getInt("priority", 0) == 1) {
//            Log.e("+++", sp.getInt("priority", 0) + " 777");
            syncData();
        } else {
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void checkNextPray() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        String ncfajr = cfajr.replace("ص", "").replace("م", "");
        String ncsunrise = csunrise.replace("ص", "").replace("م", "");
        String ncdhohr = cdhohr.replace("ص", "").replace("م", "");
        String ncasr = casr.replace("ص", "").replace("م", "");
        String ncmaghrib = cmaghrib.replace("ص", "").replace("م", "");
        String ncisha = cisha.replace("ص", "").replace("م", "");
        try {
            String t1 = ncfajr + ":00";
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(t1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(time1);

            String t2 = ncdhohr + ":00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(t2);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(time2);
            String t3 = ncasr + ":00";
            Date time3 = new SimpleDateFormat("HH:mm:ss").parse(t3);
            Calendar c3 = Calendar.getInstance();
            c3.setTime(time3);
            String t4 = ncmaghrib + ":00";
            Date time4 = new SimpleDateFormat("HH:mm:ss").parse(t4);
            Calendar c4 = Calendar.getInstance();
            c4.setTime(time4);
            String t5 = ncisha + ":00";
            Date time5 = new SimpleDateFormat("HH:mm:ss").parse(t5);
            Calendar c5 = Calendar.getInstance();
            c5.setTime(time5);

            String timeNow = hour + ":" + minute + ":00";
            Date d = new SimpleDateFormat("HH:mm:ss").parse(timeNow);
            Calendar cnow = Calendar.getInstance();
            cnow.setTime(d);
            Date now = cnow.getTime();

            if (now.before(c1.getTime())) {
                nextPray = "fajr";
                spedit.putString("next_adan", "fajr").commit();
            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                nextPray = "dhuhr";
                spedit.putString("next_adan", "dhuhr").commit();
            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                nextPray = "asr";
                spedit.putString("next_adan", "asr").commit();

            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                nextPray = "magrib";
                spedit.putString("next_adan", "magrib").commit();
            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                nextPray = "isha";
                spedit.putString("next_adan", "isha").commit();
            } else if (now.after(c5.getTime())) {
                nextPray = "fajr";
                spedit.putString("next_adan", "fajr").commit();
            }
//            Log.i("///*now", now + "");
//            Log.i("///*nextPray", nextPray + "");
//            Toast.makeText(activity, "now: "+now+"", Toast.LENGTH_LONG).show();
//            Toast.makeText(activity, nextPray+" 999", Toast.LENGTH_LONG).show();
            globalVariable.setNextPray(nextPray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void syncData() {
        if (Utils.isOnline(activity)) {
//            Log.e("+++", sp.getInt("priority", 0) + " 8888");
            progress.setVisibility(View.VISIBLE);
            new UpdateAsync().execute();
        } else {
//            Log.e("+++ //", sp.getInt("priority", 0) + " 9999");
            progress.setVisibility(View.INVISIBLE);
            Utils.showCustomToast(activity, getString(R.string.no_internet));
            startActivity(new Intent(activity, ChoosePriority.class));
            finish();

        }

    }

    private class UpdateAsync extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            long wsstartDate = new Date().getTime();
//            Log.d(LOG_TAG, "Sync service Start : " + wsstartDate);
            int id = sp.getInt("masjedId", -1);
            String GUID = sp.getString("masjedGUID", "");
            String lastUpdate = sp.getString(AppConst.LASTUPDATE, "20170701000000");
            String DeviceNo = sp.getString(AppConst.DeviceNo, "");
            JSONObject result = WS.syncData(id, GUID, lastUpdate, DeviceNo);

            Log.i(LOG_TAG, "Sync service Return data : " + result.toString());
            long wsendDate = new Date().getTime();
//            Log.d(LOG_TAG, "Sync service End : " + wsendDate);
//            Log.d(LOG_TAG, "Sync service WS elapsed : " + (wsendDate - wsstartDate));

            if (result != null && result.optBoolean("Status")) {
                serverTime = result.optLong("ResultNumber ") + "";//Utils.getFormattedCurrentDate();
                long dbstartDate = new Date().getTime();
                Log.d(LOG_TAG, "Sync service DB Insert start : " + dbstartDate);

                if (WS.InsertDataToDB(1, activity, result)) {
                    long dbendDate = new Date().getTime();
                    Log.d(LOG_TAG, "Sync service DB Insert end : " + dbendDate);
                    Log.d(LOG_TAG, "Sync service DB elapsed : " + (dbendDate - dbstartDate));

                    Log.d(LOG_TAG, "Sync service Insert Data Success");
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            Log.d(LOG_TAG, "End of Sync Service");
            if (status) {
                spedit.putString(AppConst.LASTUPDATE, serverTime).commit();
                spedit.putBoolean("isFirstLunch", false).commit();
                Log.d(LOG_TAG, "End of Sync Service Successfuly");
                go();

            } else if (sp.getBoolean("isFirstLunch", true)) {
                Utils.showCustomToast(activity, getString(R.string.error));
            } else {
                go();
            }
        }

    }

    private void go() {
        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        finish();
    }


    /*
    * String json = "{\"name\":\"bar\"}";
 Foo fooObject= realm.createObjectFromJson(Foo.class, json);
 //or
 String jsonArray = "[{\"name\":\"bar\"},{\"name\":\"baz\"}]";
 RealmList<Foo> fooObjects = realm.createAllFromJson(Foo.class, jsonArray);
 */
}
