package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.newsolution.almhrab.R;

public class ShowAdsActivity extends AppCompatActivity {

    private Activity activity;
    private TextView tvTitle, tvName, tvAdsText;
    private ImageView ivAdsImage;
    private VideoView vvAdsVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity = this;
        setContentView(R.layout.activity_show_ads);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAdsText = (TextView) findViewById(R.id.tvAdsText);
        ivAdsImage = (ImageView) findViewById(R.id.ivAdsImage);
        vvAdsVideo = (VideoView) findViewById(R.id.vvAdsVideo);
    }
}
