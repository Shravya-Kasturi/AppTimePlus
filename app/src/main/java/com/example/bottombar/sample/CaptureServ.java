package com.example.bottombar.sample;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Map;

public class CaptureServ extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public CaptureServ(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);



        onDestroy();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent restartService = new Intent("RestartService");
        sendBroadcast(restartService);
    }

}
