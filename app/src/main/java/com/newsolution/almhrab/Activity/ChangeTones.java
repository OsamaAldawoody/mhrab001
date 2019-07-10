package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChangeTones extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    Activity activity;
    private SharedPreferences.Editor spedit;
    private ImageView ivBack;
    private AppCompatImageView ivPlayAthan;
    private Button btnSaveEdit, btnChAthanSound, btnChCloseSound;
    private AppCompatImageView ivPlayClose, ivPlayIqama;
    private Button btnChIqamaSound;
    private Uri uriAthan = null;
    private Uri uriIqama = null;
    private Uri uriPhone = null;
    private MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayer3;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setColor();
        }
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        spedit = sp.edit();
        setContentView(R.layout.activity_change_tones);
        MediaPlayer mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        findViews();
    }

    private void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivPlayAthan = (AppCompatImageView) findViewById(R.id.ivPlayAthan);
        btnSaveEdit = (Button) findViewById(R.id.btnSaveEdit);
        btnChCloseSound = (Button) findViewById(R.id.btnChCloseSound);
        btnChAthanSound = (Button) findViewById(R.id.btnChAthanSound);
        ivPlayIqama = (AppCompatImageView) findViewById(R.id.ivPlayIqama);
        ivPlayClose = (AppCompatImageView) findViewById(R.id.ivPlayClose);
        btnChIqamaSound = (Button) findViewById(R.id.btnChIqamaSound);
        ivPlayAthan.setVisibility(View.GONE);
        ivPlayIqama.setVisibility(View.GONE);
        ivPlayClose.setVisibility(View.GONE);

        ivBack.setOnClickListener(this);
        btnSaveEdit.setOnClickListener(this);
        btnChCloseSound.setOnClickListener(this);
        btnChAthanSound.setOnClickListener(this);
        btnChIqamaSound.setOnClickListener(this);
        ivPlayAthan.setOnClickListener(this);
        ivPlayIqama.setOnClickListener(this);
        ivPlayClose.setOnClickListener(this);
        if (!TextUtils.isEmpty(sp.getString("uriAthan", ""))) {
            ivPlayAthan.setVisibility(View.VISIBLE);
            uriAthan = Uri.parse(sp.getString("uriAthan", ""));
        } else
            ivPlayAthan.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(sp.getString("uriIqama", ""))) {
            ivPlayIqama.setVisibility(View.VISIBLE);
            uriIqama = Uri.parse(sp.getString("uriIqama", ""));
        } else
            ivPlayIqama.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(sp.getString("uriPhone", ""))) {
            ivPlayClose.setVisibility(View.VISIBLE);
            uriPhone = Uri.parse(sp.getString("uriPhone", ""));
        } else
            ivPlayClose.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            onBackPressed();
        } else if (v == btnSaveEdit) {
            if (uriIqama != null) {
                spedit.putString("uriIqama", uriIqama.toString()).commit();
            }
            if (uriAthan != null) {
                spedit.putString("uriAthan", uriAthan.toString()).commit();
            }
            if (uriPhone != null) {
                spedit.putString("uriPhone", uriPhone.toString()).commit();
            }
            if (mediaPlayer2 != null) {
                mediaPlayer2.release();
            }
            if (mediaPlayer1 != null) {
                mediaPlayer1.release();
            }
            if (mediaPlayer3 != null) {
                mediaPlayer3.release();
            }
            if (uriAthan != null || uriIqama != null || uriPhone != null) {
                Utils.showCustomToast(activity, "تم الحفظ");
            }
        } else if (v == btnChAthanSound) {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("audio/*");
            startActivityForResult(intent, 1);
        } else if (v == btnChCloseSound) {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("audio/*");
            startActivityForResult(intent, 3);
        } else if (v == btnChIqamaSound) {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("audio/*");
            startActivityForResult(intent, 2);
        } else if (v == ivPlayAthan) {
            try {
                if (mediaPlayer2 != null) {
                    if (mediaPlayer2.isPlaying()) {
                        mediaPlayer2.stop();
                        mediaPlayer2.release();
                        mediaPlayer2 = null;
                        ivPlayIqama.setImageResource(R.drawable.ic_play);
                    }
                }
                if (mediaPlayer3 != null) {
                    if (mediaPlayer3.isPlaying()) {
                        mediaPlayer3.stop();
                        mediaPlayer3.release();
                        mediaPlayer3 = null;
                        ivPlayClose.setImageResource(R.drawable.ic_play);
                    }
                }
                if (mediaPlayer1 != null) {
                    if (mediaPlayer1.isPlaying()) {
                        mediaPlayer1.pause();
                        ivPlayAthan.setImageResource(R.drawable.ic_play);
                    } else {
                        mediaPlayer1.start();
                        ivPlayAthan.setImageResource(R.drawable.ic_pause);
                    }
                } else if (uriAthan != null) {
                    mediaPlayer1 = MediaPlayer.create(this, uriAthan);
                    mediaPlayer1.start();
                    ivPlayAthan.setImageResource(R.drawable.ic_pause);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                Toast.makeText(activity, R.string.fileNotExists, Toast.LENGTH_SHORT).show();
            }
        } else if (v == ivPlayClose) {
            try {
                if (mediaPlayer1 != null) {
                    if (mediaPlayer1.isPlaying()) {
                        mediaPlayer1.stop();
                        mediaPlayer1.release();
                        mediaPlayer1 = null;
                        ivPlayAthan.setImageResource(R.drawable.ic_play);
                    }
                }
                if (mediaPlayer2 != null) {
                    if (mediaPlayer2.isPlaying()) {
                        mediaPlayer2.stop();
                        mediaPlayer2.release();
                        mediaPlayer2 = null;
                        ivPlayIqama.setImageResource(R.drawable.ic_play);
                    }
                }
                if (mediaPlayer3 != null) {
                    if (mediaPlayer3.isPlaying()) {
                        mediaPlayer3.pause();
                        ivPlayClose.setImageResource(R.drawable.ic_play);
                    } else {
                        mediaPlayer3.start();
                        ivPlayClose.setImageResource(R.drawable.ic_pause);
                    }
                } else if (uriPhone != null) {
                    Log.i("****", uriPhone + " **-");
                    mediaPlayer3 = MediaPlayer.create(this, uriPhone);
                    mediaPlayer3.start();
                    ivPlayClose.setImageResource(R.drawable.ic_pause);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                Toast.makeText(activity, "الملف غير موجود, حاول مرة أخرى", Toast.LENGTH_SHORT).show();
            }

        } else if (v == ivPlayIqama) {
            try {
                if (mediaPlayer1 != null) {
                    if (mediaPlayer1.isPlaying()) {
                        mediaPlayer1.stop();
                        mediaPlayer1.release();
                        mediaPlayer1 = null;
                        ivPlayAthan.setImageResource(R.drawable.ic_play);
                    }
                }
                if (mediaPlayer3 != null) {
                    if (mediaPlayer3.isPlaying()) {
                        mediaPlayer3.stop();
                        mediaPlayer3.release();
                        mediaPlayer3 = null;
                        ivPlayClose.setImageResource(R.drawable.ic_play);
                    }
                }
                if (mediaPlayer2 != null) {
                    if (mediaPlayer2.isPlaying()) {
                        mediaPlayer2.pause();
                        ivPlayIqama.setImageResource(R.drawable.ic_play);
                    } else {
                        mediaPlayer2.start();
                        ivPlayIqama.setImageResource(R.drawable.ic_pause);
                    }
                } else if (uriIqama != null) {
                    mediaPlayer2 = MediaPlayer.create(this, uriIqama);
                    mediaPlayer2.start();
                    ivPlayIqama.setImageResource(R.drawable.ic_pause);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
                Toast.makeText(activity, "الملف غير موجود, حاول مرة أخرى", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        try {
            if (mediaPlayer2 != null) {
                if (mediaPlayer2.isPlaying()) {
                    mediaPlayer2.stop();
                    mediaPlayer2.release();
                    mediaPlayer2 = null;
                }
            }
            if (mediaPlayer1 != null) {
                if (mediaPlayer1.isPlaying()) {
                    mediaPlayer1.stop();
                    mediaPlayer1.release();
                    mediaPlayer1 = null;
                }
            }
            if (mediaPlayer3 != null) {
                if (mediaPlayer3.isPlaying()) {
                    mediaPlayer3.stop();
                    mediaPlayer3.release();
                    mediaPlayer3 = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ivPlayAthan.setImageResource(R.drawable.ic_play);
        ivPlayIqama.setImageResource(R.drawable.ic_play);
        ivPlayClose.setImageResource(R.drawable.ic_play);

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        try {
            if (mediaPlayer2 != null) {
                if (mediaPlayer2.isPlaying()) {
                    mediaPlayer2.stop();
                    mediaPlayer2.release();
                    mediaPlayer2 = null;
                }
            }
            if (mediaPlayer1 != null) {
                if (mediaPlayer1.isPlaying()) {
                    mediaPlayer1.stop();
                    mediaPlayer1.release();
                    mediaPlayer1 = null;
                }
            }
            if (mediaPlayer3 != null) {
                if (mediaPlayer3.isPlaying()) {
                    mediaPlayer3.stop();
                    mediaPlayer3.release();
                    mediaPlayer3 = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ivPlayAthan.setImageResource(R.drawable.ic_play);
        ivPlayIqama.setImageResource(R.drawable.ic_play);
        ivPlayClose.setImageResource(R.drawable.ic_play);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                uriAthan = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    ContentResolver resolver = getContentResolver();
                    resolver.takePersistableUriPermission(uriAthan, takeFlags);
                }
                mediaPlayer1 = MediaPlayer.create(this, data.getData());

                ivPlayAthan.setVisibility(View.VISIBLE);

            } else if (requestCode == 2) {
                uriIqama = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    ContentResolver resolver = getContentResolver();
                    resolver.takePersistableUriPermission(uriIqama, takeFlags);
                }
                mediaPlayer2 = MediaPlayer.create(this, data.getData());
                ivPlayIqama.setVisibility(View.VISIBLE);

            } else if (requestCode == 3) {
                uriPhone = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    ContentResolver resolver = getContentResolver();
                    resolver.takePersistableUriPermission(uriPhone, takeFlags);
                }
                mediaPlayer3 = MediaPlayer.create(this, data.getData());
                ivPlayClose.setVisibility(View.VISIBLE);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

}
