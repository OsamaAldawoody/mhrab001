package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.newsolution.almhrab.AppConstants.AppConst;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.R;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.Size;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FridayActivity extends  AppCompatActivity implements RtmpHandler.RtmpListener, RecordHandler.RecordListener,
        EncoderHandler.EncodeListener {

    private final String TAG = FridayActivity.class.getSimpleName();

    // Set default values for the streamer
    public final static String streamaxiaStreamName = "amal";
    public final static int bitrate = 500;
    public final static int width = 720;
    public final static int height = 1280;

    @BindView(R.id.preview)
    CameraPreview mCameraView;
    @BindView(R.id.chronometer)
    Chronometer mChronometer;

    private StreamaxiaPublisher mPublisher;
    public static String droidkufiBold = "fonts/droid_kufi_bold.ttf";
    public static String droidkufi = "fonts/droidkufi_regular.ttf";
    private Typeface font;
    private Typeface fontDroidkufi;
    public static String roboto = "fonts/roboto.ttf";
    private Typeface fontRoboto;
    public static String comfort = "fonts/comfort.ttf";//comfort
    private Typeface fontComfort;
    public static String arial = "fonts/ariblk.ttf";//comfort
    private Typeface fontArial;
    public static String bangla_mn_bold = "fonts/bangla_mn_bold.ttf";//comfort
    private Typeface fontBangla_mn_bold;


    private TextView tvUrdText;
    private TextView tvEngText;
    private TextView tvTitle;
    private TextView tvName;
    private Activity activity;
    TextView date1, time, amPm;
    private static  String recPath ;//= Environment.getExternalStorageDirectory().getPath() + "/" +Utils.getDateTime()+".mp4";
    private File saveDir;
    private SharedPreferences sp;
    private VideoView vvVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_friday);
        activity=this;
        ButterKnife.bind(this);
//        hideStatusBar();
        saveDir = new File(Environment.getExternalStorageDirectory(), "AlMhrab");
        saveDir.mkdirs();
        recPath = saveDir.getAbsolutePath()+ "/Live_" +Utils.getDateTime()+".mp4";
        mPublisher = new StreamaxiaPublisher(mCameraView, this);

        mPublisher.setEncoderHandler(new EncoderHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordEventHandler(new RecordHandler(this));
        mCameraView.startCamera();

        setStreamerDefaultValues();


        sp = getSharedPreferences(AppConst.PREFS, MODE_PRIVATE);
        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontArial = Typeface.createFromAsset(getAssets(), arial);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);
        fontBangla_mn_bold = Typeface.createFromAsset(getAssets(), bangla_mn_bold);

        vvVideo = (VideoView) findViewById(R.id.vvAdsVideo);
        tvUrdText = (TextView) findViewById(R.id.tvUrdText);
        tvEngText = (TextView) findViewById(R.id.tvEngText);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName = (TextView) findViewById(R.id.tvName);
        date1 = (TextView) findViewById(R.id.dateToday);
        time = (TextView) findViewById(R.id.Time);
        amPm = (TextView) findViewById(R.id.amPm);
        amPm.setVisibility(View.VISIBLE);
        tvTitle.setTypeface(fontBangla_mn_bold);
        time.setTypeface(fontRoboto);
        date1.setTypeface(font);
        tvUrdText.setTypeface(fontBangla_mn_bold);
        tvEngText.setSelected(true);
        tvUrdText.setSelected(true);
        tvName.setText(sp.getString("masjedName", ""));
//        tvEngText.setTypeface(fontBangla_mn_bold);
//        tvName.setTypeface(fontDroidkufi);

        checkTime();

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            stopStreaming();
            stopChronometer();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mPublisher.setVideoBitRate(144);
            mPublisher.startPublish("rtmp://rtmp.streamaxia.com/streamaxia/" + streamaxiaStreamName);
            mPublisher.startRecord(recPath);

        } else {
          finish();
            Toast.makeText(this, "You need to grant persmissions in order to begin streaming.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.stopCamera();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
        MainActivity.isOpenSermon=true;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        mPublisher.stopPublish();
//        mPublisher.stopRecord();
//        MainActivity.isOpenSermon=true;
//        finish();
//    }

    private void checkTime() {
        DateHigri hd = new DateHigri();
        date1.setText(Utils.writeIslamicDate1(this, hd));
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", new Locale("en"));
        DateFormat ampm = new SimpleDateFormat("a", new Locale("ar"));
        amPm.setText(ampm.format(Calendar.getInstance().getTime()));
        Calendar c = Calendar.getInstance();
        String timeText = timeNow.format(c.getTime());
        SpannableString string = new SpannableString(timeText);
        string.setSpan(new RelativeSizeSpan((0.5f)), 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        time.setText(string);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkTime();
            }
        }, 1000);
    }
    private void stopStreaming() {
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.setScreenOrientation(newConfig.orientation);
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void setStreamerDefaultValues() {
        List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
        Size resolution = sizes.get(0);
        mPublisher.setVideoOutputResolution(resolution.width, resolution.height, this.getResources().getConfiguration().orientation);
    }

    private void setStatusMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "[" + msg + "]", Toast.LENGTH_LONG).show();
            }
        });
    }


    /*
    * EncoderHandler implementation
    * */

    @Override
    public void onNetworkWeak() {

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }


    /*
    * RecordHandler implementation
    * */

    @Override
    public void onRecordPause() {

    }

    @Override
    public void onRecordResume() {

    }

    @Override
    public void onRecordStarted(String s) {
        Toast.makeText(activity, "[" + s + "]", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRecordFinished(String s) {
        Toast.makeText(activity, "[" + s + "]", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    /*
    * RTMPListener implementation
    * */

    @Override
    public void onRtmpConnecting(String s) {
        setStatusMessage(s);
    }

    @Override
    public void onRtmpConnected(String s) {
        setStatusMessage(s);
        Toast.makeText(activity, "[STOP]", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {
        setStatusMessage("STOPPED");
    }

    @Override
    public void onRtmpDisconnected() {
        setStatusMessage("Disconnected");
    }

    @Override
    public void onRtmpVideoFpsChanged(double v) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double v) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double v) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    @Override
    public void onRtmpAuthenticationg(String s) {

    }

    private void stopChronometer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.stop();
    }

    private void handleException(Exception e) {
        try {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            mPublisher.stopPublish();
            mPublisher.stopRecord();
        } catch (Exception e1) {
            // Ignore
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
