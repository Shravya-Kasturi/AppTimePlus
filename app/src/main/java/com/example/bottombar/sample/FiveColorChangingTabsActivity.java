package com.example.bottombar.sample;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;

public class FiveColorChangingTabsActivity extends AppCompatActivity {
    private TextView messageView;
    Fragment fragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_changing_tabs);
        messageView = (TextView) findViewById(R.id.messageView);
        fragmentManager = getSupportFragmentManager();
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                switch (tabId){
                    case R.id.homes:
                        fragment = new HomeFrag();
                        break;

                    case R.id.stats:
                        fragment=new StatFrag();
                        break;

                    case R.id.views:
                        fragment=new ViewFrag();
                        break;

                    case R.id.settings:
                        fragment=new SettingsFrag();
                        break;

                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame, fragment).commit();
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                Toast.makeText(FiveColorChangingTabsActivity.this,"Selected the same tab again",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(FiveColorChangingTabsActivity.this,FiveColorChangingTabsActivity.class));
    }
}