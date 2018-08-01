package com.wise.bottombar.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vorlonsoft.android.rate.AppRate;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
      //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.wise.bottombar.sample")));

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               AppRate.with(About.this)
                       .setInstallDays((byte) 0)           // default 10, 0 means install day
                       .setLaunchTimes((byte) 3)           // default 10
                       .monitor();

               AppRate.showRateDialogIfMeetsConditions(About.this);
           }
       },1500);

    }
}
