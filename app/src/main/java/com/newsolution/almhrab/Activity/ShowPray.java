package com.newsolution.almhrab.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.AppConstants.DateHigri;
import com.newsolution.almhrab.GlobalVars;
import com.newsolution.almhrab.Helpar.PlaySound;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowPray extends Activity implements RecognitionListener {

    DBOperations DBO;
    TextView date1, time, amPm;
    public Calendar today = Calendar.getInstance();
    double day = today.get(Calendar.DAY_OF_MONTH);
    double month = today.get(Calendar.MONTH);
    double year = today.get(Calendar.YEAR);

    public String nextPray = "dhuhr";
    String[] mosquSettings;

    private TextView rakaa;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "voiceRecognitionAct";
    public String se10 = "same";
    public String se8 = "hamada";
    public String se9 = "semmy";
    public String se = "send me";
    public String se21 = "tell me";
    public String se0 = "hamida";
    public String se1 = " semi allahu";
    public String se2 = "semi";
    public String se3 = "similar";
    public String se4 = "samuel";
    public String se5 = "سمع الله لمن حمده";
    public String se13 = "سمع الله";
    public String se16 = "حمده";
    public String se12 = "سمع لمن حمده";
    public String se6 = "لمن حمده";
    public String se7 = "ربنا لك الحمد";
    public String se11 = "حميده";
    public String se14 = "من حميده";
    public String se15 = "set me";
    public String se17 = "lululemon";
    public String se18 = "hameeda";
    public String se19 = "lemon";
    public String se20 = "hamidah";
    public String te0 = "allahu akbar";
    public String te1 = "allah hoo akbar";
    public String te2 = "allah";
    public String te3 = "allahu";
    public String te4 = "lucky";
    public String te12 = "akbar";
    public String te13 = "colour";
    public String te5 = "الله أكبر ";
    public String te9 = "الله اكبر";
    public String te11 = "اكبر";
    public String te6 = "اللهو";
    public String te14 = "الله";
    public String te10 = "bar";
    public String te15 = "Aloha";
    public String te16 = "like";
    public String te17 = "hola";
    public String te7 = "allahu ackbar";
    public String te8 = "allahu akhbar";

    public int rn = 0;
    public int maxRn = 4;
    public String t2 = "samuel";
    public String t3 = "similar";
    public Context context;
    private SharedPreferences sp;
    private OptionSiteClass settings;
    private TextView tvHumidity;
    private TextView in_masgedTemp, out_masgedTemp;
    private int period;
    private CountDownTimer countDownTimer;
    private ImageView sound_stop;
    private AudioManager mAudioManager;
    public static String droidkufiBold = "fonts/droid_kufi_bold.ttf";
    public static String droidkufi = "fonts/droidkufi_regular.ttf";
    private Typeface font;
    public static String roboto = "fonts/roboto.ttf";
    private Typeface fontRoboto;
    public static String comfort = "fonts/comfort.ttf";
    private int maxVolume;
    private Handler handlerSound;
    private Runnable soundRun;

    boolean VolIsMute = false;

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
        setContentView(R.layout.activity_show);


        ButterKnife.bind(this);
        font = Typeface.createFromAsset(getAssets(), droidkufiBold);
        fontRoboto = Typeface.createFromAsset(getAssets(), roboto);
        Typeface fontDroidkufi = Typeface.createFromAsset(getAssets(), droidkufi);
        DBO = new DBOperations(this);
        sp = getSharedPreferences(Utils.PREFS, MODE_PRIVATE);
        DBO.open();
        settings = DBO.getSettings();
        DBO.close();


        final GlobalVars gv = (GlobalVars) getApplicationContext();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mosquSettings = gv.getMousqeSettings();
        nextPray = getIntent().getStringExtra("currentPray");
        waitTimer(nextPray);

        try {
            dispCurrentPray();
            checkTime();
        } catch (Exception ignored) {
        }

        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        out_masgedTemp = (TextView) findViewById(R.id.outMasgedasged);
        in_masgedTemp = (TextView) findViewById(R.id.in_masged);
        TextView tvIn = (TextView) findViewById(R.id.tvIn);
        TextView tvOut = (TextView) findViewById(R.id.tvOut);
        TextView tvHum = (TextView) findViewById(R.id.tvHum);
        tvHum.setText(getString(R.string.outHum));
        tvIn.setTypeface(fontDroidkufi);
        tvOut.setTypeface(fontDroidkufi);
        tvHum.setTypeface(fontDroidkufi);
        sound_stop = (ImageView) findViewById(R.id.sound_stop);
        sound_stop.setVisibility(View.INVISIBLE);
        handlerSound = new Handler();
        soundRun = new Runnable() {
            @Override
            public void run() {
                if (PlaySound.isPlay(getBaseContext())) {
                    sound_stop.setVisibility(View.VISIBLE);
                } else {
                    sound_stop.setVisibility(View.INVISIBLE);
                }
            }
        };
        handlerSound.postDelayed(soundRun, 2000);

        sound_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.stop(getBaseContext());
                sound_stop.setVisibility(View.INVISIBLE);
            }
        });
        if (TextUtils.isEmpty(sp.getString("TempIn", ""))) {
            getWeather(0);
        } else {
            in_masgedTemp.setText(sp.getString("TempIn", ""));
        }
        if (TextUtils.isEmpty(sp.getString("TempOut", "")) || TextUtils.isEmpty(sp.getString("HumOut", ""))) {
            getWeather(1);
        } else {
            out_masgedTemp.setText(sp.getString("TempOut", ""));
            tvHumidity.setText(sp.getString("HumOut", "") + "%");

        }
        if (TextUtils.isEmpty(sp.getString("TempOut", "")) && TextUtils.isEmpty(sp.getString("TempIn", ""))) {
            getWeather(-1);
        }

        rakaa = (TextView) findViewById(R.id.show_num);
        rakaa.setText("الركعة الأولى");
        rakaa.setVisibility(View.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);


        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    private void waitTimer(final String currentPray) {
        switch (currentPray) {
            case "fajr":
                maxRn = 2;
                period = settings.getFajrAzkar();
                break;
            case "dhuhr":
                period = settings.getDhuhrAzkar();
                maxRn = 4;
                break;
            case "asr":
                period = settings.getAsrAzkar();
                maxRn = 4;
                break;
            case "magrib":
                period = settings.getMagribAzkar();
                maxRn = 3;
                break;
            case "isha":
                period = settings.getIshaAzkar();
                maxRn = 4;
                break;
        }
        countDownTimer = new CountDownTimer(period * (60 * 1000), 1000) {

            public void onTick(long millisUntilFinished) {
                if (PlaySound.isPlay(getBaseContext())) {
                    sound_stop.setVisibility(View.VISIBLE);
                    if (VolIsMute) stopSilentMode();
                } else {
                    sound_stop.setVisibility(View.INVISIBLE);
                    if (!VolIsMute) runSilentMode();
                    CheckRakaa();
                }
            }

            public void onFinish() {
                if (countDownTimer != null) countDownTimer.cancel();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (VolIsMute) stopSilentMode();
                        Intent cp = new Intent(getApplicationContext(), Read.class);
                        cp.setAction("b");
                        cp.putExtra("pray", currentPray);
                        startActivity(cp);
                        finish();
                    }
                }, 10000);
            }

        }.start();
    }

    private void stopSilentMode() {
        VolIsMute = false;
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        try {

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FX_KEY_CLICK);
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void runSilentMode() {
        VolIsMute = true;
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, AudioManager.FX_KEY_CLICK);
            } else
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FX_KEY_CLICK);
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void checkTime() {
        DateHigri hd = new DateHigri();
        date1 = (TextView) findViewById(R.id.dateToday);
        date1.setText(Utils.writeIslamicDate(this, hd));
        time = (TextView) findViewById(R.id.Time);
        time.setTypeface(fontRoboto);
        date1.setTypeface(font);
        DateFormat timeNow = new SimpleDateFormat("hh:mmss", Locale.ENGLISH);
        amPm = (TextView) findViewById(R.id.amPm);
        amPm.setVisibility(View.VISIBLE);
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


    private void dispCurrentPray() {
        TextView PrayName = (TextView) findViewById(R.id.show_time);
        if (nextPray.equals("fajr")) {
            PrayName.setText(getResources().getString(R.string.p1));
            maxRn = 2;
        }
        if (nextPray.equals("dhuhr")) {
            PrayName.setText(getResources().getString(R.string.p2));
            maxRn = 4;
        }
        if (nextPray.equals("asr")) {
            PrayName.setText(getResources().getString(R.string.p3));
            maxRn = 4;
        }
        if (nextPray.equals("magrib")) {
            PrayName.setText(getResources().getString(R.string.p4));
            maxRn = 3;
        }
        if (nextPray.equals("isha")) {
            PrayName.setText(getResources().getString(R.string.p5));
            maxRn = 4;
        }
    }


    public void CheckRakaa() {
        try {
            speech.startListening(recognizerIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            if (countDownTimer != null) countDownTimer.cancel();
            try {
                speech.stopListening();
                speech.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (VolIsMute) stopSilentMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speech != null) {
            if (countDownTimer != null) countDownTimer.cancel();
            try {
                speech.stopListening();
                speech.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (handlerSound != null) handlerSound.removeCallbacks(soundRun);

        if (VolIsMute) stopSilentMode();

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuilder text = new StringBuilder();
        boolean a = false;
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.blink1);
        if (matches != null) {
            for (String result : matches) {
                Log.i(LOG_TAG, "onResults: " + result);
                text.append(result).append("\n");
                if (rn < maxRn) {
                    if (rn == 0) {
                        if (checkFirstResult(result)) {
                            rn++;
                            rakaa.setVisibility(View.VISIBLE);
                            rakaa.setText("الركعة الأولى");
                        } else if (checkResult(result)) {
                            rakaa.setVisibility(View.VISIBLE);
                            rakaa.setText("الركعة الأولى");
                            rn = 2;
                            rakaa.startAnimation(myAnim);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rakaa.clearAnimation();
                                    rakaa.setText("الركعة الثانية");
                                }
                            }, 30000);
                        }

                    } else if (checkResult(result)) {
                        rn++;
                        rakaa.setVisibility(View.VISIBLE);
                        rakaa.startAnimation(myAnim);
                        if (rn == 1) {
                            rakaa.setText("الركعة الأولى");
                            rakaa.clearAnimation();
                        } else if (rn == 2)
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rakaa.clearAnimation();
                                    rakaa.setText("الركعة الثانية");
                                }
                            }, 30000);
                        else if (rn == 3)
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rakaa.clearAnimation();
                                    rakaa.setText("الركعة الثالثة");
                                }
                            }, 50000);
                        else if (rn == 4)
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rakaa.clearAnimation();
                                    rakaa.setText("الركعة الرابعة");
                                }
                            }, 30000);
                        CheckRakaa();
                        break;
                    }
                } else if (checkResult(result)) {
                    a = true;
                    if (countDownTimer != null) countDownTimer.cancel();
                    if (speech != null) speech.destroy();
                    if (speech != null) {
                        speech = null;
                        Intent cp = new Intent(getApplicationContext(), Read.class);
                        cp.putExtra("pray", nextPray);
                        cp.setAction("c");
                        startActivity(cp);
                        finish();
                    }
                }
            }
        }
        if (!a) {
            CheckRakaa();
        }
    }

    private boolean checkFirstResult(String result) {
        return result.toLowerCase().contains(te0) || result.toLowerCase().contains(te1) ||
                result.toLowerCase().contains(te2) || result.toLowerCase().contains(te13)
                || result.toLowerCase().contains(te14) || result.toLowerCase().contains(te15)
                || result.toLowerCase().contains(te16) || result.toLowerCase().contains(te17)
                || result.toLowerCase().contains(te3) || result.toLowerCase().contains(te4)
                || result.toLowerCase().contains(te9) || result.toLowerCase().contains(te10) ||
                result.toLowerCase().contains(te5) || result.toLowerCase().contains(te11) ||
                result.toLowerCase().contains(te12) || result.toLowerCase().contains(te6) ||
                result.toLowerCase().contains(te7) || result.toLowerCase().contains(te8);
    }

    private boolean checkResult(String result) {
        return result.toLowerCase().contains(t2) || result.toLowerCase().contains(t3)
                || result.toLowerCase().contains(se) || result.toLowerCase().contains(se0)
                || result.toLowerCase().contains(se1) || result.toLowerCase().contains(se2)
                || result.toLowerCase().contains(se3) || result.toLowerCase().contains(se4)
                || result.toLowerCase().contains(se5) || result.toLowerCase().contains(se6)
                || result.toLowerCase().contains(se7) || result.toLowerCase().contains(se8)
                || result.toLowerCase().contains(se9) || result.toLowerCase().contains(se10)
                || result.toLowerCase().contains(se11) || result.toLowerCase().contains(se12)
                || result.toLowerCase().contains(se13) || result.toLowerCase().contains(se14)
                || result.toLowerCase().contains(se15) || result.toLowerCase().contains(se16)
                || result.toLowerCase().contains(se17) || result.toLowerCase().contains(se18)
                || result.toLowerCase().contains(se19) || result.toLowerCase().contains(se20)
                || result.toLowerCase().contains(se21);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @SuppressLint("SetTextI18n")
    private void getWeather(final int action) {
        in_masgedTemp.setText(sp.getString("TempIn", "24"));
        out_masgedTemp.setText(sp.getString("TempOut", "30"));
        tvHumidity.setText(sp.getString("HumOut", "35") + "%");
    }

}
