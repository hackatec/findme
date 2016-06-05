package com.example.hernan.findme;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SecondActivity extends Activity {

    protected TextView textView;
    protected ImageView intensidad1;
    protected ImageView intensidad2;
    protected ImageView intensidad3;
    final Handler handler = new Handler();
    private final int MAX_TIME = 1000;
    private final int MAX_DB = -90;
    private final int MIN_DB = -20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.VIBRATE }, 1);

        WifiManager wifiManager;
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        wifiManager.startScan();

        try {
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run() throws InterruptedException {

        textView = (TextView) findViewById(R.id.texto);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int intensidad = leerIntensidad();
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    textView.setText(String.valueOf(intensidad));
                    cambiarIntensidad(intensidad);

                    if (Math.abs(intensidad) > 0) {

                        v.vibrate(getMillis(intensidad));
                    }
                    //ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, getMillis(intensidad));
                    //toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, getMillis(intensidad));


                    handler.postDelayed(this,2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        handler.post(runnable);

    }


    public int leerIntensidad() throws InterruptedException {
        List<ScanResult> redes;
        WifiManager wifiManager;
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();

        redes = wifiManager.getScanResults();
        int level = 0;
        for(ScanResult red : redes)
        {
            if (red.SSID.equals("Findme"))
            {
                //Mostrar en pantalla el red.level
                level = red.level;
                break;
            }
        }

        return level;

    }

    public int getMillis(int intensity) {
        int millis = 0;
        double intensityAbs = Math.abs(intensity);
        if (intensityAbs < (MAX_TIME)) {
            double maxDBAbs = Math.abs(MAX_DB);
            double minDBAbs = Math.abs(MIN_DB);
            double diff = maxDBAbs + minDBAbs - intensityAbs;
            double diffAvg = (diff / maxDBAbs);
            millis = (int) (MAX_TIME * diffAvg);
            if (millis > MAX_TIME) {
                millis = MAX_TIME;
            }
        }
        return millis;
    }

    public int getMillis(int intensity) {
        double intensityPercent = getIntensityPercent(intensity);
        int millis = (int) (MAX_TIME * intensityPercent);
        if (millis > MAX_TIME) {
            millis = MAX_TIME;
        }
        return millis;
    }

    public double getIntensityPercent(int intensity) {
        double diffAvg = 0;
        double intensityAbs = Math.abs(intensity);
        if (intensityAbs < (MAX_TIME)) {
            double maxDBAbs = Math.abs(MAX_DB);
            double minDBAbs = Math.abs(MIN_DB);
            double diff = maxDBAbs + minDBAbs - intensityAbs;
            diffAvg = (diff / maxDBAbs);
        }
        return diffAvg;
    }

    public void cambiarIntensidad(){
        intensidad1 = (ImageView) findViewById(R.id.intensidad1);
        intensidad2 = (ImageView) findViewById(R.id.intensidad2);
        intensidad3 = (ImageView) findViewById(R.id.intensidad3);

        int i = getIntensityPercent(int intensity);

        if(i < 25){
            intensidad1.setVisibility(View.INVISIBLE);
            intensidad2.setVisibility(View.INVISIBLE);
            intensidad3.setVisibility(View.INVISIBLE);
        }else if(i > 25){
            intensidad1.setVisibility(View.VISIBLE);
            intensidad2.setVisibility(View.INVISIBLE);
            intensidad3.setVisibility(View.INVISIBLE);
        }else if(i > 50 && i < 75){
            intensidad1.setVisibility(View.VISIBLE);
            intensidad2.setVisibility(View.VISIBLE);
            intensidad3.setVisibility(View.INVISIBLE);
        }else if(i > 75){
            intensidad1.setVisibility(View.VISIBLE);
            intensidad2.setVisibility(View.VISIBLE);
            intensidad3.setVisibility(View.VISIBLE);
        }
    }
}