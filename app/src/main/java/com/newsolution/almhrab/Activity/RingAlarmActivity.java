package com.newsolution.almhrab.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.newsolution.almhrab.AppConstants.AlarmUtils;
import com.newsolution.almhrab.AppConstants.Constants;
import com.newsolution.almhrab.AppConstants.ScreenUtils;
import com.newsolution.almhrab.R;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class  RingAlarmActivity extends Activity implements Constants, OnClickListener, OnCompletionListener {
    static int mAudioStream = 4;
    private Button mAlarmOff;
    AscendingAlarmHandler mAscHandler;
    AudioManager mAudioManager;
    Runnable mAutoStop = null;
    MediaPlayer mMediaPlayer = null;
    private NotificationManager mNotificationManager;
    private OnAudioFocusChangeListener mOnAudioFocusChangeListener = new C07901();
    int mOriginalVolume = -1;
    private TextView mPrayerName;
    String mPrayerNameString = null;
    boolean mPreAlarm = false;
    Runnable mStartDua = null;
    private PhoneStateListener phoneStateListener = new C07912();
    private long now;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    class C07901 implements OnAudioFocusChangeListener {
        C07901() {
        }

        public void onAudioFocusChange(int focusChange) {
            if (focusChange == -1) {
                 RingAlarmActivity.this.sendNotification(true);
                 RingAlarmActivity.this.stopAlarm();
                 RingAlarmActivity.this.finish();
            } else if (focusChange == 1) {
                 RingAlarmActivity.this.startAlarm();
            }
        }
    }

    class C07912 extends PhoneStateListener {
        C07912() {
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == 1) {
                 RingAlarmActivity.this.mOnAudioFocusChangeListener.onAudioFocusChange(-1);
            }
        }
    }

    class C07923 implements Runnable {
        C07923() {
        }

        public void run() {
             RingAlarmActivity.this.sendNotification();
             RingAlarmActivity.this.mAlarmOff.performClick();
        }
    }

    class C07934 implements OnCompletionListener {
        C07934() {
        }

        public void onCompletion(MediaPlayer mp) {
             RingAlarmActivity.this.stopAlarm();
             RingAlarmActivity.this.finish();
        }
    }

    private static class AscendingAlarmHandler extends Handler {
        WeakReference<AudioManager> mAudioManagerRef = null;

        public AscendingAlarmHandler(AudioManager audioManager) {
            this.mAudioManagerRef = new WeakReference(audioManager);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AudioManager audioManager = (AudioManager) this.mAudioManagerRef.get();
            if (audioManager != null) {
                audioManager.setStreamVolume(4, AlarmUtils.getAlarmVolumeFromPercentage(audioManager
                        ,  RingAlarmActivity.mAudioStream, ((float) msg.what) * 20.0f), 0);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        String language_code;
        super.onCreate(savedInstanceState);
        Context context = this;
        ScreenUtils.lockOrientation(this);
        setContentView(R.layout.activity_ring_alarm);
        setVolumeControlStream(4);
       now= Calendar.getInstance().getTimeInMillis();
        Calendar.getInstance(TimeZone.getDefault()).setTimeInMillis(System.currentTimeMillis());
        this.mPrayerName = (TextView) findViewById( R.id.prayer_name);
        this.mPrayerNameString = getIntent().getStringExtra(Constants.EXTRA_PRAYER_NAME);
        if (this.mPrayerNameString.equals("Fajr")) {
            this.mPrayerNameString = getResources().getString( R.string.pn1);
        } else if (this.mPrayerNameString.equals("Dhuhr")) {
            this.mPrayerNameString = getResources().getString( R.string.pn3);
        } else if (this.mPrayerNameString.equals("Asr")) {
            this.mPrayerNameString = getResources().getString( R.string.pn4);
        } else if (this.mPrayerNameString.equals("Maghrib")) {
            this.mPrayerNameString = getResources().getString( R.string.pn5);
        } else if (this.mPrayerNameString.equals("Isha")) {
            this.mPrayerNameString = getResources().getString( R.string.pn6);
        }
        boolean z = getIntent().hasExtra(Constants.EXTRA_PRE_ALARM_FLAG)
                && getIntent().getBooleanExtra(Constants.EXTRA_PRE_ALARM_FLAG, true);
        this.mPreAlarm = z;
        Log.i("///*",this.mPrayerNameString);
//        if (this.mPreAlarm) {
            this.mPrayerName.setText(this.mPrayerNameString);
//            this.mPrayerName.setText(String.format("%2$tl:%2$tM %2$tp %1$s", new Object[]{getString(R.string.notification_body, this.mPrayerNameString),
//                    now}));
//        } else {
//            this.mPrayerName.setText(getString( R.string.prayer_name_time, new Object[]{this.mPrayerNameString}));
//        }
        this.mAlarmOff = (Button) findViewById( R.id.alarm_off);
        this.mAlarmOff.setOnClickListener(this);
        try {
            playAlarm();
        } catch (Exception e) {
            Log.e("RingAlarmActivity", e.getMessage(), e);
        }
        ((TelephonyManager) getSystemService("phone")).listen(this.phoneStateListener, 32);
    }

    private void playAlarm() throws Exception {
        AssetFileDescriptor assetFileDescriptor = null;
        assetFileDescriptor = getResources().openRawResourceFd( R.raw.phone);

        this.mMediaPlayer = new MediaPlayer();
        if (null != null) {
            this.mMediaPlayer.setDataSource(this, null);
        } else {
            this.mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor()
                    , assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        }
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mOriginalVolume = this.mAudioManager.getStreamVolume(mAudioStream);
        if (this.mOriginalVolume == 0) {
            this.mAudioManager.setStreamVolume(4, AlarmUtils.getAlarmVolumeFromPercentage(this.mAudioManager, mAudioStream, 50.0f), 0);
        }
        this.mMediaPlayer.setLooping(false);
        this.mMediaPlayer.setAudioStreamType(mAudioStream);
        this.mMediaPlayer.prepare();
        if (assetFileDescriptor != null) {
            this.mMediaPlayer.setOnCompletionListener(this);
        }
        if (this.mAudioManager.requestAudioFocus(this.mOnAudioFocusChangeListener, mAudioStream, 1) == 1) {
            startAlarm();
        }
    }

    private void startAlarm() {
        AudioManager am = (AudioManager) getSystemService("audio");
        if (am.getMode() != 2) {
            switch (am.getRingerMode()) {
                case 0:
                    Log.i("MyApp", "Silent mode");
                    return;
                case 1:
                    Log.i("MyApp", "Vibrate mode");
                    return;
                case 2:
                    Log.i("MyApp", "Normal mode");
                    this.mMediaPlayer.start();
                    Button button = this.mAlarmOff;
                    Runnable c07923 = new C07923();
                    this.mAutoStop = c07923;
                    button.postDelayed(c07923, Constants.ONE_MINUTE);
                    this.mMediaPlayer.setOnCompletionListener(new C07934());
                    return;
                default:
                    return;
            }
        }
    }

    private void stopAlarm() {
        if (this.mAscHandler != null) {
            if (this.mAscHandler.hasMessages(20)) {
                this.mAscHandler.removeMessages(20);
            }
            if (this.mAscHandler.hasMessages(40)) {
                this.mAscHandler.removeMessages(20);
            }
            if (this.mAscHandler.hasMessages(60)) {
                this.mAscHandler.removeMessages(20);
            }
            if (this.mAscHandler.hasMessages(80)) {
                this.mAscHandler.removeMessages(20);
            }
            if (this.mAscHandler.hasMessages(100)) {
                this.mAscHandler.removeMessages(20);
            }
            this.mAscHandler = null;
        }
        if (this.mAutoStop != null) {
            this.mAlarmOff.removeCallbacks(this.mAutoStop);
            this.mAutoStop = null;
        }
        if (this.mStartDua != null) {
            this.mAlarmOff.removeCallbacks(this.mStartDua);
            this.mStartDua = null;
        }
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            if (this.mOriginalVolume != -1) {
                this.mAudioManager.setStreamVolume(mAudioStream, this.mOriginalVolume, 0);
            }
        }
        this.mAudioManager.abandonAudioFocus(this.mOnAudioFocusChangeListener);
        ((TelephonyManager) getSystemService("phone")).listen(this.phoneStateListener, 0);
    }

    public void onClick(View v) {
        try {
            stopAlarm();
            finish();
        } catch (Exception ex) {
            Log.i("stop alarm exception", ex.getLocalizedMessage());
        }
    }

    public void onBackPressed() {
    }

    protected void onDestroy() {
        stopAlarm();
        super.onDestroy();
    }

    private void sendNotification() {
        sendNotification(false);
    }

    private void sendNotification(boolean interrupted) {
        Calendar.getInstance(TimeZone.getDefault()).setTimeInMillis(System.currentTimeMillis());
        String formatString = " %1$tl:%1$tM %1$tp " + getString( R.string.alarm_timed_out,
                new Object[]{this.mPrayerName.getText().toString()});
        String title = interrupted ? getString( R.string.alarm_interrupted) : getString( R.string.alarm_timed_out_only);
        String body = String.format(formatString, new Object[]{now});
        if (interrupted) {
            body = this.mPrayerName.getText().toString();
        }
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class),
                PendingIntent.FLAG_ONE_SHOT);
        Builder mBuilder = new Builder(this).setSmallIcon( R.drawable.logo).setContentTitle(title)
                .setStyle(new BigTextStyle().bigText(body)).setContentText(body);
        mBuilder.setContentIntent(contentIntent);
        this.mNotificationManager.notify(Constants.NOTIFICATION_ID, mBuilder.build());
    }

    public void onCompletion(MediaPlayer mp) {
        stopAlarm();
        finish();
    }
}
