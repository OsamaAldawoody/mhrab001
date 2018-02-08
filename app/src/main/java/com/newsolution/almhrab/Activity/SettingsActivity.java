package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.newsolution.almhrab.Adapters.LocationAdapter;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DataBaseHelper;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends Activity {
    ImageView iv_back;
    private LinearLayout ll_location,
            llTones, ll_adv, parentPanel, ll_NewAds, ll_azkar;
    private SharedPreferences sp;
    Activity activity;
    private SharedPreferences.Editor spedit;
    private SQLiteDatabase mDb;
    private DataBaseHelper DbHelper;
    ArrayList<City> cities;

    private ListView lv;
    LocationAdapter locations;
    private EditText ed_location;
    private GlobalVars gv;
    private Button btn_save;
    private EditText ed_fajer, ed_duhr, ed_aser, ed_magrib, ed_isha;
    private boolean isDialogShown = false;
    private DBOperations DBO;
    String[] prayTimes;
    private int cityId;
    public String cfajr, csunrise, cdhohr, casr, cmaghrib, cisha;
    public String nextPray;
    public Calendar today = Calendar.getInstance();
    double day = today.get(Calendar.DAY_OF_MONTH);
    double month = today.get(Calendar.MONTH) + 1;
    double year = today.get(Calendar.YEAR);
    private int long1, long2;
    private int lat1, lat2;
    private boolean iqamaSoundOn, soundOn;
    private TextView tvAbout, tvBatteryIn, tvBatteryOut, tvLogout, tvNotificationSet, tvًPriority, tvOtherSet, tvIqamaSet;
    TextView edHijriSet;
    private Button btnSaveTempIn, btnSaveTempOut, btnSaveHijri;
    private OptionSiteClass settings;
    private int hijriDiff;
    private ArrayList<String> list;
    private TextView tvSettingAccount;
    private CheckBox cb_voiceRec;
    private LinearLayout ll_voiceRec;
    private EditText edTempIn, edTempOut;
    private LinearLayout llbatteryOut, llbatteryIn;
    private TextView tvScanSensors, tvLocationHint;
    private City city;
    private CheckBox cb_sleep, cb_news, cb_friday;
    private LinearLayout layout, llSleep, llNews, llFriday;
    private Dialog fridayDialog;
    private Dialog sleepDialog;
    private EditText edAppearTimer, edRecordTimer, ed_play, ed_stop;
    private Button btn_cancel, btn_add;

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
        activity = this;
        setColor();
        gv = (GlobalVars) getApplicationContext();
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        spedit = sp.edit();

        setContentView(R.layout.activity_settings);
//        Utils.showCustomToast(activity,sp.getString("masjedPW","")+"  99");
        checkLogin();
        DBO = new DBOperations(this);
        gv = (GlobalVars) getApplicationContext();
        DBO.open();
        city = DBO.getCityById(sp.getInt("cityId", -1));
        settings = DBO.getSettings();
        DBO.close();

        iv_back = (ImageView) findViewById(R.id.iv_back);
        parentPanel = (LinearLayout) findViewById(R.id.parentPanel);
        layout = (LinearLayout) findViewById(R.id.layout);
        llSleep = (LinearLayout) findViewById(R.id.llSleep);
        cb_sleep = (CheckBox) findViewById(R.id.cb_sleep);
        llNews = (LinearLayout) findViewById(R.id.llNews);
        cb_news = (CheckBox) findViewById(R.id.cb_news);
        llFriday = (LinearLayout) findViewById(R.id.llFriday);
        llFriday.setVisibility(View.GONE);
        cb_friday = (CheckBox) findViewById(R.id.cb_friday);
        edTempIn = (EditText) findViewById(R.id.edTemp);
        edTempOut = (EditText) findViewById(R.id.edTempOut);
        btnSaveTempIn = (Button) findViewById(R.id.btnSaveTemp);
        btnSaveTempOut = (Button) findViewById(R.id.btnSaveTempOut);
        llbatteryOut = (LinearLayout) findViewById(R.id.llbatteryOut);
        llbatteryIn = (LinearLayout) findViewById(R.id.llbatteryIn);
        tvBatteryOut = (TextView) findViewById(R.id.tvBatteryOut);
        tvBatteryIn = (TextView) findViewById(R.id.tvBatteryIn);
        tvًPriority = (TextView) findViewById(R.id.tvًPriority);
        tvOtherSet = (TextView) findViewById(R.id.tvOtherSet);
        tvIqamaSet = (TextView) findViewById(R.id.tvIqamaSet);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvAbout = (TextView) findViewById(R.id.tvAbout);
        tvNotificationSet = (TextView) findViewById(R.id.tvNotificationSet);
        edHijriSet = (TextView) findViewById(R.id.edHijriSet);
        btnSaveHijri = (Button) findViewById(R.id.btnSaveHijri);
        tvSettingAccount = (TextView) findViewById(R.id.tvSettingAccount);
        cb_voiceRec = (CheckBox) findViewById(R.id.cb_voiceRec);
        ll_voiceRec = (LinearLayout) findViewById(R.id.ll_voiceRec);
        cb_voiceRec.setChecked(true);
        if (sp.getBoolean("voiceRec", true)) {
            cb_voiceRec.setChecked(true);
        } else {
            cb_voiceRec.setChecked(false);
        }

        if (sp.getBoolean("news", true)) {
            cb_news.setChecked(true);
        } else {
            cb_news.setChecked(false);
        }
        if (sp.getBoolean("friday", false)) {
            cb_friday.setChecked(true);
        } else {
            cb_friday.setChecked(false);
        }
        if (sp.getBoolean("sleep", false)) {
            cb_sleep.setChecked(true);
        } else {
            cb_sleep.setChecked(false);
        }
        //ll_eqama_settings.setVisibility(View.GONE);

        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        tvScanSensors = (TextView) findViewById(R.id.tvScanSensors);
        tvLocationHint = (TextView) findViewById(R.id.tvLocationHint);
        if (city != null)
            tvLocationHint.setText("المدينة الحالية " + city.getName());
        llTones = (LinearLayout) findViewById(R.id.llTones);
        ll_adv = (LinearLayout) findViewById(R.id.ll_adv);
        ll_azkar = (LinearLayout) findViewById(R.id.ll_azkar);
        ll_NewAds = (LinearLayout) findViewById(R.id.ll_NewAds);
        hijriDiff = settings.getDateHijri();
        if (hijriDiff == 0)
            edHijriSet.setText("لا يوجد فرق");
        else if (hijriDiff == +1)
            edHijriSet.setText("إضافة يوم");
        else if (hijriDiff == +2)
            edHijriSet.setText("إضافة يومين");
        else if (hijriDiff == -1)
            edHijriSet.setText("إرجاع يوم");
        else if (hijriDiff == -2)
            edHijriSet.setText("إرجاع يومين");
        list = new ArrayList<String>();
        list.add("لا يوجد فرق");
        list.add("إضافة يوم");
        list.add("إضافة يومين");
        list.add("إرجاع يوم");
        list.add("إرجاع يومين");
        tvBatteryIn.setText(sp.getString("batteryIn", "0") + "%");
        tvBatteryOut.setText(sp.getString("batteryOut", "0") + "%");
        edTempIn.setText(sp.getString("SN1", ""));
        edTempOut.setText(sp.getString("SN2", ""));
        ll_voiceRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_voiceRec.isChecked()) {
                    spedit.putBoolean("voiceRec", false).commit();
                    cb_voiceRec.setChecked(false);
                } else {
                    cb_voiceRec.setChecked(true);
                    spedit.putBoolean("voiceRec", true).commit();
                }
            }
        });
        cb_voiceRec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_voiceRec.isChecked()) {
                    spedit.putBoolean("voiceRec", true).commit();
                } else {
                    spedit.putBoolean("voiceRec", false).commit();
                }
            }
        });
        llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_news.isChecked()) {
                    spedit.putBoolean("news", false).commit();
                    cb_news.setChecked(false);
                } else {
                    cb_news.setChecked(true);
                    spedit.putBoolean("news", true).commit();
                }
            }
        });
        cb_news.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_news.isChecked()) {
                    spedit.putBoolean("news", true).commit();
                } else {
                    spedit.putBoolean("news", false).commit();
                }
            }
        });
        llSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_sleep.isChecked()) {
                    spedit.putBoolean("sleep", false).commit();
                    cb_sleep.setChecked(false);
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, (24 * 60 * 60 * 1000));
                } else {
                    cb_sleep.setChecked(true);
                    spedit.putBoolean("sleep", true).commit();
//                    showSleepDialog();c
                }
            }
        });
        cb_sleep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_sleep.isChecked()) {
                    spedit.putBoolean("sleep", true).commit();
                    showSleepDialog();
                } else {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, (24 * 60 * 60 * 1000));
                    spedit.putBoolean("sleep", false).commit();
                }

            }
        });
        llFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_friday.isChecked()) {
                    spedit.putBoolean("friday", false).commit();
                    cb_friday.setChecked(false);
                } else {
                    cb_friday.setChecked(true);
                    spedit.putBoolean("friday", true).commit();
//                    showFridayDialog();
                }
            }
        });
        cb_friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_friday.isChecked()) {
                    spedit.putBoolean("friday", true).commit();
                    showFridayDialog();
                } else {
                    spedit.putBoolean("friday", false).commit();
                }
            }
        });
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, AboutApp.class));
            }
        });
        tvScanSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, ScanDeviceActivity.class));
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(getString(R.string.logout)).
                        setMessage(getString(R.string.confirm_logout))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                spedit.remove("masjedId");
                                spedit.putInt("masjedId", -1).commit();
                                spedit.remove("cityId");
                                spedit.remove("masjedGUID");
                                spedit.remove(AppConst.LASTUPDATE);
                                spedit.clear();
                                Intent intent = new Intent(activity, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        edHijriSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show Dialog
                initiat(list);
            }
        });
        btnSaveTempIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edTempIn.getText().toString().trim())) {
                    edTempIn.setError("أدخل رقم جهاز مستشعر الحرارة الداخلي");
                    return;
                }
                spedit.putString("SN1", edTempIn.getText().toString().trim()).commit();
                Utils.showCustomToast(activity, getString(R.string.success_edit));

            }
        });
        btnSaveTempOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edTempOut.getText().toString().trim())) {
                    edTempOut.setError("أدخل رقم جهاز مستشعر الحرارة الخارجي");
                    return;
                }
                spedit.putString("SN2", edTempOut.getText().toString().trim()).commit();
                Utils.showCustomToast(activity, getString(R.string.success_edit));

            }
        });
        btnSaveHijri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setDateHijri(hijriDiff);
                if (sp.getInt("priority", 0) == 1) {
                    if (Utils.isOnline(activity)) {
                        saveChanges();
                    } else {
                        Utils.showCustomToast(activity, getString(R.string.no_internet));
                    }
                } else {
                    DBO.insertSettings(settings);
                    spedit.putInt("hijriDiff", settings.getDateHijri()).commit();
                    Utils.showCustomToast(activity, getString(R.string.success_edit));
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(activity, MainActivity.class));
                finish();
            }
        });

        ll_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, NewsActivity.class));
            }
        });
        tvًPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChoosePriority.class);
                startActivity(intent);
                //finish();
            }
        });
        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
