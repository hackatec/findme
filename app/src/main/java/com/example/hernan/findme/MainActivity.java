package com.example.hernan.findme;

import android.Manifest;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected TextView textView;
    final Handler handler = new Handler();
    private final int MAX_TIME = 1000;
    private final int MAX_DB = -50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.VIBRATE }, 1);

        WifiManager wifiManager;
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        wifiManager.startScan();

    }

    public void run(View view) throws InterruptedException {

            textView = (TextView) findViewById(R.id.texto);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        int intensidad = leerIntensidad();
                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        textView.setText(String.valueOf(intensidad));

                        if ((intensidad * -1) < 10) {
                            v.vibrate(2000);
                        }
                        else if ((intensidad * -1) < 30) {
                            v.vibrate(1000);
                        }
                        else if ((intensidad * -1) < 50) {
                            v.vibrate(500);
                        }
                        else {

                        }

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

        redes = wifiManager.getScanResults();
        int level = 0;
        for(ScanResult red : redes)
        {
            if (red.SSID.equals("Hackatec-2016"))
            {
                //Mostrar en pantalla el red.level
                level = red.level;
                break;
            }
        }

        return level;

    }

    public double getMillis(int intensity) {
        int millis = 0;
        double intensityAbs = Math.abs(intensity);
        if (intensityAbs < MAX_TIME) {
            double maxDBAbs = Math.abs(MAX_DB);
            double diff = maxDBAbs - intensityAbs;
            double diffAvg = (diff / maxDBAbs);
            millis = (int) (MAX_TIME * diffAvg);
        }
        return millis;
    }
}

