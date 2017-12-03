package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnFetched;
import com.newsolution.almhrab.Model.Users;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.newsolution.almhrab.AppConstants.Constants.RESULT_LOAD_IMAGE;

public class AccountSetting extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences.Editor spedit;
    private SharedPreferences sp;
    private ImageView ivMasjedLogo;
    private ProgressBar progress;
    private Activity activity;
    private LinearLayout ll_accountImage;
    private LinearLayout ll_changePassword;
    private RelativeLayout ll_password;
    private String image_str = "";
    private Uri selectedImage = null;
    private String oldPW;
    private EditText tvMasjedName;
    private ImageView iv_back, showHide;
    private EditText tvOldPW, tvNewPW, tvConfirmPW;
    private Button btnSaveEdit;
    private int REQUEST_PERMISSIONS = 100;
    private byte[] byte_arr=null;
    private Bitmap yourbitmap=null;

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
        setContentView(R.layout.activity_account_setting);
        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        oldPW = sp.getString("masjedPW", "");
        btnSaveEdit = (Button) findViewById(R.id.btnSaveEdit);
        tvMasjedName = (EditText) findViewById(R.id.tvHintAccountName);
        tvOldPW = (EditText) findViewById(R.id.tvOldPW);
        tvNewPW = (EditText) findViewById(R.id.tvNewPW);
        tvConfirmPW = (EditText) findViewById(R.id.tvConfirmPW);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        showHide = (ImageView) findViewById(R.id.showHide);
        progress = (ProgressBar) findViewById(R.id.progress);
        ivMasjedLogo = (ImageView) findViewById(R.id.ivMasjedLogo);
        if (!TextUtils.isEmpty(sp.getString("masjedImg", "")))
            Glide.with(activity).load(Uri.parse(sp.getString("masjedImg", "")))
                    .override(100, 100).listener(new RequestListener<Uri, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                    Log.i("exce: ", e.getMessage());
                  progress.setVisibility(View.GONE);
                    ivMasjedLogo.setImageResource(R.drawable.ic_mosque);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progress.setVisibility(View.GONE);
                    return false;
                }
            }).into(ivMasjedLogo);
        tvMasjedName.setText(sp.getString("masjedName", ""));

        ll_password = (RelativeLayout) findViewById(R.id.ll_password);
        ll_changePassword = (LinearLayout) findViewById(R.id.ll_changePassword);
//        ll_changePassword.setVisibility(View.VISIBLE);
//        showHide.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        ll_accountImage = (LinearLayout) findViewById(R.id.ll_accountImage);
        ll_accountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermissions(new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        });
        ll_password.setOnClickListener(this);
        showHide.setOnClickListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOldPW.setError(null);
                tvNewPW.setError(null);
                tvConfirmPW.setError(null);
                String name = tvMasjedName.getText().toString().trim();
                String oldPassWord = tvOldPW.getText().toString().trim();
                String newPassWord = tvNewPW.getText().toString().trim();
                String confirmNewPW = tvConfirmPW.getText().toString().trim();
                if (!TextUtils.isEmpty(oldPassWord) && !oldPassWord.equals(oldPW)) {
                    tvOldPW.setError("كلمة المرور غير صحيحة");
                    return;
                }
                if (TextUtils.isEmpty(oldPassWord) && !TextUtils.isEmpty(newPassWord)) {
                    tvOldPW.setError("أدخل كلمة المرور الحالية");
                    return;
                }
