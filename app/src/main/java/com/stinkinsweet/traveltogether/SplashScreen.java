package com.stinkinsweet.traveltogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Funkies PC on 18-Dec-16.
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
Thread myThread = new Thread()
{
    @Override
    public void run() {
        try {
            sleep(1000);
            Intent intent=new Intent(getApplicationContext(),getting_started.class);
            startActivity(intent);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
};

        myThread.start();
    }
}