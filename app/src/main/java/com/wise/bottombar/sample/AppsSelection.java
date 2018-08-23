package com.wise.bottombar.sample;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AppsSelection extends AppCompatActivity {

    ListView listView;
    ArrayList<String> sysapps;
    android.support.v7.widget.Toolbar th;
    ArrayList<AppSelectView> s;
    AppSelectView a;
    static ArrayList<String> uncheckedApps;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_selection);
        uncheckedApps=new ArrayList<String>();
        listView = (ListView) findViewById(R.id.appselectlv);
        b=(Button)findViewById(R.id.appseldone);
        th=(android.support.v7.widget.Toolbar)findViewById(R.id.appseltb);
        th.setTitle("Select Apps");
        th.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(th);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences prefs = getSharedPreferences("AppSel", MODE_PRIVATE);
        uncheckedApps.clear();
        Map<String,?> keys = prefs.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            uncheckedApps.add(entry.getKey());
        }


        sysapps = new ArrayList<String>();
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
        s=new ArrayList<AppSelectView>();
        List<AppList> res = new ArrayList<AppList>();
        List<PackageInfo> packs = AppsSelection.this.getPackageManager().getInstalledPackages(0);

        for (int j = 0; j < packs.size(); j++) {

            PackageInfo p = packs.get(j);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(AppsSelection.this.getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(AppsSelection.this.getPackageManager());
                a=new AppSelectView(appName,icon,p.packageName);
                s.add(a);
            }
            else {
                for (String k : sysapps) {
                    if (k.equals(p.packageName)) {
                        String appName = p.applicationInfo.loadLabel(AppsSelection.this.getPackageManager()).toString();
                        Drawable icon = p.applicationInfo.loadIcon(AppsSelection.this.getPackageManager());
                        a=new AppSelectView(appName,icon,p.packageName);
                        s.add(a);
                    }
                }
            }

        }
        Collections.sort(s, new Comparator<AppSelectView>() {
            @Override
            public int compare(AppSelectView s1, AppSelectView s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });
        AppSelectAdapter customAdapter = new AppSelectAdapter(getApplicationContext(), s);
        listView.setAdapter(customAdapter);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences("AppSel", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                for(String i:uncheckedApps)
                {
                    editor.putString(i,i);
                }
                editor.apply();
                editor.commit();
                onBackPressed();
            }
        });
    }
        private boolean isSystemPackage(PackageInfo pkgInfo) {
            return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
        }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}