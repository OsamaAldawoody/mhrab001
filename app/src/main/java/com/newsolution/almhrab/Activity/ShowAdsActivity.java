package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        DBO.createDatabase();
        DBO.open();
        ads = DBO.getAds(sp.getInt("masjedId", -1));
        DBO.close();
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

    }

    private void fillData() {
        tvName.setText(sp.getString("masjedName", ""));
        int type = ads.getType();
        String title = ads.getTitle();
        String start=ads.getStartTime();
        String end=ads.getEndTime();
        String text=ads.getText();
        String image=ads.getImage();
        String video=ads.getVideo();
        tvTitle.setText(title);
        if (type==1){
            ivAdsImage.setVisibility(View.VISIBLE);
            vvAdsVideo.setVisibility(View.GONE);
            tvAdsText.setVisibility(View.GONE);
            tvAdsText.setText(text);
            Glide.with(activity).load(Uri.parse(image)).into(ivAdsImage) ;
        }else
        if (type==2){
            ivAdsImage.setVisibility(View.GONE);
            vvAdsVideo.setVisibility(View.VISIBLE);
            tvAdsText.setVisibility(View.GONE);
            tvAdsText.setText(text);
            vvAdsVideo.setVideoURI(Uri.parse(video));
            vvAdsVideo.start();
        }else
        if (type==3){
            ivAdsImage.setVisibility(View.GONE);
            vvAdsVideo.setVisibility(View.GONE);
            tvAdsText.setText(text);
            tvAdsText.setVisibility(View.VISIBLE);
        }

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