package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.Constants;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.HorizontalMarqueeTextView;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.scheduler.SalaatAlarmReceiver;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.ScalingMode;
import com.streamaxia.android.utils.Size;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ViewEmamActivity extends AppCompatActivity implements RtmpHandler.RtmpListener, RecordHandler.RecordListener,
        EncoderHandler.EncodeListener {

    private final String TAG = PreviewLiveStreamActivity.class.getSimpleName();

    public static String streamaxiaStreamName = "AlMhrab_";
    public final static int bitrate = 500;
    public final static int width = 720;
    public final static int height = 1280;

    @BindView(R.id.preview)
    CameraPreview mCameraView;
    @BindView(R.id.chronometer)
    Chronometer mChronometer;

    private StreamaxiaPublisher mPublisher;
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
    public static String bangla_mn_bold = "fonts/bangla_mn_bold.ttf";//comfort
    public static String sansBold = "fonts/neosans_black.otf";//comfort
    private Typeface fontBangla_mn_bold;
    private Typeface fontSansBold;
    private Typeface ptBoldHeading;

    TextView dateTodayM, dateTodayH;

    private TextView tvName;
    TextView date1, time, amPm;
    private static String recPath;//= Environment.getExternalStorageDirectory().getPath() + "/" +Utils.getDateTime()+".mp4";
    private File saveDir;
    private VideoView vvVideo;
    private RelativeLayout rlLivingStream;
    private Handler timerHandler = new Handler();
    private Runnable timerRun;
    private CountDownTimer countDownTimer;
    private boolean showLive = true;
    public static final String BROADCAST = Constants.PACKAGE_NAME + ".Activity.android.action.broadcast";
    private HorizontalMarqueeTextView tvTra1, tvTra2;
    TextView fajrIqama, fajrTitle, shroqTitle,
            duhrTitle, asrTitle, ishaTitle,
            in_masgedTemp, out_masgedTemp, tvHumidity, tvMasjedName;
    private TextView magribIqama, maghribTime, asrIqama, asrTime, ishaIqama, ishaTime,
            maghribTitle, dhuhrTime, fajrTime, sunriseIqama, duhrIqama, sunriseTime;
    LinearLayout llText, llFajer, llSun, llDuhr, llAsr, llMagrib, llIsha;
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
    int matNumCur = 0;
    ArrayList<String> advs = new ArrayList<String>();
    ArrayList<String> azkar = new ArrayList<String>();
    public String nextPray;
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
    private boolean isFajrEkamaIsTime, isAlShrouqEkamaIsTime, isDhuhrEkamaIsTime, ishaEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;
    private OptionSiteClass settings;
    private double long1, long2;
    private double lat1, lat2;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private GlobalVars gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_emam_view);
        activity = this;
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);

        ButterKnife.bind(this);

