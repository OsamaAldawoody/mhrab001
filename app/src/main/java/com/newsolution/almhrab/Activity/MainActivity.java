package com.newsolution.almhrab.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.newsolution.almhrab.AppConstants.Constants;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.Khotab;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;

import com.newsolution.almhrab.Tempreture.BLE;
import com.newsolution.almhrab.Tempreture.BroadcastService;
import com.newsolution.almhrab.Tempreture.Device;
import com.newsolution.almhrab.Tempreture.ILocalBluetoothCallBack;
import com.newsolution.almhrab.Tempreture.StringUtil;
import com.newsolution.almhrab.WebServices.WS;
import com.newsolution.almhrab.scheduler.SalaatAlarmReceiver;
import com.newsolution.almhrab.scheduler.SleepService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.newsolution.almhrab.Activity.FridayActivity.BROADCAST;

public class MainActivity extends Activity {
    private BroadcastReceiver myReceiver;
    private double long1, long2;
    private double lat1, lat2;
    private SharedPreferences sp;
    private GlobalVars gv;
    private SharedPreferences.Editor spedit;
    private TextView tvIqama, tvJmaaPray;
    private TextView fajrIqama;
    private TextView in_masgedTemp;
    private TextView out_masgedTemp;
    private TextView tvHumidity;
    private TextView tvMasjedName;
    private TextView time1;
    private TextView tText2;
    private TextView time2;
    private TextView tText1;
    private TextView magribIqama, maghribTime, asrIqama, asrTime, ishaIqama, ishaTime,
            dhuhrTime, fajrTime, sunriseIqama, duhrIqama, sunriseTime;
    private AppCompatImageView fajrTitle, shroqTitle, duhrTitle, asrTitle, maghribTitle, ishaTitle;
    private LinearLayout llRemainingTime;
    public String cfajr = "";
    public String icfajr = "";
    String csunrise = "";
    String icsunrise = "";
    String cdhohr = "";
    String icdhohr = "";
    String casr = "";
    String icasr = "";
    String cmaghrib = "";
    String icmaghrib = "";
    String cisha = "";
    String icisha = "";

    ArrayList<String> advs = new ArrayList<String>();
    public String nextPray;
    String[] mosquSettings;
    String[] prayTimes;
    private Timer timer;
    private int cityId;
    private DBOperations DBO;
    private Activity activity;
    private City city;
    private boolean isAlShrouqEkamaIsTime, isFajrEkamaIsTime, isDhuhrEkamaIsTime, ishaEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;
    private OptionSiteClass settings;
    private ImageView sound_stop;
    private String iqamatime = "";
    private int period = 5;
    private int REQUEST_PERMISSIONS = 101;
    private boolean stopTimer = false;
    public static String droidkufiBold = "fonts/droid_kufi_bold.ttf";
    public static String droidkufi = "fonts/droidkufi_regular.ttf";
    private Typeface font;
    private Typeface fontDroidkufi;
    public static String roboto = "fonts/roboto.ttf";
    private Typeface fontRoboto;
    public static String arial = "fonts/ariblk.ttf";
    private Typeface fontArial;
    final Handler AdsHandler = new Handler();

    int masjedId;
    private TextView tvIsPrayTime;
    private TextView tvIqRemaingTime;
    private TextView tvUnit;
    private TextView tvPrayName;
    private TextView tvPrayRemaingTime;
    private RelativeLayout rlIqRemainingT, rlIsPrayTime, rlPrayRemainingT;
    private Typeface digital;
    private Typeface ptBoldHeading;
    private Typeface battarFont;

    private String SN1 = "11126776";
    private String SN2 = "11126776";
    private BroadcastService _BroadcastService;
    private boolean _IsScanning = false;
    private boolean _IsAllowScan = false;



    private String TempIn = "";
    private String TempOut = "";
    public ILocalBluetoothCallBack _LocalBluetoothCallBack = new C05785();
    private RelativeLayout rlNews;
    private LinearLayout rlTitle;
    private TextView tvName;
    public static boolean isOpenSermon = false;
    public static boolean isOpenAds = false;
    private Runnable adsRunnable;
    private String recPath = "";
    private int IdKhotab;
    private String DateKhotab = "";


    class C05785 implements ILocalBluetoothCallBack {
        C05785() {
        }

