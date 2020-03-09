package com.esq.openmediafile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class PlaySongActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MEDIA_FILE = "SelectedMediaFile";
    private final String TAG = "PlaySongActivity";

    TextView nameOfSongPlaying;
    Button pause;
    Button stop;
    Button resume;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        setUpMedia();
        nameOfSongPlaying = findViewById(R.id.nameOfSong);
        stop = findViewById(R.id.stopSong);
        stop.setOnClickListener(this);
        pause = findViewById(R.id.pauseSong);
        pause.setOnClickListener(this);
        resume = findViewById(R.id.resumeSong);
        resume.setOnClickListener(this);
    }

    private void setNameOfSongPlaying(){
        String str = "";
        //nameOfSongPlaying.setText();
    }

    private void setUpMedia(){
        mediaPlayer = new MediaPlayer();
        Intent intent = getIntent();
        String stringUri = intent.getStringExtra(MEDIA_FILE);
        Uri uri = Uri.parse(stringUri);
        if(uri != null){
            Log.i(TAG, "Uri has a data");
        }else if(uri == null){
            Log.i(TAG, "Uri is null");
        }

        String media_path = uri.getPath() ;
        Log.i(TAG, "Path is " + media_path);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            try {
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();//Used to play local media files
                mediaPlayer.start();
                Toast.makeText(PlaySongActivity.this, "Playing Mp3", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlayer();
                }
            });


    }

    //Handle click events
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.resumeSong:
                onClickResume();
                break;
            case R.id.stopSong:
                onClickStop();
                break;
            case R.id.pauseSong:
                onClickPause();
             break;
        }
    }


    public void onClickPause(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            Log.i(TAG,  "onClickResume: Media is paused");
            Toast.makeText(PlaySongActivity.this, "Media is paused", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickResume(){
        if(mediaPlayer != null){
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }else{
                Log.i(TAG,  "onClickResume: Media already playing");
                Toast.makeText(PlaySongActivity.this, "Media already playing", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickStop(){
        stopPlayer();
    }

    public void stopPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            Log.i(TAG,  "stopPlayer: Media is stopped");
            Toast.makeText(PlaySongActivity.this, "Mp3 ended", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
