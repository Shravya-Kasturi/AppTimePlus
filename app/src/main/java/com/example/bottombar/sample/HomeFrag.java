/*
 * BottomBar library for Android
 * Copyright (c) 2016 Iiro Krankka (http://github.com/roughike).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bottombar.sample;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.example.bottombar.sample.AppPage.lim;

public class HomeFrag extends Fragment {

    ListView listview;
    static String Appname;
    static Drawable Appicon;
    static String packn;
    List<String> packnames;
    ArrayList<String> timew;
    String PackName;
    String timealpsed,lastseen;

    ArrayList<Integer> challt;
    long tot=0;
    int deg=0;
    ArrayList<String> sysapps;
    ArrayList<String> datatime;
    TextView alr;
    ArrayList<String> datap;
    ImageView imageView;
    ArrayList<String> badge;
    DatabaseReference databaseReference;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences1;
    SharedPreferences.Editor editor1;
    static int pr=0;
    long totalt=0;
    List<UsageStats> queryUsageStats;
    List<UsageStats> queryUsageStatsWeekly;
    @SuppressLint("ValidFragment")
    HomeFrag(){}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_frag,container,false);
        imageView=(ImageView)view.findViewById(R.id.needle);
        final String MyPREFERENCES = "Weekly" ;
        final String MyPREFERENCES1 = "Monthly" ;
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences1 = getActivity().getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        alr=(TextView)view.findViewById(R.id.comment);
        editor = sharedpreferences.edit();
        editor1 = sharedpreferences1.edit();
        timew=new ArrayList<>();
        challt=new ArrayList<Integer>();
        challt.add(360);
        challt.add(360);
        challt.add(360);
        challt.add(360);
        challt.add(360);
        challt.add(360);
        challt.add(360);


        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        badge=new ArrayList<String>();
        packnames=new ArrayList<String>();
        datap=new ArrayList<String>();
        datap.add("com.facebook.katana");
        datap.add("com.whatsapp");
        datap.add("com.instagram.android");
        datap.add("com.twitter.android");
        datap.add("com.android.chrome");
        datap.add("com.bsb.hike");
        datap.add("com.google.android.youtube");
        datatime=new ArrayList<String>();
        for(int q=0;q<7;q++) {
            datatime.add("NA");
            timew.add(String.valueOf(0));
        }
        sysapps=new ArrayList<String>();
        sysapps.add("com.google.android.youtube");
        sysapps.add("com.android.chrome");
        sysapps.add("com.google.android.apps.photos");
        sysapps.add("com.google.android.apps.messaging");
        sysapps.add("com.android.camera");
        sysapps.add("com.google.android.calculator");
        sysapps.add("com.google.android.calendar");
        sysapps.add("com.google.android.gm");
        sysapps.add("com.google.android.music");
        sysapps.add("com.google.android.apps.maps");
        sysapps.add("com.google.android.contacts");
        Intent se=new Intent(getActivity(),BlockServ.class);
        getActivity().startService(se);
        @SuppressLint("WrongConstant") final UsageStatsManager mUsageStatsManager=(UsageStatsManager)getContext().getSystemService("usagestats");

        final long currentTime = System.currentTimeMillis();
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        final long beginTime = cal.getTimeInMillis();
        queryUsageStats=mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, cal.getTimeInMillis(), currentTime);
        if(queryUsageStats==null||queryUsageStats.isEmpty())
        {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }


        final ListView userInstalledApps = (ListView)view.findViewById(R.id.apps_list);

        final List<AppList> installedApps = getInstalledApps();
        Date now = new Date();
        Calendar calnow = Calendar.getInstance();
        calnow.setTime(now);
        editor.putString(String.valueOf(calnow.get(Calendar.DAY_OF_WEEK)),String.valueOf(tot));
        Log.d("abc",String.valueOf(calnow.get(Calendar.DAY_OF_WEEK))+" "+String.valueOf(calnow.get(Calendar.DAY_OF_MONTH))+" "+String.valueOf(tot));
        editor.commit();
        editor1.putString(String.valueOf(calnow.get(Calendar.DAY_OF_MONTH)),String.valueOf(tot));
        editor1.commit();
        tot=tot/360;
        if(tot>=90){
            deg=(int)((tot-90));
            if(tot>=90)
                deg=180;
        }
        else{
            deg=(int)((90-tot)*-1);
        }

        RotateAnimation rotateAnim = new RotateAnimation(0.0f, deg,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 1f);
        rotateAnim.setDuration(2500);
        rotateAnim.setFillAfter(true);
        imageView.setAnimation(rotateAnim);
        rotateAnim.start();

        if(tot<=50){
            alr.setText("Poor Zone");
            alr.setTextColor(Color.BLUE);
        }
        else if(tot>=50 && tot<=130){
            alr.setText("Safe Zone");
            alr.setTextColor(Color.GREEN);
        }
        else{
            alr.setText("Danger Zone");
            alr.setTextColor(Color.RED);
        }

        AppAdapter installedAppAdapter = new AppAdapter(getActivity(), installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);
        userInstalledApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                AppList a=installedApps.get(pos);
                Appname=a.getName();
                Appicon=a.getIcon();
                pr=(Integer.parseInt(a.getTime().substring(12,14))*60)+Integer.parseInt(a.getTime().substring(15,17));
                packn=a.getPack();
                Intent in = new Intent(getActivity(), AppPage.class);
                startActivity(in);

            }
        });
        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("times").setValue(datatime);

        final long currentTimeWeekly = System.currentTimeMillis();
        final Calendar calWeekly = Calendar.getInstance();
        calWeekly.add(Calendar.DAY_OF_WEEK, -7);
        final long beginTimeWeekly = calWeekly.getTimeInMillis();
        queryUsageStatsWeekly=mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, cal.getTimeInMillis(), currentTimeWeekly);
        if(queryUsageStatsWeekly==null||queryUsageStatsWeekly.isEmpty())
        {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        if(calWeekly.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReference.removeEventListener(this);
                    if(dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("sunday").getValue(String.class).equals("0"))
                    {
                        getInstalledAppsWeekly();
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("sunday").setValue("1");
                    }
                    else{

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        else{
            databaseReference.child(FirebaseAuth.getInstance().getUid()).child("sunday").setValue("0");
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private List<AppList> getInstalledApps() {
        List<AppList> res = new ArrayList<AppList>();
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
        Collections.sort(packs,new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo s1, PackageInfo s2) {
                return s1.applicationInfo.loadLabel(getActivity().getPackageManager()).toString().compareToIgnoreCase(s2.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
            }
        });
        for (int j = 0; j < packs.size(); j++) {

            PackageInfo p = packs.get(j);
           if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                PackName=p.packageName;
                timealpsed="NA";
                totalt=0;
                lastseen="NA";
                long s=0,m=0,h=0;
                for(UsageStats i: queryUsageStats) {
                    if (i.getPackageName().equals(PackName)) {
                        DateFormat mDateFormat = new SimpleDateFormat();
                        long seconds = i.getTotalTimeInForeground();
                        totalt+=seconds;
                        long seconds1 = i.getLastTimeStamp();

                        lastseen= mDateFormat.format(new Date(seconds1));
                    }
                }
                totalt = totalt / 1000;
                tot+=totalt;
                s = totalt % 60;
                m = (totalt / 60) % 60;
                h = (totalt / (60 * 60)) % 24;
               timealpsed= String.format("%02d:%02d:%02d", h, m, s);
               for(int q=0;q<7;q++){
                   if(datap.get(q).equals(PackName)){
                       datatime.set(q,timealpsed);
                   }
               }

                    res.add(new AppList(appName, "Total Time: "+timealpsed,"Last Seen: "+lastseen,icon,PackName));

            }
            else{
               for(String k:sysapps)
               {
                   if(k.equals(p.packageName)){
                       String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                       Drawable icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                       PackName=p.packageName;
                       timealpsed="";
                       totalt=0;
                       lastseen="NA";
                       long s=0,m=0,h=0;
                       for(UsageStats i: queryUsageStats) {
                           if (i.getPackageName().equals(PackName)) {
                               DateFormat mDateFormat = new SimpleDateFormat();
                               long seconds = i.getTotalTimeInForeground();
                               totalt+=seconds;
                               long seconds1 = i.getLastTimeStamp();

                               lastseen= mDateFormat.format(new Date(seconds1));
                           }
                       }
                       totalt = totalt / 1000;
                       tot+=totalt;
                       s = totalt % 60;
                       m = (totalt / 60) % 60;
                       h = (totalt / (60 * 60)) % 24;
                       timealpsed= String.format("%02d:%02d:%02d", h, m, s);
                       for(int q=0;q<7;q++){
                           if(datap.get(q).equals(PackName)){
                               datatime.set(q,timealpsed);
                           }
                       }
                       res.add(new AppList(appName, "Total Time: "+timealpsed,"Last Seen: "+lastseen,icon,PackName));


                   }
               }
           }
        }
        return res;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getInstalledAppsWeekly() {
        List<AppList> res = new ArrayList<AppList>();
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
        for (int j = 0; j < packs.size(); j++) {

            PackageInfo p = packs.get(j);

                for(int k=0;k<7;k++)
                {
                    if(datap.get(k).equals(p.packageName)){
                        String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                        PackName=p.packageName;
                        timealpsed="";
                        totalt=0;
                        long s=0,m=0,h=0;
                        for(UsageStats i: queryUsageStatsWeekly) {
                            if (i.getPackageName().equals(PackName)) {
                                long seconds = i.getTotalTimeInForeground();
                                totalt+=seconds;
                            }
                        }
                        totalt = totalt / (1000*60);
                        Log.d("qwe",datap.get(k)+String.valueOf(k)+String.valueOf(totalt));
                        if(challt.get(k)>=totalt){
                            final int finalK = k;
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    databaseReference.removeEventListener(this);
                                    String s=dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("badges").child(String.valueOf(finalK)).getValue(String.class);
                                    databaseReference.child(FirebaseAuth.getInstance().getUid()).child("badges").child(String.valueOf(finalK)).setValue(String.valueOf(Integer.parseInt(s)+1));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                }
            }
        }
    }


    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
    }