//                if (!TextUtils.isEmpty(oldPassWord) && !TextUtils.isEmpty(newPassWord) && !newPassWord.equals(oldPassWord)) {
//                    tvNewPW.setError("كلمتا المرور غير متطابقتين");
//                    return;
//                }
                if (!TextUtils.isEmpty(newPassWord) && !newPassWord.equals(confirmNewPW)) {
                    tvConfirmPW.setError("كلمتا المرور غير متطابقتين");
                    return;
                }

                if (sp.getInt("priority", 0) == 1) {
                    if (Utils.isOnline(activity)) {
                        updateAccount();
                    } else {
                        Utils.showCustomToast(activity, getString(R.string.no_internet));
                    }
                } else {
                    if (!TextUtils.isEmpty(oldPassWord) && !TextUtils.isEmpty(newPassWord)) {
                        spedit.putString("masjedPW", newPassWord).commit();
                    }
                    if (!TextUtils.isEmpty(name)) {
                        spedit.putString("masjedName", name).commit();
                    }
                    if (!TextUtils.isEmpty(image_str)) {
                        spedit.putString("masjedImg", selectedImage + "").commit();
                    }
                    Utils.showCustomToast(activity, getString(R.string.success_edit));

                }
            }
        });
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
        } else selectImage();
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                new AlertDialog.Builder(
                        activity)
                        .setTitle(getString(R.string.alert))
                        .setMessage(getString(R.string.permission_rationale))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                askForPermissions(new String[]{
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel_delete), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.showCustomToast(activity, "يجب  منح التطبيق الإذن للسماح بالدخول الى الصور على جهازك");
                                dialog.dismiss();
                            }
                        }).create().show();
            } else selectImage();
        }
    }


    private void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, RESULT_LOAD_IMAGE);
       try {
           Intent i = new Intent();
           i.setType("image/*");
           i.setAction(Intent.ACTION_PICK);
           startActivityForResult(Intent.createChooser(i, "اختر صورة"), RESULT_LOAD_IMAGE);
       }catch (Exception r){
           r.printStackTrace();
           Utils.showCustomToast(activity," لا يوجد مجلد صور على الجهاز");
       }
    }

    public void updateAccount() {
        final ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage(getString(R.string.wait));
        pd.show();
        pd.setCanceledOnTouchOutside(false);
//        SubscribeEditProfile(int IdSubscribe, string GUID,
//                string DeviceNo, string OldPass = "", string NewPass = "",
//                string MyName = "", int IdCity = 0, String ImgSubscribe = null)
        final String newPW = tvNewPW.getText().toString().trim();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(AppConst.DeviceNo, "");
        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("MyName", tvMasjedName.getText().toString().trim());
        param.put("OldPass", sp.getString("masjedPW", ""));
        param.put("NewPass", newPW);
        param.put("ProfileImg", image_str);
        param.put("IdCity", sp.getInt("cityId", 1) + "");
        WS.updateAccount(this, param, new OnFetched() {
            @Override
            public void onSuccess(Users success) {
                if (!TextUtils.isEmpty(newPW)) {
                    spedit.putString("masjedPW", newPW).commit();
                }
                if (pd.isShowing())
                    pd.dismiss();
                Utils.showCustomToast(activity, getString(R.string.success_edit));

            }

            @Override
            public void onFail(String fail) {
                if (pd.isShowing())
                    pd.dismiss();
                Utils.showCustomToast(activity, fail);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
           try {
               selectedImage = data.getData();
               Glide.with(activity).load(selectedImage + "").into(ivMasjedLogo);
               String selectedImagePath = getRealPathFromURI(selectedImage);
//               Log.i("////// path", selectedImagePath);
               image_str = makePictureToBase64(checkImg(selectedImagePath), ivMasjedLogo);
               generateNoteOnSD(activity, "imgstr", image_str);
           }catch (Exception e){
               Utils.showCustomToast(activity,"حدث خطأ اختر صورة أخرى");
               e.printStackTrace();
           }
        }
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
//        Bitmap bitmap = BitmapFactory.decodeFile(image_path);
        Bitmap bitmap = ShrinkBitmap(image_path, 300, 300);
//        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
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
//image.setImageBitmap(bitmap);
        return type + Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
//            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result = null;

        Cursor cursor =getContentResolver().query(contentURI, null, null, null, null);

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
    public void onClick(View view) {
        if (view == showHide || view == ll_password) {
            if (ll_changePassword.getVisibility() == View.VISIBLE) {
                ll_changePassword.setVisibility(View.GONE);
                showHide.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            } else {
                ll_changePassword.setVisibility(View.VISIBLE);
                showHide.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            }
        }
    }

    private void setColor() {
        try {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.back_text));
        }catch (NoSuchMethodError ex){
            ex.printStackTrace();
        } }

}
