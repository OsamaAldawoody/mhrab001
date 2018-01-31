package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.Model.AdsPeriods;
import com.newsolution.almhrab.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.newsolution.almhrab.AppConstants.Constants.RESULT_LOAD_IMAGE;

public class EditAdsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Activity activity;
    private ImageView ivAdsVideoThumb, ivSelectVideo, ivAdsImg, ivThumb, ivSelectImg, iv_back;
    private RadioGroup rgAdsType;
    private RadioButton rbText, rbVideo, rbImage;
    private RelativeLayout rlImage, rlVideo, rlText;
    private EditText edAdsTitle, edAdsText, ed_endTime, ed_startTime, ed_start, ed_end, edAddAppearance;
    private TextView tvSave;
    private TextView tittleA;
    private LinearLayout llAdsPeriods;
    private int REQUEST_PERMISSIONS = 100;
    private Uri selectedImage = null;
    private String image_str;
    private int VIDEO_SELECT = 2;
    private Uri videoData = null;
    private int type = 1;
    private String selectedImagePath = "";
    private String selectedVideoPath = "";
    private SharedPreferences sp;
    private DBOperations DBO;
    private CheckBox cbSat, cbSun, cbMon, cbTue, cbWed, cbThu, cbFri;
    private boolean isConflict = false;
    private boolean isConflictAds = false;
    private Button btnCheck;
    ArrayList<Boolean> conflictList = new ArrayList<>();
    ArrayList<Integer> checkList = new ArrayList<>();
    ArrayList<AdsPeriods> adsPeriodsList = new ArrayList<>();
    ArrayList<AdsPeriods> adsList = new ArrayList<>();
    //    ArrayList<Ads> adsList = new ArrayList<>();
    ArrayList<String> prayerTimes = new ArrayList<>();
    private int advId;
    int items = 0;
    int count = 0;
    private AppCompatImageView down, up;
    private Ads adv;
    private String advText, title, endDate, startDate;

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
        setContentView(R.layout.activity_edit_ads);
        DBO = new DBOperations(activity);
        adv = (Ads) getIntent().getSerializableExtra("ads");
        advId = adv.getId();
        DBO.open();
        ArrayList<AdsPeriods> list = DBO.getAdvPeriods(adv.getId());
        DBO.close();
        int size = list.size();
        String day = "";
        String ids = "";
        Log.i("//list size: ", size + "");
        for (int i = 0; i < size; i++) {
            DBO.open();
            AdsPeriods periods = list.get(i);
            periods.setAdvId(adv.getId());
            day = DBO.getAdvPeriods(adv.getId(), periods.getStartTime(), periods.getEndTime());
            ids = DBO.getAdvPeriodsIds(adv.getId(), periods.getStartTime(), periods.getEndTime());
            DBO.close();
            AdsPeriods newAdsPeriod = new AdsPeriods(periods.getAdvId(), periods.getStartTime(), periods.getEndTime(),
                    periods.getStartDate(), periods.getEndDate(), day, ids, true);
            Log.i("//list day int: ", day + " : ids: " + ids);
            if (!isContain(adsList, newAdsPeriod)) adsList.add(newAdsPeriod);
        }
        Log.i("//list Size: ", adsList.size() + "");
        List<String> al = new ArrayList<>();
// add elements to al, including duplicates
//        Set<AdsPeriods> hs = new HashSet<>();
//        hs.addAll(adsList);
//        adsList.clear();
//        adsList.addAll(hs);
        Log.i("//list Size after set: ", adsList.size() + "");
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        askForPermissions(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);
        prayerTimes.add(sp.getString("suh", ""));
//        prayerTimes.add( sp.getString("sun", ""));
        prayerTimes.add(sp.getString("duh", ""));
        prayerTimes.add(sp.getString("asr", ""));
        prayerTimes.add(sp.getString("magrib", ""));
        prayerTimes.add(sp.getString("isha", ""));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        ivThumb = (ImageView) findViewById(R.id.ivThumb);
        ivSelectImg = (ImageView) findViewById(R.id.ivSelectImg);
        ivAdsImg = (ImageView) findViewById(R.id.ivAdsImg);
        ivAdsVideoThumb = (ImageView) findViewById(R.id.ivAdsVideoThumb);
        ivSelectVideo = (ImageView) findViewById(R.id.ivSelectVideo);
        rgAdsType = (RadioGroup) findViewById(R.id.rgAdsType);
        rbImage = (RadioButton) findViewById(R.id.rbImage);
        rbVideo = (RadioButton) findViewById(R.id.rbVideo);
        rbText = (RadioButton) findViewById(R.id.rbText);
        rlImage = (RelativeLayout) findViewById(R.id.rlImage);
        rlVideo = (RelativeLayout) findViewById(R.id.rlVideo);
        rlText = (RelativeLayout) findViewById(R.id.rlText);
        edAdsText = (EditText) findViewById(R.id.edAdsText);
        edAdsTitle = (EditText) findViewById(R.id.edAdsTitle);
        edAddAppearance = (EditText) findViewById(R.id.edAddAppearance);
        edAddAppearance.setFocusable(true);
        up = (AppCompatImageView) findViewById(R.id.up);
        down = (AppCompatImageView) findViewById(R.id.down);
        llAdsPeriods = (LinearLayout) findViewById(R.id.llAdsPeriods);
        ed_start = (EditText) findViewById(R.id.ed_start);
        ed_end = (EditText) findViewById(R.id.ed_end);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvSave.setVisibility(View.VISIBLE);
        tittleA = (TextView) findViewById(R.id.tittleA);

        iv_back.setOnClickListener(this);
        ivSelectImg.setOnClickListener(this);
        ivSelectVideo.setOnClickListener(this);
        ivAdsVideoThumb.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        rgAdsType.setOnCheckedChangeListener(this);

        edAddAppearance.setText(adsList.size() + "");
        llAdsPeriods.removeAllViews();
        tittleA.setVisibility(View.VISIBLE);
        for (int i = 0; i < adsList.size(); i++) {
            checkList.add(i);
        }
        if (size < checkList.size()) {
            size = checkList.size();
            edAddAppearance.setText(size + "");
        }
        size = Integer.parseInt(edAddAppearance.getText().toString().trim());
        if (size > 0) {
            llAdsPeriods.removeAllViews();
            for (int i = 0; i < size; i++) {
                AdsPeriods adsPeriods = null;
                Log.i("///*: size ", adsList.size() + "");
                if (i < adsList.size()) {
                    adsPeriods = adsList.get(i);
                    llAdsPeriods.addView(getItem(i, adsPeriods));
                } else
                    llAdsPeriods.addView(getItem(i, null));
            }
        } else {
            llAdsPeriods.removeAllViews();
            tittleA.setVisibility(View.GONE);
            for (int i = 0; i < adsList.size(); i++) {
                AdsPeriods adsPeriods = null;
                Log.i("///*: size ", adsList.size() + "");
                tittleA.setVisibility(View.VISIBLE);
                adsPeriods = adsList.get(i);
                llAdsPeriods.addView(getItem(i, adsPeriods));
            }
        }
        endDate = adv.getEndDate();
        startDate = adv.getStartDate();
        advText = adv.getText();
        title = adv.getTitle();
        type = adv.getType();
        selectedImagePath = adv.getImage();
        selectedVideoPath = adv.getVideo();
        ed_end.setText(endDate);
        ed_start.setText(startDate);
        edAdsText.setText(advText);
        edAdsTitle.setText(title);
        if (type == 1) {
            videoData = null;
            selectedVideoPath = "";
            edAdsText.setText("");
            rbImage.setChecked(true);
            File f = new File(selectedImagePath);
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            ivAdsImg.setImageBitmap(bmp);
            Glide.with(activity).load(f).into(ivAdsImg);
        } else if (type == 2) {
            selectedImage = null;
            selectedImagePath = "";
            edAdsText.setText("");
            rbVideo.setChecked(true);
            videoData = Uri.parse(selectedVideoPath);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(selectedVideoPath
                    , MediaStore.Video.Thumbnails.MINI_KIND);
            System.out.println(">>>> bitmap " + bitmap);
            if (bitmap == null)
                return;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            image_str = Base64.encodeToString(byte_arr, 0);
            ivAdsVideoThumb.setImageBitmap(bitmap);
            ivThumb.setVisibility(View.VISIBLE);
        } else if (type == 3) {
            rbText.setChecked(true);
            selectedImage = null;
            selectedImagePath = "";
            videoData = null;
            selectedVideoPath = "";
            edAdsText.setText(advText);
            rlImage.setVisibility(View.GONE);
            rlVideo.setVisibility(View.GONE);
            rlText.setVisibility(View.VISIBLE);
        }

        ed_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDatePicker(ed_start);
