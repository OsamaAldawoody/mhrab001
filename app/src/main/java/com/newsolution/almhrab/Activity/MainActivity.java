package com.newsolution.almhrab.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.CustomTypefaceSpan;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.MarqueeViewSingle;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.Khotab;
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;
//import com.newsolution.almhrab.TZONE.Bluetooth.AppConfig;
//import com.newsolution.almhrab.TZONE.Bluetooth.BLE;
//import com.newsolution.almhrab.TZONE.Bluetooth.BLEGattService;
//import com.newsolution.almhrab.TZONE.Bluetooth.IConfigCallBack;
//import com.newsolution.almhrab.TZONE.Bluetooth.ILocalBluetoothCallBack;
//import com.newsolution.almhrab.TZONE.Bluetooth.Temperature.BroadcastService;
//import com.newsolution.almhrab.TZONE.Bluetooth.Temperature.ConfigService;
//import com.newsolution.almhrab.TZONE.Bluetooth.Temperature.Model.CharacteristicHandle;
//import com.newsolution.almhrab.TZONE.Bluetooth.Temperature.Model.CharacteristicType;
//import com.newsolution.almhrab.TZONE.Bluetooth.Temperature.Model.Device;
//import com.newsolution.almhrab.TZONE.Bluetooth.Utils.BinaryUtil;
//import com.newsolution.almhrab.TZONE.Bluetooth.Utils.DateUtil;
//import com.newsolution.almhrab.TZONE.Bluetooth.Utils.StringConvertUtil;
//import com.newsolution.almhrab.TZONE.Bluetooth.Utils.StringUtil;
//import com.newsolution.almhrab.TZONE.Bluetooth.Utils.TemperatureUnitUtil;
//import com.newsolution.almhrab.TZONE.Core.ReportHelper;
//import com.newsolution.almhrab.TZONE.Model.Report;
//import com.newsolution.almhrab.TZONE.Model.ReportData;
import com.newsolution.almhrab.Tempreture.AppConfig;
import com.newsolution.almhrab.Tempreture.BLE;
import com.newsolution.almhrab.Tempreture.BroadcastService;
import com.newsolution.almhrab.Tempreture.ConfigService;
import com.newsolution.almhrab.Tempreture.DateUtil;
import com.newsolution.almhrab.Tempreture.Device;
import com.newsolution.almhrab.Tempreture.ILocalBluetoothCallBack;
import com.newsolution.almhrab.Tempreture.StringUtil;
import com.newsolution.almhrab.Tempreture.TemperatureUnitUtil;
import com.newsolution.almhrab.Weather.JSONWeatherParser;
import com.newsolution.almhrab.Weather.Weather;
import com.newsolution.almhrab.WebServices.UserOperations;
import com.newsolution.almhrab.WebServices.WS;
//import com.newsolution.almhrab.harmony.java.awt.color.ColorSpace;
import com.newsolution.almhrab.scheduler.SalaatAlarmReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity/* implements RecognitionListener*/ {
    //    private Report _Report;
    public Queue<byte[]> Buffer = new LinkedList();
    private String DeviceName = "";
    private String Firmware = "";
    private String HardwareModel = "3A01";
    public boolean IsRunning = false;
    public boolean IsUploadRunning = false;
    private String SN1 = "11126776";
    private String SN2 = "11126776";
    //    private String Token = "000000";
//    private Dialog _AlertDialog;
    private BluetoothAdapter _BluetoothAdapter;
    private BroadcastService _BroadcastService;
    private Timer _Timer;
    private ConfigService _ConfigService;
    private Device _Device = null;
    private boolean _IsConnecting = false;
    private boolean _IsInit = false;
    private boolean _IsNeedEmptyPassword = false;
    private boolean _IsReading = false;
    private boolean _IsSaving = false;
    private boolean _IsScanning = false;
    private boolean _IsAllowScan = false;
    private boolean _IsSync = false;
    private boolean _IsSynced = false;
    int _LastSerial = 0;
    //    private PrintHelper _Print = null;
    private ProgressDialog _ProgressDialog;
    private int _SyncCount = 0;
    //    public long _new_sync_datatime = 0;
//    public long _new_sync_interval = 0;
//    private int _SyncDateTime = 0;
//    private Date _SyncEndTime = new Date();
//    private int _SyncIndex = 0;
    private int _SyncMode = 0;
//    private Date _SyncBeginTime = new Date(new Date().getTime() - 86400000);
//    private int _SyncProgress = 0;

    private String TempIn = "";
    private String TempOut = "";
    public ILocalBluetoothCallBack _LocalBluetoothCallBack = new C05785();
    private RelativeLayout rlNews;
    private LinearLayout rlTitle;
    private AppCompatImageView ivLogo;
    private TextView tvName;
    public static boolean isOpenSermon = false;
    public static boolean isOpenAds = false;
    private Runnable adsRunnable;

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
            if (this._ProgressDialog != null && this._ProgressDialog.isShowing()) {
                this._ProgressDialog.dismiss();
            }
            this._IsAllowScan = true;
            if (device != null) {
//                 Toast.makeText(activity,"(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                if (!(device.HardwareModel == null || device.HardwareModel.equals(""))) {
//                    if (device.HardwareModel.equals("3901")) {
//                       Toast.makeText(activity,"BT04(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                    } else if (device.HardwareModel.equals("3C01")) {
//                        Toast.makeText(activity,"BT04B(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                    } else if (device.HardwareModel.equals("3A01")) {
//                        Toast.makeText(activity,"BT05(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                    }
//                }
//                if (device.Battery != -1000) {
//                    Toast.makeText(activity,getString(R.string.lan_45) + " " + device.Battery + "%",Toast.LENGTH_LONG).show();
//                }
                if (device.Temperature != -1000.0d) {
                    TempIn = (int) Math.round(device.Temperature) + "";
//                    TempIn=  new TemperatureUnitUtil(device.Temperature).GetStringTemperature(com.newsolution.almhrab
//                            .AppConfig.TemperatureUnit);
                    in_masgedTemp.setText(TempIn);
                }
//                if (device.Humidity != -1000.0d) {
//                  tvHumidity.setText(StringUtil.ToString(device.Humidity, 1) + "%");
//                }
                spedit.putString("batteryIn", device.Battery + "").commit();
                spedit.putString("TempIn", TempIn).commit();

            }
        } catch (Exception ex) {
//            Toast.makeText(this, getString(R.string.lan_105) + " ex:" + ex.toString(), 0).show();
//            finish();
            getWeather(0);
        }
    }

    protected void ShowConfig1(Device device) {
        try {
            if (this._ProgressDialog != null && this._ProgressDialog.isShowing()) {
                this._ProgressDialog.dismiss();
            }
            this._IsAllowScan = true;
            if (device != null) {
//                 Toast.makeText(activity,"(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                if (!(device.HardwareModel == null || device.HardwareModel.equals(""))) {
//                    if (device.HardwareModel.equals("3901")) {
//                       Toast.makeText(activity,"BT04(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                    } else if (device.HardwareModel.equals("3C01")) {
//                        Toast.makeText(activity,"BT04B(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                    } else if (device.HardwareModel.equals("3A01")) {
//                        Toast.makeText(activity,"BT05(" + device.SN + ")",Toast.LENGTH_LONG).show();
//                    }
//                }
//                if (device.Battery != -1000) {
//                    Toast.makeText(activity,getString(R.string.lan_45) + " " + device.Battery + "%",Toast.LENGTH_LONG).show();
//                }
                if (device.Temperature != -1000.0d) {
                    TempOut = (int) Math.round(device.Temperature) + "";
//                    TempIn=  new TemperatureUnitUtil(device.Temperature).GetStringTemperature(com.newsolution.almhrab
//                            .AppConfig.TemperatureUnit);
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
//            Toast.makeText(this, getString(R.string.lan_105) + " ex:" + ex.toString(), 0).show();
//            finish();
            getWeather(1);
        }
    }

    private double long1, long2;
    private double lat1, lat2;
    private SharedPreferences sp;
    private GlobalVars gv;
    private SharedPreferences.Editor spedit;
    private AppCompatImageView ivMasjedLogo, ivMenu;
    private TextView tvIqama, tvJmaaPray;
    private TextView tvIn, tvOut, tvHum;
    TextView date1, time, amPm, fajrIqama, IqamaTitle, salaTitle, AthanTitle,
            in_masgedTemp, out_masgedTemp, tvHumidity, tvMasjedName, time1, tText2, time2, tText1, nextIqamaTime;
    private TextView magribIqama, maghribTime, asrIqama, asrTime, ishaIqama, ishaTime,
            dhuhrTime, fajrTime, sunriseIqama, duhrIqama, sunriseTime;
    AppCompatImageView fajrTitle, shroqTitle, duhrTitle, asrTitle, maghribTitle, ishaTitle;
    LinearLayout llRemainingTime;
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
    public Calendar today = Calendar.getInstance();
    double day = today.get(Calendar.DAY_OF_MONTH);
    double month = today.get(Calendar.MONTH) + 1;
    double year = today.get(Calendar.YEAR);
    ArrayList<String> mats = new ArrayList<String>();
    ArrayList<String> advs = new ArrayList<String>();
    ArrayList<News> newses = new ArrayList<News>();
    ArrayList<String> azkar = new ArrayList<String>();
    public String nextPray;
    public String mosqueName;
    String[] mosquSettings;
    String[] prayTimes;
    private Timer timer;
    private TimerTask async;
    private int cityId;
    private DBOperations DBO;
    private Timer timerPray;
    private TimerTask asyncPray;
    private Activity activity;
    private City city;
    private boolean isAlShrouqEkamaIsTime, isFajrEkamaIsTime, isDhuhrEkamaIsTime, ishaEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;
    private OptionSiteClass settings;
    private ImageView sound_stop;
    private String iqamatime = "";
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "voiceRecognitionAct";
    public String te0 = "Allahu Akbar";
    public String te1 = "Allah hoo Akbar";
    public String te2 = "Allah";
    public String te3 = "Allahu";
    public String te4 = "Allah";
    public String te5 = "الله أكبر ";
    public String te6 = "اللهو";
    public String te7 = "Allahu Ackbar";
    public String t1 = "send me ";
    public String t2 = "samuel";
    public String t3 = "similar";
    public String s1 = "assalam";
    public String s2 = "salam";
    public int rn = 1;
    public int maxRn = 0;
    private String currentPray = "";
    private CountDownTimer countDownTimer;
    private int period = 300000;
    private int VOICE_RECOGNITION_REQUEST_CODE = 100;
    private int REQUEST_PERMISSIONS = 101;
    private boolean stopTimer = false;
    private LinearLayout llTitles;
    private LinearLayout llIqamaTime;
    //    public static String FONT_NAME_EB = "fonts/neosans_black.otf";
    public static String droidkufiBold = "fonts/droid_kufi_bold.ttf";
    public static String droidkufi = "fonts/droidkufi_regular.ttf";
    private Typeface font;
    private Typeface fontDroidkufi;
    public static String roboto = "fonts/roboto.ttf";
    private Typeface fontRoboto;
    public static String comfort = "fonts/comfort.ttf";//comfort
    private Typeface fontComfort;
    public static String arial = "fonts/ariblk.ttf";//comfort
    private Typeface fontArial;
    private RelativeLayout rlMasjedTitle;
    final Handler AdsHandler = new Handler();


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
                .setDefaultFontPath("fonts/neosansarabic.ttf")//battar  neosans_black droidkufi_regular droid_sans_arabic neosansarabic //mcs_shafa_normal
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);
//        Utils.applyFontBold(activity,findViewById(R.id.layout));

        activity = this;
        askForPermissions(new String[]{
                        //    Manifest.permission.MANAGE_DOCUMENTS,
//                        android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                },
                REQUEST_PERMISSIONS);
        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontComfort = Typeface.createFromAsset(getAssets(), comfort);
        fontArial = Typeface.createFromAsset(getAssets(), arial);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);
        DBO = new DBOperations(this);
        gv = (GlobalVars) getApplicationContext();
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
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


//        prayTimes=  gv.getPrayTimes();
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
//        setSleepPeriod();
        try {
            checkTime();
        } catch (Exception e) {
            Log.e("checkTime Error : ", "" + e);
        }


//        speech = SpeechRecognizer.createSpeechRecognizer(this);
//        speech.setRecognitionListener(this);
//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar");
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);

