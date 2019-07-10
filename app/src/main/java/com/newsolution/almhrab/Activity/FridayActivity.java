package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
//import android.lib.widget.verticalmarqueetextview.VerticalMarqueeTextView;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.newsolution.almhrab.AppConstants.Constants;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.AppConstants.DeveloperKey;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.Khotab;
import com.newsolution.almhrab.R;
import com.newsolution.almhrab.WebServices.WS;
import com.streamaxia.android.CameraPreview;
import com.streamaxia.android.StreamaxiaPublisher;
import com.streamaxia.android.handlers.EncoderHandler;
import com.streamaxia.android.handlers.RecordHandler;
import com.streamaxia.android.handlers.RtmpHandler;
import com.streamaxia.android.utils.ScalingMode;
import com.streamaxia.android.utils.Size;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FridayActivity extends YouTubeFailureRecoveryActivity implements RtmpHandler.RtmpListener, RecordHandler.RecordListener,
        EncoderHandler.EncodeListener {

    private final String TAG = FridayActivity.class.getSimpleName();

    // Set default values for the streamer
    public static String streamaxiaStreamName = "AlMhrab_";
    // Set default values for the streamer
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
    public static String roboto = "fonts/roboto.ttf";
    public static String comfort = "fonts/comfort.ttf";
    public static String arial = "fonts/ariblk.ttf";
    public static String bangla_mn_bold = "fonts/bangla_mn_bold.ttf";
    public static String sansBold = "fonts/neosans_black.otf";


    private TextView tvTitle;
    private Activity activity;
    TextView date1, time, amPm;
    private static String recPath;
    private SharedPreferences sp;
    private VideoView vvVideo;
    private Khotab khotab;
    private RelativeLayout rlLivingStream;
    private CountDownTimer countDownTimer;
    public static final String BROADCAST = Constants.PACKAGE_NAME + ".Activity.android.action.broadcast";
    private YouTubePlayerView youtube_view;
    private WebView wvEngText;
    private WebView wvUrdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_friday);
        activity = this;
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        khotab = (Khotab) getIntent().getSerializableExtra("khotba");

        ButterKnife.bind(this);
        File saveDir = new File(Environment.getExternalStorageDirectory(), "AlMhrab");
        saveDir.mkdirs();
        recPath = saveDir.getAbsolutePath() + "/AlMhrab_" + sp.getInt("masjedId", -1)
                + "_" + Utils.getFormattedCurrentDate() + ".mp4";
        streamaxiaStreamName = streamaxiaStreamName + sp.getInt("masjedId", -1) + "";
        mPublisher = new StreamaxiaPublisher(mCameraView, this);
        mCameraView.setScalingMode(ScalingMode.TRIM);
        try {

            mPublisher.setEncoderHandler(new EncoderHandler(this));
            mPublisher.setRtmpHandler(new RtmpHandler(this));
            mPublisher.setRecordEventHandler(new RecordHandler(this));
            mCameraView.startCamera();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

        setStreamerDefaultValues();

        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        Typeface font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        Typeface fontArial = Typeface.createFromAsset(getAssets(), arial);
        Typeface fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        Typeface fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);
        Typeface fontBangla_mn_bold = Typeface.createFromAsset(getAssets(), bangla_mn_bold);
        Typeface fontSansBold = Typeface.createFromAsset(getAssets(), sansBold);

        rlLivingStream = (RelativeLayout) findViewById(R.id.rlLivingStream);
        vvVideo = (VideoView) findViewById(R.id.vvAdsVideo);
        youtube_view = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youtube_view.initialize(DeveloperKey.DEVELOPER_KEY, this);
        TextView tvUrdText = (TextView) findViewById(R.id.tvUrdText);
        TextView tvEngText = (TextView) findViewById(R.id.tvEngText);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        date1 = (TextView) findViewById(R.id.dateToday);
        time = (TextView) findViewById(R.id.Time);
        amPm = (TextView) findViewById(R.id.amPm);
        amPm.setVisibility(View.VISIBLE);
        wvEngText = (WebView) findViewById(R.id.wvEngText);
        wvUrdText = (WebView) findViewById(R.id.wvUrdText);
        tvTitle.setTypeface(fontBangla_mn_bold);
        time.setTypeface(fontRoboto);
        date1.setTypeface(font);
        tvUrdText.setTypeface(fontDroidkufi);
        tvEngText.setTypeface(fontDroidkufi);

        tvEngText.setText("");
        tvUrdText.setText("");
        tvName.setText(sp.getString("masjedName", ""));
        mChronometer.setVisibility(View.GONE);
        checkTime();
        fillData();
        isStreaming();

    }

    private void isStreaming() {
        WS.isStreaming(activity, khotab.getTimeExpected(), new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private void startKhotbaTimer() {
        long khotbaPeriod = (khotab.getTimeExpected()) * 60 * 1000;
        Log.i("khotbaPeriod: ", khotbaPeriod + "");
        countDownTimer = new CountDownTimer(khotbaPeriod, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void fillData() {
        String khotbaTitle = "";
        if (!TextUtils.isEmpty(khotab.getTitle1()))
            khotbaTitle = khotbaTitle + khotab.getTitle1() + " - ";
        if (!TextUtils.isEmpty(khotab.getTitle2()))
            khotbaTitle = khotbaTitle + khotab.getTitle2();
        if (khotbaTitle.endsWith(" - "))
            khotbaTitle.substring(0, khotbaTitle.length() - 2);
        tvTitle.setText(khotbaTitle);
        if (sp.getBoolean("IsDeaf", false)) {
            if (!khotab.getUrlVideoDeaf().equals("null")) {
                youtube_view.setVisibility(View.VISIBLE);
                rlLivingStream.setVisibility(View.GONE);

            } else {
                vvVideo.setVisibility(View.GONE);
                youtube_view.setVisibility(View.GONE);
                rlLivingStream.setVisibility(View.VISIBLE);
            }
        } else {
            vvVideo.setVisibility(View.GONE);
            youtube_view.setVisibility(View.GONE);
            rlLivingStream.setVisibility(View.VISIBLE);
        }
        vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vvVideo.setVisibility(View.GONE);
                youtube_view.setVisibility(View.GONE);
                rlLivingStream.setVisibility(View.VISIBLE);
            }
        });

        animTranslation();
        startKhotbaTimer();
    }

    private void animTranslation() {
        final String body1 = (khotab.getBody1().equals("null")) ? "" : khotab.getBody1();
        final String body2 = (khotab.getBody2().equals("null")) ? "" : khotab.getBody2();
        int speed = khotab.getTranslationSpeed();
        WebSettings settings1 = wvUrdText.getSettings();
        WebSettings settings = wvEngText.getSettings();
        settings1.setDefaultTextEncodingName("utf-8");
        settings.setDefaultTextEncodingName("utf-8");
        setScrollText(wvEngText, body1, khotab.isDirection1RTL() ? "right" : "left", speed);
        setScrollText(wvUrdText, body2, khotab.isDirection2RTL() ? "right" : "left", speed);
    }


    private void setScrollText(WebView wv, String body, String dir, int speed) {
        wv.setBackgroundColor(Color.TRANSPARENT);

        String fontSize = getResources().getDimensionPixelSize(R.dimen.translateFont) + "px";
        String htmlS = "<html><head><style type='text/css'>@font-face {font-family: 'droid_kufi_bold';src: url('file:///android_asset/fonts/droid_kufi_bold.ttf');} body {font-family: droid_kufi_bold;background-color: transparent;border: 0px;margin: 0px;padding-bottom: 8px; font-size: " + fontSize + ";color: #ffffff;}</style></head><body><marquee behavior=\"scroll\" direction=\"" + dir + "\" scrollamount=\"" + speed + "\">&nbsp;&nbsp;&nbsp;" + body + "</a>&nbsp;&nbsp;&nbsp;&nbsp;</marquee></body></html>";
        wv.loadData(htmlS, "text/html; charset=utf-8", "utf-8");
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                stopStreaming();
                stopChronometer();
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                mPublisher.startPublish("rtmp://rtmp.streamaxia.com/streamaxia/" + streamaxiaStreamName);
                mPublisher.startRecord(recPath);
            } else {
                finish();
                Toast.makeText(this, R.string.allowCameraPermissions, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            mCameraView.stopCamera();
            mPublisher.pauseRecord();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            if (countDownTimer != null) countDownTimer.cancel();
            MainActivity.isOpenSermon = true;
            Log.i("recPath: ", recPath);
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            DateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
            Date date = df.parse(khotab.getDateKhotab());
            String DateKhotab = sdf.format(date);

            Intent intent = new Intent(BROADCAST);
            Bundle extras = new Bundle();
            extras.putBoolean("isKhotba", true);
            extras.putString("recPath", recPath);
            extras.putInt("IdKhotab", khotab.getId());
            extras.putString("DateKhotab", DateKhotab);
            intent.putExtras(extras);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void checkTime() {
        DateHigri hd = new DateHigri();
        date1.setText(Utils.writeIslamicDate1(this, hd));
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);
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
        try {
            if (mPublisher != null) {
                List<Size> sizes = mPublisher.getSupportedPictureSizes(getResources().getConfiguration().orientation);
                Size resolution = sizes.get(0);
                mPublisher.setVideoOutputResolution(480, 640, this.getResources().getConfiguration().orientation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void setStatusMessage(final String msg) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "[" + msg + "]");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }

    @Override
    public void onRecordFinished(String s) {
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
    public void onRtmpVideoBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Video bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Video bitrate: %d bps", rate));
        }

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
            Log.i("streaming: ", e.getMessage());
            mPublisher.stopPublish();
            mPublisher.stopRecord();
        } catch (Exception e1) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player,
                                        boolean wasRestored) {
        Log.i(TAG, khotab.getUrlVideoDeaf());
        if (sp.getBoolean("IsDeaf", false)) {
            if (!khotab.getUrlVideoDeaf().equals("null")) {
                if (khotab.getUrlVideoDeaf().contains("youtube") || khotab.getUrlVideoDeaf().contains("youtu.be")) {
                    player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            player.play();
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                    if (!wasRestored) {
                        String id = null;
                        try {
                            String prefix = "https://youtu.be/";
                            String prefix1 = "http://youtu.be/";
                            if (khotab.getUrlVideoDeaf().contains(prefix) || khotab.getUrlVideoDeaf().contains(prefix1)) {
                                id = khotab.getUrlVideoDeaf().split("youtu.be/")[1];
                            } else id = extractYoutubeId(khotab.getUrlVideoDeaf());
                            player.cueVideo(id);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            rlLivingStream.setVisibility(View.VISIBLE);
                            youtube_view.setVisibility(View.GONE);
                        }
                    }
                } else {
                    youtube_view.setVisibility(View.GONE);
                    vvVideo.setVisibility(View.VISIBLE);
                    vvVideo.setVideoURI(Uri.parse(khotab.getUrlVideoDeaf()));
                    vvVideo.start();
                }
            }
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    public String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }
}
