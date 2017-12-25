package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.Dialog;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.DateTimePicker.CustomDateTimePicker;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.newsolution.almhrab.AppConstants.Constants.RESULT_LOAD_IMAGE;

public class AddAdsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Activity activity;
    private ImageView ivAdsVideoThumb, ivSelectVideo, ivAdsImg, ivThumb, ivSelectImg, iv_back;
    private RadioGroup rgAdsType;
    private RadioButton rbText, rbVideo, rbImage;
    private RelativeLayout rlImage, rlVideo, rlText;
    private EditText edAdsTitle, edAdsText, ed_start, ed_end;
    private TextView tvSave;
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
        setContentView(R.layout.activity_add_ads);
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        askForPermissions(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);
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
        ed_start = (EditText) findViewById(R.id.ed_start);
        ed_end = (EditText) findViewById(R.id.ed_end);
        tvSave = (TextView) findViewById(R.id.tvSave);
        cbSat = (CheckBox) findViewById(R.id.cbSat);
        cbSun = (CheckBox) findViewById(R.id.cbSun);
        cbMon = (CheckBox) findViewById(R.id.cbMon);
        cbTue = (CheckBox) findViewById(R.id.cbTue);
        cbWed = (CheckBox) findViewById(R.id.cbWed);
        cbThu = (CheckBox) findViewById(R.id.cbThu);
        cbFri = (CheckBox) findViewById(R.id.cbFri);

        iv_back.setOnClickListener(this);
        ivSelectImg.setOnClickListener(this);
        ivSelectVideo.setOnClickListener(this);
        ivAdsVideoThumb.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        rgAdsType.setOnCheckedChangeListener(this);
        ed_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker(ed_start);
//                showDatePicker(ed_start);
            }
        });
        ed_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(activity);
                showDateTimePicker(ed_end);
//                showDatePicker(ed_end);
            }
        });
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
            edAdsTitle.setError(null);
            edAdsText.setError(null);
            ed_start.setError(null);
            ed_end.setError(null);
            if (TextUtils.isEmpty(edAdsTitle.getText().toString().toString())) {
                edAdsTitle.setError(getString(R.string.required));
                return;
            }
            if (type == 1 && selectedImage == null) {
                Utils.showCustomToast(activity, "يجب اختيار صورة للإعلان");
                return;
            }
            if (type == 2 && videoData == null) {
                Utils.showCustomToast(activity, "يجب اختيار فيديو للإعلان");
                return;
            }
            if (type == 3 && TextUtils.isEmpty(edAdsText.getText().toString().toString())) {
                edAdsText.setError("يجب إدخال نص للإعلان");
                Utils.showCustomToast(activity, "يجب إدخال نص للإعلان");
                return;
            }
            if (TextUtils.isEmpty(ed_start.getText().toString())) {
                ed_start.setError("أدخل وقت بداية ظهورالإعلان");
                return;
            }
            if (TextUtils.isEmpty(ed_end.getText().toString())) {
                ed_end.setError("أدخل وقت نهاية ظهورالإعلان");
                return;
            }
            if (!Utils.compareTimes(ed_start.getText().toString(), ed_end.getText().toString())) {
                Utils.showCustomToast(activity, getString(R.string.error_date));
                ed_end.setError(getString(R.string.error_date));
                return;
            }
            if (!cbSat.isChecked() && !cbSun.isChecked() && !cbMon.isChecked() && !cbTue.isChecked() && !cbWed.isChecked()
                    && !cbThu.isChecked() && !cbFri.isChecked()) {
                Utils.showCustomToast(activity, "يجب إدخال أيام ظهور الإعلان");
                return;
            }
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
            ads.setMasjedID(sp.getInt("masjedId", -1));
            ads.setTitle(edAdsTitle.getText().toString().trim());
            ads.setType(type);
            ads.setImage(selectedImagePath);
            ads.setVideo(selectedVideoPath);
            ads.setText(edAdsText.getText().toString().trim());
            ads.setStartTime(ed_start.getText().toString().trim());
            ads.setEndTime(ed_end.getText().toString().trim());
            ads.setSaturday(cbSat.isChecked());
            ads.setSunday(cbSun.isChecked());
            ads.setMonday(cbMon.isChecked());
            ads.setTuesday(cbTue.isChecked());
            ads.setWednesday(cbWed.isChecked());
            ads.setThursday(cbThu.isChecked());
            ads.setFriday(cbFri.isChecked());
            DBO = new DBOperations(this);
            DBO.createDatabase();
            DBO.open();
            ArrayList<Ads> adsList = DBO.getAdsListByDay(sp.getInt("masjedId", -1), ads);
            Log.i("+++ads", adsList.size() + "  ");
            DBO.close();
            if (adsList.size() > 0) {
                for (int i = 0; i < adsList.size(); i++) {
                    Ads adv = adsList.get(i);
                    String oldAdvStart = adv.getStartTime();
                    String oldAdvEnd = adv.getEndTime();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm", new Locale("en"));
                    try {
                        Date oldAdvStartDate = df.parse(oldAdvStart);//From1
                        Date oldAdvEndDate = df.parse(oldAdvEnd);//To1
                        Date newAdvStartDate = df.parse(ads.getStartTime());//From2
                        Date newAdvEndDate = df.parse(ads.getEndTime());//To2
                        if (((oldAdvStartDate.after(newAdvStartDate) || (oldAdvStartDate.equals(newAdvStartDate))
                                && oldAdvStartDate.before(newAdvEndDate)) ||
                                (oldAdvEndDate.after(newAdvStartDate) && (oldAdvEndDate.before(newAdvEndDate)) || oldAdvEndDate.equals(newAdvEndDate))
                                || (newAdvStartDate.after(oldAdvStartDate) && newAdvStartDate.before(oldAdvEndDate))
                                || (newAdvEndDate.after(oldAdvStartDate) && newAdvEndDate.before(oldAdvEndDate)))) {

                            isConflict = true;
                        }
                        if (i == adsList.size() - 1) {
                            if (isConflict) {
                                Utils.showCustomToast(activity, "يوجد تعارض في الوقت مع إعلان آخر");
                            } else {
                                DBO.insertAds(ads);
                                Utils.showCustomToast(activity, "تم إضافة الإعلان");
                            }
                        }
//                        }

                    } catch (ParseException e) {
                        Utils.showCustomToast(activity, "حدث خطأ");
                    }
                }
            } else {
                DBO.insertAds(ads);
                Utils.showCustomToast(activity, "تم إضافة الإعلان");
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
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
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
