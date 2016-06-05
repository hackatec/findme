package com.example.hernan.findme;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    protected TextView textView;
    final Handler handler = new Handler();
    private final int MAX_TIME = 1000;
    private final int MAX_DB = -90;
    private final int MIN_DB = -20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.VIBRATE }, 1);

    }

    public void pasarActividad(View view) {
        EditText tesoro = (EditText) findViewById(R.id.tesoro);
        Intent siguiente = new Intent(this, SecondActivity.class);
        siguiente.putExtra("tesoro", tesoro.getText().toString());
        startActivity(siguiente);
    }

}