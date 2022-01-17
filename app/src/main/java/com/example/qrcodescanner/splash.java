package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splach_screen);

        new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Intent intent = new Intent(splash.this, MainActivity.class);
                  startActivity(intent);
                  finish(); }
              },3000);



    }
}