//        getWeather();
//        Scan();
    }


    private void getWeather(final int action) {
        in_masgedTemp.setText(sp.getString("TempIn", "24"));
        out_masgedTemp.setText(sp.getString("TempOut", "30"));
        tvHumidity.setText(sp.getString("HumOut", "35") + "%");
//        double latitude = lat1 + (lat2 / 60);
//        double longitude = long1 + (long2 / 60);
//        Log.i("555: ", latitude + " : " + longitude);
//        String url = ("http://api.openweathermap.org/data/2.5/weather?APPID=bc420023d5d42b4f183f1c811717874f" + "&lat="
//                + latitude
//                + "&lon=" + longitude
//                + "&units=metric");
//
//        UserOperations.getInstance(activity).sendGetRequest(url, new OnLoadedFinished() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    Log.i("/// response: ", response);
//
//                    Weather weather = JSONWeatherParser.getWeather(response);
//
//                    String humidity = ((int) weather.currentCondition.getHumidity()) + "%";
//                    String temp = ((int) weather.temperature.getTemp()) + "";
//                    String temp1 = ((int) weather.temperature.getMinTemp()) + "";
//                    if (action==0) {
//                        in_masgedTemp.setText(temp1);
//                        tvHumidity.setText(humidity);
//                    }else if (action==1){
//                        out_masgedTemp.setText(temp);
//                        tvHumidity.setText(humidity);
//                    }else {
//                        in_masgedTemp.setText(temp1);
//                        out_masgedTemp.setText(temp);
//                        tvHumidity.setText(humidity);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFail(String error) {
//            }
//        });
    }

    public void buildUI() {

        ivMenu = (AppCompatImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispMenu(view);
            }
        });
        tvJmaaPray = (TextView) findViewById(R.id.tvJmaaPray);
        tvIqama = (TextView) findViewById(R.id.tvIqama);
        tvIn = (TextView) findViewById(R.id.tvIn);
        tvOut = (TextView) findViewById(R.id.tvOut);
        tvHum = (TextView) findViewById(R.id.tvHum);
        llTitles = (LinearLayout) findViewById(R.id.llTitles);
        rlMasjedTitle = (RelativeLayout) findViewById(R.id.rlMasjedTitle);
        rlMasjedTitle.setVisibility(View.GONE);
        llIqamaTime = (LinearLayout) findViewById(R.id.llIqamaTime);
        Utils.applyFont(activity, llTitles);
        rlNews = (RelativeLayout) findViewById(R.id.rlNews);
        rlTitle = (LinearLayout) findViewById(R.id.rlTitle);
        ivLogo = (AppCompatImageView) findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.GONE);
        tvName = (TextView) findViewById(R.id.tvName);
        ivMasjedLogo = (AppCompatImageView) findViewById(R.id.ivMasjedLogo);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        out_masgedTemp = (TextView) findViewById(R.id.outMasgedasged);
        in_masgedTemp = (TextView) findViewById(R.id.in_masged);
        tvMasjedName = (TextView) findViewById(R.id.tvMasjedName);
        tvMasjedName.setVisibility(View.GONE);
        salaTitle = (TextView) findViewById(R.id.salaTitle);
        AthanTitle = (TextView) findViewById(R.id.AthanTitle);
        IqamaTitle = (TextView) findViewById(R.id.IqamaTitle);
        out_masgedTemp.setTypeface(fontRoboto);
        in_masgedTemp.setTypeface(fontRoboto);
        tvHumidity.setTypeface(fontRoboto);