//                startActivity(new Intent(SettingsActivity.this, Advertisments.class));
            }
        });
        llTones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeTones.class));
            }
        });
        ll_azkar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AzkarAct.class));
            }
        });
        ll_NewAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AdsActivity.class));
            }
        });
        tvIqamaSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showIqamaDialog();
                startActivity(new Intent(activity, IqamaSettings.class));

            }
        });
        tvOtherSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, IqamaSettings.class));

            }
        });
        tvNotificationSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showIqamaDialog();
                startActivity(new Intent(activity, NotificationSettings.class));

            }
        });
        tvSettingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, AccountSetting.class));
            }
        });
    }

    private void checkLogin() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_user, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
        final EditText ed_caption = (EditText) dialogView.findViewById(R.id.ed_caption);
        ed_caption.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed_caption, InputMethodManager.SHOW_IMPLICIT);

        Button save = (Button) dialogView.findViewById(R.id.save);
        Button cancel = (Button) dialogView.findViewById(R.id.cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(activity);
                if (TextUtils.isEmpty(ed_caption.getText().toString().trim())) {
                    ed_caption.setError("أدخل كلمة المرور للحساب");
                    return;
                }
                Log.i("//////////: ", sp.getString("masjedPW", "") + " ll");
                if (!(ed_caption.getText().toString().trim()).equals(sp.getString("masjedPW", ""))) {
                    ed_caption.setError("كلمة المرور غير صحيحة");
                    return;
                }
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showFridayDialog() {
        View view = getLayoutInflater().inflate(R.layout.friday_dialog, null);
        edRecordTimer = (EditText) view.findViewById(R.id.edRecordTimer);
        edAppearTimer = (EditText) view.findViewById(R.id.edAppearTimer);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        fridayDialog = new Dialog(this);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edRecordTimer.getText().toString())) {
                    edRecordTimer.setError("هذا الحقل مطلوب");
                    return;
                }
                if (TextUtils.isEmpty(edAppearTimer.getText().toString())) {
                    edAppearTimer.setError("هذا الحقل مطلوب");
                    return;
                }
                spedit.putInt("recordPeriod", Integer.parseInt(edRecordTimer.getText().toString().trim())).commit();
                spedit.putInt("appearPeriod", Integer.parseInt(edAppearTimer.getText().toString().trim())).commit();
                Utils.showCustomToast(activity, getString(R.string.saved));
                if (fridayDialog.isShowing()) fridayDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp.getInt("recordPeriod", -1) == -1 && sp.getInt("appearPeriod", -1) == -1)
                    cb_friday.setChecked(false);
                if (fridayDialog.isShowing()) fridayDialog.dismiss();
            }
        });
        edRecordTimer.setText(sp.getInt("recordPeriod", -1) != -1 ? sp.getInt("recordPeriod", -1) + "" : "");
        edAppearTimer.setText(sp.getInt("appearPeriod", -1) != -1 ? sp.getInt("appearPeriod", -1) + "" : "");
        fridayDialog.setContentView(view);
        fridayDialog.show();
        fridayDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (sp.getInt("recordPeriod", -1) == -1 && sp.getInt("appearPeriod", -1) == -1)
                    cb_friday.setChecked(false);
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = fridayDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void showSleepDialog() {
        View view = getLayoutInflater().inflate(R.layout.sleep_dialog, null);
        ed_play = (EditText) view.findViewById(R.id.ed_play);
        ed_stop = (EditText) view.findViewById(R.id.ed_stop);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        sleepDialog = new Dialog(this);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ed_play.getText().toString())) {
                    ed_play.setError("هذا الحقل مطلوب");
                    return;
                }
                if (TextUtils.isEmpty(ed_stop.getText().toString())) {
                    ed_stop.setError("هذا الحقل مطلوب");
                    return;
                }
                if (sleepDialog.isShowing()) sleepDialog.dismiss();
                settings.setCloseScreenAfterIsha(Integer.parseInt(ed_play.getText().toString().trim()));
                settings.setRunScreenBeforeFajr(Integer.parseInt(ed_stop.getText().toString().trim()));
                if (sp.getInt("priority", 0) == 1) {
                    if (Utils.isOnline(activity)) {
                        final ProgressDialog pdS = new ProgressDialog(activity);
                        pdS.setMessage(getString(R.string.wait));
                        pdS.setCanceledOnTouchOutside(false);
                        pdS.show();
                        WS.UpdateSettings(activity, settings, new OnLoadedFinished() {
                            @Override
                            public void onSuccess(String response) {
                                if (pdS.isShowing())
                                    pdS.dismiss();
                                spedit.putInt("sleepOn", Integer.parseInt(ed_play.getText().toString().trim())).commit();
                                spedit.putInt("sleepOff", Integer.parseInt(ed_stop.getText().toString().trim())).commit();
//                String sleepOnTime= Utils.addToTime(sp.getString("isha",""),sp.getInt("sleepOn",0)+"");
//                String sleepOffTime= Utils.diffFromTime(sp.getString("suh",""),sp.getInt("sleepOff",0)+"");
//                spedit.putString("sleepOnTime", sleepOnTime).commit();
//                spedit.putString("sleepOffTime", sleepOffTime).commit();
                                Utils.showCustomToast(activity, getString(R.string.saved));
                                setSleepPeriod();
                            }

                            @Override
                            public void onFail(String error) {
                                if (pdS.isShowing())
                                    pdS.dismiss();
                            }
                        });

                    } else {
                        Utils.showCustomToast(activity, getString(R.string.no_internet));
                    }
                } else {
                    DBO.insertSettings(settings);
                    spedit.putInt("sleepOn", Integer.parseInt(ed_play.getText().toString().trim())).commit();
                    spedit.putInt("sleepOff", Integer.parseInt(ed_stop.getText().toString().trim())).commit();
                    Utils.showCustomToast(activity, getString(R.string.saved));
                    setSleepPeriod();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp.getInt("sleepOff", -1) == -1 && sp.getInt("sleepOff", -1) == -1)
                    cb_sleep.setChecked(false);
                if (sleepDialog.isShowing()) sleepDialog.dismiss();
            }
        });
        ed_play.setText(sp.getInt("sleepOn", -1) != -1 ? sp.getInt("sleepOn", -1) + "" : "");
        ed_stop.setText(sp.getInt("sleepOff", -1) != -1 ? sp.getInt("sleepOff", -1) + "" : "");
        sleepDialog.setContentView(view);
        sleepDialog.show();
        sleepDialog.setCancelable(false);
        sleepDialog.setCanceledOnTouchOutside(false);
