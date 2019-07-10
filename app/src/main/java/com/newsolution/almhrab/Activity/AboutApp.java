package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Utils.applyFont(this, findViewById(R.id.parentPanel));
        ImageView logo = (ImageView) findViewById(R.id.logo);
        ImageView logoCo = (ImageView) findViewById(R.id.logoCo);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        logo.getLayoutParams().width = (int) (width * 0.3);
        logo.getLayoutParams().height = (int) (width * 0.3);
        logoCo.getLayoutParams().width = (int) (width * 0.2);
        logoCo.getLayoutParams().height = (int) (width * 0.2);

    }

}