//        out_masgedTemp.setText("0");
//        in_masgedTemp.setText("0");
//        tvHumidity.setText("0%");
        tvHum.setText("الرطوبة الخارجية");
        tvJmaaPray.setTypeface(fontArial);
        tvMasjedName.setTypeface(fontDroidkufi);
//        tvName.setTypeface(fontDroidkufi);
        tvIn.setTypeface(fontDroidkufi);
        tvOut.setTypeface(fontDroidkufi);
        tvHum.setTypeface(fontDroidkufi);
        fajrTitle = (AppCompatImageView) findViewById(R.id.fajrTitle);
        fajrTime = (TextView) findViewById(R.id.fajrTime);
        fajrIqama = (TextView) findViewById(R.id.fajrIqama);

        shroqTitle = (AppCompatImageView) findViewById(R.id.shroqTitle);
        sunriseTime = (TextView) findViewById(R.id.sunriseTime);
        sunriseIqama = (TextView) findViewById(R.id.sunriseIqama);
//        sunriseIqama.setText(" _______ ");

        duhrTitle = (AppCompatImageView) findViewById(R.id.duhrTitle);
        dhuhrTime = (TextView) findViewById(R.id.dhuhrTime);
        duhrIqama = (TextView) findViewById(R.id.duhrIqama);

        asrTitle = (AppCompatImageView) findViewById(R.id.asrTitle);
        asrTime = (TextView) findViewById(R.id.asrTime);
        asrIqama = (TextView) findViewById(R.id.asrIqama);

        maghribTitle = (AppCompatImageView) findViewById(R.id.mgrbTitle);
        maghribTime = (TextView) findViewById(R.id.maghribTime);
        magribIqama = (TextView) findViewById(R.id.magribIqama);
        // nextIqamaTime = (TextView) findViewById(R.id.nextIqamaTime);
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
//        IqamaTitle.setTypeface(font);
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
//        AthanTitle.setTypeface(font);
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

//                Utils.applyFontEnBold(activity, llIqamaTime);

//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar");
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
//        speech = SpeechRecognizer.createSpeechRecognizer(this);
//        speech.setRecognitionListener(this);
//        buildTheme();
//        getWeather(-1);
//        Scan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FX_KEY_CLICK);
            } else {
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getWeather(-1);
        Scan();
//        AutoScan();
        tvMasjedName.setText(sp.getString("masjedName", ""));
        tvName.setText(sp.getString("masjedName", ""));
//        if (!TextUtils.isEmpty(sp.getString("masjedImg", ""))) {
//            setImage(sp.getString("masjedImg", ""), ivLogo);
//            setImage(sp.getString("masjedImg", ""), ivMasjedLogo);
//        } else {
//            ivMasjedLogo.setImageResource(R.drawable.ic_mosque);
//            ivLogo.setImageResource(R.drawable.ic_mosque);
//        }
        DBO.open();
        advs = DBO.getNews(Utils.getFormattedCurrentDate());
        settings = DBO.getSettings();
        DBO.close();
        getPrayerTimes();

        showNews();
        checkAds();
        buildTheme();
        if (sp.getInt("priority", 0) == 1) {
            syncData();
        }
        if (Utils.isOnline(activity)) {
            getAllKhotab();
        }
        changeSettings();
        setSleepPeriod();

//        else {
//            DBO.open();
//            advs = DBO.getNews(Utils.getFormattedCurrentDate());
//            settings = DBO.getSettings();
//            DBO.close();
//            animAdvs();
//        }
//        speech = SpeechRecognizer.createSpeechRecognizer(this);
//        speech.setRecognitionListener(this);
//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar");
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
//        if (speech!=null)speech.stopListening();
        stopTimer = false;
    }

    private void setImage(String masjedImg, final AppCompatImageView imageView) {
        try {
            Glide.with(activity).load(Uri.parse(masjedImg))
                    .override(100, 100).listener(new RequestListener<Uri, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
//                    Log.i("exce: ", e.getMessage());
                    imageView.setImageResource(R.drawable.ic_mosque);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(imageView);
        } catch (Exception e) {
        }
    }

    public void setAlarm() {
        ///// start service /////
        SalaatAlarmReceiver sar = new SalaatAlarmReceiver();
        sar.cancelAlarm(this);
        sar.setAlarm(this);
        ///// start service /////
    }

    public void lockScreen() {
//        Window window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = 0;
        getWindow().setAttributes(params);
    }

    public void unlockScreen() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = -1f;
        getWindow().setAttributes(params);
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
            Date dtoday = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date dtomorrow = calendar.getTime();

            DateFormat cdate = new SimpleDateFormat("MM/dd/yyyy", new Locale("en"));
            DateFormat ctime = new SimpleDateFormat("HH:mm:ss", new Locale("en"));

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
        async = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                    Log.i("exception", "" + e.getMessage());
                }
            }
        };
        timer.schedule(async, 0, 1000);
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
            Log.e("//// ", e.getMessage());
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

            DateFormat cdate = new SimpleDateFormat("MM/dd/yyyy", new Locale("en"));
            DateFormat ctime = new SimpleDateFormat("HH:mm:ss", new Locale("en"));

            String dayAsString = cdate.format(dtoday);
            String tomorrowAsString = cdate.format(dtomorrow);
            String timeAsString = ctime.format(Calendar.getInstance().getTime());
            String npt = "";

            clearAllStyles();

            if ((c1.getTime().before(now) || c1.getTime().equals(now))
                    && ((c11.getTime().after(now)) || c11.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t11));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                currentPray = "fajr";
//                setTextColor(fajrTitle);
//                setLargeTextSize(fajrTitle);
                fajrTitle.setImageResource(R.drawable.ic_fajer_co);
                setNoLargeTextSize(fajrIqama);
                setIqamaTextColor(fajrIqama);
                setPrayTextColor(fajrTime);
                setNoLargeTextSize(fajrTime);
//                nextIqamaTime.setText(setCustomFontStyle(npt));
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                tvIqama.setText(TextUtils.isEmpty(npt) ? "حان وقت الصلاة " : "إقامة الصلاة بعد");
                Log.i("***iqama:", iqamatime + " /pp");
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText("حان وقت الصلاة ");
                    llRemainingTime.setVisibility(View.GONE);
//                    nextIqamaTime.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c2.getTime().before(now) || c2.getTime().equals(now))
                    && ((c22.getTime().after(now)) || c22.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                currentPray = "dhuhr";
                setIqamaTextColor(duhrIqama);
                setNoLargeTextSize(duhrIqama);
//                setTextColor(duhrTitle);
//                setLargeTextSize(duhrTitle);
                duhrTitle.setImageResource(R.drawable.ic_duhr_on);
                setPrayTextColor(dhuhrTime);
                setNoLargeTextSize(dhuhrTime);
//                nextIqamaTime.setText(setCustomFontStyle(npt));
                Log.i("***iqama:", iqamatime + " /pp");
                if (!isFriday()) {
                    tvIqama.setText(TextUtils.isEmpty(npt) ? "حان وقت الصلاة " : "إقامة الصلاة بعد");
                    setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t22));
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                    isOpenSermon = false;
                } else {
                    spedit.putString("phoneAlert", "").commit();
                    tvIqama.setText("صلاة الجمعة");
                    llRemainingTime.setVisibility(View.GONE);
                    if (!isOpenSermon) playSermon();
                }
                if (iqamatime.equals("00:00:00")) {
                    llRemainingTime.setVisibility(View.GONE);
//                    nextIqamaTime.setText("");
                    if (!isFriday()) {
                        tvIqama.setText("حان وقت الصلاة ");
                        runVoiceRecognition(currentPray);
                    }
                }
                return;
            }
            if ((c3.getTime().before(now) || c3.getTime().equals(now))
                    && ((c33.getTime().after(now)) || c33.getTime().equals(now))) {
                isOpenSermon = false;
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t33));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                currentPray = "asr";
                asrTitle.setImageResource(R.drawable.ic_asr_on);
                setIqamaTextColor(asrIqama);
                setPrayTextColor(asrTime);
