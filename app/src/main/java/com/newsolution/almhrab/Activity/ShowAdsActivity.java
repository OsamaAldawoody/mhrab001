package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAdsActivity extends AppCompatActivity {

    private Activity activity;
    private TextView tvTitle, tvName, tvAdsText;
    private ImageView ivAdsImage;
    private VideoView vvAdsVideo;
    private SharedPreferences sp;
    TextView date1, time, amPm;
    public static String droidkufiBold = "fonts/droid_kufi_bold.ttf";
    private Typeface font;
    public static String roboto = "fonts/roboto.ttf";
    private Typeface fontRoboto;
    private DBOperations DBO;
    private Ads ads;
    private Runnable adsRunnable;
    private Handler AdsHandler = new Handler();

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
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        DBO = new DBOperations(this);
//        DBO.createDatabase();
//        DBO.open();
//        ads = DBO.getAds(sp.getInt("masjedId", -1));
//        DBO.close();
        ads = (Ads) getIntent().getSerializableExtra("ads");
        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAdsText = (TextView) findViewById(R.id.tvAdsText);
        ivAdsImage = (ImageView) findViewById(R.id.ivAdsImage);
        vvAdsVideo = (VideoView) findViewById(R.id.vvAdsVideo);
        date1 = (TextView) findViewById(R.id.dateToday);
        time = (TextView) findViewById(R.id.Time);
        amPm = (TextView) findViewById(R.id.amPm);
        amPm.setVisibility(View.VISIBLE);
        time.setTypeface(fontRoboto);
        date1.setTypeface(font);
        checkTime();
        fillData();
        if (getIntent().getAction().equals("main")) checkAds();
    }

    private void fillData() {
        tvName.setText(sp.getString("masjedName", ""));
        int type = ads.getType();
        String title = ads.getTitle();
        String start = ads.getStartTime();
        String end = ads.getEndTime();
        String text = ads.getText();
        String image = ads.getImage();
        String video = ads.getVideo();
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
//            Log.i("---++ image: ", image);
                Glide.with(activity).load(f).into(ivAdsImage);
            } else if (type == 2) {
                ivAdsImage.setVisibility(View.GONE);
                vvAdsVideo.setVisibility(View.VISIBLE);
                tvAdsText.setVisibility(View.GONE);
                tvAdsText.setText(text);
                vvAdsVideo.setVideoURI(Uri.parse(video));
                vvAdsVideo.start();
                Log.i("---++ video: ", video);
            } else if (type == 3) {
                ivAdsImage.setVisibility(View.GONE);
                vvAdsVideo.setVisibility(View.GONE);
                tvAdsText.setText(text);
                tvAdsText.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showCustomToast(activity, "تم حذف ملف الاعلان");
        }

    }

    private void checkAds() {
        if (ads != null) {
            String adsStartTime = ads.getStartTime();
            String adsEndTime = ads.getEndTime();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", new Locale("en"));
            Date date = new Date();
            String currentTime = df.format(date);
            try {
                Date start = df.parse(adsStartTime);
                Date end = df.parse(adsEndTime);
                Date now = df.parse(currentTime);
                Log.i("---++ end: ", end.toString());
                Log.i("---++ now: ", now.toString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdsHandler.removeCallbacks(adsRunnable);
        MainActivity.isOpenAds = true;

    }

    private void checkTime() {
        DateHigri hd = new DateHigri();
        date1.setText(Utils.writeIslamicDate1(this, hd));
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
}
