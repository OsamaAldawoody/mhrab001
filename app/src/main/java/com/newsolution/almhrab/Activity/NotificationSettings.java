package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotificationSettings extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private LinearLayout llCloseNotifSound;
    private CheckBox cbCloseNotifSound;
    private View view;
    private CheckBox cbCloseNotifScreen;
    private EditText edNotifTimer;
    private EditText edArNotif;
    private EditText edEnNotif;
    private EditText edUrNotif;
    private Button btnSave;
    private Activity activity;
    private DBOperations DBO;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private OptionSiteClass settings;
    private int notifTimer;
    private String urNotif, enNotif, arNotif;

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
        activity = this;
        setColor();
        setContentView(R.layout.activity_notification_settings);

        DBO = new DBOperations(this);
        GlobalVars gv = (GlobalVars) getApplicationContext();
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        DBO.open();
        settings = DBO.getSettings();
        DBO.close();
        notifTimer = settings.getPhoneShowAlertsBeforEkama();
        arNotif = settings.getPhoneAlertsArabic();
        enNotif = settings.getPhoneAlertsEnglish();
        urNotif = settings.getPhoneAlertsUrdu();

        findViews();
        fillData();
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        llCloseNotifSound = (LinearLayout) findViewById(R.id.ll_closeNotifSound);
        cbCloseNotifSound = (CheckBox) findViewById(R.id.cb_closeNotifSound);
        view = findViewById(R.id.view);
        LinearLayout llCloseNotifScreen = (LinearLayout) findViewById(R.id.ll_closeNotifScreen);
        cbCloseNotifScreen = (CheckBox) findViewById(R.id.cb_closeNotifScreen);
        edNotifTimer = (EditText) findViewById(R.id.ed_notifTimer);
        edArNotif = (EditText) findViewById(R.id.ed_arNotif);
        edEnNotif = (EditText) findViewById(R.id.ed_enNotif);
        edUrNotif = (EditText) findViewById(R.id.ed_urNotif);
        btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llCloseNotifSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cbCloseNotifSound.isChecked()) {
                    spedit.putBoolean("close_voice", false).commit();
                    cbCloseNotifSound.setChecked(false);
                } else {
                    cbCloseNotifSound.setChecked(true);
                    spedit.putBoolean("close_voice", true).commit();
                }
            }
        });
        cbCloseNotifSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbCloseNotifSound.isChecked()) {
                    spedit.putBoolean("close_voice", true).commit();
                } else {
                    spedit.putBoolean("close_voice", false).commit();
                }
            }
        });


        llCloseNotifScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbCloseNotifScreen.isChecked()) {
                    spedit.putBoolean("close_screen", false).commit();
                    spedit.putBoolean("close_voice", false).commit();
                    view.setVisibility(View.GONE);
                    cbCloseNotifSound.setChecked(false);
                    llCloseNotifSound.setVisibility(View.GONE);
                    cbCloseNotifScreen.setChecked(false);
                } else {
                    cbCloseNotifScreen.setChecked(true);
                    view.setVisibility(View.VISIBLE);
                    llCloseNotifSound.setVisibility(View.VISIBLE);
                    spedit.putBoolean("close_screen", true).commit();
                }
            }
        });
        cbCloseNotifScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbCloseNotifScreen.isChecked()) {
                    spedit.putBoolean("close_screen", true).commit();
                    view.setVisibility(View.VISIBLE);
                    llCloseNotifSound.setVisibility(View.VISIBLE);
                } else {
                    spedit.putBoolean("close_screen", false).commit();
                    spedit.putBoolean("close_voice", false).commit();
                    cbCloseNotifSound.setChecked(false);
                    llCloseNotifSound.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);

                }
            }
        });


    }

    private void fillData() {
        edNotifTimer.setText(String.valueOf(notifTimer));
        edUrNotif.setText(urNotif);
        edArNotif.setText(arNotif);
        edEnNotif.setText(enNotif);
        if (sp.getBoolean("close_screen", true)) {
            cbCloseNotifScreen.setChecked(true);
        } else {
            cbCloseNotifScreen.setChecked(false);
        }
        if (sp.getBoolean("close_voice", true)) {
            cbCloseNotifSound.setChecked(true);
        } else {
            cbCloseNotifSound.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            // Handle clicks for btnSave
            settings.setPhoneAlertsArabic(TextUtils.isEmpty(edArNotif.getText().toString().trim()) ? settings.getPhoneAlertsArabic()
                    : (edArNotif.getText().toString().trim()));
            settings.setPhoneAlertsEnglish(TextUtils.isEmpty(edEnNotif.getText().toString().trim()) ? settings.getPhoneAlertsEnglish()
                    : (edEnNotif.getText().toString().trim()));
            settings.setPhoneAlertsUrdu(TextUtils.isEmpty(edUrNotif.getText().toString().trim()) ? settings.getPhoneAlertsUrdu()
                    : (edUrNotif.getText().toString().trim()));


            settings.setPhoneShowAlertsBeforEkama(TextUtils.isEmpty(edNotifTimer.getText().toString().trim()) ? settings.getPhoneShowAlertsBeforEkama()
                    : Integer.parseInt(edNotifTimer.getText().toString().trim()));

            settings.setPhoneStatusAlerts(cbCloseNotifScreen.isChecked());
            settings.setPhoneStatusVoice(cbCloseNotifSound.isChecked());

            if (sp.getInt("priority", 0) == 1) {
                if (Utils.isOnline(activity)) {
                    saveChanges();
                } else {
                    Utils.showCustomToast(activity, getString(R.string.no_internet));
                }
            } else {
                DBO.insertSettings(settings);
                Utils.showCustomToast(activity, getString(R.string.success_edit));
            }
        } else if (v == ivBack) {
            onBackPressed();
        }
    }

    private void saveChanges() {
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage(getString(R.string.saving));
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        WS.UpdateSettings(activity, settings, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                pd.dismiss();
                Utils.showCustomToast(activity, "" + response);
                DBO.insertSettings(settings);
            }

            @Override
            public void onFail(String error) {
                pd.dismiss();
                Utils.showCustomToast(activity, "" + error);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setColor() {
        try {
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.back_text));
            }
        } catch (NoSuchMethodError ex) {
            ex.printStackTrace();
        }
    }
}
