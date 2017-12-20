package com.newsolution.almhrab.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.newsolution.almhrab.Helpar.VideoControllerView;
import com.newsolution.almhrab.R;

public class VideoViewActivity extends AppCompatActivity implements  SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    private Intent intent;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_view);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        loading = (ProgressBar) findViewById(R.id.loading);
        loading.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        intent=getIntent();
        if (intent.getAction().equals("uri")){
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(this, (Uri)intent.getParcelableExtra("videoURI"));
                player.setOnPreparedListener(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }// Implement SurfaceHolder.Callback

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            player.setDisplay(holder);
            player.prepareAsync();
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    // End SurfaceHolder.Callback
    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        try {
            return player.getCurrentPosition();
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPlaying()){
            pause();
            player.stop();
        }
        player.release();
        //  player=null;
    }
}