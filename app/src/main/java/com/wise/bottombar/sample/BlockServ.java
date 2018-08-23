package com.wise.bottombar.sample;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BlockServ extends Service {
    String name;
    public static final String MyPREFERENCES = "Limits";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    long seconds;
    int minutes;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {



        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
         sharedpreferences= getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor  = sharedpreferences.edit();

    }

    public BlockServ(){}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

       name= getForegroundApp();
        //Log.d("abc",name);
        Map<String, ?> allEntries = sharedpreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
           if(name.equals(entry.getKey()) && minutes > (Integer) entry.getValue())
           {
               showHomeScreen();
           }
           }


       onDestroy();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent restartService = new Intent("RestartService");
       // Log.d("abc","exiting");
        sendBroadcast(restartService);
    }

    public String getForegroundApp() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
            final long currentTime = System.currentTimeMillis();
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -1);
            final long beginTime = cal.getTimeInMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST, cal.getTimeInMillis(), currentTime);
            if (appList != null && appList.size() > 0) {
                TreeMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    seconds=mySortedMap.get(mySortedMap.lastKey()).getTotalTimeInForeground();
                    minutes=(int)seconds/(1000*60);
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        return currentApp;
    }

    public boolean showHomeScreen(){
        /*Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(startMain);*/

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.wise.bottombar.sample");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
        return true;
    }
}
