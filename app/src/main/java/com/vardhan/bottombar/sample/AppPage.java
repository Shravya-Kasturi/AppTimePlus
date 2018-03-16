package com.vardhan.bottombar.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AppPage extends AppCompatActivity {

    ListView l;
    ProgressBar progressBar;
    public static final String MyPREFERENCES = "Limits" ;
   static int lim = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_page);
        l=(ListView)findViewById(R.id.app_stats);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        ImageView imageView=(ImageView)findViewById(R.id.imageView2);
        imageView.setImageDrawable(HomeFrag.Appicon);
        TextView textView=(TextView)findViewById(R.id.app_title);
        textView.setText(HomeFrag.Appname);
        int t=HomeFrag.pr;
        progressBar.setProgress(t);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppPage.this);
                    builder.setTitle("Set Limit");
                    final EditText input = new EditText(AppPage.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);
                    builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lim = Integer.parseInt(input.getText().toString());
                            editor.putInt(HomeFrag.packn,lim);
                            editor.commit();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

            }
        });

        ArrayList<EachAppStat> appItemList = new ArrayList<EachAppStat>();
        appItemList.add(new EachAppStat("Set Limit:",String.valueOf(sharedpreferences.getInt(HomeFrag.packn,0))+" min"));
        AppStatAdapter adapter = new AppStatAdapter(AppPage.this, R.layout.app_stat_item,appItemList);
        l.setAdapter(adapter);
    }
}