//                setTextColor(asrTitle);
                setNoLargeTextSize(asrIqama);
                setNoLargeTextSize(asrTime);
//                setLargeTextSize(asrTitle);

                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
//                nextIqamaTime.setText(setCustomFontStyle(npt));
                tvIqama.setText(TextUtils.isEmpty(npt) ? "حان وقت الصلاة " : "إقامة الصلاة بعد");
                Log.i("***iqama:", iqamatime + " /pp");
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText("حان وقت الصلاة ");
                    llRemainingTime.setVisibility(View.GONE);
//                    nextIqamaTime.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c4.getTime().before(now) || c4.getTime().equals(now))
                    && ((c44.getTime().after(now)) || c44.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t44));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                currentPray = "magrib";

                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                maghribTitle.setImageResource(R.drawable.ic_magrib_on);
                setPrayTextColor(maghribTime);
                setNoLargeTextSize(maghribTime);
//                setTextColor(maghribTitle);
//                setLargeTextSize(maghribTitle);
                setIqamaTextColor(magribIqama);
                setNoLargeTextSize(magribIqama);
//                nextIqamaTime.setText(setCustomFontStyle(npt));
                tvIqama.setText(TextUtils.isEmpty(npt) ? "حان وقت الصلاة " : "إقامة الصلاة بعد");
                Log.i("***iqama:", iqamatime + " /pp");
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText("حان وقت الصلاة ");
                    llRemainingTime.setVisibility(View.GONE);
//                    nextIqamaTime.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c5.getTime().before(now) || c5.getTime().equals(now))
                    && ((c55.getTime().after(now)) || c55.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (t55));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                currentPray = "isha";
                ishaTitle.setImageResource(R.drawable.ic_isha_on);
                setIqamaTextColor(ishaIqama);
                setNoLargeTextSize(ishaIqama);
                setPrayTextColor(ishaTime);
                setNoLargeTextSize(ishaTime);
//                setTextColor(ishaTitle);
//                setLargeTextSize(ishaTitle);
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                tvIqama.setText(TextUtils.isEmpty(npt) ? "حان وقت الصلاة " : "إقامة الصلاة بعد");
//                nextIqamaTime.setText(setCustomFontStyle(npt));
                Log.i("***iqama:", iqamatime + " /pp");
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText("حان وقت الصلاة ");
                    llRemainingTime.setVisibility(View.GONE);
//                    nextIqamaTime.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }
//            if (iqamatime.equals("00:00:00")) {
//                tvIqama.setText("حان وقت الصلاة ");
//                nextIqamaTime.setText("");
//                runVoiceRecognition(currentPray);
//            }
            clearAllStyles();

            final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
            if (now.before(c1.getTime())) {
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr,settings.getPhoneShowAlertsBeforEkama()+"")).commit();
                nextPray = "fajr";
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                globalVariable.setNextPray("fajr");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t1)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t1)));
//                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t1)));
//                setTextColor(fajrTitle);
//                setLargeTextSize(fajrTitle);
                fajrTitle.setImageResource(R.drawable.ic_fajer_co);
                setIqamaTextColor(fajrIqama);
                setNoLargeTextSize(fajrIqama);
                setPrayTextColor(fajrTime);
                setNoLargeTextSize(fajrTime);
            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                nextPray = "dhuhr";
                if (!isFriday()) {
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                } else {
                    spedit.putString("phoneAlert", Utils.setPhoneAlert(cdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                }
                globalVariable.setNextPray("dhuhr");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
//                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
                duhrTitle.setImageResource(R.drawable.ic_duhr_on);
                setIqamaTextColor(duhrIqama);
                setNoLargeTextSize(duhrIqama);
//                setTextColor(duhrTitle);
//                setLargeTextSize(duhrTitle);
                setPrayTextColor(dhuhrTime);
                setNoLargeTextSize(dhuhrTime);
            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                isOpenSermon = false;
                nextPray = "asr";
                globalVariable.setNextPray("asr");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t3)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t3)));
//                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t3)));
                asrTitle.setImageResource(R.drawable.ic_asr_on);
                setIqamaTextColor(asrIqama);
                setNoLargeTextSize(asrIqama);
                setPrayTextColor(asrTime);
                setNoLargeTextSize(asrTime);
//                setTextColor(asrTitle);
//                setLargeTextSize(asrTitle);
            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                nextPray = "maghrib";
                globalVariable.setNextPray("maghrib");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t4)));
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t4)));
                maghribTitle.setImageResource(R.drawable.ic_magrib_on);
                setPrayTextColor(maghribTime);
                setNoLargeTextSize(maghribTime);
//                setTextColor(maghribTitle);
//                setLargeTextSize(maghribTitle);
                setIqamaTextColor(magribIqama);
                setNoLargeTextSize(magribIqama);
            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                nextPray = "isha";
                globalVariable.setNextPray("isha");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t5)));//getIqama(t5)
                setCustomFontStyle(dayAsString, timeAsString, dayAsString, "" + (getIqama(t5)));
                ishaTitle.setImageResource(R.drawable.ic_isha_on);
                setIqamaTextColor(ishaIqama);
                setNoLargeTextSize(ishaIqama);
                setPrayTextColor(ishaTime);
                setNoLargeTextSize(ishaTime);
