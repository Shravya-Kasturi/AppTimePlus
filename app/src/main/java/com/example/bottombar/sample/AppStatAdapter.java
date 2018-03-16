package com.example.bottombar.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Manohar-PC on 29-Jan-18.
 */

public class AppStatAdapter extends ArrayAdapter<EachAppStat> {
    int resource;
    Context c;
    ArrayList<EachAppStat> list;
    public AppStatAdapter(Context c, int resource, ArrayList<EachAppStat> list){
        super(c,resource,list);
        this.c=c;
        this.resource=resource;
        this.list=list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater= LayoutInflater.from(c);
        View v = inflater.inflate(resource,null,true);

        TextView name = (TextView) v.findViewById(R.id.stat_name);
        TextView value = (TextView) v.findViewById(R.id.stat_value);

        EachAppStat item = list.get(position);
        name.setText(item.getStatName());
        value.setText(item.getStatValue());

        return v;
    }
}
