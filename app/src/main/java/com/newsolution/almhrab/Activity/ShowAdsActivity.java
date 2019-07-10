package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.scheduler.SalaatAlarmReceiver;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAdsActivity extends AppCompatActivity {

    private static final String TAG = ShowAdsActivity.class.getSimpleName();
    private TextView tvTitle, tvName, tvAdsText;
    private ImageView ivAdsImage;
    private VideoView vvAdsVideo;
    private TextView dateTodayM, dateTodayH, time, amPm;
    public static String roboto = "fonts/roboto.ttf";
    public static String droidkufi = "fonts/droidkufi_regular.ttf";
    private Typeface fontRoboto;
    private Typeface fontDroidkufi;
    private Ads ads;
    private Runnable adsRunnable;
    private Handler AdsHandler = new Handler();
    public static String arial = "fonts/ariblk.ttf";
    private Typeface fontArial;
    private Typeface ptBoldHeading;
    LinearLayout llText, llFajer, llSun, llDuhr, llAsr, llMagrib, llIsha;
    private double long1, long2;
    private double lat1, lat2;
    private SharedPreferences sp;
    private GlobalVars gv;
    private SharedPreferences.Editor spedit;

    TextView fajrTitle, shroqTitle,
            duhrTitle, asrTitle, ishaTitle;
    private TextView maghribTime, asrTime, ishaTime,
            maghribTitle, dhuhrTime, fajrTime, sunriseTime;
    public String cfajr = "";
    public String icfajr = "";
    private String csunrise = "";
    private String icsunrise = "";
    private String cdhohr = "";
    private String icdhohr = "";
    private String casr = "";
    private String icasr = "";
    private String cmaghrib = "";
    private String icmaghrib = "";
    private String cisha = "";
    private String icisha = "";
    public Calendar today = Calendar.getInstance();
    double day = today.get(Calendar.DAY_OF_MONTH);
    double month = today.get(Calendar.MONTH) + 1;
    double year = today.get(Calendar.YEAR);
    public String nextPray;
    private String[] mosquSettings;
    private Timer timer;
    private int cityId;
    private DBOperations DBO;

    private Activity activity;
    private City city;
    private boolean isFajrEkamaIsTime, isAlShrouqEkamaIsTime, isDhuhrEkamaIsTime, ishaEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;
    private OptionSiteClass settings;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity = this;
        setContentView(R.layout.activity_show_ads);

        ads = (Ads) getIntent().getSerializableExtra("ads");
        ptBoldHeading = Typeface.createFromAsset(getAssets(), "fonts/pt_bold_heading.ttf");
        fontArial = Typeface.createFromAsset(getAssets(), arial);

        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);

        ads = (Ads) getIntent().getSerializableExtra("ads");
        ptBoldHeading = Typeface.createFromAsset(getAssets(), "fonts/pt_bold_heading.ttf");
        fontArial = Typeface.createFromAsset(getAssets(), arial);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);

        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        DBO = new DBOperations(this);
        gv = (GlobalVars) getApplicationContext();
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
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

        buildUI();

        checkTime();
        fillData();
        if (getIntent().getAction().equals("main")) checkAds();
    }

    private void fillData() {
        tvName.setText(sp.getString("masjedName", ""));
        int type = ads.getType();
        String title = ads.getTitle();
        String text = ads.getText();
        String image = ads.getImage();
        final String video = ads.getVideo();
        tvTitle.setText(title);
        try {
            if (type == 1) {
                ivAdsImage.setVisibility(View.VISIBLE);
                vvAdsVideo.setVisibility(View.GONE);
                tvAdsText.setVisibility(View.GONE);
                tvAdsText.setText(text);
                File f = new File(image);
                Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
                ivAdsImage.setImageBitmap(bmp);
                Glide.with(activity).load(f).into(ivAdsImage);
            } else if (type == 2) {
                ivAdsImage.setVisibility(View.GONE);
                vvAdsVideo.setVisibility(View.VISIBLE);
                tvAdsText.setVisibility(View.GONE);
                tvAdsText.setText(text);
                vvAdsVideo.setVideoURI(Uri.parse(video));
                vvAdsVideo.start();
            } else if (type == 3) {
                ivAdsImage.setVisibility(View.GONE);
                vvAdsVideo.setVisibility(View.GONE);
                tvAdsText.setText(text);
                tvAdsText.setVisibility(View.VISIBLE);
            }
            vvAdsVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    vvAdsVideo.setVideoURI(Uri.parse(video));
                    vvAdsVideo.start();
                }
            });
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Utils.showCustomToast(activity, getString(R.string.advError));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkAds() {
        if (ads != null) {
            String adsEndTime = ads.getEndTime();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            Date date = new Date();
            String currentTime = df.format(date);
            try {
                Date end = df.parse(adsEndTime);
                Date now = df.parse(currentTime);
                Log.i(TAG + " End: ", end.toString());
                Log.i(TAG + " now: ", now.toString());
                if (now.after(end)) {
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
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

    private void checkTime() {
        DateHigri hd = new DateHigri();
        dateTodayM.setText(Utils.writeMDate(this, hd));
        dateTodayH.setText(Utils.writeHDate(this, hd));
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);
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

    public void buildUI() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAdsText = (TextView) findViewById(R.id.tvAdsText);
        ivAdsImage = (ImageView) findViewById(R.id.ivAdsImage);
        vvAdsVideo = (VideoView) findViewById(R.id.vvAdsVideo);
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
        TimerTask async = new TimerTask() {
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
            SimpleDateFormat spd = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdsHandler.removeCallbacks(adsRunnable);
        MainActivity.isOpenAds = true;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
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
            String[] prayTimes = calculate();
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
}
