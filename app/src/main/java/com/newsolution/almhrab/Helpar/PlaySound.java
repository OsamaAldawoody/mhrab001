package com.newsolution.almhrab.Helpar;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

public class PlaySound {
    static MediaPlayer mp;
    static Vibrator v;
    static Boolean sound; // false off true on
    private static String soundName;
    private static Uri soundURI;

    public static Boolean getSound() {
        return sound;
    }

    private static void setSoundFromSP(Context c, String soundName, int i) {
        AudioManager mAudioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FX_KEY_CLICK);

        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        PlaySound.sound = true;//sp.getBoolean("sound_on", true);
        if (i == 0) {
            mp = MediaPlayer.create(c, c.getResources().getIdentifier(soundName, "raw", c.getPackageName()));
        } else {
            mp = MediaPlayer.create(c, Uri.parse(soundName));
        }
    }

    public static void play(Context c, String soundName) {
//        stop(c);
        setSoundFromSP(c, soundName, 0);
        SharedPreferences sp = c.getSharedPreferences(Utils.PREFS, c.MODE_PRIVATE);
        sp.edit().putInt("actionPlay", 0).commit();
        sp.edit().putString("sound", soundName).commit();
        Log.i("**** close: sound", sp.getString("sound", " ll"));
        if (sound) {
            try {


                int resId = c.getResources().getIdentifier(soundName, "raw", c.getPackageName());
                soundURI = Uri.parse("android.resource://" + c.getPackageName() + "/" + resId);
                mp = MediaPlayer.create(c, soundURI);

                if (!mp.isPlaying())
                    mp.start();


            } catch (Exception e) {
                if (mp != null) {
                    mp = null;
                }

                e.printStackTrace();

            }
            if (mp != null) {
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (mp != null) {
                            mp.release();
                            mp = null;
                        }
                    }
                });
            }
        }
    }

    public static void playSDCard(Context c, String soundName, String soundText) {
//        stop(c);
        setSoundFromSP(c, soundName, 1);
        SharedPreferences sp = c.getSharedPreferences(Utils.PREFS, c.MODE_PRIVATE);
        sp.edit().putInt("actionPlay", 1).commit();
        sp.edit().putString("sound", soundName).commit();
        Log.i("****: sound", Uri.parse(soundName) + " **");
        if (sound) {
            try {
                mp = MediaPlayer.create(c, Uri.parse(soundName));
//                if (!mp.isPlaying())
                mp.start();
            } catch (Exception e) {
                if (mp != null) {
                    mp = null;
                }
                e.getMessage();
                play(c, soundText);
            }

        }
    }

    public static void vibrate(Context c) {
        if (v == null)
            v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public static void stop(Context c) {
        SharedPreferences sp = c.getSharedPreferences(Utils.PREFS, c.MODE_PRIVATE);
        soundName = sp.getString("sound", "dhiabi");
        int action = sp.getInt("actionPlay", 0);
        if (mp == null)
//            return;
            if (action == 0)
                mp = MediaPlayer.create(c, c.getResources().getIdentifier(soundName, "raw", c.getPackageName()));
            else mp = MediaPlayer.create(c, Uri.parse(soundName));

        if (mp.isPlaying()) {
            mp.stop();
            mp.release();

            mp = null;

        }
    }

    public static Boolean isPlay(Context c) {
        SharedPreferences sp = c.getSharedPreferences(Utils.PREFS, c.MODE_PRIVATE);
        soundName = sp.getString("sound", "dhiabi");

        boolean isPlay = false;
        if (mp != null)
            isPlay = mp.isPlaying();

        return isPlay;
    }
}