//                showDatePicker(ed_start);
            }
        });
        ed_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDatePicker(ed_end);
//                showDatePicker(ed_end);
            }
        });

//        up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                    onBtnClickListener1.increase(view, position);
//                int amount = (Integer.parseInt(edAddAppearance.getText().toString())) + 1;
//                edAddAppearance.setText(amount + "");
//                tittleA.setVisibility(View.VISIBLE);
//            }
//        });
//        down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int amount = (Integer.parseInt(edAddAppearance.getText().toString()));
//                if (amount > 1) {
//                    amount = amount - 1;
//                }
//                if (checkList.size() == 0) {
//                    llAdsPeriods.removeAllViews();
//                    tittleA.setVisibility(View.GONE);
//                    if (amount != 0) {
//                        for (int i = 0; i < amount; i++) {
//                            llAdsPeriods.addView(getItem(i, adsPeriods));
//                        }
//                    }
//
//                }
////                else {
////                    amount = 1;
////                }
//                edAddAppearance.setText(amount + "");
//            }
//        });
        edAddAppearance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                checkList.clear();
//                tvSave.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(edAddAppearance.getText().toString().trim())) {
                    int size = Integer.parseInt(edAddAppearance.getText().toString().trim());
                    llAdsPeriods.removeAllViews();
                    tittleA.setVisibility(View.VISIBLE);
                    if (size < checkList.size()) {
                        size = checkList.size();
                        edAddAppearance.setText(size + "");
                    }
                    if (size > 0) {
                        llAdsPeriods.removeAllViews();
                        for (int i = 0; i < size; i++) {
                            AdsPeriods adsPeriods = null;
                            Log.i("///*: size ", adsList.size() + "");
                            if (i < adsList.size()) {
                                adsPeriods = adsList.get(i);
                                llAdsPeriods.addView(getItem(i, adsPeriods));
                            } else
                                llAdsPeriods.addView(getItem(i, null));
                        }
                    } else {
                        llAdsPeriods.removeAllViews();
                        tittleA.setVisibility(View.GONE);
                        for (int i = 0; i < adsList.size(); i++) {
                            AdsPeriods adsPeriods = null;
                            Log.i("///*: size ", adsList.size() + "");
                            tittleA.setVisibility(View.VISIBLE);
                            adsPeriods = adsList.get(i);
                            llAdsPeriods.addView(getItem(i, adsPeriods));
                        }
                    }
                } else {
//                        int size = checkList.size();
//                        edAddAppearance.setText(size+"");
                    llAdsPeriods.removeAllViews();
                    tittleA.setVisibility(View.GONE);
                }

            }
        });
    }

    private LinearLayout getItem(final int pos, AdsPeriods adsPeriods) {
        final LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.ads_period_row, null);
        ed_startTime = (EditText) ll.findViewById(R.id.ed_start);
        ed_endTime = (EditText) ll.findViewById(R.id.ed_end);
        CheckBox cbSa = (CheckBox) ll.findViewById(R.id.cbSat);
        CheckBox cbSu = (CheckBox) ll.findViewById(R.id.cbSun);
        CheckBox cbMo = (CheckBox) ll.findViewById(R.id.cbMon);
        CheckBox cbTu = (CheckBox) ll.findViewById(R.id.cbTue);
        CheckBox cbWe = (CheckBox) ll.findViewById(R.id.cbWed);
        CheckBox cbTh = (CheckBox) ll.findViewById(R.id.cbThu);
        CheckBox cbFr = (CheckBox) ll.findViewById(R.id.cbFri);
        Button btnCheck = (Button) ll.findViewById(R.id.btnCheck);
        Button btnDelete = (Button) ll.findViewById(R.id.btnDelete);
        Button btnUpdate = (Button) ll.findViewById(R.id.btnUpdate);
        if (adsPeriods != null) {
            if (adsPeriods.getDays().contains("1"))
                cbSa.setChecked(true);
            if (adsPeriods.getDays().contains("2"))
                cbSu.setChecked(true);
            if (adsPeriods.getDays().contains("3"))
                cbMo.setChecked(true);
            if (adsPeriods.getDays().contains("4"))
                cbTu.setChecked(true);
            if (adsPeriods.getDays().contains("5"))
                cbWe.setChecked(true);
            if (adsPeriods.getDays().contains("6"))
                cbTh.setChecked(true);
            if (adsPeriods.getDays().contains("7"))
                cbFr.setChecked(true);
            ed_startTime.setText(adsPeriods.getStartTime());
            ed_endTime.setText(adsPeriods.getEndTime());
//            btnCheck.setText(getString(R.string.edit));
            btnCheck.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
//            btnCheck.setText(getString(R.string.add));
            btnCheck.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        ed_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker((EditText) ((llAdsPeriods.getChildAt(pos)).findViewById(R.id.ed_start)));
            }
        });
        ed_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
