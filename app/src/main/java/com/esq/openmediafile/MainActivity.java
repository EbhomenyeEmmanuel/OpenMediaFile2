package com.esq.openmediafile;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final int READ_MEDIA_CODE = 10;
    private boolean readPDFPermissionsGranted;

    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        getReadPDFPermission();
        setUpFab();
    }
    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_MEDIA_CODE && resultCode == RESULT_OK && data != null) {

                Log.i(TAG, "onActivityResult: Permissions has been granted");
               Uri selectedMediaFile = data.getData();//Get data from what the user chose
                Intent intent = new Intent(this, PlaySongActivity.class);
                intent.putExtra(PlaySongActivity.MEDIA_FILE, selectedMediaFile.toString());
                String media_path = selectedMediaFile.getPath() ;
                //Check if the right file was chosen i.e mp3
                if (media_path.contains(".mp3") || media_path.contains(".ogg") || media_path.contains(".m4a")|| media_path.contains(".wav")) {
                    startActivity(intent);
                }else{
                    Log.i(TAG, "Please select a mp3 file");
                    Log.i(TAG, "You selected " + media_path);
                    Toast.makeText(MainActivity.this, "You selected " + media_path, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Please select a mp3 file", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "onActivityResult: PlaySongActivity Started");

        }
    }

    private void setUpFab(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readPDFPermissionsGranted) {
                    Log.i(TAG, "setUpFab: FAB is clicked");
                    Intent PDFIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    PDFIntent.setType("*/*"); //Set type of data
                    PDFIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(PDFIntent, "Select Your Media File "), READ_MEDIA_CODE);
                    Log.i(TAG, "setUpFab: startActivityForResult method is called");
                }else{
                    Toast.makeText(MainActivity.this, "Read permissions has not been granted", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "setUpFab: startActivityForResult method can't be called, Please grant permissions");
                }
            }
        });
    }

    //Get permission to read PDF in the user's device
    private void getReadPDFPermission() {
        Log.i(TAG, "getReadPDFPermission: getting PDF permissions");
        String [] permissions = {READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                readPDFPermissionsGranted = true;
                Log.i(TAG, "getReadPDFPermission: Read permissions has been granted");
        } else {
            readPDFPermissionsGranted = false;
                Log.i(TAG, "getReadPDFPermission: Read permissions has not been granted");
                ActivityCompat.requestPermissions(MainActivity.this, permissions , READ_MEDIA_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