//        saveDir = new File(Environment.getExternalStorageDirectory(), "AlMhrab");
//        saveDir.mkdirs();
//        recPath = saveDir.getAbsolutePath() + "/AlMhrab_" + sp.getInt("masjedId", -1)//+ "_" + khotab.getTitle()
//                + "_" + Utils.getFormattedCurrentDate() + ".mp4";
        streamaxiaStreamName = streamaxiaStreamName + sp.getInt("masjedId", -1) + "";
        mPublisher = new StreamaxiaPublisher(mCameraView, this);
        mCameraView.setScalingMode(ScalingMode.TRIM);
        try {
            mPublisher.setEncoderHandler(new EncoderHandler(this));
            mPublisher.setRtmpHandler(new RtmpHandler(this));
//            mPublisher.setRecordEventHandler(new RecordHandler(this));
            mCameraView.startCamera();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

        setStreamerDefaultValues();

        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontArial = Typeface.createFromAsset(getAssets(), arial);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);
        fontBangla_mn_bold = Typeface.createFromAsset(getAssets(), bangla_mn_bold);
        fontSansBold = Typeface.createFromAsset(getAssets(), sansBold);
        ptBoldHeading = Typeface.createFromAsset(getAssets(), "fonts/pt_bold_heading.ttf");

        DBO = new DBOperations(this);
        gv = (GlobalVars) getApplicationContext();
        spedit = sp.edit();
        cityId = sp.getInt("cityId", 1);
        DBO.open();
        city = DBO.getCityById(cityId);
        settings = DBO.getSettings();
        DBO.close();

        lat1 = sp.getInt("lat1", city.getLat1());
        lat2 = sp.getInt("lat2", city.getLat2());
        long1 = sp.getInt("long1", city.getLon1());
        long2 = sp.getInt("long2", city.getLon2());

        gv.setMousqeSettings(settings.getFajrEkama() + "", settings.getAlShrouqEkama() + "", settings.getDhuhrEkama() + ""
                , settings.getAsrEkama() + "", settings.getMagribEkama() + "", settings.getIshaEkama() + "");
        mosquSettings = gv.getMousqeSettings();
        Log.i("shor: ", settings.getAlShrouqEkama() + " time");
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

        ///// start service /////
        SalaatAlarmReceiver sar = new SalaatAlarmReceiver();
        sar.cancelAlarm(this);
        sar.setAlarm(this);
        ///// start service /////

        buildUI();
        checkTime();
        startKhotbaTimer();

    }

    private void buildUI() {
        rlLivingStream = (RelativeLayout) findViewById(R.id.rlLivingStream);
        tvName = (TextView) findViewById(R.id.tvName);
        dateTodayM = (TextView) findViewById(R.id.dateTodayM);
        dateTodayH = (TextView) findViewById(R.id.dateTodayH);
        time = (TextView) findViewById(R.id.Time);
        amPm = (TextView) findViewById(R.id.amPm);
        amPm.setVisibility(View.VISIBLE);
        time.setTypeface(fontArial);
        dateTodayM.setTypeface(ptBoldHeading);
        dateTodayH.setTypeface(ptBoldHeading);
        llText = (LinearLayout) findViewById(R.id.llText);
        llFajer = (LinearLayout) findViewById(R.id.llFajer);
        llSun = (LinearLayout) findViewById(R.id.llSunrise);
        llDuhr = (LinearLayout) findViewById(R.id.llDuhr);
        llAsr = (LinearLayout) findViewById(R.id.llAsr);
        llMagrib = (LinearLayout) findViewById(R.id.llMagrib);
        llIsha = (LinearLayout) findViewById(R.id.llIsha);
        tvName.setTypeface(fontDroidkufi);
        tvName.setText(sp.getString("masjedName", ""));
        mChronometer.setVisibility(View.GONE);

        fajrTitle = (TextView) findViewById(R.id.fajrTitle);
        fajrTime = (TextView) findViewById(R.id.fajrTime);

        shroqTitle = (TextView) findViewById(R.id.shroqTitle);
        sunriseTime = (TextView) findViewById(R.id.sunriseTime);

        duhrTitle = (TextView) findViewById(R.id.duhrTitle);
        dhuhrTime = (TextView) findViewById(R.id.dhuhrTime);

        asrTitle = (TextView) findViewById(R.id.asrTitle);
        asrTime = (TextView) findViewById(R.id.asrTime);

        maghribTitle = (TextView) findViewById(R.id.mgrbTitle);
        maghribTime = (TextView) findViewById(R.id.maghribTime);

        ishaTitle = (TextView) findViewById(R.id.ishaTitle);
        ishaTime = (TextView) findViewById(R.id.ishaTime);

        fajrTitle.setTypeface(fontDroidkufi);
        fajrTime.setTypeface(fontRoboto);
        shroqTitle.setTypeface(fontDroidkufi);
        sunriseTime.setTypeface(fontRoboto);
        duhrTitle.setTypeface(fontDroidkufi);
        dhuhrTime.setTypeface(fontRoboto);
        asrTitle.setTypeface(fontDroidkufi);
        asrTime.setTypeface(fontRoboto);
        maghribTitle.setTypeface(fontDroidkufi);
        maghribTime.setTypeface(fontRoboto);
        ishaTime.setTypeface(fontRoboto);
        ishaTitle.setTypeface(fontDroidkufi);

        timer = new Timer();
        //try {
        async = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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

    private void checkTime() {
        DateHigri hd = new DateHigri();
        dateTodayM.setText(Utils.writeMDate(this, hd));
        dateTodayH.setText(Utils.writeHDate(this, hd));
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", new Locale("en"));
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

    private void startKhotbaTimer() {
        long khotbaPeriod = (sp.getInt("appearPeriod", 20)) * 60 * 1000;
        Log.i("khotbaPeriod: ", khotbaPeriod + "");
        countDownTimer = new CountDownTimer(khotbaPeriod, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void checkNextPrayTheme() {
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
//            Toast.makeText(MainActivity.this, "" + getString(R.string.warnning), Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//            finish();
        }
//}
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
//            String t6 = csunrise + ":00";
//            Date time6 = new SimpleDateFormat("HH:mm:ss").parse(t6);
//            Calendar c6 = Calendar.getInstance();
//            c6.setTime(time6);


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
//            mosquetxt = (TextView) findViewById(R.id.nextPrayTime);
//            String cur = mosquetxt.getText().toString();


//            if(cur.equals("00:01")){
//                Intent cp = new Intent (getApplicationContext(), CurruntPray.class);
//                startActivity(cp);
//            }

//            if (sp.getString("suh", "").toLowerCase().equals(timeAsString.toLowerCase())) {
            if ((c1.getTime().before(now) || c1.getTime().equals(now))
                    && ((c11.getTime().after(now)) || c11.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                setBackground(llFajer);
                setTextColor(fajrTitle);
                setTextColor(fajrTime);
                return;
            }
            if ((c2.getTime().before(now) || c2.getTime().equals(now))
                    && ((c22.getTime().after(now)) || c22.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                setBackground(llDuhr);
                setTextColor(duhrTitle);
                setTextColor(dhuhrTime);

                return;
            }
            if ((c3.getTime().before(now) || c3.getTime().equals(now))
                    && ((c33.getTime().after(now)) || c33.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                setBackground(llAsr);
                setTextColor(asrTime);
                setTextColor(asrTitle);
                return;
            }
            if ((c4.getTime().before(now) || c4.getTime().equals(now))
                    && ((c44.getTime().after(now)) || c44.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                setBackground(llMagrib);
                setTextColor(maghribTime);
                setTextColor(maghribTitle);
                return;
            }
            if ((c5.getTime().before(now) || c5.getTime().equals(now))
                    && ((c55.getTime().after(now)) || c55.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                setBackground(llIsha);
                setTextColor(ishaTime);
                setTextColor(ishaTitle);
                return;
            }
            clearAllStyles();

            final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
            if (now.before(c1.getTime())) {
//                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr,settings.getPhoneShowAlertsBeforEkama()+"")).commit();
                nextPray = "fajr";
                globalVariable.setNextPray("fajr");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t1)));
                setBackground(llFajer);
                setTextColor(fajrTitle);
                setTextColor(fajrTime);
            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                nextPray = "dhuhr";
                globalVariable.setNextPray("dhuhr");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t2)));
                setBackground(llDuhr);
                setTextColor(duhrTitle);
                setTextColor(dhuhrTime);
            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                nextPray = "asr";
                globalVariable.setNextPray("asr");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t3)));
                setBackground(llAsr);
                setTextColor(asrTime);
                setTextColor(asrTitle);
            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                nextPray = "maghrib";
                globalVariable.setNextPray("maghrib");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t4)));
                setBackground(llMagrib);
                setTextColor(maghribTime);
                setTextColor(maghribTitle);
            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                nextPray = "isha";
                globalVariable.setNextPray("isha");
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t5)));//getIqama(t5)
                setBackground(llIsha);
                setTextColor(ishaTime);
                setTextColor(ishaTitle);
            } else if (now.after(c5.getTime())) {
                globalVariable.setNextPray("fajr");
                npt = getDifTime(dayAsString, timeAsString, tomorrowAsString, "" + (getIqama(t1)));
                setBackground(llFajer);
                setTextColor(fajrTitle);
                setTextColor(fajrTime);
            }

        } catch (ParseException e) {
            e.printStackTrace();
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
    }

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
        if (diffHours > 0)
            val = fh + " " + getString(R.string.h) + " و " + fm + " " + getString(R.string.m);
        else
            val = fm + " " + getString(R.string.m);

        return val;
    }

    private String convTime(String time) {
        String intime[] = time.split(":");
        int hour = Integer.parseInt(intime[0]);
        int minutes = Integer.parseInt(intime[1]);
        String disTime;
        String h;
        String m;
//        if (hour < 12) {
//            if (hour < 10) {
//                h = "0" + hour;
//            } else {
//                h = "" + hour;
//            }
//            if (minutes < 10) {
//                m = "0" + minutes;
//            } else {
//                m = "" + minutes;
//            }
//            disTime = h + ":" + m + " " + getString(R.string.AM);
//        } else  {
//            hour = hour - 12;
//            if (hour < 10) {
//                h = "0" + hour;
//            } else {
//                h = "" + hour;
//            }
//            if (minutes < 10) {
//                m = "0" + minutes;
//            } else {
//                m = "" + minutes;
//            }
        //            disTime = h + ":" + m + " " + getString(R.string.PM);

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
            return timeHHMM;//+ "ص";
        } else if (hour > 12) {
            hour = hour - 12;
            if (minutes < 10) {
                timeHHMM = "" + String.valueOf(hour) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = "" + String.valueOf(hour) + ":" + String.valueOf(minutes);
            }
            return timeHHMM;//+ "م";
        } else {
            if (minutes < 10) {
                timeHHMM = String.valueOf(hour) + ":0" + String.valueOf(minutes);
            } else {
                timeHHMM = String.valueOf(hour) + ":" + String.valueOf(minutes);
            }
            if (hour == 12) {
                return timeHHMM;// + "م";
            }
            return timeHHMM;// + "ص";
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

    private void clearAllStyles() {
        fajrTitle.setTextColor(Color.parseColor("#ffffff"));
        fajrTitle.setBackgroundColor(0);
        fajrTime.setTextColor(Color.parseColor("#ffffff"));
        fajrTime.setBackgroundResource(0);
        llFajer.setBackgroundResource(0);
//        setTextSize(fajrTitle);
//        setTextSize(fajrTime);

        sunriseTime.setTextColor(Color.parseColor("#ffffff"));
        sunriseTime.setBackgroundColor(0);
        llSun.setBackgroundResource(0);
        shroqTitle.setTextColor(Color.parseColor("#ffffff"));
        shroqTitle.setBackgroundResource(0);
//        setTextSize(sunriseTime);
//        setTextSize(shroqTitle);

        duhrTitle.setTextColor(Color.parseColor("#ffffff"));
        duhrTitle.setBackgroundColor(0);
        dhuhrTime.setTextColor(Color.parseColor("#ffffff"));
        dhuhrTime.setBackgroundResource(0);
        llDuhr.setBackgroundResource(0);
//        setTextSize(dhuhrTime);
//        setTextSize(duhrTitle);

        asrTitle.setTextColor(Color.parseColor("#ffffff"));
        asrTitle.setBackgroundColor(0);
        asrTime.setTextColor(Color.parseColor("#ffffff"));
        asrTime.setBackgroundResource(0);
        llAsr.setBackgroundResource(0);
//        setTextSize(asrTime);
//        setTextSize(asrTitle);

        maghribTitle.setTextColor(Color.parseColor("#ffffff"));
        maghribTitle.setBackgroundColor(0);
        maghribTime.setTextColor(Color.parseColor("#ffffff"));
        maghribTime.setBackgroundResource(0);
        llMagrib.setBackgroundResource(0);
//        setTextSize(maghribTitle);
//        setTextSize(maghribTime);

        ishaTitle.setTextColor(Color.parseColor("#ffffff"));
        ishaTitle.setBackgroundColor(0);
        ishaTime.setTextColor(Color.parseColor("#ffffff"));
        ishaTime.setBackgroundResource(0);
        llIsha.setBackgroundResource(0);
//        setTextSize(ishaTitle);
//        setTextSize(ishaTime);
    }

    private void setTextColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.black));
    }

    private void setBackground(LinearLayout linearLayout) {
        linearLayout.setBackground(getResources().getDrawable(R.drawable.active));
    }

    private String[] calculate() {
        Calendar today = Calendar.getInstance();
        day = today.get(Calendar.DAY_OF_MONTH);
        month = today.get(Calendar.MONTH) + 1;
        year = today.get(Calendar.YEAR);
        try {
            DBO.open();
            city = DBO.getCityById(cityId);
            settings = DBO.getSettings();
            DBO.close();
            Log.e("//// ", "" + sp.getInt("cityId", 1));
            lat1 = sp.getInt("lat1", city.getLat1());
            lat2 = sp.getInt("lat2", city.getLat2());
            long1 = sp.getInt("long1", city.getLon1());
            long2 = sp.getInt("long2", city.getLon2());

//            Calendar today = Calendar.getInstance();
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
            if (now.after(c5.getTime())) {
                Date dt = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(dt);
                c.add(Calendar.DATE, 1);
                dt = c.getTime();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH) + 1;
                year = c.get(Calendar.YEAR);
            }
        } catch (ParseException e0) {
        }
        Hijri_Cal_Tools.calculation((double) lat1, (double) lat2, (double) long1, (double) long2,
                year, month, day);
        Log.e("///init()", lat1 + "," + lat2 + "," + long1 + "," + long2 + "," + year + "," +
                month + "," + day);
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
//            checkNextPray();
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
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                nextPray = "dhuhr";
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icdhohr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                spedit.putString("next_adan", "dhuhr").commit();
            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                nextPray = "asr";
                spedit.putString("next_adan", "asr").commit();
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icasr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();

            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                nextPray = "magrib";
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icmaghrib, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                spedit.putString("next_adan", "magrib").commit();
            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                nextPray = "isha";
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icisha, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                spedit.putString("next_adan", "isha").commit();
            } else if (now.after(c5.getTime())) {
                spedit.putString("phoneAlert", Utils.setPhoneAlert(icfajr, settings.getPhoneShowAlertsBeforEkama() + "")).commit();
                nextPray = "fajr";
                spedit.putString("next_adan", "fajr").commit();
            }
            gv.setNextPray(nextPray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
//                stopStreaming();
//                stopChronometer();
//                mChronometer.setBase(SystemClock.elapsedRealtime());
//                mChronometer.start();
//                mPublisher.setVideoBitRate(720);
//                mPublisher.startPublish("rtmp://rtmp.streamaxia.com/streamaxia/" + streamaxiaStreamName);
//                mPublisher.startRecord(recPath);
            } else {
                finish();
                Toast.makeText(this, "يجب السماح باستخدام الكاميرا", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "لا يوجد كاميرا متصلة", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
            mCameraView.stopCamera();
//            mPublisher.pauseRecord();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (PlaySound.isPlay(activity)) {
                PlaySound.stop(activity);
            }
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
//            mPublisher.stopPublish();
//            mPublisher.stopRecord();
            if (countDownTimer != null) countDownTimer.cancel();
//            MainActivity.isOpenSermon = true;
//            Log.i("recPath: ", recPath);
//            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
//            DateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
//            Date date = df.parse(khotab.getDateKhotab());
//            String DateKhotab = sdf.format(date);
//
//            Intent intent = new Intent(BROADCAST);
//            Bundle extras = new Bundle();
//            extras.putString("recPath", recPath);
//            extras.putInt("IdKhotab", khotab.getId());
//            extras.putString("DateKhotab", DateKhotab);
//            intent.putExtras(extras);
//            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

//    private void checkTime() {
//        DateHigri hd = new DateHigri();
//        date1.setText(Utils.writeIslamicDate1(this, hd));
//        DateFormat timeNow = new SimpleDateFormat("hh:mmss", new Locale("en"));
//        DateFormat ampm = new SimpleDateFormat("a", new Locale("ar"));
//        amPm.setText(ampm.format(Calendar.getInstance().getTime()));
//        Calendar c = Calendar.getInstance();
//        String timeText = timeNow.format(c.getTime());
//        SpannableString string = new SpannableString(timeText);
//        string.setSpan(new RelativeSizeSpan((0.5f)), 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        time.setText(string);
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkTime();
//            }
//        }, 1000);
//    }

    private void stopStreaming() {
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.setScreenOrientation(newConfig.orientation);
    }


    private void setStreamerDefaultValues() {
        try {
            if (mPublisher != null) {
                List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
                Size resolution = sizes.get(0);
//                mPublisher.setVideoOutputResolution(resolution.width, resolution.height, this.getResources().getConfiguration().orientation);
                mPublisher.setVideoOutputResolution(480, 640, this.getResources().getConfiguration().orientation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void setStatusMessage(final String msg) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("////*: ", "[" + msg + "]");
//                    Toast.makeText(activity, "[" + msg + "]", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * EncoderHandler implementation
    * */

    @Override
    public void onNetworkWeak() {

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }


    /*
    * RecordHandler implementation
    * */

    @Override
    public void onRecordPause() {

    }

    @Override
    public void onRecordResume() {

    }

    @Override
    public void onRecordStarted(String s) {
//        Toast.makeText(activity, "[" + s + "]", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRecordFinished(String s) {
//        try {
//            Toast.makeText(activity, "[" + s + "]", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            finish();
//        }
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    /*
    * RTMPListener implementation
    * */

    @Override
    public void onRtmpConnecting(String s) {
        setStatusMessage(s);
    }

    @Override
    public void onRtmpConnected(String s) {
        setStatusMessage(s);
    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {
        setStatusMessage("STOPPED");
    }

    @Override
    public void onRtmpDisconnected() {
        setStatusMessage("Disconnected");
    }

    @Override
    public void onRtmpVideoFpsChanged(double v) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Video bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Video bitrate: %d bps", rate));
        }

    }

    @Override
    public void onRtmpAudioBitrateChanged(double v) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    @Override
    public void onRtmpAuthenticationg(String s) {

    }

    private void stopChronometer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.stop();
    }

    private void handleException(Exception e) {
        try {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("streaming: ", e.getMessage());
//            mPublisher.stopPublish();
//            mPublisher.stopRecord();
        } catch (Exception e1) {
            // Ignore
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}