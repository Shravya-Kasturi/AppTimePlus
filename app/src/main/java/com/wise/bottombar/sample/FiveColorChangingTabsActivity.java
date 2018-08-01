package com.wise.bottombar.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class FiveColorChangingTabsActivity extends AppCompatActivity {
    private TextView messageView;
    Fragment fragment;
    FragmentManager fragmentManager;
    int flag = 0;

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

                switch (tabId) {
                    case R.id.homes:
                        flag = 1;
                        fragment = new HomeFrag();
                        break;

                    case R.id.stats:
                        flag = 0;
                        fragment = new StatFrag();
                        break;

                    case R.id.views:
                        flag = 0;
                        fragment = new ViewFrag();
                        break;

                    case R.id.settings:
                        flag = 0;
                        fragment = new SettingsFrag();
                        break;

                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame, fragment).addToBackStack("kat").commit();


            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                Toast.makeText(FiveColorChangingTabsActivity.this, "Selected the same tab again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (flag == 1) {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                        }
                    }).create().show();
        }
    }
}