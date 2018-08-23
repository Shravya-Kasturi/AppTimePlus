package com.wise.bottombar.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AppSelectAdapter extends BaseAdapter {
    ArrayList<AppSelectView> names;
    Context context;
    LayoutInflater inflter;
    String value;

    public AppSelectAdapter(Context context, ArrayList<AppSelectView> names) {
        this.context = context;
        this.names = names;
        inflter = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.appselectxml, null);
        final CheckedTextView simpleCheckedTextView = (CheckedTextView) view.findViewById(R.id.appselectitem);
        ImageView img=(ImageView)view.findViewById(R.id.appselimg);
        simpleCheckedTextView.setText(names.get(position).getName());
        img.setImageDrawable(names.get(position).getIcon());
        if(AppsSelection.uncheckedApps.contains(names.get(position).getPck()))
        {
            simpleCheckedTextView.setCheckMarkDrawable(0);
            simpleCheckedTextView.setChecked(false);
        }
        else
        {
            simpleCheckedTextView.setCheckMarkDrawable(R.mipmap.checkmark);
            simpleCheckedTextView.setChecked(true);
        }
        simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleCheckedTextView.isChecked()) {
                    //Toast.makeText(context,names.get(position).getPck(),Toast.LENGTH_SHORT).show();
                    if(!AppsSelection.uncheckedApps.contains(names.get(position).getPck()))
                    AppsSelection.uncheckedApps.add(names.get(position).getPck());
                    value = "un-Checked";
                   // Toast.makeText(context,names.get(position).getName(),Toast.LENGTH_SHORT).show();
                    simpleCheckedTextView.setCheckMarkDrawable(0);
                    simpleCheckedTextView.setChecked(false);
                } else {
                    //Toast.makeText(context,names.get(position).getPck(),Toast.LENGTH_SHORT).show();
                    if(AppsSelection.uncheckedApps.contains(names.get(position).getPck()))
                    AppsSelection.uncheckedApps.remove(names.get(position).getPck());
                    value = "Checked";
                   // Toast.makeText(context,names.get(position).getName(),Toast.LENGTH_SHORT).show();
                    simpleCheckedTextView.setCheckMarkDrawable(R.mipmap.checkmark);
                    simpleCheckedTextView.setChecked(true);
                }

            }
        });
        return view;
    }
}