//                setTextColor(ishaTitle);
//                setLargeTextSize(ishaTitle);
            } else if (now.after(c5.getTime())) {
                globalVariable.setNextPray("fajr");
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                npt = getDifTime(dayAsString, timeAsString, tomorrowAsString, "" + (getIqama(t1)));
                setCustomFontStyle(dayAsString, timeAsString, tomorrowAsString, "" + (getIqama(t1)));
//                setTextColor(fajrTitle);
//                setLargeTextSize(fajrTitle);
                fajrTitle.setImageResource(R.drawable.ic_fajer_co);
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
//                    nextIqamaTime.setText("");
                tvIqama.setText(String.format(getString(R.string.isAthanTime), next_adan));


            } else {
//                setCustomFontStyle(npt);
//                nextIqamaTime.setText(setCustomFontStyle(npt));
                tvIqama.setText("باقٍ على الأذان");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        DateFormat ampm = new SimpleDateFormat("a");
//        amPm.setText(ampm.format(Calendar.getInstance().getTime()));
    }

    private void playSermon() {
        DBO.open();
        Khotab khotba = DBO.getKhotba(Utils.getCurrentDate());
        DBO.close();
        if (!sp.getBoolean("IsMasjed", false)) {
            //isException هذه الجامع لا يوجد عليه رقابة يعني ما في بث مباشر او ترجمة
            if (khotba != null) {
                if (khotba.getIsException() == 0) {
                    if (Utils.isOnline(activity)) {
                        stopTimer = true;
                        timer.cancel();
                        timer.purge();
                        Log.i("***voice1", "countDown");
                        iqamatime = "";
                        Intent cp = new Intent(activity, FridayActivity.class);
                        cp.putExtra("khotba",khotba);
                        startActivity(cp);
                        isOpenSermon = true;
                    }
                }
            }
        }
    }


    private void runVoiceRecognition(final String currentPray) {
        if (currentPray.equals("fajr")) {
            maxRn = 2;
            period = settings.getFajrAzkar();
        } else if (currentPray.equals("dhuhr")) {
            period = settings.getDhuhrAzkar();
            maxRn = 4;
        } else if (currentPray.equals("asr")) {
            period = settings.getAsrAzkar();
            maxRn = 4;
        } else if (currentPray.equals("magrib")) {
            period = settings.getMagribAzkar();
            maxRn = 3;
        } else if (currentPray.equals("isha")) {
            period = settings.getIshaAzkar();
            maxRn = 4;
        }
        if (sp.getBoolean("voiceRec", true)) {
            stopTimer = true;
            timer.cancel();
            timer.purge();
            Log.i("***voice1", "countDown");
            iqamatime = "";
//        countDownTimer.cancel();
//        speech.stopListening();
            Intent cp = new Intent(activity, ShowPray.class);
            cp.setAction("a");
            cp.putExtra("currentPray", currentPray);
            //  gv.setNextPray(currentPray);
            startActivity(cp);
        } else {
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
//        countDownTimer = new CountDownTimer(period * (60 * 1000), 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                speech.startListening(recognizerIntent);
////                Log.i("voice", "tick");
//            }
//
//            public void onFinish() {
//                iqamatime="";
//                countDownTimer.cancel();
//              speech.stopListening();
//                Intent intent = new Intent(activity, Read.class);
//                intent.putExtra("pray", currentPray);
//                intent.setAction("a");
//                startActivity(intent);
//            }
//
//        }.start();
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
//                spedit.putString("iqsun", csunrise).commit();
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

//        if (isFajrEkamaIsTime)
//            fajrIqama.setText(settings.getFajrEkamaTime());
//        else
//            fajrIqama.setText(convTime(Utils.getIqama(cfajr, mosquSettings[0])));
//        if (isDhuhrEkamaIsTime)
//            duhrIqama.setText(settings.getDhuhrEkamaTime());
//        else
//            duhrIqama.setText(convTime(Utils.getIqama(cdhohr, mosquSettings[1])));
//        if (isAsrEkamaIsTime)
//            asrIqama.setText(settings.getAsrEkamaTime());
//        else
//            asrIqama.setText(convTime(Utils.getIqama(casr, mosquSettings[2])));
//        if (isMagribEkamaIsTime)
//            magribIqama.setText(settings.getMagribEkamaTime());
//        else magribIqama.setText(convTime(Utils.getIqama(cmaghrib, mosquSettings[3])));
//        if (ishaEkamaIsTime)
//            ishaIqama.setText(settings.getIshaEkamaTime());
//        else
//            ishaIqama.setText(convTime(Utils.getIqama(cisha, mosquSettings[4])));

    }

    private boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.FRIDAY) {
//            Log.e("****/ friday/", " " + true);
            return true;
        } else return false;
    }
//    private boolean isFriday() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
//        Calendar cal = getNextFridayDate();
//        String friday = dateFormat.format(cal.getTime());
//        Calendar calendar=Calendar.getInstance();
//        String today = dateFormat.format(calendar.getTime());
//        Log.e("****/ friday/", " " + friday);
//        Log.e("****/ today/", " " + today);
//        if (today.equals(friday)){
//            return  true;
//        }
//        return  false;
//    }

    private void getNextFriday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));
        Calendar cal = getNextFridayDate();
        String date = dateFormat.format(cal.getTime());
        if (!TextUtils.isEmpty(date)) {
            String[] dateParts = date.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            Hijri_Cal_Tools.calculation((double) lat1, (double) lat2, (double) long1, (double) long2,
                    year, month, day);
            String fridayPray = Hijri_Cal_Tools.getDhuhur();
            tvJmaaPray.setText(convTime(fridayPray));

        }
