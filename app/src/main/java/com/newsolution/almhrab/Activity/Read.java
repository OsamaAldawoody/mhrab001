package com.newsolution.almhrab.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;

import com.newsolution.almhrab.scheduler.SalaatAlarmReceiver;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Read extends Activity {

    LinearLayout llText, llFajer, llSun, llDuhr, llAsr, llMagrib, llIsha;

    private double long1, long2;
    private double lat1, lat2;
    private SharedPreferences sp;
    private GlobalVars gv;
    private SharedPreferences.Editor spedit;
    TextView date1, time, amPm, fajrTitle, shroqTitle,
            duhrTitle, asrTitle, ishaTitle,
            in_masgedTemp, out_masgedTemp, tvHumidity, tvMasjedName;
    private TextView maghribTime, asrTime, ishaTime,
            maghribTitle, dhuhrTime, fajrTime, sunriseTime;
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
    private int cityId;
    private DBOperations DBO;
    private Activity activity;
    private City city;
    private boolean isFajrEkamaIsTime, isAlShrouqEkamaIsTime, isDhuhrEkamaIsTime, ishaEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;
    private OptionSiteClass settings;
    private ImageView sound_stop;
    private int action = 1;
    private String pray;
    private int period;
    private CountDownTimer countDownTimer;
    private Runnable run;
    private Handler handler;
    public static String droidkufiBold = "fonts/droid_kufi_bold.ttf";
    public static String droidkufi = "fonts/droidkufi_regular.ttf";
    private Typeface font;
    private Typeface fontDroidkufi;
    public static String roboto = "fonts/roboto.ttf";
    private Typeface fontRoboto;
    private LinearLayout llAzkar;
    TextView azkarDisc1, azkarDisc2, azkarDisc3;
    private RelativeLayout rlNews;
    private LinearLayout rlTitle;
    private String TAG = Read.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private int dispCurrentPray() {
        pray = getIntent().getStringExtra("pray");
        if (pray.equals("fajr")) {
            action = 1;
        }
        if (pray.equals("dhuhr")) {
            action = 2;
        }
        if (pray.equals("asr")) {
            action = 3;
        }
        if (pray.equals("magrib")) {
            action = 4;
        }
        if (pray.equals("isha")) {
            action = 5;
        }
        return action;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/neosansarabic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_read);
        activity = this;
        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);
        DBO = new DBOperations(this);
        gv = (GlobalVars) getApplicationContext();
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        cityId = sp.getInt("cityId", 1);
        DBO.open();
        city = DBO.getCityById(cityId);
        settings = DBO.getSettings();
        advs = DBO.getNews(Utils.getFormattedCurrentDate());
        azkar = DBO.getAzkar(dispCurrentPray());
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

        SalaatAlarmReceiver sar = new SalaatAlarmReceiver();
        sar.cancelAlarm(this);
        sar.setAlarm(this);

        stopSilentMode();
        buildUI();
        showNews();
        if (TextUtils.isEmpty(sp.getString("TempIn", ""))) {
            getWeather(0);
        } else {
            in_masgedTemp.setText(sp.getString("TempIn", ""));
        }
        if (TextUtils.isEmpty(sp.getString("TempOut", "")) || TextUtils.isEmpty(sp.getString("HumOut", ""))) {
            getWeather(1);
        } else {
            out_masgedTemp.setText(sp.getString("TempOut", ""));
            tvHumidity.setText(sp.getString("HumOut", "") + "%");

        }
        if (TextUtils.isEmpty(sp.getString("TempOut", "")) && TextUtils.isEmpty(sp.getString("TempIn", ""))) {
            getWeather(-1);
        }

        screenVisibilty();
        try {
            checkTime();
        } catch (Exception ignored) {
        }
    }

    private void stopSilentMode() {
        try {
            AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxVolume;
            if (mAudioManager != null) {
                maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FX_KEY_CLICK);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void checkTime() {
        DateHigri hd = new DateHigri();
        date1 = (TextView) findViewById(R.id.dateToday);
        date1.setText(Utils.writeIslamicDate(this, hd));
        time = (TextView) findViewById(R.id.Time);
        time.setTypeface(fontRoboto);
        date1.setTypeface(font);
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (azkar.size() > 0)
            animAzkar();
    }

    private void screenVisibilty() {
        pray = getIntent().getStringExtra("pray");
        switch (pray) {
            case "fajr":
                period = settings.getFajrAzkarTimer();
                break;
            case "dhuhr":
                period = settings.getDhuhrAzkarTimer();
                break;
            case "asr":
                period = settings.getAsrAzkarTimer();
                break;
            case "magrib":
                period = settings.getMagribAzkarTimer();
                break;
            case "isha":
                period = settings.getIshaAzkarTimer();
                break;
        }

        countDownTimer = new CountDownTimer(period * (60 * 1000), 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (countDownTimer != null) countDownTimer.cancel();
                finish();
            }

        }.start();
    }

    public void buildUI() {
        llAzkar = (LinearLayout) findViewById(R.id.llAzkar);
        azkarDisc1 = (TextView) findViewById(R.id.read1);
        azkarDisc2 = (TextView) findViewById(R.id.read2);
        azkarDisc3 = (TextView) findViewById(R.id.read3);
        azkarDisc3.setVisibility(View.GONE);
        azkarDisc1.setTypeface(fontDroidkufi);
        azkarDisc2.setTypeface(fontDroidkufi);
        azkarDisc3.setTypeface(fontDroidkufi);

        RelativeLayout rlMasjedTitle = (RelativeLayout) findViewById(R.id.rlMasjedTitle);
        rlMasjedTitle.setVisibility(View.GONE);
        llText = (LinearLayout) findViewById(R.id.llText);
        llFajer = (LinearLayout) findViewById(R.id.llFajer);
        llSun = (LinearLayout) findViewById(R.id.llSunrise);
        llDuhr = (LinearLayout) findViewById(R.id.llDuhr);
        llAsr = (LinearLayout) findViewById(R.id.llAsr);
        llMagrib = (LinearLayout) findViewById(R.id.llMagrib);
        llIsha = (LinearLayout) findViewById(R.id.llIsha);
        tvMasjedName = (TextView) findViewById(R.id.tvMasjedName);
        AppCompatImageView ivMasjedLogo = (AppCompatImageView) findViewById(R.id.ivMasjedLogo);
        rlNews = (RelativeLayout) findViewById(R.id.rlNews);
        rlTitle = (LinearLayout) findViewById(R.id.rlTitle);
        AppCompatImageView ivLogo = (AppCompatImageView) findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.GONE);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        out_masgedTemp = (TextView) findViewById(R.id.outMasgedasged);
        in_masgedTemp = (TextView) findViewById(R.id.in_masged);
        ImageView ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispMenu(view);
            }
        });
        TextView tvIn = (TextView) findViewById(R.id.tvIn);
        TextView tvOut = (TextView) findViewById(R.id.tvOut);
        TextView tvHum = (TextView) findViewById(R.id.tvHum);
        tvHum.setText(getString(R.string.outHum));
        tvMasjedName.setTypeface(fontDroidkufi);
        tvIn.setTypeface(fontDroidkufi);
        tvOut.setTypeface(fontDroidkufi);
        tvHum.setTypeface(fontDroidkufi);

        tvMasjedName.setText(sp.getString("masjedName", ""));
        tvName.setText(sp.getString("masjedName", ""));
        if (!TextUtils.isEmpty(sp.getString("masjedImg", ""))) {
            Utils.setImage(activity, sp.getString("masjedImg", ""), ivLogo);
            Utils.setImage(activity, sp.getString("masjedImg", ""), ivMasjedLogo);
        } else {
            ivMasjedLogo.setImageResource(R.drawable.ic_mosque);
            ivLogo.setImageResource(R.drawable.ic_mosque);
        }
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
        out_masgedTemp.setTypeface(fontRoboto);
        in_masgedTemp.setTypeface(fontRoboto);
        tvHumidity.setTypeface(fontRoboto);
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

        sound_stop = (ImageView) findViewById(R.id.sound_stop);
        sound_stop.setVisibility(View.GONE);

        sound_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.stop(getBaseContext());
                sound_stop.setVisibility(View.GONE);
                Log.i("sound", "completed");
            }
        });
        timer = new Timer();
        TimerTask async = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (PlaySound.isPlay(getBaseContext())) {
                                sound_stop.setVisibility(View.VISIBLE);
                            } else sound_stop.setVisibility(View.GONE);
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
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            clearAllStyles();

            if ((c1.getTime().before(now) || c1.getTime().equals(now))
                    && ((c11.getTime().after(now)) || c11.getTime().equals(now))) {
                setBackground(llFajer);
                setTextColor(fajrTitle);
                setTextColor(fajrTime);
                return;
            }
            if ((c2.getTime().before(now) || c2.getTime().equals(now))
                    && ((c22.getTime().after(now)) || c22.getTime().equals(now))) {
                setBackground(llDuhr);
                setTextColor(duhrTitle);
                setTextColor(dhuhrTime);

                return;
            }
            if ((c3.getTime().before(now) || c3.getTime().equals(now))
                    && ((c33.getTime().after(now)) || c33.getTime().equals(now))) {
                setBackground(llAsr);
                setTextColor(asrTime);
                setTextColor(asrTitle);
                return;
            }
            if ((c4.getTime().before(now) || c4.getTime().equals(now))
                    && ((c44.getTime().after(now)) || c44.getTime().equals(now))) {
                setBackground(llMagrib);
                setTextColor(maghribTime);
                setTextColor(maghribTitle);
                return;
            }
            if ((c5.getTime().before(now) || c5.getTime().equals(now))
                    && ((c55.getTime().after(now)) || c55.getTime().equals(now))) {
                setBackground(llIsha);
                setTextColor(ishaTime);
                setTextColor(ishaTitle);
                return;
            }
            clearAllStyles();

            final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
            if (now.before(c1.getTime())) {
                nextPray = "fajr";
                globalVariable.setNextPray("fajr");
                setBackground(llFajer);
                setTextColor(fajrTitle);
                setTextColor(fajrTime);
            } else if (now.after(c1.getTime()) && now.before(c2.getTime())) {
                nextPray = "dhuhr";
                globalVariable.setNextPray("dhuhr");
                setBackground(llDuhr);
                setTextColor(duhrTitle);
                setTextColor(dhuhrTime);
            } else if (now.after(c2.getTime()) && now.before(c3.getTime())) {
                nextPray = "asr";
                globalVariable.setNextPray("asr");
                setBackground(llAsr);
                setTextColor(asrTime);
                setTextColor(asrTitle);
            } else if (now.after(c3.getTime()) && now.before(c4.getTime())) {
                nextPray = "maghrib";
                globalVariable.setNextPray("maghrib");
                setBackground(llMagrib);
                setTextColor(maghribTime);
                setTextColor(maghribTitle);
            } else if (now.after(c4.getTime()) && now.before(c5.getTime())) {
                nextPray = "isha";
                globalVariable.setNextPray("isha");
                setBackground(llIsha);
                setTextColor(ishaTime);
                setTextColor(ishaTitle);
            } else if (now.after(c5.getTime())) {
                globalVariable.setNextPray("fajr");
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


    private void animAdvs() {
        TextView advTitle;
        int advSize = advs.size();
        Log.e("advSize : = ", "" + advSize);
        int start = 0;
        StringBuilder advText = new StringBuilder();

        while (start < advSize) {
            String advers = advs.get(start);
            String[] adv = advers.substring(1, advers.length() - 1).split(",");
            advText.append("    ").append(adv[0]);
            start++;
        }
        advTitle = (TextView) findViewById(R.id.advText);
        advTitle.setText(Utils.padText(advText.toString(), advTitle.getPaint(), advTitle.getWidth()));
        advTitle.setTypeface(fontDroidkufi);
        advTitle.setSelected(true);

    }

    private void animAzkar() {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);

        int matSize = azkar.size();
        if (matNumCur >= matSize) {
            matNumCur = 0;
        }
        String material1 = "", material2 = "";
        try {
            material1 = azkar.get(matNumCur);
            matNumCur++;
            if (matNumCur < matSize) {
                material2 = azkar.get(matNumCur);
            }
        } catch (Exception e) {
            e.printStackTrace();
            matNumCur = 0;
            animAzkar();
        }
        if (!TextUtils.isEmpty(material1)) {
            azkarDisc1.setVisibility(View.VISIBLE);
            azkarDisc1.setText(material1);
        } else
            azkarDisc1.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(material2)) {
            azkarDisc2.setVisibility(View.VISIBLE);
            azkarDisc2.setText(material2);
        } else
            azkarDisc2.setVisibility(View.GONE);


        llAzkar.startAnimation(fadeIn);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(false);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(false);

        run = new Runnable() {
            @Override
            public void run() {
                matNumCur++;/**/
                animAzkar();
            }
        };
        handler = new Handler();
        handler.postDelayed(run, 30000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (handler != null)
            handler.removeCallbacks(run);
        if (countDownTimer != null) countDownTimer.cancel();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (handler != null)
            handler.removeCallbacks(run);
        if (countDownTimer != null) countDownTimer.cancel();

    }

    private void clearAllStyles() {
        fajrTitle.setTextColor(Color.parseColor("#ffffff"));
        fajrTitle.setBackgroundColor(0);
        fajrTime.setTextColor(Color.parseColor("#ffffff"));
        fajrTime.setBackgroundResource(0);
        llFajer.setBackgroundResource(0);

        sunriseTime.setTextColor(Color.parseColor("#ffffff"));
        sunriseTime.setBackgroundColor(0);
        llSun.setBackgroundResource(0);
        shroqTitle.setTextColor(Color.parseColor("#ffffff"));
        shroqTitle.setBackgroundResource(0);


        duhrTitle.setTextColor(Color.parseColor("#ffffff"));
        duhrTitle.setBackgroundColor(0);
        dhuhrTime.setTextColor(Color.parseColor("#ffffff"));
        dhuhrTime.setBackgroundResource(0);
        llDuhr.setBackgroundResource(0);

        asrTitle.setTextColor(Color.parseColor("#ffffff"));
        asrTitle.setBackgroundColor(0);
        asrTime.setTextColor(Color.parseColor("#ffffff"));
        asrTime.setBackgroundResource(0);
        llAsr.setBackgroundResource(0);

        maghribTitle.setTextColor(Color.parseColor("#ffffff"));
        maghribTitle.setBackgroundColor(0);
        maghribTime.setTextColor(Color.parseColor("#ffffff"));
        maghribTime.setBackgroundResource(0);
        llMagrib.setBackgroundResource(0);

        ishaTitle.setTextColor(Color.parseColor("#ffffff"));
        ishaTitle.setBackgroundColor(0);
        ishaTime.setTextColor(Color.parseColor("#ffffff"));
        ishaTime.setBackgroundResource(0);
        llIsha.setBackgroundResource(0);
    }

    private void setTextColor(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.black));
    }

    private void setBackground(LinearLayout linearLayout) {
        linearLayout.setBackground(getResources().getDrawable(R.drawable.active));
    }

    public void dispMenu(View view) {
        startActivity(new Intent(activity, SettingsActivity.class));

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
        } catch (ParseException ignored) {
        }
        Hijri_Cal_Tools.calculation(lat1, lat2, long1, long2, year, month, day);
        Log.e(TAG, lat1 + "," + lat2 + "," + long1 + "," + long2 + "," + year + "," + month + "," + day);
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


    @Override
    public void onBackPressed() {
        if (PlaySound.isPlay(activity)) {
            PlaySound.stop(activity);
        }
        finish();
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void getWeather(final int action) {
        in_masgedTemp.setText(sp.getString("TempIn", "24"));
        out_masgedTemp.setText(sp.getString("TempOut", "30"));
        tvHumidity.setText(sp.getString("HumOut", "35") + "%");
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

}
