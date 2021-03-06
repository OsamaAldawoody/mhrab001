package com.newsolution.almhrab.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.Khotab;
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;

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

public class ClosePhone extends Activity {
    LinearLayout llText, llFajer, llSun, llDuhr, llAsr, llMagrib, llIsha;
    private double long1, long2;
    private double lat1, lat2;
    private SharedPreferences sp;
    private GlobalVars gv;
    private SharedPreferences.Editor spedit;
    private TextView tvIqama;
    private TextView prev_tet1;
    private TextView prev_tet2;
    private TextView prev_tet3;
    private TextView in_masgedTemp;
    private TextView out_masgedTemp;
    private TextView tvHumidity;
    private TextView maghribTime, asrTime, ishaTime, fajrTitle, shroqTitle, duhrTitle,
            asrTitle, ishaTitle, maghribTitle, dhuhrTime, fajrTime, sunriseTime;
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
    public String nextPray = "";
    public String mosqueName;
    String[] mosquSettings;
    String[] prayTimes;
    private Timer timer;
    private TimerTask async;
    private int cityId;
    private DBOperations DBO;
    private Timer timerPray;
    private TimerTask asyncPray;
    private AudioManager mAudioManager;
    private int maxVolume;
    private Activity activity;
    private City city;
    private boolean isFajrEkamaIsTime, isDhuhrEkamaIsTime, ishaEkamaIsTime, isAlShrouqEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;
    private OptionSiteClass settings;
    private ImageView sound_stop;
    private String iqamatime = "";
    private String currentPray = "";
    private boolean stopTimer = false;
    private TextView remainTime1;
    private Typeface digital;
    private TextView tvIn, tvOut, tvHum;
    public static String droidkufiBold = "fonts/droid_kufi_bold.ttf";
    public static String droidkufi = "fonts/droidkufi_regular.ttf";
    private Typeface font;
    private Typeface fontDroidkufi;
    public static String roboto = "fonts/roboto.ttf";
    private Typeface fontRoboto;
    public static String comfort = "fonts/comfort.ttf";
    private Typeface fontComfort;
    private RelativeLayout rlNews;
    private LinearLayout rlTitle;
    public boolean isOpenSermon = false;

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
        setContentView(R.layout.activity_close_phone);
        activity = this;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager != null) {
            maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontComfort = Typeface.createFromAsset(getAssets(), comfort);
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

        ImageView ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispMenu(view);
            }
        });
        tvIqama = (TextView) findViewById(R.id.tvIqama);
        tvIn = (TextView) findViewById(R.id.tvIn);
        tvOut = (TextView) findViewById(R.id.tvOut);
        tvHum = (TextView) findViewById(R.id.tvHum);
        remainTime1 = (TextView) findViewById(R.id.t1);

        digital = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        remainTime1.setTypeface(digital);
        remainTime1.setTextColor(getResources().getColor(R.color.backLogo));
        tvIqama.setTypeface(fontDroidkufi);

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
            tvHumidity.setText(sp.getString("HumOut", "") +"%");

        }
        if (TextUtils.isEmpty(sp.getString("TempOut", "")) && TextUtils.isEmpty(sp.getString("TempIn", ""))) {
            getWeather(-1);
        }

    }

    public void buildUI() {
        llText = (LinearLayout) findViewById(R.id.llText);
        llFajer = (LinearLayout) findViewById(R.id.llFajer);
        llSun = (LinearLayout) findViewById(R.id.llSunrise);
        llDuhr = (LinearLayout) findViewById(R.id.llDuhr);
        llAsr = (LinearLayout) findViewById(R.id.llAsr);
        llMagrib = (LinearLayout) findViewById(R.id.llMagrib);
        llIsha = (LinearLayout) findViewById(R.id.llIsha);
        RelativeLayout rlMasjedTitle = (RelativeLayout) findViewById(R.id.rlMasjedTitle);
        rlMasjedTitle.setVisibility(View.GONE);
        AppCompatImageView ivMasjedLogo = (AppCompatImageView) findViewById(R.id.ivMasjedLogo);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        out_masgedTemp = (TextView) findViewById(R.id.outMasgedasged);
        in_masgedTemp = (TextView) findViewById(R.id.in_masged);
        TextView tvMasjedName = (TextView) findViewById(R.id.tvMasjedName);
        rlNews = (RelativeLayout) findViewById(R.id.rlNews);
        rlTitle = (LinearLayout) findViewById(R.id.rlTitle);
        AppCompatImageView ivLogo = (AppCompatImageView) findViewById(R.id.ivLogo);
        ivLogo.setVisibility(View.GONE);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvHum.setText(R.string.outHum);
        tvMasjedName.setTypeface(fontDroidkufi);
        tvIn.setTypeface(fontDroidkufi);
        tvOut.setTypeface(fontDroidkufi);
        tvHum.setTypeface(fontDroidkufi);
        tvMasjedName.setText(sp.getString("masjedName", ""));
        tvName.setText(sp.getString("masjedName", ""));
        if (!TextUtils.isEmpty(sp.getString("masjedImg", ""))) {
            setImage(sp.getString("masjedImg", ""), ivLogo);
            setImage(sp.getString("masjedImg", ""), ivMasjedLogo);
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

        prev_tet1 = (TextView) findViewById(R.id.prev_tet1);
        prev_tet2 = (TextView) findViewById(R.id.prev_tet2);
        prev_tet3 = (TextView) findViewById(R.id.prev_tet3);


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
        setCloseNotificationTitle();

        sound_stop = (ImageView) findViewById(R.id.sound_stop);
        sound_stop.setVisibility(View.GONE);

        sound_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.stop(getBaseContext());
                sound_stop.setVisibility(View.GONE);
            }
        });
        try {
            checkTime();
        } catch (Exception ignored) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        async = new TimerTask() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (PlaySound.isPlay(getBaseContext())) {
                                stopSilentMode();
                                sound_stop.setVisibility(View.VISIBLE);
                            } else sound_stop.setVisibility(View.GONE);
                            if (stopTimer) {
                                return;
                            }
                            getPrayerTimes();
                            checkNextPrayTheme();
                        }
                    });
                } catch (NullPointerException ignored) {
                }
            }
        };
        timer.schedule(async, 0, 1000);
    }

    private void checkTime() {
        DateHigri hd = new DateHigri();
        TextView date1 = (TextView) findViewById(R.id.dateToday);
        date1.setText(Utils.writeIslamicDate(this, hd));
        TextView time = (TextView) findViewById(R.id.Time);
        time.setTypeface(fontRoboto);
        date1.setTypeface(font);
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", new Locale("en"));
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

    public void checkNextPrayTheme() {
        remainTime1.setTypeface(digital);
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);

        fajrTime.setText(convTime(cfajr));
        sunriseTime.setText(convTime(csunrise));
        dhuhrTime.setText(convTime(cdhohr));
        asrTime.setText(convTime(casr));
        maghribTime.setText(convTime(cmaghrib));
        ishaTime.setText(convTime(cisha));
        setIqamaTime();
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
            if ((c1.getTime().before(now) || c1.getTime().equals(now))
                    && ((c11.getTime().after(now)) || c11.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                setBackground(llFajer);
                setTextColor(fajrTitle);
                setTextColor(fajrTime);
                remainTime1.setText(npt);
                tvIqama.setText(TextUtils.isEmpty(npt) ?  getString(R.string.itPrayTime)  : getString(R.string.remainingToEqama));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t11));
                currentPray = "fajr";
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText( getString(R.string.itPrayTime) );
                    remainTime1.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c2.getTime().before(now) || c2.getTime().equals(now))
                    && ((c22.getTime().after(now)) || c22.getTime().equals(now))) {
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                setBackground(llDuhr);
                setTextColor(duhrTitle);
                setTextColor(dhuhrTime);
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t22));
                currentPray = "dhuhr";
                if (!isFriday()) {
                    remainTime1.setText(npt);
                    tvIqama.setText(TextUtils.isEmpty(npt) ?  getString(R.string.itPrayTime)  : getString(R.string.remainingToEqama));
                } else {
                    tvIqama.setText("صلاة الجمعة");
                    remainTime1.setText("");
                    if (!isOpenSermon) playSermon();
                }
                if (iqamatime.equals("00:00:00")) {
                    remainTime1.setText("");
                    if (!isFriday()) {
                        tvIqama.setText( getString(R.string.itPrayTime) );
                        runVoiceRecognition(currentPray);
                    }
                }
                return;
            }
            if ((c3.getTime().before(now) || c3.getTime().equals(now))
                    && ((c33.getTime().after(now)) || c33.getTime().equals(now))) {
                MainActivity.isOpenSermon = false;
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                setBackground(llAsr);
                setTextColor(asrTime);
                setTextColor(asrTitle);
                remainTime1.setText(npt);
                tvIqama.setText(TextUtils.isEmpty(npt) ?  getString(R.string.itPrayTime)  : getString(R.string.remainingToEqama));
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t33));
                currentPray = "asr";
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText( getString(R.string.itPrayTime) );
                    remainTime1.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c4.getTime().before(now) || c4.getTime().equals(now))
                    && ((c44.getTime().after(now)) || c44.getTime().equals(now))) {
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                currentPray = "magrib";
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t44));
                setBackground(llMagrib);
                setTextColor(maghribTime);
                setTextColor(maghribTitle);
                remainTime1.setText(npt);
                tvIqama.setText(TextUtils.isEmpty(npt) ?  getString(R.string.itPrayTime)  : getString(R.string.remainingToEqama));
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText(getString(R.string.itPrayTime) );
                    remainTime1.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }
            if ((c5.getTime().before(now) || c5.getTime().equals(now))
                    && ((c55.getTime().after(now)) || c55.getTime().equals(now))) {
                iqamatime = getDifferentTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                currentPray = "isha";
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (t55));
                setBackground(llIsha);
                setTextColor(ishaTime);
                setTextColor(ishaTitle);
                tvIqama.setText(TextUtils.isEmpty(npt) ?  getString(R.string.itPrayTime)  : getString(R.string.remainingToEqama));
                remainTime1.setText(npt);
                if (iqamatime.equals("00:00:00")) {
                    tvIqama.setText( getString(R.string.itPrayTime) );
                    remainTime1.setText("");
                    runVoiceRecognition(currentPray);
                }
                return;
            }

            clearAllStyles();

            final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
            if (now.before(c1.getTime())) {
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
                MainActivity.isOpenSermon = false;
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
                npt = getDifTime(dayAsString, timeAsString, dayAsString, "" + (getIqama(t5)));
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
                remainTime1.setText("");
                tvIqama.setText(String.format(getString(R.string.isAthanTime), next_adan));
                remainTime1.setTypeface(digital);
            } else {
                remainTime1.setTypeface(digital);
                remainTime1.setText(npt);
                tvIqama.setText(R.string.remainingToAthan);
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
            finish();
        }

    }

    private void setIqamaTime() {

        String[] mosquSettings = {settings.getFajrEkama() + "", settings.getAlShrouqEkama() + "", settings.getDhuhrEkama() + "", settings.getAsrEkama() + ""
                , settings.getMagribEkama() + "", settings.getIshaEkama() + ""};
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

        if (diffSeconds == 0)
            fs = "0" + diffSeconds;
        val = val + ":" + fs;

        return val;
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
        if (diffHours > 0) {
            if (diffHours < 10)
                fh = "0" + fh;
            val = fh;
        } else if (diffMinutes > 0) {
            if (diffMinutes < 10)
                fm = "0" + fm;
            val = fm;
            if (diffMinutes == 1) val = "60";
        } else if (diffSeconds > 0) {
            if (diffSeconds < 10)
                fs = "0" + fs;
            val = fs;
        } else val = "";
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

    }

    private String getIqama(String time) {
        String intime[] = time.split(":");
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


    private void animAdvs() {
        TextView advTitle;
        int advSize = advs.size();
        Log.e("advSize : = ", "" + advSize);
        int start = 0;
        String advText = "";

        while (start < advSize) {
            String advers = advs.get(start);
            String adv[] = advers.substring(1, advers.length() - 1).split(",");
            advText += "   " + adv[0];
            start++;
        }
        advTitle = (TextView) findViewById(R.id.advText);
        advTitle.setText(padText(advText, advTitle.getPaint(), advTitle.getWidth()));
        advTitle.setTypeface(fontDroidkufi);

        advTitle.setSelected(true);

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

        } catch (ParseException ignored) {
        }
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

    private boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.FRIDAY) {
            Log.e("****/ friday/", " " + true);
            return true;
        } else return false;
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

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
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

    private void setCloseNotificationTitle() {

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(3000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(3000);
        prev_tet1.setText(settings.getPhoneAlertsArabic());
        prev_tet2.setText(settings.getPhoneAlertsEnglish());
        prev_tet3.setText(settings.getPhoneAlertsUrdu());
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.blink);
        animation.setDuration(3000);
        llText.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFriday() && (nextPray.equals("dhuhr") || currentPray.equals("dhuhr"))) {
                    finish();
                } else if (sp.getBoolean("voiceRec", true)) {
                    llText.startAnimation(animation);
                } else
                    finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer = true;
        timer.cancel();
        timer.purge();
    }

    private void stopSilentMode() {
        Log.i("stop Silent: ", "truer");
        try {
            if (mAudioManager == null) {
                mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            }
            if (mAudioManager != null) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FX_KEY_CLICK);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void setImage(String masjedImg, final AppCompatImageView imageView) {
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
    }

}