//        sleepDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                if (sp.getInt("sleepOff", -1) == -1 && sp.getInt("sleepOff", -1) == -1)
//                    cb_sleep.setChecked(false);
//            }
//        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = sleepDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }
    private void setSleepPeriod() {
        String sleepOn = Utils.addToTime(sp.getString("isha", ""), settings.getCloseScreenAfterIsha()/* sp.getInt("sleepOn", 0) */ + "");
        String sleepOff = Utils.diffFromTime(sp.getString("suh", ""), settings.getRunScreenBeforeFajr()/*sp.getInt("sleepOff", 0) */ + "");
//        Log.e("**//sleepOn", sleepOn + "  **");
//        Log.e("**//sleepOff", sleepOff);
//        sleepOn = "13:15:00";
//        sleepOff = "13:20:00";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date start = sdf.parse(sleepOn);
            Date end = sdf.parse(sleepOff);
            if (end.before(start)) {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(Calendar.DAY_OF_YEAR, 1);
                end.setTime(mCal.getTimeInMillis());
            }
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String startDate = df.format(start);
            String endDate = df.format(end);
            spedit.putString("startTime", startDate).commit();
            spedit.putString("endTime", endDate).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiat(final ArrayList<String> list) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        for (int i = 0; i < list.size(); i++) {
            arrayAdapter.add(list.get(i).toString());
        }

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    hijriDiff = 0;
                    edHijriSet.setText("لا يوجد فرق");
                } else if (which == 1) {
                    hijriDiff = +1;
                    edHijriSet.setText("إضافة يوم");
                } else if (which == 2) {
                    hijriDiff = +2;
                    edHijriSet.setText("إضافة يومين");
                } else if (which == 3) {
                    hijriDiff = -1;
                    edHijriSet.setText("إرجاع يوم");
                } else if (which == 4) {
                    hijriDiff = -2;
                    edHijriSet.setText("إرجاع يومين");
                }
                String hijriDiffTit = (arrayAdapter.getItem(which));
                edHijriSet.setText(hijriDiffTit);
                dialog.dismiss();
            }
        });
        builderSingle.show();
        builderSingle.setCancelable(true);
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
                spedit.putInt("hijriDiff", settings.getDateHijri()).commit();
            }

            @Override
            public void onFail(String error) {
                pd.dismiss();
                Utils.showCustomToast(activity, "" + error);

            }
        });
    }

    private void setColor() {
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.back_text));
        } catch (NoSuchMethodError ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(getString(R.string.location_hints));
        View view = getLayoutInflater().inflate(R.layout.dialog_main, null);
        lv = (ListView) view.findViewById(R.id.custom_list);
        ed_location = (EditText) view.findViewById(R.id.ed_location);
        DBO.open();
        cities = DBO.getAllCity();
        Log.i("+++city", cities.size() + "  ");
        DBO.close();
        locations = new LocationAdapter(SettingsActivity.this, cities);
        lv.setAdapter(locations);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityId = ((City) lv.getItemAtPosition(i)).getId();
                City city = ((City) lv.getItemAtPosition(i));
//                Toast.makeText(SettingsActivity.this,city.getId()+" :"+city.getName(), Toast.LENGTH_SHORT).show();
                updateMasjedCity(city);
                dialog.dismiss();

            }
        });
        ed_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    cities.clear();
                    DBO.open();
                    cities = DBO.searchCity(ed_location.getText().toString());
                    DBO.close();
                    locations = new LocationAdapter(SettingsActivity.this, cities);
                    lv.setAdapter(locations);
                    locations.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dialog.setContentView(view);

        dialog.show();

    }

    private void updateMasjedCity(final City city) {
        if (sp.getInt("priority", 0) == 1) {
            if (Utils.isOnline(activity)) {
                final ProgressDialog pd = new ProgressDialog(activity);
                pd.setMessage(getString(R.string.wait));
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                WS.updateCity(activity, city.getId(), new OnLoadedFinished() {
                    @Override
                    public void onSuccess(String response) {
                        if (pd.isShowing())
                            pd.dismiss();
                        spedit.remove("cityId").commit();
                        spedit.putInt("cityId", cityId).commit();
                        spedit.putInt("lat1", city.getLat1()).commit();
                        spedit.putInt("lat2", city.getLat2()).commit();
                        spedit.putInt("long1", city.getLon1()).commit();
                        spedit.putInt("long2", city.getLon2()).commit();
                        tvLocationHint.setText("المدينة الحالية " + city.getName());
                        Utils.showCustomToast(activity, getString(R.string.success_edit));
                    }

                    @Override
                    public void onFail(String error) {
                        if (pd.isShowing())
                            pd.dismiss();
                    }
                });
            } else {
                Utils.showCustomToast(activity, getString(R.string.no_internet));
            }
        } else {
            spedit.putInt("cityId", cityId).commit();
            spedit.putInt("lat1", city.getLat1()).commit();
            spedit.putInt("lat2", city.getLat2()).commit();
            spedit.putInt("long1", city.getLon1()).commit();
            spedit.putInt("long2", city.getLon2()).commit();
            Utils.showCustomToast(activity, getString(R.string.success_edit));
            tvLocationHint.setText("المدينة الحالية " + city.getName());
        }
    }
}