//                showDateTimePicker(ed_endTime);
                showDateTimePicker((EditText) ((llAdsPeriods.getChildAt(pos)).findViewById(R.id.ed_end)));
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adsList.size() == 1) {
                    deleteAds();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity).setTitle(getString(R.string.confirm_delete))
                            .setMessage(getString(R.string.tv_delMsg))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deletePeriods(pos, adsList.get(pos));
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton(getString(R.string.cancel_delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setCancelable(false);
                    AlertDialog dialog = alert.create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }
            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adsPeriodsList.clear();
                final View v = llAdsPeriods.getChildAt(pos);
                EditText edStartTime = ((EditText) (v.findViewById(R.id.ed_start)));
                EditText edEndTime = ((EditText) (v.findViewById(R.id.ed_end)));
                cbSat = (CheckBox) v.findViewById(R.id.cbSat);
                cbSun = (CheckBox) v.findViewById(R.id.cbSun);
                cbMon = (CheckBox) v.findViewById(R.id.cbMon);
                cbTue = (CheckBox) v.findViewById(R.id.cbTue);
                cbWed = (CheckBox) v.findViewById(R.id.cbWed);
                cbThu = (CheckBox) v.findViewById(R.id.cbThu);
                cbFri = (CheckBox) v.findViewById(R.id.cbFri);
                Button btn = (Button) v.findViewById(R.id.btnCheck);
                Button btnUp = (Button) v.findViewById(R.id.btnUpdate);
                Button btnD = (Button) v.findViewById(R.id.btnDelete);
                isConflictAds = false;
                edAdsTitle.setError(null);
                edAdsText.setError(null);
                ed_start.setError(null);
                ed_end.setError(null);
                edStartTime.setError(null);
                edEndTime.setError(null);
                if (TextUtils.isEmpty(edAdsTitle.getText().toString().toString())) {
                    if (TextUtils.isEmpty(adv.getTitle())) {
                        edAdsTitle.setError(getString(R.string.required));
                        return;
                    } else edAdsTitle.setText(adv.getTitle());
                }
                if (type == 1 && selectedImage == null) {
                    if (TextUtils.isEmpty(adv.getImage())) {
                        Utils.showCustomToast(activity, "يجب اختيار صورة للإعلان");
                        return;
                    }
                    selectedImagePath = adv.getImage();

                }
                if (type == 2 && videoData == null) {
                    if (TextUtils.isEmpty(adv.getVideo())) {
                        Utils.showCustomToast(activity, "يجب اختيار فيديو للإعلان");
                        return;
                    }
                    selectedVideoPath = adv.getVideo();
                }
                if (type == 3 && TextUtils.isEmpty(edAdsText.getText().toString().toString())) {
                    if (TextUtils.isEmpty(adv.getText())) {
                        Utils.showCustomToast(activity, "يجب إدخال نص للإعلان");
                        return;
                    }
                    edAdsText.setText(adv.getText());
                }
                if (TextUtils.isEmpty(ed_start.getText().toString())) {
                    if (TextUtils.isEmpty(adv.getStartDate())) {
                        ed_start.setError("أدخل تاريخ بداية ظهورالإعلان");
                        return;
                    }
                    ed_start.setText(adv.getStartDate());

                }
                if (TextUtils.isEmpty(ed_end.getText().toString())) {
                    if (TextUtils.isEmpty(adv.getStartDate())) {
                        ed_end.setError("أدخل تاريخ نهاية ظهورالإعلان");
                        return;
                    }
                    ed_end.setText(adv.getEndDate());

                }
                if (!Utils.compareDate(ed_start.getText().toString(), ed_end.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_date));
                    ed_end.setError(getString(R.string.error_date));
                    return;
                }
                if (TextUtils.isEmpty(edStartTime.getText().toString())) {
                    edStartTime.setError("أدخل وقت بداية ظهورالإعلان");
                }
                if (TextUtils.isEmpty(edEndTime.getText().toString())) {
                    edEndTime.setError("أدخل وقت نهاية ظهورالإعلان");
                    return;
                }
                if (!Utils.compareTimes(edStartTime.getText().toString(), edEndTime.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_time));
                    edEndTime.setError(getString(R.string.error_time));
                    return;
                }
                if (!cbSat.isChecked() && !cbSun.isChecked() && !cbMon.isChecked() && !cbTue.isChecked() && !cbWed.isChecked()
                        && !cbThu.isChecked() && !cbFri.isChecked()) {
                    Utils.showCustomToast(activity, "يجب إدخال أيام ظهور الإعلان");
                    return;
                }
                Log.i("/////: ", edStartTime.getText().toString().trim());
                AdsPeriods adsPeriods = new AdsPeriods();
                adsPeriods.setStartTime(edStartTime.getText().toString().trim());
                adsPeriods.setEndTime(edEndTime.getText().toString().trim());
                adsPeriods.setStartDate(ed_start.getText().toString().trim());
                adsPeriods.setEndDate(ed_end.getText().toString().trim());
                List<Integer> dayList = new ArrayList<>();
                dayList.clear();
                if (cbSat.isChecked()) {
                    dayList.add(1);
                }
                if (cbSun.isChecked()) {
                    dayList.add(2);
                }
                if (cbMon.isChecked()) {
                    dayList.add(3);
                }
                if (cbTue.isChecked()) {
                    dayList.add(4);
                }
                if (cbWed.isChecked()) {
                    dayList.add(5);
                }
                if (cbThu.isChecked()) {
                    dayList.add(6);
                }
                if (cbFri.isChecked()) {
                    dayList.add(7);
                }
                String days = "";

                for (int x = 0; x < dayList.size(); x++) {
                    days = dayList.get(x) + "," + days;
                    adsPeriods.setDay(dayList.get(x));
//                    adsPeriodsList.add(adsPeriods);
                    DBO.open();
                    boolean hasConflict = DBO.itHasConflict(sp.getInt("masjedId", -1), adsPeriods);
                    DBO.close();
                    if (hasConflict) {
                        isConflictAds = true;
                        Utils.showCustomToast(activity, "يوجد تعارض في الوقت مع إعلان آخر");
                        break;
                    }
                    if (inPrayPeriod(prayerTimes, adsPeriods)) {
                        Utils.showCustomToast(activity, "يوجد تعارض مع مواعيد الصلاة");
                        break;
                    }
                    adsPeriodsList.add(adsPeriods);
                    if (x == dayList.size() - 1) {
                        if (!isConflictAds) {
                            int repeatNo = Integer.parseInt(edAddAppearance.getText().toString().trim());
                            if (type == 1) {
                                videoData = null;
                                selectedVideoPath = "";
                                edAdsText.setText("");
                            } else if (type == 2) {
                                selectedImage = null;
                                selectedImagePath = "";
                                edAdsText.setText("");
                            } else if (type == 3) {
                                selectedImage = null;
                                selectedImagePath = "";
                                videoData = null;
                                selectedVideoPath = "";
                            }
                            Ads ads = new Ads();
                            ads.setId(adv.getId());
                            ads.setMasjedID(sp.getInt("masjedId", -1));
                            ads.setTitle(edAdsTitle.getText().toString().trim());
                            ads.setType(type);
                            ads.setImage(selectedImagePath);
                            ads.setVideo(selectedVideoPath);
                            ads.setText(edAdsText.getText().toString().trim());
                            ads.setStartDate(ed_start.getText().toString().trim());
                            ads.setEndDate(ed_end.getText().toString().trim());
                            String msg = "تم إضافة الاعلان";
//                            if (advId == -1) {
                            DBO.updateAds(ads);
//                            }
                            if (advId != -1) {
                                if (count == 0) {
                                    msg = "تم إضافة الاعلان";
                                    count++;
                                } else
                                    msg = "تم إضافة الفترة إلى  الإعلان";

                                for (int i = 0; i < adsPeriodsList.size(); i++) {
                                    adsPeriodsList.get(i).setAdvId(advId);
                                    adsPeriodsList.get(i).setAdded(true);
                                }
                                DBO.insertAdsPeriod(adsPeriodsList);
                                adsPeriods.setDays(days);
                                DBO.open();
                                String AdsDay = DBO.getAdvPeriods(advId, adsPeriods.getStartTime(), adsPeriods.getEndTime());
                                String AdsIds = DBO.getAdvPeriodsIds(advId, adsPeriods.getStartTime(), adsPeriods.getEndTime());
                                DBO.close();

                                adsList.add(new AdsPeriods(adv.getId(), adsPeriods.getStartTime(), adsPeriods.getEndTime(),
                                        adsPeriods.getStartDate(), adsPeriods.getEndDate(), adsPeriods.getDays(), AdsIds, true));
                                btn.setVisibility(View.GONE);
                                btnUp.setVisibility(View.VISIBLE);
                                btnD.setVisibility(View.VISIBLE);
                                Utils.showCustomToast(activity, msg);
                            } else {
                                Utils.showCustomToast(activity, "لم يتم إضافة الإعلان ");
                            }


//                            if (checkList.size() >= repeatNo)
//                                tvSave.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adsPeriodsList.clear();
//                AdsPeriods adsPeriods = adsList.get(pos);
                final View v = llAdsPeriods.getChildAt(pos);
                EditText edStartTime = ((EditText) (v.findViewById(R.id.ed_start)));
                EditText edEndTime = ((EditText) (v.findViewById(R.id.ed_end)));
                cbSat = (CheckBox) v.findViewById(R.id.cbSat);
                cbSun = (CheckBox) v.findViewById(R.id.cbSun);
                cbMon = (CheckBox) v.findViewById(R.id.cbMon);
                cbTue = (CheckBox) v.findViewById(R.id.cbTue);
                cbWed = (CheckBox) v.findViewById(R.id.cbWed);
                cbThu = (CheckBox) v.findViewById(R.id.cbThu);
                cbFri = (CheckBox) v.findViewById(R.id.cbFri);
                Button btn = (Button) v.findViewById(R.id.btnCheck);
                Button btnUp = (Button) v.findViewById(R.id.btnUpdate);
                Button btnD = (Button) v.findViewById(R.id.btnDelete);
                isConflictAds = false;
                edAdsTitle.setError(null);
                edAdsText.setError(null);
                ed_start.setError(null);
                ed_end.setError(null);
                edStartTime.setError(null);
                edEndTime.setError(null);
                if (!ed_end.getText().toString().equals(adv.getEndDate()) || !ed_start.getText().toString().equals(adv.getStartDate())) {
                    if (check(pos)) {
                        Utils.showCustomToast(activity, "يوجد تعارض مع إعلانات أخرى ");
                        return;
                    }
                }
                getAdsList();
                if (TextUtils.isEmpty(edAdsTitle.getText().toString().toString())) {
                    if (TextUtils.isEmpty(adv.getTitle())) {
                        edAdsTitle.setError(getString(R.string.required));
                        return;
                    } else edAdsTitle.setText(adv.getTitle());
                }
                if (type == 1 && selectedImage == null) {
                    if (TextUtils.isEmpty(adv.getImage())) {
                        Utils.showCustomToast(activity, "يجب اختيار صورة للإعلان");
                        return;
                    }
                    selectedImagePath = adv.getImage();

                }
                if (type == 2 && videoData == null) {
                    if (TextUtils.isEmpty(adv.getVideo())) {
                        Utils.showCustomToast(activity, "يجب اختيار فيديو للإعلان");
                        return;
                    }
                    selectedVideoPath = adv.getVideo();
                }
                if (type == 3 && TextUtils.isEmpty(edAdsText.getText().toString().toString())) {
                    if (TextUtils.isEmpty(adv.getText())) {
                        Utils.showCustomToast(activity, "يجب إدخال نص للإعلان");
                        return;
                    }
                    edAdsText.setText(adv.getText());
                }
                if (TextUtils.isEmpty(ed_start.getText().toString())) {
                    if (TextUtils.isEmpty(adv.getStartDate())) {
                        ed_start.setError("أدخل تاريخ بداية ظهورالإعلان");
                        return;
                    }
                    ed_start.setText(adv.getStartDate());

                }
                if (TextUtils.isEmpty(ed_end.getText().toString())) {
                    if (TextUtils.isEmpty(adv.getStartDate())) {
                        ed_end.setError("أدخل تاريخ نهاية ظهورالإعلان");
                        return;
                    }
                    ed_end.setText(adv.getEndDate());

                }
                if (!Utils.compareDate(ed_start.getText().toString(), ed_end.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_date));
                    ed_end.setError(getString(R.string.error_date));
                    return;
                }
                if (TextUtils.isEmpty(edStartTime.getText().toString())) {
                    edStartTime.setError("أدخل وقت بداية ظهورالإعلان");
                }
                if (TextUtils.isEmpty(edEndTime.getText().toString())) {
                    edEndTime.setError("أدخل وقت نهاية ظهورالإعلان");
                    return;
                }
                if (!Utils.compareTimes(edStartTime.getText().toString(), edEndTime.getText().toString())) {
                    Utils.showCustomToast(activity, getString(R.string.error_time));
                    edEndTime.setError(getString(R.string.error_time));
                    return;
                }
                if (!cbSat.isChecked() && !cbSun.isChecked() && !cbMon.isChecked() && !cbTue.isChecked() && !cbWed.isChecked()
                        && !cbThu.isChecked() && !cbFri.isChecked()) {
                    Utils.showCustomToast(activity, "يجب إدخال أيام ظهور الإعلان");
                    return;
                }
                Log.i("/////: ", edStartTime.getText().toString().trim());
//                adsPeriods.setStartTime(edStartTime.getText().toString().trim());
//                adsPeriods.setEndTime(edEndTime.getText().toString().trim());
//                adsPeriods.setStartDate(ed_start.getText().toString().trim());
//                adsPeriods.setEndDate(ed_end.getText().toString().trim());
                List<Integer> dayList = new ArrayList<>();
                dayList.clear();
                if (cbSat.isChecked()) {
                    dayList.add(1);
                }
                if (cbSun.isChecked()) {
                    dayList.add(2);
                }
                if (cbMon.isChecked()) {
                    dayList.add(3);
                }
                if (cbTue.isChecked()) {
                    dayList.add(4);
                }
                if (cbWed.isChecked()) {
                    dayList.add(5);
                }
                if (cbThu.isChecked()) {
                    dayList.add(6);
                }
                if (cbFri.isChecked()) {
                    dayList.add(7);
                }
                String days = "";

                for (int x = 0; x < dayList.size(); x++) {
                    days = dayList.get(x) + "," + days;
                    AdsPeriods adsPeriods = new AdsPeriods();
                    adsPeriods.setStartTime(edStartTime.getText().toString().trim());
                    adsPeriods.setEndTime(edEndTime.getText().toString().trim());
                    adsPeriods.setStartDate(ed_start.getText().toString().trim());
                    adsPeriods.setEndDate(ed_end.getText().toString().trim());
                    adsPeriods.setAdvId(adv.getId());
                    adsPeriods.setDay(dayList.get(x));
//                    adsPeriodsList.add(adsPeriods);
                    boolean hasConflict = false;
//                    List<String> arrayList = new ArrayList<String>
//                            (Arrays.asList(adsList.get(pos).getPeriodsId().split(",")));
//                    List<Integer> idsList = new ArrayList<Integer>();
//                    for(String ids:arrayList){
//                        idsList.add(Integer.parseInt(ids.trim()));
//
////                        if (hasConflict)break;
//                    }
                    DBO.open();
                    hasConflict = DBO.itHasConflict(sp.getInt("masjedId", -1), adsPeriods
                            , adsList.get(pos));
                    DBO.close();
                    if (hasConflict) {
                        isConflictAds = true;
                        Utils.showCustomToast(activity, "يوجد تعارض في الوقت مع إعلان آخر");
                        break;
                    }
                    if (inPrayPeriod(prayerTimes, adsPeriods)) {
                        Utils.showCustomToast(activity, "يوجد تعارض مع مواعيد الصلاة");
                        break;
                    }
                    adsPeriodsList.add(adsPeriods);
                    if (x == dayList.size() - 1) {
                        if (!isConflictAds) {
                            int repeatNo = Integer.parseInt(edAddAppearance.getText().toString().trim());
                            if (type == 1) {
                                videoData = null;
                                selectedVideoPath = "";
                                edAdsText.setText("");
                            } else if (type == 2) {
                                selectedImage = null;
                                selectedImagePath = "";
                                edAdsText.setText("");
                            } else if (type == 3) {
                                selectedImage = null;
                                selectedImagePath = "";
                                videoData = null;
                                selectedVideoPath = "";
                            }
                            Ads ads = new Ads();
                            ads.setId(adv.getId());
                            ads.setMasjedID(sp.getInt("masjedId", -1));
                            ads.setTitle(edAdsTitle.getText().toString().trim());
                            ads.setType(type);
                            ads.setImage(selectedImagePath);
                            ads.setVideo(selectedVideoPath);
                            ads.setText(edAdsText.getText().toString().trim());
                            ads.setStartDate(ed_start.getText().toString().trim());
                            ads.setEndDate(ed_end.getText().toString().trim());
                            String msg = "تم تعديل فترة الاعلان";
//                            if (advId == -1) {
                            DBO.updateAds(ads);
//                            }
//                            if (advId != -1) {
                            if (count == 0) {
                                msg = "تم تعديل فترة الاعلان";
                                count++;
                            } else
                                msg = "تم تعديل فترة الاعلان";

                            for (int i = 0; i < adsPeriodsList.size(); i++) {
                                adsPeriodsList.get(i).setAdvId(advId);
                                adsPeriodsList.get(i).setAdded(true);
                            }
                            DBO.open();
                            DBO.delAdvPeriod(adv.getId(), adsList.get(pos));
                            DBO.close();

                            DBO.insertAdsPeriod(adsPeriodsList);
                            adsPeriods.setDays(days);
                            getAdsList();
//                            DBO.open();
//                            String AdsDay = DBO.getAdvPeriods(advId, adsPeriods.getStartTime(), adsPeriods.getEndTime());
//                            String AdsIds = DBO.getAdvPeriodsIds(advId, adsPeriods.getStartTime(), adsPeriods.getEndTime());
//                            DBO.close();
//
//                            adsList.add(new AdsPeriods(adv.getId(), adsPeriods.getStartTime(), adsPeriods.getEndTime(),
//                                    adsPeriods.getStartDate(), adsPeriods.getEndDate(), adsPeriods.getDays(), AdsIds, true));
                            btn.setVisibility(View.GONE);
                            btnUp.setVisibility(View.VISIBLE);
                            btnD.setVisibility(View.VISIBLE);
                            Utils.showCustomToast(activity, msg);
//                            } else {
//                                Utils.showCustomToast(activity, "لم يتم تعديل فترة الإعلان ");
//                            }


//                            if (checkList.size() >= repeatNo)
//                                tvSave.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        return ll;
    }

    private boolean check(int pos) {
        getAdsList();
        if (adsList.size() >= 0) adsList.remove(pos);
//                DBO.open();
//                ArrayList<AdsPeriods> list = DBO.getAdvPeriods(adv.getId());
//                DBO.close();
        Log.i("//list size: ", "" + adsList.size());
        for (int i = 0; i < adsList.size(); i++) {
            AdsPeriods adsPeriods = adsList.get(i);
//                    adsPeriodsList.clear();
            isConflictAds = false;
            String edStartTime = adsPeriods.getStartTime();
            String edEndTime = adsPeriods.getEndTime();
            int day = adsPeriods.getDay();
            String days = adsPeriods.getDays();
            AdsPeriods advPeriod = new AdsPeriods();
            Log.i("/////: ", edStartTime + " : " + edEndTime);
            advPeriod.setAdvId(adv.getId());
            advPeriod.setStartTime(edStartTime);
            advPeriod.setEndTime(edEndTime);
            advPeriod.setStartDate(ed_start.getText().toString().trim());
            advPeriod.setEndDate(ed_end.getText().toString().trim());
            advPeriod.setDays(days);
            List<Integer> dayList = new ArrayList<>();
            dayList.clear();
            if (adsPeriods.getDays().contains("1")) {
                dayList.add(1);
            }
            if (adsPeriods.getDays().contains("2")) {
                dayList.add(2);
            }
            if (adsPeriods.getDays().contains("3")) {
                dayList.add(3);
            }
            if (adsPeriods.getDays().contains("4")) {
                dayList.add(4);
            }
            if (adsPeriods.getDays().contains("5")) {
                dayList.add(5);
            }
            if (adsPeriods.getDays().contains("6")) {
                dayList.add(6);
            }
            if (adsPeriods.getDays().contains("7")) {
                dayList.add(7);
            }
//                    String days = "";

            for (int x = 0; x < dayList.size(); x++) {
//                        days = dayList.get(x) + "," + days;
                advPeriod.setDay(dayList.get(x));
//                        adsPeriodsList.add(advPeriod);
                DBO.open();
                boolean hasConflict = DBO.itHasConflict(sp.getInt("masjedId", -1), advPeriod, adsList.get(i));
                DBO.close();
                conflictList.add(hasConflict);
                if (hasConflict) {
                    isConflictAds = true;
                    Utils.showCustomToast(activity, "يوجد تعارض في الوقت مع إعلان آخر");
                    break;
                }
                if (inPrayPeriod(prayerTimes, adsPeriods)) {
                    isConflictAds = true;
                    Utils.showCustomToast(activity, "يوجد تعارض مع مواعيد الصلاة");
                    break;
                }
                DBO.open();
                int advPeriodId = DBO.getAdvPeriodsId(adv.getId(), adsList.get(i));
                DBO.close();
                advPeriod.setId(advPeriodId);
                adsPeriodsList.add(advPeriod);
            }
            if (i == adsList.size() - 1) {
                if (conflictList.contains(true)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void deletePeriods(int pos, AdsPeriods advPeriods) {
        DBO.open();
        DBO.delAdvPeriods(advPeriods);
        DBO.close();
        Utils.showCustomToast(activity, getString(R.string.success_delete));
        adsList.remove(pos);
        int size = adsList.size();
        checkList.clear();
        for (int i = 0; i < adsList.size(); i++) {
            checkList.add(i);
        }
        if (size < checkList.size()) {
            size = checkList.size();
            edAddAppearance.setText(size + "");
        }
        size = Integer.parseInt(edAddAppearance.getText().toString().trim());
        if (size > 0) {
            llAdsPeriods.removeAllViews();
            for (int i = 0; i < size; i++) {
                AdsPeriods adsPeriods = null;
                Log.i("///*: size ", adsList.size() + "");
                if (i < adsList.size()) {
                    adsPeriods = adsList.get(i);
                    llAdsPeriods.addView(getItem(i, adsPeriods));
                } else
                    llAdsPeriods.addView(getItem(i, null));
            }
        }
    }

    private void deleteAds() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity).setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.tv_delMsg))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        DBO.open();
                        DBO.delAdvertisement(advId, sp.getInt("masjedId", -1));
                        DBO.close();
                        Utils.showCustomToast(activity, getString(R.string.success_delete));
                        finish();
                    }
                }).setNegativeButton(getString(R.string.cancel_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }


    public static boolean inPrayPeriod(ArrayList<String> list, AdsPeriods adsPeriods) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        for (String object : list) {
            try {
                Date prayTime = df.parse(object);
                Date startTime = df.parse(adsPeriods.getStartTime());
                Date endTime = df.parse(adsPeriods.getEndTime());
                if ((prayTime.after(startTime) || prayTime.equals(startTime))
                        && (prayTime.before(endTime) || prayTime.equals(endTime))) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    public static boolean isContain(ArrayList<AdsPeriods> list, AdsPeriods adsPeriods) {
        for (AdsPeriods object : list) {
            String startTime = adsPeriods.getStartTime();
            String endTime = adsPeriods.getEndTime();
            String days = adsPeriods.getDays();
            if (object.getStartTime().equals(startTime) && object.getEndTime().equals(endTime) && object.getDays().equals(days)) {
                return true;
            }

        }
        return false;
    }

    private void showDatePicker(final EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        DatePickerDialog mDatePicker;
        Calendar cal = Calendar.getInstance();
        int calYear = cal.get(Calendar.YEAR);
        int calMonth = cal.get(Calendar.MONTH);
        int calDay = cal.get(Calendar.DAY_OF_MONTH);
        mDatePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int MONTH = (month + 1);
                int DAY = day;
                String monthS = MONTH + "";
                String dayS = DAY + "";
                if (MONTH < 10)
                    monthS = "0" + MONTH;
                if (DAY < 10)
                    dayS = "0" + DAY;
                editText.setText(year + "-" + monthS + "-" + dayS);//ed_endTime

            }
        }, calYear, calMonth, calDay);
        mDatePicker.show();
    }

    public void showDateTimePicker(final EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hours = "" + selectedHour;
                if (selectedHour < 10)
                    hours = "0" + selectedHour;

                String minute = "" + selectedMinute;
                if (selectedMinute < 10)
                    minute = "0" + selectedMinute;
                editText.setText(hours + ":" + minute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("اختر وقت");
        mTimePicker.show();
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
    public void onClick(View view) {
        if (view == iv_back) {
            finish();
        } else if (view == tvSave) {
            adsPeriodsList.clear();
            edAdsTitle.setError(null);
            edAdsText.setError(null);
            ed_start.setError(null);
            ed_end.setError(null);
            if (TextUtils.isEmpty(edAdsTitle.getText().toString().toString())) {
                if (TextUtils.isEmpty(adv.getTitle())) {
                    edAdsTitle.setError(getString(R.string.required));
                    return;
                } else edAdsTitle.setText(adv.getTitle());
            }
            if (type == 1 && selectedImage == null) {
                if (TextUtils.isEmpty(adv.getImage())) {
                    Utils.showCustomToast(activity, "يجب اختيار صورة للإعلان");
                    return;
                }
                selectedImagePath = adv.getImage();

            }
            if (type == 2 && videoData == null) {
                if (TextUtils.isEmpty(adv.getVideo())) {
                    Utils.showCustomToast(activity, "يجب اختيار فيديو للإعلان");
                    return;
                }
                selectedVideoPath = adv.getVideo();
            }
            if (type == 3 && TextUtils.isEmpty(edAdsText.getText().toString().toString())) {
                if (TextUtils.isEmpty(adv.getText())) {
                    Utils.showCustomToast(activity, "يجب إدخال نص للإعلان");
                    return;
                }
                edAdsText.setText(adv.getText());
            }
            if (TextUtils.isEmpty(ed_start.getText().toString())) {
                if (TextUtils.isEmpty(adv.getStartDate())) {
                    ed_start.setError("أدخل تاريخ بداية ظهورالإعلان");
                    return;
                }
                ed_start.setText(adv.getStartDate());

            }
            if (TextUtils.isEmpty(ed_end.getText().toString())) {
                if (TextUtils.isEmpty(adv.getStartDate())) {
                    ed_end.setError("أدخل تاريخ نهاية ظهورالإعلان");
                    return;
                }
                ed_end.setText(adv.getEndDate());

            }
            if (!Utils.compareDate(ed_start.getText().toString(), ed_end.getText().toString())) {
                Utils.showCustomToast(activity, getString(R.string.error_date));
                ed_end.setError(getString(R.string.error_date));
                return;
            }
            if (adv.getStartDate().equals(ed_start.getText().toString().trim())
                    && adv.getEndDate().endsWith(ed_end.getText().toString().trim())) {
                if (type == 1) {
                    videoData = null;
                    selectedVideoPath = "";
                    edAdsText.setText("");
                } else if (type == 2) {
                    selectedImage = null;
                    selectedImagePath = "";
                    edAdsText.setText("");
                } else if (type == 3) {
                    selectedImage = null;
                    selectedImagePath = "";
                    videoData = null;
                    selectedVideoPath = "";
                }
                Log.i("///: ", type + "");
                Ads ads = new Ads();
                ads.setId(adv.getId());
                ads.setMasjedID(sp.getInt("masjedId", -1));
                ads.setTitle(edAdsTitle.getText().toString().trim());
                ads.setType(type);
                ads.setImage(selectedImagePath);
                ads.setVideo(selectedVideoPath);
                ads.setText(edAdsText.getText().toString().trim());
                ads.setStartDate(ed_start.getText().toString().trim());
                ads.setEndDate(ed_end.getText().toString().trim());
                boolean c=DBO.updateAds(ads);
                Log.i("////: ",c+"");
                String msg = "تم تعديل الاعلان";
                Utils.showCustomToast(activity, msg);
            } else {
                getAdsList();
//                DBO.open();
//                ArrayList<AdsPeriods> list = DBO.getAdvPeriods(adv.getId());
//                DBO.close();
                Log.i("//list size: ", "" + adsList.size());
                for (int i = 0; i < adsList.size(); i++) {
                    AdsPeriods adsPeriods = adsList.get(i);
//                    adsPeriodsList.clear();
                    isConflictAds = false;
                    String edStartTime = adsPeriods.getStartTime();
                    String edEndTime = adsPeriods.getEndTime();
                    int day = adsPeriods.getDay();
                    String days = adsPeriods.getDays();
                    AdsPeriods advPeriod = new AdsPeriods();
                    Log.i("/////: ", edStartTime + " : " + edEndTime);
                    advPeriod.setAdvId(adv.getId());
                    advPeriod.setStartTime(edStartTime);
                    advPeriod.setEndTime(edEndTime);
                    advPeriod.setStartDate(ed_start.getText().toString().trim());
                    advPeriod.setEndDate(ed_end.getText().toString().trim());
                    advPeriod.setDays(days);
                    List<Integer> dayList = new ArrayList<>();
                    dayList.clear();
                    if (adsPeriods.getDays().contains("1")) {
                        dayList.add(1);
                    }
                    if (adsPeriods.getDays().contains("2")) {
                        dayList.add(2);
                    }
                    if (adsPeriods.getDays().contains("3")) {
                        dayList.add(3);
                    }
                    if (adsPeriods.getDays().contains("4")) {
                        dayList.add(4);
                    }
                    if (adsPeriods.getDays().contains("5")) {
                        dayList.add(5);
                    }
                    if (adsPeriods.getDays().contains("6")) {
                        dayList.add(6);
                    }
                    if (adsPeriods.getDays().contains("7")) {
                        dayList.add(7);
                    }
//                    String days = "";

                    for (int x = 0; x < dayList.size(); x++) {
//                        days = dayList.get(x) + "," + days;
                        advPeriod.setDay(dayList.get(x));
//                        adsPeriodsList.add(advPeriod);
                        DBO.open();
                        boolean hasConflict = DBO.itHasConflict(sp.getInt("masjedId", -1), advPeriod, adsList.get(i));
                        DBO.close();
                        if (hasConflict) {
                            isConflictAds = true;
                            Utils.showCustomToast(activity, "يوجد تعارض في الوقت مع إعلان آخر");
                            conflictList.add(hasConflict);
                            break;
                        }
                        if (inPrayPeriod(prayerTimes, adsPeriods)) {
                            isConflictAds = true;
                            Utils.showCustomToast(activity, "يوجد تعارض مع مواعيد الصلاة");
                            conflictList.add(true);
                            break;
                        }
                        conflictList.add(hasConflict);
                        DBO.open();
                        int advPeriodId = DBO.getAdvPeriodsId(adv.getId(), adsList.get(i));
                        DBO.close();
                        advPeriod.setId(advPeriodId);
                        adsPeriodsList.add(advPeriod);
                    }
                    if (i == adsList.size() - 1) {
                        if (!conflictList.contains(true)) {
                            int repeatNo = Integer.parseInt(edAddAppearance.getText().toString().trim());
                            if (type == 1) {
                                videoData = null;
                                selectedVideoPath = "";
                                edAdsText.setText("");
                            } else if (type == 2) {
                                selectedImage = null;
                                selectedImagePath = "";
                                edAdsText.setText("");
                            } else if (type == 3) {
                                selectedImage = null;
                                selectedImagePath = "";
                                videoData = null;
                                selectedVideoPath = "";
                            }
                            Ads ads = new Ads();
                            ads.setId(adv.getId());
                            ads.setMasjedID(sp.getInt("masjedId", -1));
                            ads.setTitle(edAdsTitle.getText().toString().trim());
                            ads.setType(type);
                            ads.setImage(selectedImagePath);
                            ads.setVideo(selectedVideoPath);
                            ads.setText(edAdsText.getText().toString().trim());
                            ads.setStartDate(ed_start.getText().toString().trim());
                            ads.setEndDate(ed_end.getText().toString().trim());
                            DBO.updateAds(ads);

//                           for (int xx=0;xx<adsList.size();xx++)
                            DBO.open();
                            DBO.delAdsPeriods(adv.getId());
                            DBO.close();

                            DBO.insertAdsPeriod(adsPeriodsList);
                            adsPeriods.setDays(days);
                            getAdsList();
//                                if (advId != -1) {
                            String msg = "تم تعديل الاعلان";
                            Utils.showCustomToast(activity, msg);

                        } else {
                            String message = "يوجد تعارض في الوقت مع إعلان آخر";
                            for (int y = 0; y < adsPeriodsList.size(); y++) {
                                if (inPrayPeriod(prayerTimes, adsPeriodsList.get(y))) {
                                    message = "يوجد تعارض مع مواعيد الصلاة";
                                }
                                if (y == adsPeriodsList.size() - 1)
                                    Utils.showCustomToast(activity, " * " + message);
                            }
                        }
                    }
                }
            }
        } else if (view == ivSelectImg) {
            selectImage();
        } else if (view == ivSelectVideo) {
            selectVideo();
        } else if (view == ivAdsVideoThumb) {
            if (videoData != null) {
                Intent intent = new Intent(activity, VideoViewActivity.class);
                intent.setAction("uri");
                intent.putExtra("videoURI", videoData);
                startActivity(intent);
            }
        }
    }

    private void getAdsList() {
        DBO.open();
        ArrayList<AdsPeriods> list = DBO.getAdvPeriods(adv.getId());
        DBO.close();
        int size = list.size();
        String day = "";
        String ids = "";
        adsList.clear();
        for (int i = 0; i < size; i++) {
            DBO.open();
            AdsPeriods periods = list.get(i);
            day = DBO.getAdvPeriods(adv.getId(), periods.getStartTime(), periods.getEndTime());
            ids = DBO.getAdvPeriodsIds(adv.getId(), periods.getStartTime(), periods.getEndTime());
            DBO.close();
            AdsPeriods newAdsPeriod = new AdsPeriods(periods.getAdvId(), periods.getStartTime(), periods.getEndTime(),
                    periods.getStartDate(), periods.getEndDate(), day, ids, true);
            Log.i("//list day int: ", day + " : ids: " + ids);
            if (!isContain(adsList, newAdsPeriod))
                adsList.add(newAdsPeriod);
        }
    }


    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            }
        }
    }


    private void selectImage() {
        try {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(i, "اختر صورة"), RESULT_LOAD_IMAGE);
        } catch (Exception r) {
            r.printStackTrace();
            Utils.showCustomToast(activity, " لا يوجد مجلد صور على الجهاز");
        }
    }

    private void selectVideo() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/*");
            startActivityForResult(intent, VIDEO_SELECT);
        } catch (Exception r) {
            r.printStackTrace();
            Utils.showCustomToast(activity, " لا يوجد مجلد فيديو على الجهاز");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                selectedImage = data.getData();
                Glide.with(activity).load(selectedImage + "").into(ivAdsImg);
                selectedImagePath = getRealPathFromURI(selectedImage);
                Log.i("////// path", selectedImagePath + " **//");
                image_str = makePictureToBase64(checkImg(selectedImagePath), ivAdsImg);
            } catch (Exception e) {
                Utils.showCustomToast(activity, "حدث خطأ اختر صورة أخرى");
                e.printStackTrace();
            }
        } else if (requestCode == VIDEO_SELECT && resultCode == RESULT_OK && null != data) {
            videoData = data.getData();
            Log.i("/// selectVideo", videoData + "");
            selectedVideoPath = getRealPath(videoData);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail
                    (getRealPath(data.getData()), MediaStore.Video.Thumbnails.MINI_KIND);
            System.out.println(">>>> data " + getRealPath(videoData));
            System.out.println(">>>> bitmap " + bitmap);
            if (bitmap == null)
                return;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            image_str = Base64.encodeToString(byte_arr, 0);
            ivAdsVideoThumb.setImageBitmap(bitmap);
            ivThumb.setVisibility(View.VISIBLE);
        }
    }

    public String getRealPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    private String checkImg(String path) {
        String newPath = path;
        if (!TextUtils.isEmpty(path)) {
            File f = new File(Uri.parse(path).getPath());
            if (f.exists()) {
                Bitmap resized = getResizedBitmap(Uri.parse(path).getPath(), 640, 640);
                if (resized != null) {
                    String npath = saveToFile(resized);
                    if (!TextUtils.isEmpty(npath)) {
                        newPath = npath;
                    }
                }
            }

        }

        return newPath;
    }

    public Bitmap getResizedBitmap(String path, float widthRatio, float heightRatio) {
        float scale = 1;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Uri.parse(path).getPath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageHeight > imageWidth) {
            if (imageHeight > heightRatio) {
                scale = ((float) heightRatio) / imageHeight;
            }

        } else {
            if (imageWidth > widthRatio) {
                scale = ((float) widthRatio) / imageWidth;
            }
        }
        if (scale == 0) return null;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap bm = BitmapFactory.decodeFile(Uri.parse(path).getPath());
        if (bm != null) {
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
            bm.recycle();
            return resizedBitmap;
        }

        return null;
    }

    private String saveToFile(Bitmap bm) {
        File sd = getTempStoreDirectory(activity);
        String path = null;
        FileOutputStream fOut = null;
        try {
            if (sd.canWrite()) {
                File temp = new File(sd, "temp" + System.currentTimeMillis() + ".jpg");
                fOut = new FileOutputStream(temp);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                path = temp.getPath();

                bm.recycle();
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static File getTempStoreDirectory(Context context) {
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        return context.getExternalFilesDir("temp").getAbsoluteFile();
    }

    public String makePictureToBase64(String image_path, ImageView image) {
        Bitmap bitmap = ShrinkBitmap(image_path, 300, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String type = "";
        if (image_path.endsWith("jpg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            type = "data:image/jpeg;base64,";
        } else if (image_path.endsWith("png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            type = "data:image/png;base64,";
        } else
            Toast.makeText(activity, "make_sure_extension", Toast.LENGTH_LONG).show();

        byte[] byteArrayImage = baos.toByteArray();
        return type + Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    public static Bitmap ShrinkBitmap(String file, int width, int height) {

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        Utils.hideSoftKeyboard(activity);
        ivThumb.setVisibility(View.GONE);
        if (checkedId == R.id.rbImage) {
            rlImage.setVisibility(View.VISIBLE);
            rlVideo.setVisibility(View.GONE);
            rlText.setVisibility(View.GONE);
//            ivAdsVideoThumb.setImageResource(0);
//            videoData = null;
//            selectedVideoPath = "";
//            edAdsText.setText("");
            type = 1;
        } else if (checkedId == R.id.rbVideo) {
            rlImage.setVisibility(View.GONE);
            rlVideo.setVisibility(View.VISIBLE);
            rlText.setVisibility(View.GONE);
//            selectedImage = null;
//            selectedImagePath = "";
//            edAdsText.setText("");
//            ivAdsImg.setImageResource(0);
            type = 2;
        } else if (checkedId == R.id.rbText) {
            rlImage.setVisibility(View.GONE);
            rlVideo.setVisibility(View.GONE);
            rlText.setVisibility(View.VISIBLE);
//            selectedImage = null;
//            selectedImagePath = "";
//            videoData = null;
//            selectedVideoPath = "";
//            ivAdsImg.setImageResource(0);
//            ivAdsVideoThumb.setImageResource(0);
            type = 3;
        }
    }
}
