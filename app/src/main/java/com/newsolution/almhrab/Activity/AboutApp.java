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

    private ImageView iv_back;
    private TextView tvAbout;
    Activity activity;
    private ImageView logoCo,logo;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
//        setColor() ;
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/ae_battar.ttf")//battar  droidkufi_regular droid_sans_arabic neosansarabic //mcs_shafa_normal
//                .setFontAttrId(R.attr.fontPath)
//                .build());

        setContentView(R.layout.activity_about_app);
        Utils.applyFont(activity,findViewById(R.id.parentPanel));
        logo=(ImageView)findViewById(R.id.logo);
        logoCo=(ImageView)findViewById(R.id.logoCo);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        logo.getLayoutParams().width=(int)(width*0.3);
        logo.getLayoutParams().height=(int)(width*0.3);
        logoCo.getLayoutParams().width=(int)(width*0.2);
        logoCo.getLayoutParams().height=(int)(width*0.2);

        // iv_back=(ImageView)findViewById(R.id.iv_back);
//        tvAbout=(TextView)findViewById(R.id.tvAbout);
//        tvAbout.setMovementMethod(new ScrollingMovementMethod());
//        tvAbout.setText(getString(R.string.aboutText));
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
 }
    private void setColor() {
        try {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.back_text));
        } catch (NoSuchMethodError ex) {
            ex.printStackTrace();
        }
    }

}