//        Log.e("****/ date1/", " " + date);

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

    private void setCustomFontStyle(String cdate, String ctime, String tdate, String ttime) {
        String val = "";
        String dateStart = cdate + " " + ctime;
        String dateStop = tdate + " " + ttime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", new Locale("en"));
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
        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
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
                tText1.setText(getString(R.string.h) + " و");
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
            tText1.setText(getString(R.string.m) + " و");
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

//    private SpannableString setCustomFontStyle(String val) {
//       llRemainingTime.setVisibility(View.VISIBLE);
//        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/neosansarabic.ttf");
//        Typeface digital = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
//
//        TypefaceSpan regularSpan = new CustomTypefaceSpan("", regular);
//        TypefaceSpan digitalSpan = new CustomTypefaceSpan("", digital);
//        SpannableString sb=  new SpannableString(val);
//        if (!TextUtils.isEmpty(val)){
//        if (val.contains(getString(R.string.h)) && val.contains(getString(R.string.m))) {
//            sb.setSpan(regularSpan, 2, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(regularSpan, 10, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(digitalSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(digitalSpan, 8, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary))
//                    , 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary))
//                    , 8, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        } else if (val.contains(getString(R.string.h))) {
//            sb.setSpan(regularSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(regularSpan, 2, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary))
//                    , 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        } else if (val.contains(getString(R.string.m)) && val.contains(getString(R.string.s))) {
//            sb.setSpan(regularSpan, 3, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(regularSpan, 12, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(digitalSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(digitalSpan, 10, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary))
//                    , 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary))
//                    , 10, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        } else if (val.contains(getString(R.string.s))) {
//            sb.setSpan(digitalSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(regularSpan, 2, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary))
//                    , 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        }}
//        return sb;
//    }

    private String getDifTime(String cdate, String ctime, String tdate, String ttime) {
        String val = "";
        String dateStart = cdate + " " + ctime;
        String dateStop = tdate + " " + ttime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", new Locale("en"));
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
//        if (diffHours < 10) {
////            fh = "0" + diffHours;
//            fh = "" + diffHours;
//        } else {
        fh = "" + diffHours;
//        }
//        if (diffMinutes < 10) {
//            fm = "" + diffMinutes;
////            fm = "0" + diffMinutes;
//        } else {
        fm = "" + diffMinutes;
//        }

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

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", new Locale("en"));
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

//        if (diffSeconds > 0) {
        if (diffSeconds == 0)
            fs = "0" + diffSeconds;
//        } else fs = "00";
        val = val + ":" + fs;

        return val;
    }


    private String convTime(String time) {
        String intime[] = time.split(":");
        int hour = Integer.parseInt(intime[0]);
        int minutes = Integer.parseInt(intime[1]);
        String disTime;
        String h;
        String m;
        if (minutes == 60) {
            minutes = 0;
            hour++;
        }
        String timeHHMM;
        if (hour < 10) {
            if (minutes < 10) {
                timeHHMM = "" + String.valueOf(hour) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = "" + String.valueOf(hour) + ":" + String.valueOf(minutes);
            }
            return timeHHMM;//+ " ص";
        } else if (hour > 12) {
            hour = hour - 12;
            if (minutes < 10) {
                timeHHMM = "" + String.valueOf(hour) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = "" + String.valueOf(hour) + ":" + String.valueOf(minutes);
            }
            return timeHHMM;//+ " م";
        } else {
            if (minutes < 10) {
                timeHHMM = String.valueOf(hour) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = String.valueOf(hour) + ":" + String.valueOf(minutes);
            }
            if (hour == 12) {
                return timeHHMM;// + "م";
            }
            return timeHHMM;//+ "ص";
        }

//        return timeHHMM;
        //  return time;
    }


    private String getIqama(String time) {
//       time=time.replace("ص","").replace("م","");
        String intime[] = time.split(":");
        int hour = Integer.parseInt(intime[0]);
        int minutes = Integer.parseInt(intime[1]);
        int h = hour;
        long m = minutes + Long.parseLong("00");//getIqamaTime()

        if (m > 59) {
            m = m - 60;
            h++;
        }
        String Iqama = h + ":" + m + ":00";
        // Log.e("pray + iqama = ", time + " -- " + Iqama);
        return Iqama;
    }


    private String getIqamaTime() {
        String iqamatime = "00";
        //Log.i("---++",nextPray);

        if (sp.getString("next_adan", "").equals("fajr")) {
            iqamatime = mosquSettings[0];
//            iqamatime = sp.getString("ifajer", "20");
        }

        if (sp.getString("next_adan", "").equals("dhuhr")) {
            iqamatime = mosquSettings[1];
//            iqamatime = sp.getString("idhor", "10");

        }
        if (sp.getString("current_pray", "").equals("asr")) {
            iqamatime = mosquSettings[2];
//            iqamatime = sp.getString("iaser", "10");

        }
        if (sp.getString("next_adan", "").equals("magrib")) {
            iqamatime = mosquSettings[3];
//            iqamatime = sp.getString("imagrib", "05");

        }
        if (sp.getString("next_adan", "").equals("isha")) {
            iqamatime = mosquSettings[4];
//            iqamatime = sp.getString("iisha", "05");

        }
//        Log.i("---++ iqamatime: ",iqamatime);

        return iqamatime;
    }


    private void syncData() {
        timerPray = new Timer();
        //try {
        asyncPray = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new UpdateAsync().execute();
                        }
                    });
                } catch (NullPointerException e) {
                }
            }
        };
        timerPray.schedule(asyncPray, 0, 1800000);
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
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", new Locale("en"));
        Date date = new Date();
        String currentTime = df.format(date);
        DBO.open();
        ArrayList<Ads> adsList = DBO.getAdsByTime(sp.getInt("masjedId", -1), currentTime);
        DBO.close();
        if (adsList.size() > 0) {
            for (int i = 0; i < adsList.size(); i++) {
                Ads ads = adsList.get(i);
                String adsStartTime = ads.getStartTime();
                String adsEndTime = ads.getStartTime();
                try {
                    Date start = df.parse(adsStartTime);
                    Date end = df.parse(adsEndTime);
                    Date now = df.parse(currentTime);
                    if (start.equals(now)) {
                        if ((Utils.isSaturday() && ads.isSaturday()) || (Utils.isSunday() && ads.isSunday())
                                || (Utils.isMonday() && ads.isMonday()) || (Utils.isTuesday() && ads.isTuesday())
                                || (Utils.isWednesday() && ads.isWednesday()) || (Utils.isThursday() && ads.isThursday())
                                || (Utils.isFriday() && ads.isFriday())) {
                            if (!isOpenAds) {
                                isOpenAds = true;
                                Intent intent = new Intent(activity, ShowAdsActivity.class);
                                intent.putExtra("ads", ads);
                                intent.setAction("main");
                                startActivity(intent);
                            }
                            break;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        adsRunnable = new Runnable() {
            @Override
            public void run() {
                checkAds();
            }
        };
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
        date1 = (TextView) findViewById(R.id.dateToday);
        date1.setTypeface(font);
        date1.setText((Utils.writeIslamicDate(this, hd)));
        time = (TextView) findViewById(R.id.Time);
        time.setTypeface(fontRoboto);
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", new Locale("en"));
        amPm = (TextView) findViewById(R.id.amPm);
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
        String advText = "";

        while (start < advSize) {
            String advers = advs.get(start);
            String adv[] = advers.substring(1, advers.length() - 1).split(",");
            advText += "  " + adv[0];
            start++;
        }
        String repeated = "";
//        try {
//            repeated = new String(new char[100 - advText.length()]).replace("\0", " ");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        advTitle = (TextView) findViewById(R.id.advText);
        advTitle.setText(padText(advText, advTitle.getPaint(), advTitle.getWidth()) /*+ repeated*/);
        advTitle.setTypeface(fontDroidkufi);
        advTitle.setSelected(true);
//        MarqueeViewSingle marquee = (MarqueeViewSingle) findViewById(R.id.advTxt);
//        marquee.setText1(advText);
//        marquee.setTextDirection(View.LAYOUT_DIRECTION_RTL);
//        marquee.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        advTitle.setTypeface(fontDroidkufi);
//        marquee.startMarquee();
    }

    private void clearAllStyles() {
        fajrTitle.setImageResource(R.drawable.ic_fajer);
        shroqTitle.setImageResource(R.drawable.ic_shrouq);
        duhrTitle.setImageResource(R.drawable.ic_duhr);
        asrTitle.setImageResource(R.drawable.ic_asr);
        maghribTitle.setImageResource(R.drawable.ic_magrib);
        ishaTitle.setImageResource(R.drawable.ic_isha);

        fajrTitle.setBackgroundColor(0);
//        fajrTime.setTextColor(Color.parseColor("#674426"));
        fajrTime.setBackgroundResource(0);
//        fajrIqama.setTextColor(Color.parseColor("#674426"));
        fajrIqama.setBackgroundResource(0);
//        setTextSize(fajrTitle);
        setNoTextSize(fajrIqama);
        setNoTextSize(fajrTime);

//        sunriseTime.setTextColor(Color.parseColor("#674426"));
        sunriseTime.setBackgroundColor(0);
//        sunriseIqama.setTextColor(Color.parseColor("#674426"));
        sunriseIqama.setBackgroundResource(0);
        shroqTitle.setBackgroundResource(0);
        setNoTextSize(sunriseTime);
        setNoTextSize(sunriseIqama);
//        shroqTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//                getResources().getDimension(R.dimen.shro_font_size));

        duhrTitle.setBackgroundColor(0);
//        dhuhrTime.setTextColor(Color.parseColor("#674426"));
        dhuhrTime.setBackgroundResource(0);
//        duhrIqama.setTextColor(Color.parseColor("#674426"));
        duhrIqama.setBackgroundResource(0);
        setNoTextSize(duhrIqama);
        setNoTextSize(dhuhrTime);
//        setTextSize(duhrTitle);

        asrTitle.setBackgroundColor(0);
//        asrTime.setTextColor(Color.parseColor("#674426"));
        asrTime.setBackgroundResource(0);
//        asrIqama.setTextColor(Color.parseColor("#674426"));
        asrIqama.setBackgroundResource(0);
        setNoTextSize(asrIqama);
        setNoTextSize(asrTime);
//        setTextSize(asrTitle);

        maghribTitle.setBackgroundColor(0);
//        maghribTime.setTextColor(Color.parseColor("#674426"));
        maghribTime.setBackgroundResource(0);
//        magribIqama.setTextColor(Color.parseColor("#674426"));
        magribIqama.setBackgroundResource(0);
        setNoTextSize(magribIqama);
//        setTextSize(maghribTitle);
        setNoTextSize(maghribTime);

        ishaTitle.setBackgroundColor(0);
//        ishaTime.setTextColor(Color.parseColor("#674426"));
        ishaTime.setBackgroundResource(0);
//        ishaIqama.setTextColor(Color.parseColor("#674426"));
        ishaIqama.setBackgroundResource(0);
//        setTextSize(ishaTitle);
        setNoTextSize(ishaTime);
        setNoTextSize(ishaIqama);
    }

    private void setTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size));
    }

    private void setNoTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.no_l_font_size));
    }

    private void setLargeTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.l_font_size));
    }

    private void setIqamaTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.iqama_font_size));
    }

    private void setNoLargeTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.no_l_font_size));
    }

    private void setTextColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.back_text));
    }

    private void setIqamaTextColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.googleR));
    }

    private void setPrayTextColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.googleB));
    }

    public void dispMenu(View view) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.confirm_user, null);
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
//        final AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//        final EditText ed_caption = (EditText) dialogView.findViewById(R.id.ed_caption);
//        ed_caption.requestFocus();
//        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.showSoftInput(ed_caption, InputMethodManager.SHOW_IMPLICIT);
//
////
//        Button save = (Button) dialogView.findViewById(R.id.save);
//        Button cancel = (Button) dialogView.findViewById(R.id.cancel);
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.hideSoftKeyboard(activity);
//                if (TextUtils.isEmpty(ed_caption.getText().toString().trim())) {
//                    ed_caption.setError("أدخل كلمة المرور للحساب");
//                    return;
//                }
//                if (!(ed_caption.getText().toString().trim()).equals(sp.getString("masjedPW", ""))) {
//                    ed_caption.setError("كلمة المرور غير صحيحة");
//                    return;
//                }
////                startActivity(new Intent(activity, SettingsActivity.class));//SettingsActivity ClosePhone
//                alertDialog.dismiss();
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
        startActivity(new Intent(activity, SettingsActivity.class));//SettingsActivity ClosePhone

    }

    private String[] calculate() {
        try {
            DBO.open();
            city = DBO.getCityById(cityId);
            settings = DBO.getSettings();
            DBO.close();
//            Log.e("//// ", "" + sp.getInt("cityId", 1));
            lat1 = sp.getInt("lat1", city.getLat1());
            lat2 = sp.getInt("lat2", city.getLat2());
            long1 = sp.getInt("long1", city.getLon1());
            long2 = sp.getInt("long2", city.getLon2());

            Calendar today = Calendar.getInstance();
            int hour = today.get(Calendar.HOUR_OF_DAY);
            int minute = today.get(Calendar.MINUTE);
            String timeNow = hour + ":" + minute + ":00";
            Date d = new SimpleDateFormat("HH:mm:ss", new Locale("en")).parse(timeNow);
            Calendar cnow = Calendar.getInstance();
            cnow.setTime(d);
            Date now = cnow.getTime();
            String t5 = cisha + ":00";
            Date time5 = new SimpleDateFormat("HH:mm:ss").parse(t5);
            Calendar c5 = Calendar.getInstance();
            c5.setTime(time5);
//            if (now.after(c5.getTime())) {
//                Date dt = new Date();
//                Calendar c = Calendar.getInstance();
//                c.setTime(dt);
//                c.add(Calendar.DATE, 1);
//                dt = c.getTime();
//                day = c.get(Calendar.DAY_OF_MONTH);
//                month = c.get(Calendar.MONTH) + 1;
//                year = c.get(Calendar.YEAR);
//            }
        } catch (ParseException e0) {
        }
        Hijri_Cal_Tools.calculation((double) lat1, (double) lat2, (double) long1, (double) long2,
                year, month, day);
//        Log.e("///init()", lat1 + "," + lat2 + "," + long1 + "," + long2 + "," + year + "," +
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
//        Toast.makeText(activity,sp.getInt("cityId",1)+"", Toast.LENGTH_SHORT).show();
        Context context;
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

                icfajr = sp.getString("iqsuh", "");
                icsunrise = sp.getString("iqsun", "");
                icdhohr = sp.getString("iqduh", "");
                icasr = sp.getString("iqasr", "");
                icmaghrib = sp.getString("iqmagrib", "");
                icisha = sp.getString("iqisha", "");
//                Log.i("****", cdhohr);
            }
            gv.setPrayTimes(cfajr, csunrise, cdhohr, casr, cmaghrib, cisha);
            checkNextPray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSleepPeriod() {
        String sleepOn = Utils.addToTime(sp.getString("isha", ""), settings.getCloseScreenAfterIsha()/* sp.getInt("sleepOn", 0) */ + "");
        String sleepOff = Utils.diffFromTime(sp.getString("suh", ""), settings.getRunScreenBeforeFajr()/*sp.getInt("sleepOff", 0) */ + "");
        Log.e("**//sleepOn", sleepOn + "  **");
        Log.e("**//sleepOff", sleepOff);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date start = sdf.parse(sleepOn);
            Date end = sdf.parse(sleepOff);
            if (end.before(start)) {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(Calendar.DAY_OF_YEAR, 1);
                end.setTime(mCal.getTimeInMillis());
            }
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String startDate = df.format(start);
            String endDate = df.format(end);
            spedit.putString("startTime", startDate).commit();
            spedit.putString("endTime", endDate).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkNextPray() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        try {
            String t1 = cfajr + ":00";
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(t1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(time1);

            String t2 = cdhohr + ":00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(t2);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(time2);
            String t3 = casr + ":00";
            Date time3 = new SimpleDateFormat("HH:mm:ss").parse(t3);
            Calendar c3 = Calendar.getInstance();
            c3.setTime(time3);
            String t4 = cmaghrib + ":00";
            Date time4 = new SimpleDateFormat("HH:mm:ss").parse(t4);
            Calendar c4 = Calendar.getInstance();
            c4.setTime(time4);
            String t5 = cisha + ":00";
            Date time5 = new SimpleDateFormat("HH:mm:ss").parse(t5);
            Calendar c5 = Calendar.getInstance();
            c5.setTime(time5);

            String timeNow = hour + ":" + minute + ":00";
            Date d = new SimpleDateFormat("HH:mm:ss", new Locale("en")).parse(timeNow);
            Calendar cnow = Calendar.getInstance();
            cnow.setTime(d);
            Date now = cnow.getTime();

            if (now.before(c1.getTime())) {
                nextPray = "fajr";
                spedit.putString("next_adan", "fajr").commit();
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                nextPray = "dhuhr";
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                spedit.putString("next_adan", "dhuhr").commit();
            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                nextPray = "asr";
                spedit.putString("next_adan", "asr").commit();
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();

            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                nextPray = "magrib";
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                spedit.putString("next_adan", "magrib").commit();
            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                nextPray = "isha";
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                spedit.putString("next_adan", "isha").commit();
            } else if (now.after(c5.getTime())) {
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                nextPray = "fajr";
                spedit.putString("next_adan", "fajr").commit();
            }
            gv.setNextPray(nextPray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static CharSequence padText(CharSequence text, TextPaint paint, int width) {

        // First measure the width of the text itself
        Rect textbounds = new Rect();
        paint.getTextBounds(text.toString(), 0, text.length(), textbounds);

        /**
         * check to see if it does indeed need padding to reach the target width
         */
        if (textbounds.width() > width) {
            return text;
        }

    /*
     * Measure the text of the space character (there's a bug with the
     * 'getTextBounds() method of Paint that trims the white space, thus
     * making it impossible to measure the width of a space without
     * surrounding it in arbitrary characters)
     */
        String workaroundString = "a a";
        Rect spacebounds = new Rect();
        paint.getTextBounds(workaroundString, 0, workaroundString.length(), spacebounds);

        Rect abounds = new Rect();
        paint.getTextBounds(new char[]{
                'a'
        }, 0, 1, abounds);

        float spaceWidth = spacebounds.width() - (abounds.width() * 2);

    /*
     * measure the amount of spaces needed based on the target width to fill
     * (using Math.ceil to ensure the maximum whole number of spaces)
     */
        int amountOfSpacesNeeded = (int) Math.ceil((width - textbounds.width()) / spaceWidth);

        // pad with spaces til the width is less than the text width
        return amountOfSpacesNeeded > 0 ? padRight(text.toString(), text.toString().length()
                + amountOfSpacesNeeded) : text;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            if (countDownTimer != null) countDownTimer.cancel();
            speech.stopListening();
            speech.destroy();
//            speech=null;
            Log.i(LOG_TAG, "destroy");
        }
        stopTimer = true;
        timer.cancel();
        timer.purge();
        try {
            AdsHandler.removeCallbacks(adsRunnable);
            openAds();
            if (this._Timer != null) this._Timer.cancel();
            if (this._BroadcastService != null) {
                this._BroadcastService.StopScan();
            }
        } catch (Exception e) {
        }
    }

//    @Override
//    public void onBeginningOfSpeech() {
//        Log.i(LOG_TAG, "onBeginningOfSpeech");
//    }
//
//    @Override
//    public void onBufferReceived(byte[] buffer) {
//        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
//    }
//
//    @Override
//    public void onEndOfSpeech() {
//        Log.i(LOG_TAG, "onEndOfSpeech");
//        if (speech != null) speech.startListening(recognizerIntent);
//    }
//
//    @Override
//    public void onError(int errorCode) {
//        String errorMessage = getErrorText(errorCode);
//        Log.d(LOG_TAG, "FAILED " + errorMessage);
//    }
//
//    @Override
//    public void onEvent(int arg0, Bundle arg1) {
//        Log.i(LOG_TAG, "onEvent");
//    }
//
//    @Override
//    public void onPartialResults(Bundle arg0) {
//        Log.i(LOG_TAG, "onPartialResults");
//    }
//
//    @Override
//    public void onReadyForSpeech(Bundle arg0) {
//        Log.i(LOG_TAG, "onReadyForSpeech");
//    }
//
//    @Override
//    public void onResults(Bundle results) {
//        Log.i(LOG_TAG, "onResults");
//        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        String text = "";
//        boolean a = false;
//        for (String result : matches) {
//            text += result + "\n";
//            Log.i(LOG_TAG, "onResultsText: " + text);
//            Log.i(LOG_TAG, "onResultsText: " + result + " : " + te0 + " : " + te2);
//        }
////            result = result.toLowerCase().replace(" ", "");
//        if (matches.contains(te0) || matches.contains(te1) || matches.contains(te2)
//                || matches.contains(te3)
//                || matches.contains(te4)
//                || matches.contains(te5)
//                || matches.contains(te6)
//                || matches.contains(te7)) {
//            a = true;
//            if (countDownTimer != null) countDownTimer.cancel();
//            if (timer != null) {
//                timer.cancel();
//                timer.purge();
//            }
//            if (speech != null) speech.stopListening();
//            if (speech != null) speech.destroy();
//
//            if (speech != null) {
//                speech = null;
////                    new Handler().postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
//                Intent cp = new Intent(activity, ShowPray.class);
//                cp.setAction("b");
//                cp.putExtra("currentPray", currentPray);
//                gv.setNextPray(currentPray);
//                startActivity(cp);
////                        }
////                    }, 2000);
//            }
//        }
////        }
//        if (a == false) {
//            speech.startListening(recognizerIntent);
//
//        }
//    }
//
//    @Override
//    public void onRmsChanged(float rmsdB) {
//        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
////        progressBar.setProgress((int) rmsdB);
//    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


    private class UpdateAsync extends AsyncTask<Void, Void, Boolean> {
        String serverTime = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            long wsstartDate = new Date().getTime();
            int id = sp.getInt("masjedId", -1);
            String GUID = sp.getString("masjedGUID", "");
            String lastUpdate = sp.getString(AppConst.LASTUPDATE, "20170701000000");
            String DeviceNo = sp.getString(AppConst.DeviceNo, "");
            JSONObject result = WS.syncData(id, GUID, lastUpdate, DeviceNo);

            long wsendDate = new Date().getTime();

            if (result != null && result.optBoolean("Status")) {
                serverTime = result.optLong("ResultNumber ") + "";//Utils.getFormattedCurrentDate();
                long dbstartDate = new Date().getTime();

                if (WS.InsertDataToDB(1, activity, result)) {
                    long dbendDate = new Date().getTime();
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            if (status) {
                spedit.putString(AppConst.LASTUPDATE, serverTime).commit();
                DBO.open();
                advs = DBO.getNews(Utils.getFormattedCurrentDate());
                settings = DBO.getSettings();
                DBO.close();
                showNews();
                tvMasjedName.setText(sp.getString("masjedName", ""));
                tvName.setText(sp.getString("masjedName", ""));
//                if (!TextUtils.isEmpty(sp.getString("masjedImg", ""))) {
//                    setImage(sp.getString("masjedImg", ""), ivLogo);
//                    setImage(sp.getString("masjedImg", ""), ivMasjedLogo);
//                } else {
//                    ivMasjedLogo.setImageResource(R.drawable.ic_mosque);
//                    ivLogo.setImageResource(R.drawable.ic_mosque);
//                }
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

    /**
     * Pads a string with white space on the right of the original string
     *
     * @param s The target string
     * @param n The new target length of the string
     * @return The target string padded with whitespace on the right to its new
     * length
     */
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
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
//          Log.i("***9 "," "+permissionsToRequest.size());
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
                                                //    Manifest.permission.MANAGE_DOCUMENTS,
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
            if (this.SN1 == null || this.SN1.equals("")) {
//            finish();
                getWeather(0);
            }
            if (this.SN2 == null || this.SN2.equals("")) {
                getWeather(1);
            }
            if (this._BroadcastService == null) {
                this._BroadcastService = new BroadcastService();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                this._BluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
                if (this._BluetoothAdapter != null) {
                    if (this._BroadcastService.Init(this._BluetoothAdapter, this._LocalBluetoothCallBack)) {
                        this._IsInit = true;
                        if (!this._IsScanning) {
                            this._IsScanning = true;
                            this._BroadcastService.StartScan();
//                if (this._ProgressDialog != null && this._ProgressDialog.isShowing()) {
//                    this._ProgressDialog.dismiss();
//                }
                            ProgressDialog progressDialog = new ProgressDialog(this);
//                    this._ProgressDialog = ProgressDialog.show(this, "", getString(R.string.lan_99), true, true, new C02782());
                            new Thread(new C02793()).start();
                            return;
                        }
                        return;
                    }
                }
            } else getWeather(-1);
            this._IsInit = false;
        } catch (Exception ex) {
            this._IsInit = false;
            this._IsScanning = false;
//        Toast.makeText(this, getString(R.string.lan_100) + " ex:" + ex.toString(), 0).show();
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
                    _ProgressDialog.cancel();
//                    Toast.makeText(activity,getString(R.string.lan_100), 0).show();
//                    getWeather();
                    _IsScanning = false;
                }
            } catch (Exception e) {
            }
        }
    }

    class C02782 implements DialogInterface.OnCancelListener {
        C02782() {
        }

        public void onCancel(DialogInterface dialog) {
            Toast.makeText(activity, getString(R.string.lan_100), 0).show();
//            finish();
            getWeather(-1);
        }
    }

    protected void onDestroy() {
        try {
            if (this._Timer != null) this._Timer.cancel();
            if (this._BroadcastService != null) {
                this._BroadcastService.StopScan();
            }
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    protected void AutoScan() {
        try {
            if (_Timer != null) {
                _Timer.cancel();
            }
            _Timer = new Timer();
            _Timer.schedule(new C02804(), 1000, 5000);
        } catch (Exception ex) {
            Log.e("DeviceActivity", "AutoScan:" + ex.toString());
        }
    }

    class C02804 extends TimerTask {
        C02804() {
        }

        public void run() {
            try {
                synchronized (this) {
                    if (_IsInit && _IsAllowScan) {
                        _BroadcastService.StartScan();
                    }
                }
            } catch (Exception e) {
            }
        }
    }

}