        public void OnEntered(BLE ble) {
            final Device device = new Device();
            if (device.fromScanData(ble) && device.SN != null && device.SN.equals(SN1)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowConfig(device);
                    }
                });
            }
            if (device.fromScanData(ble) && device.SN != null && device.SN.equals(SN2)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowConfig1(device);
                    }
                });
            }
        }

        public void OnUpdate(BLE ble) {
            final Device device = new Device();
            if (device.fromScanData(ble) && device.SN != null && device.SN.equals(SN1)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowConfig(device);
                    }
                });
            }
            if (device.fromScanData(ble) && device.SN != null && device.SN.equals(SN2)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowConfig1(device);
                    }
                });
            }
        }

        public void OnExited(BLE ble) {
        }

        public void OnScanComplete() {
        }
    }

    protected void ShowConfig(Device device) {
        try {
             _IsAllowScan = true;
            if (device != null) {
                if (device.Temperature != -1000.0d) {
                    TempIn = (int) Math.round(device.Temperature) + "";
                    in_masgedTemp.setText(TempIn);
                }
                spedit.putString("batteryIn", device.Battery + "").commit();
                spedit.putString("TempIn", TempIn).commit();

            }
        } catch (Exception ex) {
            getWeather(0);
        }
    }

    @SuppressLint("SetTextI18n")
    protected void ShowConfig1(Device device) {
        try {

             _IsAllowScan = true;
            if (device != null) {
                if (device.Temperature != -1000.0d) {
                    TempOut = (int) Math.round(device.Temperature) + "";
                    out_masgedTemp.setText(TempOut);
                }
                if (device.Humidity != -1000.0d) {
                    tvHumidity.setText(StringUtil.ToString(device.Humidity, 1) + "%");
                }
                spedit.putString("batteryOut", device.Battery + "").commit();
                spedit.putString("TempOut", TempOut).commit();
                spedit.putString("HumOut", StringUtil.ToString(device.Humidity, 1)).commit();

            }
        } catch (Exception ex) {
            getWeather(1);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/neosansarabic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);

        activity = this;
        askForPermissions(new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                },
                REQUEST_PERMISSIONS);
        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontArial = Typeface.createFromAsset(getAssets(), arial);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);
        digital = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        ptBoldHeading = Typeface.createFromAsset(getAssets(), "fonts/pt_bold_heading.ttf");
        battarFont = Typeface.createFromAsset(getAssets(), "fonts/ae_battar.ttf");

        DBO = new DBOperations(this);
        gv = (GlobalVars) getApplicationContext();
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        cityId = sp.getInt("cityId", 1);
        DBO.open();
        city = DBO.getCityById(cityId);
        settings = DBO.getSettings();
        advs = DBO.getNews(Utils.getFormattedCurrentDate());
        DBO.close();
        lat1 = sp.getInt("lat1", city.getLat1());
        lat2 = sp.getInt("lat2", city.getLat2());
        long1 = sp.getInt("long1", city.getLon1());
        long2 = sp.getInt("long2", city.getLon2());

        gv.setMousqeSettings(settings.getFajrEkama() + "", settings.getAlShrouqEkama() + "", settings.getDhuhrEkama() + ""
                , settings.getAsrEkama() + "", settings.getMagribEkama() + "", settings.getIshaEkama() + "");
        mosquSettings = gv.getMousqeSettings();
        spedit.putString("ifajer", settings.getFajrEkama() + "").commit();
        spedit.putString("ishroq", settings.getAlShrouqEkama() + "").commit();
        spedit.putString("idhor", settings.getDhuhrEkama() + "").commit();
        spedit.putString("iaser", settings.getAsrEkama() + "").commit();
        spedit.putString("imagrib", settings.getMagribEkama() + "").commit();
        spedit.putString("iisha", settings.getIshaEkama() + "").commit();

        myReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    recPath = extras.getString("recPath");
                    Log.i("Sermon path  : ", recPath);
                    if (extras.getBoolean("isKhotba")) {
                        DateKhotab = extras.getString("DateKhotab");
                        IdKhotab = extras.getInt("IdKhotab");
                    } else {
                        DateKhotab = "";
                        IdKhotab = -1;
                    }
                    new uploadSermonToServer().execute(recPath, IdKhotab + "", DateKhotab);
                }
            }
        };

        cfajr = sp.getString("suh", "");
        csunrise = sp.getString("sun", "");
        cdhohr = sp.getString("duh", "");
        casr = sp.getString("asr", "");
        cmaghrib = sp.getString("magrib", "");
        cisha = sp.getString("isha", "");

        icfajr = sp.getString("iqsuh", "");
        icsunrise = sp.getString("iqsun", "");
        icdhohr = sp.getString("iqduh", "");
        icasr = sp.getString("iqasr", "");
        icmaghrib = sp.getString("iqmagrib", "");
        icisha = sp.getString("iqisha", "");

        buildUI();
        try {
            checkTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSleepModePeriod();

    }


    @SuppressLint("SetTextI18n")
    private void getWeather(final int action) {
        in_masgedTemp.setText(sp.getString("TempIn", "24"));
        out_masgedTemp.setText(sp.getString("TempOut", "30"));
        tvHumidity.setText(sp.getString("HumOut", "35") + "%");
    }

    public void buildUI() {

        AppCompatImageView ivMenu = (AppCompatImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispMenu(view);
            }
        });

        rlIsPrayTime = (RelativeLayout) findViewById(R.id.rlIsPrayTime);
        rlIqRemainingT = (RelativeLayout) findViewById(R.id.rlIqRemainingT);
        rlPrayRemainingT = (RelativeLayout) findViewById(R.id.rlPrayRemainingT);
        tvIsPrayTime = (TextView) findViewById(R.id.tvIsPrayTime);
        tvIqRemaingTime = (TextView) findViewById(R.id.tvIqRemaingTime);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        TextView tvIqamaR = (TextView) findViewById(R.id.tvIqamaR);
        tvPrayName = (TextView) findViewById(R.id.tvPrayName);
        TextView tvPrayR = (TextView) findViewById(R.id.tvPrayR);
        tvPrayRemaingTime = (TextView) findViewById(R.id.tvPrayRemaingTime);
        tvIqRemaingTime.setTypeface(digital);
        tvIqamaR.setTypeface(ptBoldHeading);
        tvIsPrayTime.setTypeface(ptBoldHeading);
        tvPrayR.setTypeface(ptBoldHeading);
        tvUnit.setTypeface(ptBoldHeading);
        tvPrayName.setTypeface(battarFont);
        tvPrayRemaingTime.setTypeface(digital);

        Calendar c = Calendar.getInstance();
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", new Locale("en"));
        String timeText = timeNow.format(c.getTime());
        SpannableString string = new SpannableString(timeText);
        string.setSpan(new RelativeSizeSpan((0.5f)), 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvPrayRemaingTime.setText(string);

        tvJmaaPray = (TextView) findViewById(R.id.tvJmaaPray);
        tvIqama = (TextView) findViewById(R.id.tvIqama);
        TextView tvIn = (TextView) findViewById(R.id.tvIn);
        TextView tvOut = (TextView) findViewById(R.id.tvOut);
        TextView tvHum = (TextView) findViewById(R.id.tvHum);
        LinearLayout llTitles = (LinearLayout) findViewById(R.id.llTitles);
        RelativeLayout rlMasjedTitle = (RelativeLayout) findViewById(R.id.rlMasjedTitle);
        rlMasjedTitle.setVisibility(View.GONE);
        LinearLayout llIqamaTime = (LinearLayout) findViewById(R.id.llIqamaTime);
        Utils.applyFont(activity, llTitles);
        rlNews = (RelativeLayout) findViewById(R.id.rlNews);
        rlTitle = (LinearLayout) findViewById(R.id.rlTitle);
        AppCompatImageView ivLogo = (AppCompatImageView) findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.GONE);
        tvName = (TextView) findViewById(R.id.tvName);
        AppCompatImageView ivMasjedLogo = (AppCompatImageView) findViewById(R.id.ivMasjedLogo);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        out_masgedTemp = (TextView) findViewById(R.id.outMasgedasged);
        in_masgedTemp = (TextView) findViewById(R.id.in_masged);
        tvMasjedName = (TextView) findViewById(R.id.tvMasjedName);
        tvMasjedName.setVisibility(View.GONE);
        out_masgedTemp.setTypeface(fontRoboto);
        in_masgedTemp.setTypeface(fontRoboto);
        tvHumidity.setTypeface(fontRoboto);
        tvHum.setText(getString(R.string.outHum));
        tvJmaaPray.setTypeface(fontArial);
        tvMasjedName.setTypeface(fontDroidkufi);
        tvIn.setTypeface(fontDroidkufi);
        tvOut.setTypeface(fontDroidkufi);
        tvHum.setTypeface(fontDroidkufi);
        fajrTitle = (AppCompatImageView) findViewById(R.id.fajrTitle);
        fajrTime = (TextView) findViewById(R.id.fajrTime);
        fajrIqama = (TextView) findViewById(R.id.fajrIqama);

        shroqTitle = (AppCompatImageView) findViewById(R.id.shroqTitle);
        sunriseTime = (TextView) findViewById(R.id.sunriseTime);
        sunriseIqama = (TextView) findViewById(R.id.sunriseIqama);

        duhrTitle = (AppCompatImageView) findViewById(R.id.duhrTitle);
        dhuhrTime = (TextView) findViewById(R.id.dhuhrTime);
        duhrIqama = (TextView) findViewById(R.id.duhrIqama);

        asrTitle = (AppCompatImageView) findViewById(R.id.asrTitle);
        asrTime = (TextView) findViewById(R.id.asrTime);
        asrIqama = (TextView) findViewById(R.id.asrIqama);

        maghribTitle = (AppCompatImageView) findViewById(R.id.mgrbTitle);
        maghribTime = (TextView) findViewById(R.id.maghribTime);
        magribIqama = (TextView) findViewById(R.id.magribIqama);
        llRemainingTime = (LinearLayout) findViewById(R.id.llRemainingTime);
        time1 = (TextView) findViewById(R.id.t1);
        time2 = (TextView) findViewById(R.id.t2);
        tText1 = (TextView) findViewById(R.id.te1);
        tText2 = (TextView) findViewById(R.id.te2);

        ishaTitle = (AppCompatImageView) findViewById(R.id.ishaTitle);
        ishaTime = (TextView) findViewById(R.id.ishaTime);
        ishaIqama = (TextView) findViewById(R.id.ishaIqama);
        sound_stop = (ImageView) findViewById(R.id.sound_stop);
        sound_stop.setVisibility(View.GONE);


        if (PlaySound.isPlay(getBaseContext())) {
            sound_stop.setVisibility(View.VISIBLE);
        }
        sound_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.stop(getBaseContext());
                sound_stop.setVisibility(View.GONE);
                Log.i("sound", "completed");
            }
        });
        fajrIqama.setTypeface(fontArial);
        sunriseIqama.setTypeface(fontArial);
        duhrIqama.setTypeface(fontArial);
        asrIqama.setTypeface(fontArial);
        magribIqama.setTypeface(fontArial);
        ishaIqama.setTypeface(fontArial);
        setIqamaTextColor(fajrIqama);
        setIqamaTextColor(sunriseIqama);
        setIqamaTextColor(duhrIqama);
        setIqamaTextColor(asrIqama);
        setIqamaTextColor(magribIqama);
        setIqamaTextColor(ishaIqama);
        fajrTime.setTypeface(fontArial);
        sunriseTime.setTypeface(fontArial);
        dhuhrTime.setTypeface(fontArial);
        asrTime.setTypeface(fontArial);
        maghribTime.setTypeface(fontArial);
        ishaTime.setTypeface(fontArial);
        setPrayTextColor(fajrTime);
        setPrayTextColor(sunriseTime);
        setPrayTextColor(dhuhrTime);
        setPrayTextColor(asrTime);
        setPrayTextColor(maghribTime);
        setPrayTextColor(ishaTime);
    }

    @Override
    protected void onStart() {
        super.onStart();

        stopSilentMode();
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(BROADCAST);
        if (myReceiver != null) registerReceiver(myReceiver, intentFilter);
        super.onResume();
        stopSilentMode();

        getWeather(-1);
        try {
            scanSensors();
        } catch (Exception ignored) {
        }
        tvMasjedName.setText(sp.getString("masjedName", ""));
        tvName.setText(sp.getString("masjedName", ""));
        DBO.open();
        advs = DBO.getNews(Utils.getFormattedCurrentDate());
        settings = DBO.getSettings();
        DBO.close();
        getPrayerTimes();

        showNews();
        checkAds();
        buildTheme();
        if (Utils.isOnline(activity)) {
            getAllKhotab();
        }
        if (sp.getInt("priority", 0) == 1) {
            syncData();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis());
        Intent intent = new Intent(activity, SleepService.class);
        activity.startService(intent);

        changeSettings();
        stopTimer = false;
    }

    private void setImage(String masjedImg, final AppCompatImageView imageView) {
        try {
            Glide.with(activity).load(Uri.parse(masjedImg))
                    .override(100, 100).listener(new RequestListener<Uri, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                    imageView.setImageResource(R.drawable.ic_mosque);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(imageView);
        } catch (Exception ignored) {
        }
    }

    public void setAlarm() {
        SalaatAlarmReceiver sar = new SalaatAlarmReceiver();
        sar.cancelAlarm(this);
        sar.setAlarm(this);
    }

    private void changeSettings() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        try {
            SimpleDateFormat spd = new SimpleDateFormat("HH:mm:ss", new Locale("en"));
            String t1 = cfajr + ":00";
            Date time1 = spd.parse(t1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(time1);
            String t2 = cdhohr + ":00";
            Date time2 = spd.parse(t2);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(time2);
            String t3 = casr + ":00";
            Date time3 = spd.parse(t3);
            Calendar c3 = Calendar.getInstance();
            c3.setTime(time3);
            String t4 = cmaghrib + ":00";
            Date time4 = spd.parse(t4);
            Calendar c4 = Calendar.getInstance();
            c4.setTime(time4);
            String t5 = cisha + ":00";
            Date time5 = spd.parse(t5);
            Calendar c5 = Calendar.getInstance();
            c5.setTime(time5);

            String t11 = icfajr + ":00";
            Date time11 = spd.parse(t11);
            Calendar c11 = Calendar.getInstance();
            c11.setTime(time11);

            String t22 = icdhohr + ":00";
            Date time22 = spd.parse(t22);
            Calendar c22 = Calendar.getInstance();
            c22.setTime(time22);

            String t33 = icasr + ":00";
            Date time33 = spd.parse(t33);
            Calendar c33 = Calendar.getInstance();
            c33.setTime(time33);

            String t44 = icmaghrib + ":00";
            Date time44 = spd.parse(t44);
            Calendar c44 = Calendar.getInstance();
            c44.setTime(time44);

            String t55 = icisha + ":00";
            Date time55 = spd.parse(t55);
            Calendar c55 = Calendar.getInstance();
            c55.setTime(time55);

            String timeNow = hour + ":" + minute + ":00";
            Date d = spd.parse(timeNow);
            Calendar cnow = Calendar.getInstance();
            cnow.setTime(d);
            Date now = cnow.getTime();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            if ((c1.getTime().before(now) || c1.getTime().equals(now))
                    && ((c11.getTime().after(now)) || c11.getTime().equals(now))) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                setAlarm();
                return;
            }
            if ((c2.getTime().before(now) || c2.getTime().equals(now))
                    && ((c22.getTime().after(now)) || c22.getTime().equals(now))) {
                if (!isFriday()) {
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                } else {
                    spedit.putString("phoneAlert", "").commit();
                }
                setAlarm();
                return;
            }
            if ((c3.getTime().before(now) || c3.getTime().equals(now))
                    && ((c33.getTime().after(now)) || c33.getTime().equals(now))) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                setAlarm();
                return;
            }
            if ((c4.getTime().before(now) || c4.getTime().equals(now))
                    && ((c44.getTime().after(now)) || c44.getTime().equals(now))) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                setAlarm();
                return;
            }
            if ((c5.getTime().before(now) || c5.getTime().equals(now))
                    && ((c55.getTime().after(now)) || c55.getTime().equals(now))) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                setAlarm();
                return;
            }
            if (now.before(c1.getTime())) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();

            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                if (!isFriday()) {
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                } else {
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(cdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                }

            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();

            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();

            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();

            } else if (now.after(c5.getTime())) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
            }
            setAlarm();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void buildTheme() {
        timer = new Timer();
        //try {
        TimerTask async = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopSilentMode();
                            if (PlaySound.isPlay(getBaseContext())) {
                                sound_stop.setVisibility(View.VISIBLE);
                            } else sound_stop.setVisibility(View.GONE);
                            if (stopTimer) {
                                return;
                            }
                            getPrayerTimes();
                            checkNextPrayTheme();
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(async, 0, 1000);
    }

    private void stopSilentMode() {
        try {
            AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = 0;
            if (mAudioManager != null) {
                maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FX_KEY_CLICK);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void checkNextPrayTheme() {
        getNextFriday();
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);

        try {
            fajrTime.setText(convTime(cfajr));
            sunriseTime.setText(convTime(csunrise));
            dhuhrTime.setText(convTime(cdhohr));
            asrTime.setText(convTime(casr));
            maghribTime.setText(convTime(cmaghrib));
            ishaTime.setText(convTime(cisha));
            setIqamaTime();


        } catch (Exception e) {
            e.printStackTrace();
            cfajr = "00:00";
            csunrise = "00:00";
            cdhohr = "00:00";
            casr = "00:00";
            cmaghrib = "00:00";
            cisha = "00:00";
            nextPray = "";
            fajrTime.setText((cfajr));
            sunriseTime.setText((csunrise));
            dhuhrTime.setText((cdhohr));
            asrTime.setText((casr));
            maghribTime.setText((cmaghrib));
            ishaTime.setText((cisha));

            setIqamaTime();
        }
        try {
            SimpleDateFormat spd = new SimpleDateFormat("HH:mm:ss", new Locale("en"));
            String t1 = cfajr + ":00";
            Date time1 = spd.parse(t1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(time1);
            String t2 = cdhohr + ":00";
            Date time2 = spd.parse(t2);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(time2);
            String t3 = casr + ":00";
            Date time3 = spd.parse(t3);
            Calendar c3 = Calendar.getInstance();
            c3.setTime(time3);
            String t4 = cmaghrib + ":00";
            Date time4 = spd.parse(t4);
            Calendar c4 = Calendar.getInstance();
            c4.setTime(time4);
            String t5 = cisha + ":00";
            Date time5 = spd.parse(t5);
            Calendar c5 = Calendar.getInstance();
            c5.setTime(time5);

            String t11 = icfajr + ":00";
            Date time11 = spd.parse(t11);
            Calendar c11 = Calendar.getInstance();
            c11.setTime(time11);

            String t22 = icdhohr + ":00";
            Date time22 = spd.parse(t22);
            Calendar c22 = Calendar.getInstance();
            c22.setTime(time22);

            String t33 = icasr + ":00";
            Date time33 = spd.parse(t33);
            Calendar c33 = Calendar.getInstance();
            c33.setTime(time33);

            String t44 = icmaghrib + ":00";
            Date time44 = spd.parse(t44);
            Calendar c44 = Calendar.getInstance();
            c44.setTime(time44);

            String t55 = icisha + ":00";
            Date time55 = spd.parse(t55);
            Calendar c55 = Calendar.getInstance();
            c55.setTime(time55);

            String timeNow = hour + ":" + minute + ":00";
            Date d = spd.parse(timeNow);
            Calendar cnow = Calendar.getInstance();
            cnow.setTime(d);
            Date now = cnow.getTime();

            Calendar calendar = Calendar.getInstance();
            Date dtoday = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date dtomorrow = calendar.getTime();

            DateFormat cdate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            DateFormat ctime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

            String dayAsString = cdate.format(dtoday);
            String tomorrowAsString = cdate.format(dtomorrow);
            String timeAsString = ctime.format(Calendar.getInstance().getTime());
            String npt = "";

            clearAllStyles();

            String currentPray = "";
            if ((c1.getTime().before(now) || c1.getTime().equals(now))
                    && ((c11.getTime().after(now)) || c11.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t11));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                currentPray = "fajr";
                fajrTitle.setImageResource(R.drawable.ic_fajer);
                ZoomSelectedImage(fajrTitle);
                setNoLargeTextSize(fajrIqama);
                setNoLargeTextSize(fajrTime);
                ZoomSelectedView(fajrTime);
                ZoomSelectedView(fajrIqama);
                setIqamaTextColor(fajrIqama);
                setPrayTextColor(fajrTime);
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                tvIqama.setText(TextUtils.isEmpty(npt) ? getString(R.string.itPrayTime) : getString(R.string.iqamaAfter));
                ShowRemainingIqamaTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText(getString(R.string.itPrayTime));
                    llRemainingTime.setVisibility(View.GONE);
                    runVoiceRecognition(currentPray);
                    itIsPrayTime(false, getString(R.string.fajer_igama));
                }
                return;
            }
            if ((c2.getTime().before(now) || c2.getTime().equals(now))
                    && ((c22.getTime().after(now)) || c22.getTime().equals(now))) {
                setSleepModePeriod();
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                currentPray = "dhuhr";
                setIqamaTextColor(duhrIqama);
                setNoLargeTextSize(duhrIqama);
                duhrTitle.setImageResource(R.drawable.ic_duhr);
                ZoomSelectedImage(duhrTitle);
                setPrayTextColor(dhuhrTime);
                setNoLargeTextSize(dhuhrTime);
                ZoomSelectedView(dhuhrTime);
                ZoomSelectedView(duhrIqama);
                if (!isFriday()) {
                    tvIqama.setText(TextUtils.isEmpty(npt) ? getString(R.string.itPrayTime) : getString(R.string.iqamaAfter));
                    ShowRemainingIqamaTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                    setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t22));
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                    isOpenSermon = false;
                } else {
                    spedit.putString("phoneAlert", "").commit();
                    tvIqama.setText(getString(R.string.isFriday));
                    itIsPrayTime(true, getString(R.string.isFriday));
                    llRemainingTime.setVisibility(View.GONE);
                    if (!isOpenSermon) playSermon();
                }
                if (iqamatime.equals("00:00:00")) {
                    llRemainingTime.setVisibility(View.GONE);
                    if (!isFriday()) {
                        tvIqama.setText(getString(R.string.itPrayTime));
                        runVoiceRecognition(currentPray);
                        itIsPrayTime(false, getString(R.string.duhr_igama));
                    }
                }
                return;
            }
            if ((c3.getTime().before(now) || c3.getTime().equals(now))
                    && ((c33.getTime().after(now)) || c33.getTime().equals(now))) {
                isOpenSermon = false;
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t33));
                ShowRemainingIqamaTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                currentPray = "asr";
                asrTitle.setImageResource(R.drawable.ic_asr);//ic_asr_on
                ZoomSelectedImage(asrTitle);
                setIqamaTextColor(asrIqama);
                setPrayTextColor(asrTime);
                setNoLargeTextSize(asrIqama);
                setNoLargeTextSize(asrTime);
                ZoomSelectedView(asrIqama);
                ZoomSelectedView(asrTime);
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                tvIqama.setText(TextUtils.isEmpty(npt) ? getString(R.string.itPrayTime) : getString(R.string.iqamaAfter));
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText(getString(R.string.itPrayTime));
                    llRemainingTime.setVisibility(View.GONE);
                    itIsPrayTime(false, getString(R.string.aser_igama));
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c4.getTime().before(now) || c4.getTime().equals(now))
                    && ((c44.getTime().after(now)) || c44.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t44));
                ShowRemainingIqamaTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                currentPray = "magrib";

                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                maghribTitle.setImageResource(R.drawable.ic_magrib);//ic_magrib_on
                ZoomSelectedImage(maghribTitle);
                setPrayTextColor(maghribTime);
                setNoLargeTextSize(maghribTime);
                ZoomSelectedView(maghribTime);
                ZoomSelectedView(magribIqama);
                setIqamaTextColor(magribIqama);
                setNoLargeTextSize(magribIqama);
                tvIqama.setText(TextUtils.isEmpty(npt) ? getString(R.string.itPrayTime) : getString(R.string.iqamaAfter));
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText(getString(R.string.itPrayTime));
                    llRemainingTime.setVisibility(View.GONE);
                    itIsPrayTime(false, getString(R.string.magrib_igama));
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c5.getTime().before(now) || c5.getTime().equals(now))
                    && ((c55.getTime().after(now)) || c55.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t55));
                ShowRemainingIqamaTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                currentPray = "isha";
                ishaTitle.setImageResource(R.drawable.ic_isha);
                ZoomSelectedImage(ishaTitle);
                setIqamaTextColor(ishaIqama);
                setNoLargeTextSize(ishaIqama);
                setPrayTextColor(ishaTime);
                setNoLargeTextSize(ishaTime);
                ZoomSelectedView(ishaIqama);
                ZoomSelectedView(ishaTime);
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                tvIqama.setText(TextUtils.isEmpty(npt) ? getString(R.string.itPrayTime) : getString(R.string.iqamaAfter));
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText(getString(R.string.itPrayTime));
                    llRemainingTime.setVisibility(View.GONE);
                    itIsPrayTime(false, getString(R.string.isha_igama));
                    runVoiceRecognition(currentPray);
                }
                return;
            }

            clearAllStyles();

            final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
            if (now.before(c1.getTime())) {
                nextPray = "fajr";
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                globalVariable.setNextPray("fajr");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t1)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t1)));
                ShowRemainingPrayTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t1)));

                fajrTitle.setImageResource(R.drawable.ic_fajer);
                ZoomSelectedImage(fajrTitle);
                setIqamaTextColor(fajrIqama);
                setNoLargeTextSize(fajrIqama);
                setPrayTextColor(fajrTime);
                setNoLargeTextSize(fajrTime);
                ZoomSelectedView(fajrTime);
                ZoomSelectedView(fajrIqama);
            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                if (!isFriday()) {
                    nextPray = "dhuhr";
                    ShowRemainingPrayTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                } else {
                    nextPray = "juma";
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(cdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                }
                globalVariable.setNextPray("dhuhr");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
                ShowRemainingPrayTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
                duhrTitle.setImageResource(R.drawable.ic_duhr);//ic_duhr_on
                ZoomSelectedImage(duhrTitle);
                setIqamaTextColor(duhrIqama);
                setNoLargeTextSize(duhrIqama);
                ZoomSelectedView(duhrIqama);
                ZoomSelectedView(dhuhrTime);
                setPrayTextColor(dhuhrTime);
                setNoLargeTextSize(dhuhrTime);
            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                isOpenSermon = false;
                nextPray = "asr";
                globalVariable.setNextPray("asr");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t3)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t3)));
                ShowRemainingPrayTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t3)));
                asrTitle.setImageResource(R.drawable.ic_asr);//ic_asr_on
                ZoomSelectedImage(asrTitle);
                setIqamaTextColor(asrIqama);
                setNoLargeTextSize(asrIqama);
                setPrayTextColor(asrTime);
                setNoLargeTextSize(asrTime);
                ZoomSelectedView(asrTime);
                ZoomSelectedView(asrIqama);
            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                nextPray = "magrib";
                globalVariable.setNextPray("maghrib");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t4)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t4)));
                ShowRemainingPrayTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t4)));
                maghribTitle.setImageResource(R.drawable.ic_magrib);
                ZoomSelectedImage(maghribTitle);
                setPrayTextColor(maghribTime);
                setNoLargeTextSize(maghribTime);
                ZoomSelectedView(magribIqama);
                ZoomSelectedView(maghribTime);
                setIqamaTextColor(magribIqama);
                setNoLargeTextSize(magribIqama);
            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                nextPray = "isha";
                globalVariable.setNextPray("isha");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t5)));//getIqama(t5)
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t5)));
                ShowRemainingPrayTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t5)));
                ishaTitle.setImageResource(R.drawable.ic_isha);
                ZoomSelectedImage(ishaTitle);
                setIqamaTextColor(ishaIqama);
                setNoLargeTextSize(ishaIqama);
                setPrayTextColor(ishaTime);
                setNoLargeTextSize(ishaTime);
                ZoomSelectedView(ishaIqama);
                ZoomSelectedView(ishaTime);
            } else if (now.after(c5.getTime())) {
                nextPray = "fajr";
                globalVariable.setNextPray("fajr");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, tomorrowAsString, "" + (getIqama(t1)));
                setCustomFontStyle(dayAsString, timeAsString, tomorrowAsString, "" + (getIqama(t1)));
                ShowRemainingPrayTime(dayAsString, timeAsString, tomorrowAsString, "" + (getIqama(t1)));
                ZoomSelectedView(fajrTime);
                ZoomSelectedView(fajrIqama);
                fajrTitle.setImageResource(R.drawable.ic_fajer);
                ZoomSelectedImage(fajrTitle);
                setIqamaTextColor(fajrIqama);
                setNoLargeTextSize(fajrIqama);
                setPrayTextColor(fajrTime);
                setNoLargeTextSize(fajrTime);
            }
            if (TextUtils.isEmpty(npt)) {
                String next_adan = "";
                if (sp.getString("next_adan", "").equals("fajr")) {
                    next_adan = getResources().getString(R.string.p1);
                } else if (sp.getString("next_adan", "").equals("sunrise")) {
                    next_adan = getResources().getString(R.string.p6);
                } else if (sp.getString("next_adan", "").equals("dhuhr")) {
                    next_adan = getResources().getString(R.string.p2);
                } else if (sp.getString("next_adan", "").equals("asr")) {
                    next_adan = getResources().getString(R.string.p3);
                } else if (sp.getString("next_adan", "").equals("magrib")) {
                    next_adan = getResources().getString(R.string.p4);
                } else if (sp.getString("next_adan", "").equals("isha")) {
                    next_adan = getResources().getString(R.string.p5);
                }

                llRemainingTime.setVisibility(View.GONE);
                tvIqama.setText(String.format(getString(R.string.isAthanTime), next_adan));


            } else {
                tvIqama.setText(getString(R.string.remainingToAthan));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void playSermon() {
        DBO.open();
        final Khotab khotba = DBO.getKhotba(Utils.getCurrentDate());
        DBO.close();
        if (!sp.getBoolean("IsMasjed", false)) {
            if (Utils.isOnline(activity)) {
                if (khotba != null) {
                    if (khotba.getIsException() == 0) {
                        stopTimer = true;
                        if (timer != null) {
                            timer.cancel();
                            timer.purge();
                        }
                        iqamatime = "";
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent cp = new Intent(activity, FridayActivity.class);
                                cp.putExtra("khotba", khotba);
                                startActivity(cp);
                                isOpenSermon = true;
                            }
                        }, 120000);

                    } else if (sp.getBoolean("emamScreen", false)) goToEmamScreen(false);
                } else goToEmamScreen(true);

            } else if (sp.getBoolean("emamScreen", false)) goToEmamScreen(false);
        }
    }

    private void goToEmamScreen(final boolean isStreaming) {
        stopTimer = true;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        iqamatime = "";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent cp = new Intent(activity, ViewEmamActivity.class);
                cp.putExtra("isStreaming", isStreaming);
                startActivity(cp);
                isOpenSermon = true;
            }
        }, 120000);

    }

    class uploadSermonToServer extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject result = upLoad2Server(params[0], params[1], params[2]);
                if (result != null && result.optBoolean("Status")) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Utils.showCustomToast(activity, getString(R.string.notRecording));
            } else {
                Utils.showCustomToast(activity, getString(R.string.fileSaved));
                try {
                    if (!TextUtils.isEmpty(recPath)) {
                        File file = new File(getRealPathFromURI(Uri.parse(recPath)));
                        boolean isDeleted = false;
                        if (file.exists())
                            isDeleted = file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public JSONObject upLoad2Server(String sourceFileUri, String IdKhotab, String DateKhotab) {
        masjedId = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");
        String upLoadServerUri = Constants.Main_URL + "SaveKhotabVideo?IdSubscribe=" + masjedId
                + "&GUID=" + GUID + "&DeviceNo=" + DeviceNo + "&IdKhotab=" + IdKhotab + "&DateKhotab=" + DateKhotab;
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";

        File sourceFile = new File(getRealPathFromURI(Uri.parse(sourceFileUri)));
        if (!sourceFile.isFile()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
            Log.i("Huzza", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("Upload file to server", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            // close streams
            Log.i("Upload file to server", fileName + " File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jo = new JSONObject();
        String data = "";
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                Log.i("Huzza", "RES Message: " + line);
                jo = new JSONObject(line);
            }
            rd.close();
        } catch (Exception ioex) {
            Log.e("Huzza", "error: " + ioex.getMessage(), ioex);
        }
        return jo;  // like 200 (Ok)

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    private void runVoiceRecognition(final String currentPray) {
        if (sp.getBoolean("voiceRec", true)) {
            stopTimer = true;
            timer.cancel();
            timer.purge();
            iqamatime = "";
            Intent cp = new Intent(activity, ShowPray.class);
            cp.setAction("a");
            cp.putExtra("currentPray", currentPray);
            startActivity(cp);
        } else {
            if (currentPray.equals("fajr")) {
                period = settings.getFajrAzkar();
            } else if (currentPray.equals("dhuhr")) {
                period = settings.getDhuhrAzkar();
            } else if (currentPray.equals("asr")) {
                period = settings.getAsrAzkar();
            } else if (currentPray.equals("magrib")) {
                period = settings.getMagribAzkar();
            } else if (currentPray.equals("isha")) {
                period = settings.getIshaAzkar();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent cp = new Intent(getApplicationContext(), Read.class);
                    cp.setAction("b");
                    cp.putExtra("pray", currentPray);
                    startActivity(cp);
                }
            }, period * 60 * 1000);
        }
    }

    private void setIqamaTime() {

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
        if (isDhuhrEkamaIsTime)
            spedit.putString("iqduh", settings.getDhuhrEkamaTime() + ":00").commit();
        else
            spedit.putString("iqduh", Utils.getIqama(cdhohr, mosquSettings[2])).commit();
        if (isAsrEkamaIsTime)
            spedit.putString("iqasr", settings.getAsrEkamaTime() + ":00").commit();
        else spedit.putString("iqasr", Utils.getIqama(casr, mosquSettings[3])).commit();
        if (isMagribEkamaIsTime)
            spedit.putString("iqmagrib", settings.getMagribEkamaTime() + ":00").commit();
        else
            spedit.putString("iqmagrib", Utils.getIqama(cmaghrib, mosquSettings[4])).commit();
        if (ishaEkamaIsTime)
            spedit.putString("iqisha", settings.getIshaEkamaTime() + ":00").commit();
        else
            spedit.putString("iqisha", Utils.getIqama(cisha, mosquSettings[5])).commit();

        spedit.putInt("hijriDiff", settings.getDateHijri()).commit();

        icfajr = sp.getString("iqsuh", "");
        icsunrise = sp.getString("iqsun", "");
        icdhohr = sp.getString("iqduh", "");
        icasr = sp.getString("iqasr", "");
        icmaghrib = sp.getString("iqmagrib", "");
        icisha = sp.getString("iqisha", "");
        fajrIqama.setText(convTime(icfajr));
        sunriseIqama.setText(convTime(icsunrise));
        duhrIqama.setText(convTime(icdhohr));
        asrIqama.setText(convTime(icasr));
        magribIqama.setText(convTime(icmaghrib));
        ishaIqama.setText(convTime(icisha));
    }

    private boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.FRIDAY;
    }

    private void getNextFriday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar cal = getNextFridayDate();
        String date = dateFormat.format(cal.getTime());
        if (!TextUtils.isEmpty(date)) {
            String[] dateParts = date.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            Hijri_Cal_Tools.calculation(lat1, lat2, long1, long2,
                    year, month, day);
            String fridayPray = Hijri_Cal_Tools.getDhuhur();
            tvJmaaPray.setText(convTime(fridayPray));

        }
    }

    private static Calendar getNextFridayDate() {
        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int days = Calendar.FRIDAY - weekday;
        if (days <= 0) {
            days += 7;
        }
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    private void itIsPrayTime(boolean isFriday, String prayName) {
        rlIqRemainingT.setVisibility(View.GONE);
        rlPrayRemainingT.setVisibility(View.GONE);
        rlIsPrayTime.setVisibility(View.VISIBLE);
        if (isFriday) {
            tvIsPrayTime.setText(getString(R.string.isFriday));
        } else
            tvIsPrayTime.setText(getString(R.string.isTime) + " " + prayName);
    }

    @SuppressLint("SetTextI18n")
    private void ShowRemainingIqamaTime(String cdate, String ctime, String tdate, String ttime) {
        String val = "";
        String dateStart = cdate + " " + ctime;
        String dateStop = tdate + " " + ttime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        String fh = "";
        String fs = "" + diffSeconds;
        String fm = "";

        fh = "" + diffHours;
        fm = "" + diffMinutes;
        rlIqRemainingT.setVisibility(View.VISIBLE);
        rlPrayRemainingT.setVisibility(View.GONE);
        rlIsPrayTime.setVisibility(View.GONE);
        Log.i("mint: ", diffMinutes + "");
        if (diffHours > 0) {
            if (diffHours < 10)
                fh = "0" + fh;
            tvIqRemaingTime.setText(" " + fh + " ");
            tvUnit.setText(getString(R.string.h));
        } else if (diffMinutes > 0) {
            diffMinutes = diffMinutes + 1;
            fm = "" + diffMinutes;
            if (diffMinutes < 10)
                fm = "0" + fm;
            tvIqRemaingTime.setText(" " + fm + " ");
            tvUnit.setText(getString(R.string.m));
        } else if (diffSeconds > 0) {
            if (diffSeconds < 10)
                fs = "0" + fs;
            tvIqRemaingTime.setText(" " + fs + " ");
            tvUnit.setText(getString(R.string.s));
        } else {
            rlIqRemainingT.setVisibility(View.GONE);
            rlPrayRemainingT.setVisibility(View.GONE);
            rlIsPrayTime.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(nextPray)) {
                tvPrayName.setText(getString(R.string.isPrayTime));
            } else {
                if (nextPray.equals("fajr"))
                    tvPrayName.setText(getString(R.string.isTime) + " " + getString(R.string.pn1));
                else if (nextPray.equals("dhuhr"))
                    tvPrayName.setText(getString(R.string.isTime) + " " + getString(R.string.pn3));
                else if (nextPray.equals("juma"))
                    tvPrayName.setText(getString(R.string.isTime) + " " + getString(R.string.pn8));
                else if (nextPray.equals("asr"))
                    tvPrayName.setText(getString(R.string.isTime) + " " + getString(R.string.pn4));
                else if (nextPray.equals("magrib"))
                    tvPrayName.setText(getString(R.string.isTime) + " " + getString(R.string.pn5));
                else if (nextPray.equals("isha"))
                    tvPrayName.setText(getString(R.string.isTime) + " " + getString(R.string.pn6));
            }
        }

    }

    private void ShowRemainingPrayTime(String cdate, String ctime, String tdate, String ttime) {
        String val = "";
        String dateStart = cdate + " " + ctime;
        String dateStop = tdate + " " + ttime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        String fh = "";
        String fs = "" + diffSeconds;
        String fm = "";

        fh = "" + diffHours;
        fm = "" + diffMinutes;
        rlIqRemainingT.setVisibility(View.GONE);
        rlPrayRemainingT.setVisibility(View.VISIBLE);
        rlIsPrayTime.setVisibility(View.GONE);
        if (nextPray.equals("fajr"))
            tvPrayName.setText(getString(R.string.pn1));
        else if (nextPray.equals("dhuhr"))
            tvPrayName.setText(getString(R.string.pn3));
        else if (nextPray.equals("juma"))
            tvPrayName.setText(getString(R.string.pn7));
        else if (nextPray.equals("asr"))
            tvPrayName.setText(getString(R.string.pn4));
        else if (nextPray.equals("magrib"))
            tvPrayName.setText(getString(R.string.pn5));
        else if (nextPray.equals("isha"))
            tvPrayName.setText(getString(R.string.pn6));

        if (diffHours < 10)
            fh = "0" + fh;
        if (diffMinutes < 10)
            fm = "0" + fm;
        if (diffSeconds < 10)
            fs = "0" + fs;
        String string = fh + ":" + fm + fs;
        SpannableString remainingTime = new SpannableString(string);
        remainingTime.setSpan(new RelativeSizeSpan((0.5f)), 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvPrayRemaingTime.setText(remainingTime);
    }

    @SuppressLint("SetTextI18n")
    private void setCustomFontStyle(String cdate, String ctime, String tdate, String ttime) {
        String val = "";
        String dateStart = cdate + " " + ctime;
        String dateStop = tdate + " " + ttime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        String fh = "";
        String fs = "" + diffSeconds;
        String fm = "";

        fh = "" + diffHours;
        fm = "" + diffMinutes;
        llRemainingTime.setVisibility(View.VISIBLE);
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/neosansarabic.ttf");
        time1.setTypeface(digital);
        time2.setTypeface(digital);
        tText1.setTypeface(regular);
        tText2.setTypeface(regular);
        time1.setTextColor(getResources().getColor(R.color.colorPrimary));//colorPrimary
        time2.setTextColor(getResources().getColor(R.color.colorPrimary));//colorPrimary
        setIqamaTextSize(time1);
        setIqamaTextSize(time2);

        if (diffHours > 0) {
            if (diffMinutes > 0) {
                val = fh + "" + getString(R.string.h) + " و " + fm + "" + getString(R.string.m);
                time1.setVisibility(View.VISIBLE);
                time2.setVisibility(View.VISIBLE);
                tText1.setVisibility(View.VISIBLE);
                tText2.setVisibility(View.VISIBLE);
                time1.setText(" " + fh + " ");
                time2.setText(" " + fm + " ");
                tText1.setText(getString(R.string.h) /*+ " و"*/);
                tText2.setText(getString(R.string.m));
            } else {
                val = fh + "" + getString(R.string.h);
                time1.setVisibility(View.VISIBLE);
                time2.setVisibility(View.GONE);
                tText1.setVisibility(View.VISIBLE);
                tText2.setVisibility(View.GONE);
                time1.setText(" " + fh + " ");
                tText1.setText(getString(R.string.h));
            }
        } else if (diffMinutes > 0) {
            val = fm + "" + getString(R.string.m) + "و" + fs + "" + getString(R.string.s);
            time1.setVisibility(View.VISIBLE);
            time2.setVisibility(View.VISIBLE);
            tText1.setVisibility(View.VISIBLE);
            tText2.setVisibility(View.VISIBLE);
            time1.setText(" " + fm + " ");
            time2.setText(" " + fs + " ");
            tText1.setText(getString(R.string.m)/* + " و"*/);
            tText2.setText(getString(R.string.s));
        } else if (diffSeconds > 0) {
            val = fs + "" + getString(R.string.s);
            time1.setVisibility(View.VISIBLE);
            time2.setVisibility(View.GONE);
            tText1.setVisibility(View.VISIBLE);
            tText2.setVisibility(View.GONE);
            time1.setText(" " + fs + " ");
            tText1.setText(getString(R.string.s));
        } else {
            val = "";
            llRemainingTime.setVisibility(View.GONE);
        }

    }


    private String getDifTime(String cdate, String ctime, String tdate, String ttime) {
        String val = "";
        String dateStart = cdate + " " + ctime;
        String dateStop = tdate + " " + ttime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String fh = "";
        String fs = "" + diffSeconds;
        String fm = "";

        fh = "" + diffHours;
        fm = "" + diffMinutes;

        if (diffHours >= 0 && diffHours < 10)
            fh = "0" + fh;
        if (diffMinutes >= 0 && diffMinutes < 10)
            fm = "0" + fm;
        if (diffSeconds >= 0 && diffSeconds < 10)
            fs = "0" + fs;
        if (diffHours > 0) {
            if (diffMinutes > 0) {
                val = fh + "" + getString(R.string.h) + " و" + fm + "" + getString(R.string.m);
            } else {
                val = fh + "" + getString(R.string.h);
            }
        } else if (diffMinutes > 0) {
            val = fm + "" + getString(R.string.m) + " و" + fs + "" + getString(R.string.s);
        } else if (diffSeconds > 0) val = fs + "" + getString(R.string.s);
        else val = "";

        return val;
    }

    private String getDifferentTime(String cdate, String ctime, String tdate, String ttime) {
        String val = "";
        String dateStart = cdate + " " + ctime;
        String dateStop = tdate + " " + ttime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        String fh = "";
        String fm = "";
        String fs = "";
        fh = "" + diffHours;
        fm = "" + diffMinutes;
        fs = "" + diffSeconds;
        if (diffHours > 0) {
            if (diffHours < 10)
                fh = "0" + diffHours;
            val = fh + ":" + fm;
        } else {
            if (diffMinutes < 10)
                fm = "0" + diffMinutes;
            val = "00:" + fm;
        }

        if (diffSeconds == 0)
            fs = "0" + diffSeconds;
        val = val + ":" + fs;

        return val;
    }


    private String convTime(String time) {
        String[] intime = time.split(":");
        int hour = Integer.parseInt(intime[0]);
        int minutes = Integer.parseInt(intime[1]);
        if (minutes == 60) {
            minutes = 0;
            hour++;
        }
        String timeHHMM;
        if (hour < 10) {
            if (minutes < 10) {
                timeHHMM = "" + hour + ":0" + minutes;
            } else {
                timeHHMM = "" + hour + ":" + minutes;
            }
            return timeHHMM;
        } else if (hour > 12) {
            hour = hour - 12;
            if (minutes < 10) {
                timeHHMM = "" + hour + ":0" + minutes;
            } else {
                timeHHMM = "" + hour + ":" + minutes;
            }
            return timeHHMM;
        } else {
            if (minutes < 10) {
                timeHHMM = hour + ":0" + minutes;
            } else {
                timeHHMM = hour + ":" + minutes;
            }
            if (hour == 12) {
                return timeHHMM;
            }
            return timeHHMM;
        }

    }

    private String getIqama(String time) {
        String[] intime = time.split(":");
        int hour = Integer.parseInt(intime[0]);
        int minutes = Integer.parseInt(intime[1]);
        int h = hour;
        long m = minutes + Long.parseLong("00");

        if (m > 59) {
            m = m - 60;
            h++;
        }
        return h + ":" + m + ":00";
    }

    private void syncData() {
        Timer timerPray = new Timer();
        TimerTask asyncPray = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new UpdateAsync().execute();
                        }
                    });
                } catch (NullPointerException ignored) {
                }
            }
        };
        timerPray.schedule(asyncPray, 0, 1800000);
    }

    private void scanSensors() {
        Timer timerScan = new Timer();
        TimerTask asyncScan = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("scan ", true + "");
                            _IsScanning = false;
                            _IsAllowScan = false;
                            Scan();
                        }
                    });
                } catch (NullPointerException ignored) {
                }
            }
        };
        timerScan.schedule(asyncScan, 0, 60 * 1000);
    }

    private void getAllKhotab() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WS.getAllKhotab(activity, new OnLoadedFinished() {
                        @Override
                        public void onSuccess(String response) {

                        }

                        @Override
                        public void onFail(String error) {

                        }
                    });
                }
            });
        } catch (NullPointerException e) {
        }
    }

    private void checkAds() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        String currentTime = df.format(date);
        String currentDate = dfDate.format(date);
        int day = 0;
        if (Utils.isSaturday())
            day = 1;
        if (Utils.isSunday())
            day = 2;
        if (Utils.isMonday())
            day = 3;
        if (Utils.isTuesday())
            day = 4;
        if (Utils.isWednesday())
            day = 5;
        if (Utils.isThursday())
            day = 6;
        if (Utils.isFriday())
            day = 7;

        DBO.open();
        ArrayList<Ads> adsList = DBO.getAdsByDate(sp.getInt("masjedId", -1), currentDate, currentTime, day);
        DBO.close();
        if (adsList.size() > 0) {
            for (int i = 0; i < adsList.size(); i++) {
                Ads ads = adsList.get(i);
                if (!isOpenAds) {
                    isOpenAds = true;
                    Intent intent = new Intent(activity, ShowAdsActivity.class);
                    intent.putExtra("ads", ads);
                    intent.setAction("main");
                    startActivity(intent);
                }

            }
        }

        adsRunnable = new

                Runnable() {
                    @Override
                    public void run() {
                        try {
                            checkAds();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

        ;
        AdsHandler.postDelayed(adsRunnable, 1000);

    }

    private void openAds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isOpenAds = false;
            }
        }, 60000);
    }

    private void checkTime() {
        DateHigri hd = new DateHigri();
        TextView date1 = (TextView) findViewById(R.id.dateToday);
        date1.setTypeface(font);
        date1.setText((Utils.writeIslamicDate(activity, hd)));
        TextView time = (TextView) findViewById(R.id.Time);
        time.setTypeface(fontRoboto);
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);
        TextView amPm = (TextView) findViewById(R.id.amPm);
        amPm.setVisibility(View.VISIBLE);
        DateFormat ampm = new SimpleDateFormat("a", new Locale("ar"));
        amPm.setText(ampm.format(Calendar.getInstance().getTime()));
        Calendar c = Calendar.getInstance();
        String timeText = timeNow.format(c.getTime());
        SpannableString string = new SpannableString(timeText);
        string.setSpan(new RelativeSizeSpan((0.5f)), 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        time.setText(string);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkTime();
            }
        }, 1000);
    }

    private void animAdvs() {
        TextView advTitle;
        int advSize = advs.size();
        Log.e("advSize : = ", "" + advSize);
        int start = 0;
        StringBuilder advText = new StringBuilder();

        while (start < advSize) {
            String advers = advs.get(start);
            String[] adv = advers.substring(1, advers.length() - 1).split(",");
            advText.append("  ").append(adv[0]);
            start++;
        }

        advTitle = (TextView) findViewById(R.id.advText);
        advTitle.setText(Utils.padText(advText.toString(), advTitle.getPaint(), advTitle.getWidth()) /*+ repeated*/);
        advTitle.setTypeface(fontDroidkufi);
        advTitle.setSelected(true);
    }

    private void clearAllStyles() {
        fajrTitle.setImageResource(R.drawable.ic_fajer);
        shroqTitle.setImageResource(R.drawable.ic_shrouq);
        duhrTitle.setImageResource(R.drawable.ic_duhr);
        asrTitle.setImageResource(R.drawable.ic_asr);
        maghribTitle.setImageResource(R.drawable.ic_magrib);
        ishaTitle.setImageResource(R.drawable.ic_isha);
        setImageSize(fajrTitle);
        setViewSize(fajrIqama);
        setViewSize(fajrTime);
        setImageSize(shroqTitle);
        setViewSize(sunriseTime);
        setViewSize(sunriseIqama);
        setImageSize(duhrTitle);
        setViewSize(duhrIqama);
        setViewSize(dhuhrTime);
        setImageSize(asrTitle);
        setViewSize(asrTime);
        setViewSize(asrIqama);
        setImageSize(maghribTitle);
        setViewSize(magribIqama);
        setViewSize(maghribTime);
        setImageSize(ishaTitle);
        setViewSize(ishaTime);
        setViewSize(ishaIqama);
        fajrTitle.setBackgroundColor(0);
        fajrTime.setBackgroundResource(0);
        fajrIqama.setBackgroundResource(0);
        setNoTextSize(fajrIqama);
        setNoTextSize(fajrTime);

        sunriseTime.setBackgroundColor(0);
        sunriseIqama.setBackgroundResource(0);
        shroqTitle.setBackgroundResource(0);
        setNoTextSize(sunriseTime);
        setNoTextSize(sunriseIqama);

        duhrTitle.setBackgroundColor(0);
        dhuhrTime.setBackgroundResource(0);
        duhrIqama.setBackgroundResource(0);
        setNoTextSize(duhrIqama);
        setNoTextSize(dhuhrTime);

        asrTitle.setBackgroundColor(0);
        asrTime.setBackgroundResource(0);
        asrIqama.setBackgroundResource(0);
        setNoTextSize(asrIqama);
        setNoTextSize(asrTime);

        maghribTitle.setBackgroundColor(0);
        maghribTime.setBackgroundResource(0);
        magribIqama.setBackgroundResource(0);
        setNoTextSize(magribIqama);
        setNoTextSize(maghribTime);

        ishaTitle.setBackgroundColor(0);
        ishaTime.setBackgroundResource(0);
        ishaIqama.setBackgroundResource(0);
        setNoTextSize(ishaTime);
        setNoTextSize(ishaIqama);
    }



    private void setNoTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.no_l_font_size));
    }


    private void setIqamaTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.iqama_font_size));
    }

    private void setNoLargeTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.no_l_font_size1));
    }

    private void setImageSize(ImageView imageView) {
        imageView.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.titleImageHeight);
    }

    private void setViewSize(TextView textView) {
        textView.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.titleImageHeight);
    }

    private void ZoomSelectedImage(ImageView imageView) {
        imageView.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.titleImageHeight1);
    }

    private void ZoomSelectedView(TextView textView) {
        textView.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.titleImageHeight1);
    }


    private void setIqamaTextColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.googleR));
    }

    private void setPrayTextColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.googleB));
    }

    public void dispMenu(View view) {
        startActivity(new Intent(activity, SettingsActivity.class));
    }

    private String[] calculate() {
        Calendar today = Calendar.getInstance();
        double day = today.get(Calendar.DAY_OF_MONTH);
        double month = today.get(Calendar.MONTH) + 1;
        double year = today.get(Calendar.YEAR);
        try {
            DBO.open();
            city = DBO.getCityById(cityId);
            settings = DBO.getSettings();
            DBO.close();
            lat1 = sp.getInt("lat1", city.getLat1());
            lat2 = sp.getInt("lat2", city.getLat2());
            long1 = sp.getInt("long1", city.getLon1());
            long2 = sp.getInt("long2", city.getLon2());

            int hour = today.get(Calendar.HOUR_OF_DAY);
            int minute = today.get(Calendar.MINUTE);
            String timeNow = hour + ":" + minute + ":00";
            Date d = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(timeNow);
            Calendar cnow = Calendar.getInstance();
            cnow.setTime(d);
            Date now = cnow.getTime();
            String t5 = cisha + ":00";
            Date time5 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(t5);
            Calendar c5 = Calendar.getInstance();
            c5.setTime(time5);
        } catch (ParseException ignored) {
        }
        Hijri_Cal_Tools.calculation(lat1, lat2, long1, long2,
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
                gv.setMousqeSettings(settings.getFajrEkama() + "", settings.getAlShrouqEkama() + "", settings.getDhuhrEkama() + ""
                        , settings.getAsrEkama() + "", settings.getMagribEkama() + "", settings.getIshaEkama() + "");
                mosquSettings = gv.getMousqeSettings();
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

                icfajr = sp.getString("iqsuh", "");
                icsunrise = sp.getString("iqsun", "");
                icdhohr = sp.getString("iqduh", "");
                icasr = sp.getString("iqasr", "");
                icmaghrib = sp.getString("iqmagrib", "");
                icisha = sp.getString("iqisha", "");
            }
            gv.setPrayTimes(cfajr, csunrise, cdhohr, casr, cmaghrib, cisha);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setSleepModePeriod() {
        String sleepOn = Utils.addToTime(sp.getString("isha", ""), settings.getCloseScreenAfterIsha()/* sp.getInt("sleepOn", 0) */ + "");
        String sleepOff = Utils.diffFromTime(sp.getString("suh", ""), settings.getRunScreenBeforeFajr()/*sp.getInt("sleepOff", 0) */ + "");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            Date start = sdf.parse(sleepOn);
            Date end = sdf.parse(sleepOff);
            Date date = new Date();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(date);
            calendarStart.set(Calendar.HOUR_OF_DAY, start.getHours());
            calendarStart.set(Calendar.MINUTE, start.getMinutes());
            calendarStart.set(Calendar.SECOND, 0);
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(date);
            calendarEnd.set(Calendar.HOUR_OF_DAY, end.getHours());
            calendarEnd.set(Calendar.MINUTE, end.getMinutes());
            calendarEnd.set(Calendar.SECOND, 0);// for 0 sec
            if (end.before(start)) {
                calendarEnd.add(Calendar.DAY_OF_YEAR, 1);
            }
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
            String startDate = df.format(calendarStart.getTime());
            String endDate = df.format(calendarEnd.getTime());
            spedit.putString("startTime", startDate).commit();
            spedit.putString("endTime", endDate).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopTimer = true;
        timer.cancel();
        timer.purge();
        try {
            AdsHandler.removeCallbacks(adsRunnable);
            openAds();
            if ( _BroadcastService != null) {
                 _BroadcastService.StopScan();
            }
        } catch (Exception ignored) {
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class UpdateAsync extends AsyncTask<Void, Void, Boolean> {
        String serverTime = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            int id = sp.getInt("masjedId", -1);
            String GUID = sp.getString("masjedGUID", "");
            String lastUpdate = sp.getString(Utils.LASTUPDATE, "20170701000000");
            String DeviceNo = sp.getString(Utils.DeviceNo, "");
            JSONObject result = WS.syncData(id, GUID, lastUpdate, DeviceNo);


            if (result != null && result.optBoolean("Status")) {
                serverTime = result.optLong("ResultNumber ") + "";//Utils.getFormattedCurrentDate();

                return WS.InsertDataToDB(1, activity, result);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            if (status) {
                spedit.putString(Utils.LASTUPDATE, serverTime).commit();
                DBO.open();
                advs = DBO.getNews(Utils.getFormattedCurrentDate());
                settings = DBO.getSettings();
                DBO.close();
                showNews();
                tvMasjedName.setText(sp.getString("masjedName", ""));
                tvName.setText(sp.getString("masjedName", ""));
            }
        }

    }

    private void showNews() {
        if (sp.getBoolean("news", true)) {
            if (advs.size() > 0) {
                rlTitle.setVisibility(View.GONE);
                rlNews.setVisibility(View.VISIBLE);
                animAdvs();
            } else {
                rlTitle.setVisibility(View.VISIBLE);
                rlNews.setVisibility(View.GONE);
            }
        } else {
            rlTitle.setVisibility(View.VISIBLE);
            rlNews.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (PlaySound.isPlay(activity)) {
            PlaySound.stop(activity);
        }
        super.onBackPressed();
    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        }

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
                                                Manifest.permission.RECORD_AUDIO,
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
            }

        }
    }

    protected void Scan() {
        SN1 = sp.getString("SN1", "11126776");
        SN2 = sp.getString("SN2", "11126776");
        try {
            if ( SN1 == null ||  SN1.equals("")) {
                getWeather(0);
            }
            if ( SN2 == null ||  SN2.equals("")) {
                getWeather(1);
            }
            if ( _BroadcastService == null) {
                 _BroadcastService = new BroadcastService();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                BluetoothAdapter _BluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
                if (_BluetoothAdapter != null) {
                    if ( _BroadcastService.Init(_BluetoothAdapter,  _LocalBluetoothCallBack)) {
                        if (! _IsScanning) {
                             _IsScanning = true;
                             _BroadcastService.StartScan();
                            new Thread(new C02793()).start();
                            return;
                        }
                        return;
                    }
                }
            } else getWeather(-1);
        } catch (Exception ex) {
             _IsScanning = false;
            getWeather(-1);
        }
    }

    class C02793 implements Runnable {
        C02793() {
        }

        public void run() {
            try {
                Thread.sleep(15000);
                if (_IsScanning && !_IsAllowScan) {
                    _BroadcastService.StopScan();
                    _IsScanning = false;
                }
            } catch (Exception ignored) {
            }
        }
    }

    protected void onDestroy() {
        try {
            if (myReceiver != null) {
                unregisterReceiver(myReceiver);
                myReceiver = null;
            }
            if ( _BroadcastService != null) {
                 _BroadcastService.StopScan();
            }
        } catch (Exception ignored) {
        }
        super.onDestroy();
    }


}
