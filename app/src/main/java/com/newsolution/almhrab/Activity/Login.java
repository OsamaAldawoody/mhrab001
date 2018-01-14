package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Hijri_Cal_Tools;
import com.newsolution.almhrab.Interface.OnFetched;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.Model.Users;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends Activity implements View.OnClickListener {
    private TextView tvuserName;
    private EditText edUserName;
    private TextView tvPW;
    private EditText edPassword;
    private Button btnLogin;
    private ProgressBar progress;
    Activity activity;
    private LinearLayout parentPanel;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private int cityId;
    public Calendar today = Calendar.getInstance();
    double day = today.get(Calendar.DAY_OF_MONTH);
    double month = today.get(Calendar.MONTH) + 1;
    double year = today.get(Calendar.YEAR);
    private double long1, long2;
    private double lat1, lat2;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/neosansarabic.ttf")//battar  droidkufi_regular droid_sans_arabic neosansarabic //mcs_shafa_normal
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_login);
        activity = this;
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        findViews();
    }

    private void findViews() {
        parentPanel = (LinearLayout) findViewById(R.id.parentPanel);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#B57C2F"), android.graphics.PorterDuff.Mode.MULTIPLY);
        tvuserName = (TextView) findViewById(R.id.tvuserName);
        edUserName = (EditText) findViewById(R.id.edUserName);
        tvPW = (TextView) findViewById(R.id.tvPW);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            if (TextUtils.isEmpty(edUserName.getText().toString().trim())) {
                edUserName.setError("أدخل اسم المستخدم");
                return;
            }
            if (TextUtils.isEmpty(edPassword.getText().toString().trim())) {
                edPassword.setError("أدخل كلمة المرور ");
                return;
            }
            login();

        }
    }

    private void login() {
        if (Utils.isOnline(activity)) {
            progress.setVisibility(View.VISIBLE);
            Map<String, String> params = new HashMap<>();
            params.put("username", edUserName.getText().toString().trim());
            params.put("password", convertToEng(edPassword.getText().toString().trim()));
            params.put("DeviceNo", sp.getString(AppConst.DeviceNo,""));

            WS.login(activity, params, new OnFetched() {
                @Override
                public void onSuccess(Users user) {
                    spedit.putInt("masjedId",user.getId()).commit();
                    spedit.putInt("cityId",user.getIdCity()).commit();
                    spedit.putString("masjedGUID",user.getGUID()).commit();
                    spedit.putString("masjedName",user.getMyName()).commit();
                    spedit.putString("masjedImg",user.getImg()).commit();
                    spedit.putString("masjedPW", edPassword.getText().toString().trim()).commit();
//                    Utils.showCustomToast(activity,sp.getString("masjedPW","")+"  99");
                    progress.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Login.this, ChoosePriority.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 500);
                    // getPrayerTimes();

                }

                @Override
                public void onFail(String fail) {
                    Utils.showCustomToast(activity, fail);
                    progress.setVisibility(View.GONE);
                }
            });
        } else {
            Utils.showCustomToast(activity, getString(R.string.no_internet));
        }
    }
    public String convertToEng(String value)
    {
        String newValue =   (((((((((((value).replaceAll("١","1")).replaceAll("٢","2")).replaceAll( "٣","3"))
                .replaceAll( "٤","4")).replaceAll( "٥","5")).replaceAll("٦","6")).replaceAll( "٧","7"))
                .replaceAll( "٨","8")).replaceAll( "٩","9")).replaceAll("٠","0"));
        return newValue;
    }
}
