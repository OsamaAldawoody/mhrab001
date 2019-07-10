package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.IdRes;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IqamaSettings extends AppCompatActivity implements View.OnClickListener {
    private EditText edShroq, edShroqIq, edFajer, ed_fajerIq;
    private EditText edFajerِAzkarP;
    private EditText edFajerِAzkarApp;
    private EditText edDuhr, edDuhrIq;
    private EditText edDuhrAzkarP;
    private EditText edDuhrِAzkarApp;
    private EditText edAser, edAserIq;
    private EditText edAserAzkarP;
    private EditText edAserAzkarApp;
    private EditText edMagrib, edMagribIq;
    private EditText edMagribAzkarP;
    private EditText edMagribAzkarApp;
    private EditText edIsha, edIshaIq;
    private EditText edIshaAzkarP;
    private EditText edIshaAzkarApp;
    private Button btnSave;
    Activity activity;
    private ImageView iv_back;
    private DBOperations DBO;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private int iqSh, iqfajer, iqdhor, iqaser, iqmagrib, iqisha;
    private String iqShT, iqfajerT, iqdhorT, iqaserT, iqmagribT, iqishaT;
    private int fajrAzkar, fajrAzkarTimer, dhuhrAzkar, dhuhrAzkarTimer, aserAzkar, aserAzkarTimer, magribAzkar, magribAzkarTimer, ishaAzkar, ishaAzkarTimer;
    private LinearLayout llSRelative;
    private LinearLayout llfRelative;
    private LinearLayout lldRelative;
    private LinearLayout llARelative;
    private LinearLayout llmRelative;
    private LinearLayout llIRelative;
    private LinearLayout llSConstant;
    private LinearLayout llfConstant;
    private LinearLayout lldConstant;
    private LinearLayout llAConstant;
    private LinearLayout llmConstant;
    private LinearLayout llIConstant;
    CheckBox cb_IqamaSoundF, cb_soundF, cb_IqamaSoundD, cb_soundD, cb_IqamaSoundM, cb_soundM, cb_IqamaSoundA, cb_soundA, cb_IqamaSoundI, cb_soundI;
    RadioButton rbSRelative, rbfRelative, rbdRelative, rbARelative, rbmRelative, rbIRelative, rbSConstant, rbfConstant, rbdConstant, rbAConstant, rbmConstant, rbIConstant;
    private boolean iqamaSoundOnF, iqamaSoundOnD, iqamaSoundOnA, iqamaSoundOnM, iqamaSoundOnI, soundOnF, soundOnD, soundOnA,
            soundOnM, soundOnI;
    OptionSiteClass settings;
    private boolean isAlShrouqEkamaIsTime, isFajrEkamaIsTime, isDhuhrEkamaIsTime, ishaEkamaIsTime, isMagribEkamaIsTime, isAsrEkamaIsTime;

    public IqamaSettings() {
    }

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
        setContentView(R.layout.activity_iqama_settings);

        DBO = new DBOperations(this);
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        DBO.open();
        settings = DBO.getSettings();
        DBO.close();

        iqfajer = settings.getFajrEkama();
        iqfajerT = settings.getFajrEkamaTime() == null ? "" : settings.getFajrEkamaTime();
        fajrAzkar = settings.getFajrAzkar();
        fajrAzkarTimer = settings.getFajrAzkarTimer();

        iqSh = settings.getAlShrouqEkama();
        iqShT = settings.getAlShrouqEkamaTime() == null ? "" : settings.getAlShrouqEkamaTime();

        iqdhor = settings.getDhuhrEkama();
        iqdhorT = settings.getDhuhrEkamaTime() == null ? "" : settings.getDhuhrEkamaTime();
        dhuhrAzkar = settings.getDhuhrAzkar();
        dhuhrAzkarTimer = settings.getDhuhrAzkarTimer();

        iqaser = settings.getAsrEkama();
        iqaserT = settings.getAsrEkamaTime() == null ? "" : settings.getAsrEkamaTime();
        aserAzkar = settings.getAsrAzkar();
        aserAzkarTimer = settings.getAsrAzkarTimer();

        iqmagrib = settings.getMagribEkama();
        iqmagribT = settings.getMagribEkamaTime() == null ? "" : settings.getMagribEkamaTime();
        magribAzkar = settings.getMagribAzkar();
        magribAzkarTimer = settings.getMagribAzkarTimer();

        iqisha = settings.getIshaEkama();
        iqishaT = settings.getIshaEkamaTime() == null ? "" : settings.getIshaEkamaTime();
        ishaAzkar = settings.getIshaAzkar();
        ishaAzkarTimer = settings.getIshaAzkarTimer();

        spedit.putString("ifajer", iqfajer + "").commit();
        spedit.putString("ishroq", iqSh + "").commit();
        spedit.putString("idhor", iqdhor + "").commit();
        spedit.putString("iaser", iqaser + "").commit();
        spedit.putString("imagrib", iqmagrib + "").commit();
        spedit.putString("iisha", iqisha + "").commit();

        spedit.putString("ifajerT", iqfajerT + "").commit();
        spedit.putString("ishroqT", iqShT + "").commit();
        spedit.putString("idhorT", iqdhorT + "").commit();
        spedit.putString("iaserT", iqaserT + "").commit();
        spedit.putString("imagribT", iqmagribT + "").commit();
        spedit.putString("iishaT", iqishaT + "").commit();

        isFajrEkamaIsTime = settings.isFajrEkamaIsTime();
        isAlShrouqEkamaIsTime = settings.isAlShrouqEkamaIsTime();
        isDhuhrEkamaIsTime = settings.isDhuhrEkamaIsTime();
        isAsrEkamaIsTime = settings.isAsrEkamaIsTime();
        isMagribEkamaIsTime = settings.isMagribEkamaIsTime();
        ishaEkamaIsTime = settings.ishaEkamaIsTime();

        boolean iqamaVoiceF = settings.isStatusEkamaVoiceF();
        boolean athanVoiceF = settings.isStatusAthanVoiceF();
        boolean iqamaVoiceD = settings.isStatusEkamaVoiceD();
        boolean athanVoiceD = settings.isStatusAthanVoiceD();
        boolean iqamaVoiceA = settings.isStatusEkamaVoiceA();
        boolean athanVoiceA = settings.isStatusAthanVoiceA();
        boolean iqamaVoiceM = settings.isStatusEkamaVoiceM();
        boolean athanVoiceM = settings.isStatusAthanVoiceM();
        boolean iqamaVoiceI = settings.isStatusEkamaVoiceI();
        boolean athanVoiceI = settings.isStatusAthanVoiceI();

        spedit.putBoolean("notif_onF", athanVoiceF).commit();
        spedit.putBoolean("notif_onD", athanVoiceD).commit();
        spedit.putBoolean("notif_onA", athanVoiceA).commit();
        spedit.putBoolean("notif_onM", athanVoiceM).commit();
        spedit.putBoolean("notif_onI", athanVoiceI).commit();
        spedit.putBoolean("iqama_notif_onF", iqamaVoiceF).commit();
        spedit.putBoolean("iqama_notif_onD", iqamaVoiceD).commit();
        spedit.putBoolean("iqama_notif_onA", iqamaVoiceA).commit();
        spedit.putBoolean("iqama_notif_onM", iqamaVoiceM).commit();
        spedit.putBoolean("iqama_notif_onI", iqamaVoiceI).commit();
        findViews();
        fillData();

    }

    private void findViews() {
        edShroq = (EditText) findViewById(R.id.ed_sh);
        edFajer = (EditText) findViewById(R.id.ed_fajer);
        edFajerِAzkarP = (EditText) findViewById(R.id.ed_fajerِAzkarP);
        edFajerِAzkarApp = (EditText) findViewById(R.id.ed_fajerِAzkarApp);
        edDuhr = (EditText) findViewById(R.id.ed_duhr);
        edDuhrAzkarP = (EditText) findViewById(R.id.ed_duhrAzkarP);
        edDuhrِAzkarApp = (EditText) findViewById(R.id.ed_duhrِAzkarApp);
        edAser = (EditText) findViewById(R.id.ed_aser);
        edAserAzkarP = (EditText) findViewById(R.id.ed_aserAzkarP);
        edAserAzkarApp = (EditText) findViewById(R.id.ed_aserAzkarApp);
        edMagrib = (EditText) findViewById(R.id.ed_magrib);
        edMagribAzkarP = (EditText) findViewById(R.id.ed_magribAzkarP);
        edMagribAzkarApp = (EditText) findViewById(R.id.ed_magribAzkarApp);
        edIsha = (EditText) findViewById(R.id.ed_isha);
        edIshaAzkarP = (EditText) findViewById(R.id.ed_ishaAzkarP);
        edIshaAzkarApp = (EditText) findViewById(R.id.ed_ishaAzkarApp);
        btnSave = (Button) findViewById(R.id.btn_save);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        LinearLayout ll_adan_soundF = (LinearLayout) findViewById(R.id.ll_adan_soundF);
        LinearLayout ll_iqama_soundF = (LinearLayout) findViewById(R.id.ll_iqama_soundF);
        LinearLayout ll_adan_soundD = (LinearLayout) findViewById(R.id.ll_adan_soundD);
        LinearLayout ll_iqama_soundD = (LinearLayout) findViewById(R.id.ll_iqama_soundD);
        LinearLayout ll_adan_soundA = (LinearLayout) findViewById(R.id.ll_adan_soundA);
        LinearLayout ll_iqama_soundA = (LinearLayout) findViewById(R.id.ll_iqama_soundA);
        LinearLayout ll_adan_soundM = (LinearLayout) findViewById(R.id.ll_adan_soundM);
        LinearLayout ll_iqama_soundM = (LinearLayout) findViewById(R.id.ll_iqama_soundM);
        LinearLayout ll_adan_soundI = (LinearLayout) findViewById(R.id.ll_adan_soundI);
        LinearLayout ll_iqama_soundI = (LinearLayout) findViewById(R.id.ll_iqama_soundI);

        llfConstant = (LinearLayout) findViewById(R.id.llfConstant);
        llSConstant = (LinearLayout) findViewById(R.id.llSConstant);
        lldConstant = (LinearLayout) findViewById(R.id.lldConstant);
        llAConstant = (LinearLayout) findViewById(R.id.llAConstant);
        llmConstant = (LinearLayout) findViewById(R.id.llmConstant);
        llIConstant = (LinearLayout) findViewById(R.id.llIConstant);

        llfRelative = (LinearLayout) findViewById(R.id.llfRelative);
        llSRelative = (LinearLayout) findViewById(R.id.llSRelative);
        lldRelative = (LinearLayout) findViewById(R.id.lldRelative);
        llARelative = (LinearLayout) findViewById(R.id.llARelative);
        llmRelative = (LinearLayout) findViewById(R.id.llmRelative);
        llIRelative = (LinearLayout) findViewById(R.id.llIRelative);

        rbfConstant = (RadioButton) findViewById(R.id.rbfConstant);
        rbSConstant = (RadioButton) findViewById(R.id.rbSConstant);
        rbdConstant = (RadioButton) findViewById(R.id.rbdConstant);
        rbAConstant = (RadioButton) findViewById(R.id.rbAConstant);
        rbmConstant = (RadioButton) findViewById(R.id.rbmConstant);
        rbIConstant = (RadioButton) findViewById(R.id.rbIConstant);

        RadioGroup rg_fajer = (RadioGroup) findViewById(R.id.rg_fajer);
        RadioGroup rg_sh = (RadioGroup) findViewById(R.id.rg_sh);
        RadioGroup rg_duhr = (RadioGroup) findViewById(R.id.rg_duhr);
        RadioGroup rg_aser = (RadioGroup) findViewById(R.id.rg_aser);
        RadioGroup rg_magrib = (RadioGroup) findViewById(R.id.rg_magrib);
        RadioGroup rg_isha = (RadioGroup) findViewById(R.id.rg_isha);
        rbfRelative = (RadioButton) findViewById(R.id.rbfRelative);
        rbSRelative = (RadioButton) findViewById(R.id.rbSRelative);
        rbdRelative = (RadioButton) findViewById(R.id.rbdRelative);
        rbARelative = (RadioButton) findViewById(R.id.rbARelative);
        rbmRelative = (RadioButton) findViewById(R.id.rbmRelative);
        rbIRelative = (RadioButton) findViewById(R.id.rbIRelative);

        ed_fajerIq = (EditText) findViewById(R.id.ed_fajerIq);
        edShroqIq = (EditText) findViewById(R.id.ed_shIq);
        edDuhrIq = (EditText) findViewById(R.id.ed_duhrIq);
        edAserIq = (EditText) findViewById(R.id.ed_aserIq);
        edMagribIq = (EditText) findViewById(R.id.ed_magribIq);
        edIshaIq = (EditText) findViewById(R.id.ed_ishaIq);
        ed_fajerIq.setHint("00:00");
        edShroqIq.setHint("00:00");
        edDuhrIq.setHint("00:00");
        edAserIq.setHint("00:00");
        edMagribIq.setHint("00:00");
        edIshaIq.setHint("00:00");
        cb_soundF = (CheckBox) findViewById(R.id.cb_soundF);
        cb_IqamaSoundF = (CheckBox) findViewById(R.id.cb_IqamaSoundF);
        cb_soundD = (CheckBox) findViewById(R.id.cb_soundD);
        cb_IqamaSoundD = (CheckBox) findViewById(R.id.cb_IqamaSoundD);
        cb_soundA = (CheckBox) findViewById(R.id.cb_soundA);
        cb_IqamaSoundA = (CheckBox) findViewById(R.id.cb_IqamaSoundA);
        cb_soundM = (CheckBox) findViewById(R.id.cb_soundM);
        cb_IqamaSoundM = (CheckBox) findViewById(R.id.cb_IqamaSoundM);
        cb_soundI = (CheckBox) findViewById(R.id.cb_soundI);
        cb_IqamaSoundI = (CheckBox) findViewById(R.id.cb_IqamaSoundI);

        btnSave.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_adan_soundF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_soundF.isChecked()) {
                    spedit.putBoolean("notif_onF", false).commit();
                    cb_soundF.setChecked(false);
                } else {
                    cb_soundF.setChecked(true);
                    spedit.putBoolean("notif_onF", true).commit();
                }
            }
        });
        cb_soundF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_soundF.isChecked()) {
                    spedit.putBoolean("notif_onF", true).commit();
                    soundOnF = true;
                } else {
                    spedit.putBoolean("notif_onF", false).commit();
                    soundOnF = false;
                }
            }
        });
        ll_adan_soundD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_soundD.isChecked()) {
                    spedit.putBoolean("notif_onD", false).commit();
                    cb_soundD.setChecked(false);
                } else {
                    cb_soundD.setChecked(true);
                    spedit.putBoolean("notif_onD", true).commit();
                }
            }
        });
        cb_soundD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_soundD.isChecked()) {
                    spedit.putBoolean("notif_onD", true).commit();
                    soundOnD = true;
                } else {
                    spedit.putBoolean("notif_onD", false).commit();
                    soundOnD = false;
                }
            }
        });
        ll_adan_soundA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_soundA.isChecked()) {
                    spedit.putBoolean("notif_onA", false).commit();
                    cb_soundA.setChecked(false);
                } else {
                    cb_soundA.setChecked(true);
                    spedit.putBoolean("notif_onA", true).commit();
                }
            }
        });
        cb_soundA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_soundA.isChecked()) {
                    spedit.putBoolean("notif_onA", true).commit();
                    soundOnA = true;
                } else {
                    spedit.putBoolean("notif_onA", false).commit();
                    soundOnA = false;
                }
            }
        });
        ll_adan_soundM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_soundM.isChecked()) {
                    spedit.putBoolean("notif_onM", false).commit();
                    cb_soundM.setChecked(false);
                } else {
                    cb_soundM.setChecked(true);
                    spedit.putBoolean("notif_onM", true).commit();
                }
            }
        });
        cb_soundM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_soundM.isChecked()) {
                    spedit.putBoolean("notif_onM", true).commit();
                    soundOnM = true;
                } else {
                    spedit.putBoolean("notif_onM", false).commit();
                    soundOnM = false;
                }
            }
        });
        ll_adan_soundI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_soundI.isChecked()) {
                    spedit.putBoolean("notif_onI", false).commit();
                    cb_soundI.setChecked(false);
                } else {
                    cb_soundI.setChecked(true);
                    spedit.putBoolean("notif_onI", true).commit();
                }
            }
        });
        cb_soundI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_soundI.isChecked()) {
                    spedit.putBoolean("notif_onI", true).commit();
                    soundOnI = true;
                } else {
                    spedit.putBoolean("notif_onI", false).commit();
                    soundOnI = false;
                }
            }
        });


        ll_iqama_soundF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_IqamaSoundF.isChecked()) {
                    spedit.putBoolean("iqama_notif_onF", false).commit();
                    cb_IqamaSoundF.setChecked(false);
                } else {
                    cb_IqamaSoundF.setChecked(true);
                    spedit.putBoolean("iqama_notif_onF", true).commit();
                }
            }
        });
        cb_IqamaSoundF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_IqamaSoundF.isChecked()) {
                    spedit.putBoolean("iqama_notif_onF", true).commit();
                    iqamaSoundOnF = true;
                } else {
                    spedit.putBoolean("iqama_notif_onF", false).commit();
                    iqamaSoundOnF = false;
                }
            }
        });
        ll_iqama_soundD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_IqamaSoundD.isChecked()) {
                    spedit.putBoolean("iqama_notif_onD", false).commit();
                    cb_IqamaSoundD.setChecked(false);
                } else {
                    cb_IqamaSoundD.setChecked(true);
                    spedit.putBoolean("iqama_notif_onD", true).commit();
                }
            }
        });
        cb_IqamaSoundD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_IqamaSoundD.isChecked()) {
                    spedit.putBoolean("iqama_notif_onD", true).commit();
                    iqamaSoundOnD = true;
                } else {
                    spedit.putBoolean("iqama_notif_onD", false).commit();
                    iqamaSoundOnD = false;
                }
            }
        });
        ll_iqama_soundA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_IqamaSoundA.isChecked()) {
                    spedit.putBoolean("iqama_notif_onA", false).commit();
                    cb_IqamaSoundA.setChecked(false);
                } else {
                    cb_IqamaSoundA.setChecked(true);
                    spedit.putBoolean("iqama_notif_onA", true).commit();
                }
            }
        });
        cb_IqamaSoundA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_IqamaSoundA.isChecked()) {
                    spedit.putBoolean("iqama_notif_onA", true).commit();
                    iqamaSoundOnA = true;
                } else {
                    spedit.putBoolean("iqama_notif_onA", false).commit();
                    iqamaSoundOnA = false;
                }
            }
        });
        ll_iqama_soundM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_IqamaSoundM.isChecked()) {
                    spedit.putBoolean("iqama_notif_onM", false).commit();
                    cb_IqamaSoundM.setChecked(false);
                } else {
                    cb_IqamaSoundM.setChecked(true);
                    spedit.putBoolean("iqama_notif_onM", true).commit();
                }
            }
        });
        cb_IqamaSoundM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_IqamaSoundM.isChecked()) {
                    spedit.putBoolean("iqama_notif_onM", true).commit();
                    iqamaSoundOnM = true;
                } else {
                    spedit.putBoolean("iqama_notif_onM", false).commit();
                    iqamaSoundOnM = false;
                }
            }
        });

        ll_iqama_soundI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean on = true;
                if (cb_IqamaSoundI.isChecked()) {
                    spedit.putBoolean("iqama_notif_onI", false).commit();
                    cb_IqamaSoundI.setChecked(false);
                } else {
                    cb_IqamaSoundI.setChecked(true);
                    spedit.putBoolean("iqama_notif_onI", true).commit();
                }
            }
        });
        cb_IqamaSoundI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_IqamaSoundI.isChecked()) {
                    spedit.putBoolean("iqama_notif_onI", true).commit();
                    iqamaSoundOnI = true;
                } else {
                    spedit.putBoolean("iqama_notif_onI", false).commit();
                    iqamaSoundOnI = false;
                }
            }
        });
        rg_fajer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbfConstant) {
                    llfRelative.setVisibility(View.GONE);
                    llfConstant.setVisibility(View.VISIBLE);
                } else if (i == R.id.rbfRelative) {
                    llfRelative.setVisibility(View.VISIBLE);
                    llfConstant.setVisibility(View.GONE);
                }
            }
        });
        rg_sh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbSConstant) {
                    llSRelative.setVisibility(View.GONE);
                    llSConstant.setVisibility(View.VISIBLE);
                } else if (i == R.id.rbSRelative) {
                    llSRelative.setVisibility(View.VISIBLE);
                    llSConstant.setVisibility(View.GONE);
                }
            }
        });
        rg_duhr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbdConstant) {
                    lldRelative.setVisibility(View.GONE);
                    lldConstant.setVisibility(View.VISIBLE);
                } else if (i == R.id.rbdRelative) {
                    lldRelative.setVisibility(View.VISIBLE);
                    lldConstant.setVisibility(View.GONE);
                }
            }
        });
        rg_aser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbAConstant) {
                    llARelative.setVisibility(View.GONE);
                    llAConstant.setVisibility(View.VISIBLE);
                } else if (i == R.id.rbARelative) {
                    llARelative.setVisibility(View.VISIBLE);
                    llAConstant.setVisibility(View.GONE);
                }
            }
        });
        rg_magrib.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbmConstant) {
                    llmRelative.setVisibility(View.GONE);
                    llmConstant.setVisibility(View.VISIBLE);
                } else if (i == R.id.rbmRelative) {
                    llmRelative.setVisibility(View.VISIBLE);
                    llmConstant.setVisibility(View.GONE);
                }
            }
        });
        rg_isha.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rbIConstant) {
                    llIRelative.setVisibility(View.GONE);
                    llIConstant.setVisibility(View.VISIBLE);
                } else if (i == R.id.rbIRelative) {
                    llIRelative.setVisibility(View.VISIBLE);
                    llIConstant.setVisibility(View.GONE);
                }
            }
        });
        ed_fajerIq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(ed_fajerIq);
            }
        });
        edShroqIq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(edShroqIq);
            }
        });
        edDuhrIq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(edDuhrIq);
            }
        });
        edAserIq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(edAserIq);
            }
        });
        edMagribIq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(edMagribIq);
            }
        });
        edIshaIq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker(edIshaIq);
            }
        });

    }

    private void showPicker(final EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hr, min;
                if (selectedHour < 10)
                    hr = "0" + selectedHour;
                else hr = selectedHour + "";
                if (selectedMinute < 10)
                    min = "0" + selectedMinute;
                else min = selectedMinute + "";
                editText.setText(hr + ":" + min);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.show();

    }

    private void fillData() {
        edFajer.setText(String.valueOf(iqfajer));
        ed_fajerIq.setText(String.valueOf(iqfajerT));
        edFajerِAzkarP.setText(String.valueOf(fajrAzkar));
        edFajerِAzkarApp.setText(String.valueOf(fajrAzkarTimer));

        edShroq.setText(String.valueOf(iqSh));
        edShroqIq.setText(String.valueOf(iqShT));

        edDuhr.setText(String.valueOf(iqdhor));
        edDuhrIq.setText(String.valueOf(iqdhorT));
        edDuhrAzkarP.setText(String.valueOf(dhuhrAzkar));
        edDuhrِAzkarApp.setText(String.valueOf(dhuhrAzkarTimer));

        edAser.setText(String.valueOf(iqaser));
        edAserIq.setText(String.valueOf(iqaserT));
        edAserAzkarP.setText(String.valueOf(aserAzkar));
        edAserAzkarApp.setText(String.valueOf(aserAzkarTimer));

        edMagrib.setText(String.valueOf(iqmagrib));
        edMagribIq.setText(String.valueOf(iqmagribT));
        edMagribAzkarP.setText(String.valueOf(magribAzkar));
        edMagribAzkarApp.setText(String.valueOf(magribAzkarTimer));

        edIsha.setText(String.valueOf(iqisha));
        edIshaIq.setText(String.valueOf(iqishaT));
        edIshaAzkarP.setText(String.valueOf(ishaAzkar));
        edIshaAzkarApp.setText(String.valueOf(ishaAzkarTimer));
        if (sp.getBoolean("notif_onF", true)) {
            cb_soundF.setChecked(true);
            soundOnF = true;
        } else {
            cb_soundF.setChecked(false);
            soundOnF = false;
        }
        if (sp.getBoolean("iqama_notif_onF", true)) {
            cb_IqamaSoundF.setChecked(true);
            iqamaSoundOnF = true;
        } else {
            cb_IqamaSoundF.setChecked(false);
            iqamaSoundOnF = false;
        }
        if (sp.getBoolean("notif_onD", true)) {
            cb_soundD.setChecked(true);
            soundOnD = true;
        } else {
            cb_soundD.setChecked(false);
            soundOnD = false;
        }
        if (sp.getBoolean("iqama_notif_onD", true)) {
            cb_IqamaSoundD.setChecked(true);
            iqamaSoundOnD = true;
        } else {
            cb_IqamaSoundD.setChecked(false);
            iqamaSoundOnD = false;
        }
        if (sp.getBoolean("notif_onA", true)) {
            cb_soundA.setChecked(true);
            soundOnA = true;
        } else {
            cb_soundA.setChecked(false);
            soundOnA = false;
        }
        if (sp.getBoolean("iqama_notif_onA", true)) {
            cb_IqamaSoundA.setChecked(true);
            iqamaSoundOnA = true;
        } else {
            cb_IqamaSoundA.setChecked(false);
            iqamaSoundOnA = false;
        }
        if (sp.getBoolean("notif_onM", true)) {
            cb_soundM.setChecked(true);
            soundOnM = true;
        } else {
            cb_soundM.setChecked(false);
            soundOnM = false;
        }
        if (sp.getBoolean("iqama_notif_onM", true)) {
            cb_IqamaSoundM.setChecked(true);
            iqamaSoundOnM = true;
        } else {
            cb_IqamaSoundM.setChecked(false);
            iqamaSoundOnM = false;
        }
        if (sp.getBoolean("notif_onF", true)) {
            cb_soundF.setChecked(true);
            soundOnF = true;
        } else {
            cb_soundF.setChecked(false);
            soundOnF = false;
        }
        if (sp.getBoolean("iqama_notif_onF", true)) {
            cb_IqamaSoundF.setChecked(true);
            iqamaSoundOnF = true;
        } else {
            cb_IqamaSoundF.setChecked(false);
            iqamaSoundOnF = false;
        }

        if (isFajrEkamaIsTime) {
            llfRelative.setVisibility(View.GONE);
            llfConstant.setVisibility(View.VISIBLE);
            rbfConstant.setChecked(true);
            rbfRelative.setChecked(false);
        } else {
            llfRelative.setVisibility(View.VISIBLE);
            llfConstant.setVisibility(View.GONE);
            rbfConstant.setChecked(false);
            rbfRelative.setChecked(true);
        }
        if (isAlShrouqEkamaIsTime) {
            llSRelative.setVisibility(View.GONE);
            llSConstant.setVisibility(View.VISIBLE);
            rbSConstant.setChecked(true);
            rbSRelative.setChecked(false);
        } else {
            llSRelative.setVisibility(View.VISIBLE);
            llSConstant.setVisibility(View.GONE);
            rbSConstant.setChecked(false);
            rbSRelative.setChecked(true);
        }
        if (isDhuhrEkamaIsTime) {
            lldRelative.setVisibility(View.GONE);
            lldConstant.setVisibility(View.VISIBLE);
            rbdConstant.setChecked(true);
            rbdRelative.setChecked(false);
        } else {
            lldRelative.setVisibility(View.VISIBLE);
            lldConstant.setVisibility(View.GONE);
            rbdConstant.setChecked(false);
            rbdRelative.setChecked(true);
        }
        if (isAsrEkamaIsTime) {
            llARelative.setVisibility(View.GONE);
            llAConstant.setVisibility(View.VISIBLE);
            rbAConstant.setChecked(true);
            rbARelative.setChecked(false);
        } else {
            llARelative.setVisibility(View.VISIBLE);
            llAConstant.setVisibility(View.GONE);
            rbAConstant.setChecked(false);
            rbARelative.setChecked(true);
        }
        if (isMagribEkamaIsTime) {
            llmRelative.setVisibility(View.GONE);
            llmConstant.setVisibility(View.VISIBLE);
            rbmConstant.setChecked(true);
            rbmRelative.setChecked(false);
        } else {
            llmRelative.setVisibility(View.VISIBLE);
            llmConstant.setVisibility(View.GONE);
            rbmConstant.setChecked(false);
            rbmRelative.setChecked(true);
        }
        if (ishaEkamaIsTime) {
            llIRelative.setVisibility(View.GONE);
            llIConstant.setVisibility(View.VISIBLE);
            rbIConstant.setChecked(true);
            rbIRelative.setChecked(false);
        } else {
            llIRelative.setVisibility(View.VISIBLE);
            llIConstant.setVisibility(View.GONE);
            rbIConstant.setChecked(false);
            rbIRelative.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            if (rbfConstant.isChecked() && TextUtils.isEmpty(ed_fajerIq.getText().toString().trim())) {
                Utils.showCustomToast(activity, getString(R.string.chooseFajerIqama));
                return;
            }
            if (rbSConstant.isChecked() && TextUtils.isEmpty(edShroqIq.getText().toString().trim())) {
                Utils.showCustomToast(activity, getString(R.string.chooseSunriseIqama));
                return;
            }
            if (rbdConstant.isChecked() && TextUtils.isEmpty(edDuhrIq.getText().toString().trim())) {
                Utils.showCustomToast(activity, getString(R.string.chooseDuhrIqama));
                return;
            }
            if (rbAConstant.isChecked() && TextUtils.isEmpty(edAserIq.getText().toString().trim())) {
                Utils.showCustomToast(activity, getString(R.string.chooseAsrIqama));
                return;
            }
            if (rbmConstant.isChecked() && TextUtils.isEmpty(edMagribIq.getText().toString().trim())) {
                Utils.showCustomToast(activity, getString(R.string.chooseMaghribIqama));
                return;
            }
            if (rbIConstant.isChecked() && TextUtils.isEmpty(edIshaIq.getText().toString().trim())) {
                Utils.showCustomToast(activity, getString(R.string.chooseIshaIqama));
                return;
            }
            settings.setFajrEkamaIsTime(rbfConstant.isChecked());
            settings.setFajrEkamaTime((rbfConstant.isChecked()) ? (ed_fajerIq.getText().toString().trim()) : settings.getFajrEkamaTime());
            settings.setFajrEkama(TextUtils.isEmpty(edFajer.getText().toString().trim()) ? settings.getFajrEkama()
                    : Integer.parseInt(edFajer.getText().toString().trim()));
            settings.setFajrAzkar(TextUtils.isEmpty(edFajerِAzkarP.getText().toString().trim()) ? settings.getFajrAzkar()
                    : Integer.parseInt(edFajerِAzkarP.getText().toString().trim()));
            settings.setFajrAzkarTimer(TextUtils.isEmpty(edFajerِAzkarApp.getText().toString().trim()) ? settings.getFajrAzkarTimer()
                    : Integer.parseInt(edFajerِAzkarApp.getText().toString().trim()));

            settings.setDhuhrEkamaIsTime(rbdConstant.isChecked());
            settings.setDhuhrEkamaTime((rbdConstant.isChecked()) ? (edDuhrIq.getText().toString().trim()) : settings.getDhuhrEkamaTime());
            settings.setDhuhrEkama(TextUtils.isEmpty(edDuhr.getText().toString().trim()) ? settings.getDhuhrEkama()
                    : Integer.parseInt(edDuhr.getText().toString().trim()));
            settings.setDhuhrAzkar(TextUtils.isEmpty(edDuhrAzkarP.getText().toString().trim()) ? settings.getDhuhrAzkar()
                    : Integer.parseInt(edDuhrAzkarP.getText().toString().trim()));
            settings.setDhuhrAzkarTimer(TextUtils.isEmpty(edDuhrِAzkarApp.getText().toString().trim()) ? settings.getDhuhrAzkarTimer()
                    : Integer.parseInt(edDuhrِAzkarApp.getText().toString().trim()));

            settings.setAsrEkamaIsTime(rbAConstant.isChecked());
            settings.setAsrEkamaTime((rbAConstant.isChecked()) ? (edAserIq.getText().toString().trim()) : settings.getAsrEkamaTime());
            settings.setAsrEkama(TextUtils.isEmpty(edAser.getText().toString().trim()) ? settings.getAsrEkama()
                    : Integer.parseInt(edAser.getText().toString().trim()));
            settings.setAsrAzkar(TextUtils.isEmpty(edAserAzkarP.getText().toString().trim()) ? settings.getAsrAzkar()
                    : Integer.parseInt(edAserAzkarP.getText().toString().trim()));
            settings.setAsrAzkarTimer(TextUtils.isEmpty(edAserAzkarApp.getText().toString().trim()) ? settings.getAsrAzkarTimer()
                    : Integer.parseInt(edAserAzkarApp.getText().toString().trim()));

            settings.setMagribEkamaIsTime(rbmConstant.isChecked());
            settings.setMagribEkamaTime((rbmConstant.isChecked()) ? (edMagribIq.getText().toString().trim()) : settings.getMagribEkamaTime());
            settings.setMagribEkama(TextUtils.isEmpty(edMagrib.getText().toString().trim()) ? settings.getMagribEkama()
                    : Integer.parseInt(edMagrib.getText().toString().trim()));
            settings.setMagribAzkar(TextUtils.isEmpty(edMagribAzkarP.getText().toString().trim()) ? settings.getMagribAzkar()
                    : Integer.parseInt(edMagribAzkarP.getText().toString().trim()));
            settings.setMagribAzkarTimer(TextUtils.isEmpty(edMagribAzkarApp.getText().toString().trim()) ? settings.getMagribAzkarTimer()
                    : Integer.parseInt(edMagribAzkarApp.getText().toString().trim()));

            settings.setIshaEkamaIsTime(rbIConstant.isChecked());
            settings.setIshaEkamaTime((rbIConstant.isChecked()) ? (edIshaIq.getText().toString().trim()) : settings.getIshaEkamaTime());
            settings.setIshaEkama(TextUtils.isEmpty(edIsha.getText().toString().trim()) ? settings.getIshaEkama()
                    : Integer.parseInt(edIsha.getText().toString().trim()));
            settings.setIshaAzkar(TextUtils.isEmpty(edIshaAzkarP.getText().toString().trim()) ? settings.getIshaAzkar()
                    : Integer.parseInt(edIshaAzkarP.getText().toString().trim()));
            settings.setIshaAzkarTimer(TextUtils.isEmpty(edIshaAzkarApp.getText().toString().trim()) ? settings.getIshaAzkarTimer()
                    : Integer.parseInt(edIshaAzkarApp.getText().toString().trim()));

            settings.setAlShrouqEkamaIsTime(rbSConstant.isChecked());
            settings.setAlShrouqEkamaTime((rbSConstant.isChecked()) ? (edShroqIq.getText().toString().trim()) : iqShT);
            settings.setAlShrouqEkama(TextUtils.isEmpty(edShroq.getText().toString().trim()) ? iqSh
                    : Integer.parseInt(edShroq.getText().toString().trim()));

            settings.setStatusAthanVoiceF(cb_soundF.isChecked());
            settings.setStatusEkamaVoiceF(cb_IqamaSoundF.isChecked());
            settings.setStatusAthanVoiceD(cb_soundD.isChecked());
            settings.setStatusEkamaVoiceD(cb_IqamaSoundD.isChecked());
            settings.setStatusAthanVoiceA(cb_soundA.isChecked());
            settings.setStatusEkamaVoiceA(cb_IqamaSoundA.isChecked());
            settings.setStatusAthanVoiceM(cb_soundM.isChecked());
            settings.setStatusEkamaVoiceM(cb_IqamaSoundM.isChecked());
            settings.setStatusAthanVoiceI(cb_soundI.isChecked());
            settings.setStatusEkamaVoiceI(cb_IqamaSoundI.isChecked());

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
        } else if (v == iv_back) {
            onBackPressed();
        }
    }

    private void saveChanges() {
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("جاري الحفظ...");
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
