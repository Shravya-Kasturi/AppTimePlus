package com.wise.bottombar.sample;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import pl.droidsonroids.gif.GifTextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    TextView splashtext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Intent decideIntent;
    MediaPlayer mp;
    GifTextView splashgif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mp = MediaPlayer.create(this, R.raw.splashsound);
        mAuth = FirebaseAuth.getInstance();
        splashtext=(TextView)findViewById(R.id.splashtext);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    decideIntent=new Intent(SplashScreen.this, FiveColorChangingTabsActivity.class);
                }
                else{
                    decideIntent = new Intent(SplashScreen.this, MainActivity.class);

                }
            }
        };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.start();
                splashtext.setText("APP TIME PLUS");
            }
        },500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(decideIntent);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        super.onStop();
        mp.stop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }
}