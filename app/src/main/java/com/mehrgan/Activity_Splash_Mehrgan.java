package com.mehrgan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import pl.droidsonroids.gif.GifImageView;

public class Activity_Splash_Mehrgan extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_mehrgan);

        GifImageView gifImage = findViewById(R.id.gifImage);
        gifImage.setImageResource(R.drawable.gif);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, Activity_Main_Mehrgan.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }
}
