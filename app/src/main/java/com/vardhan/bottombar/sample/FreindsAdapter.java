package com.vardhan.bottombar.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class FreindsAdapter extends ArrayAdapter<Sett>
{
    Context c;
    int resource;
    ArrayList<Sett> list;

    public FreindsAdapter(@NonNull Context c, int resource, ArrayList<Sett> list) {
        super(c,resource,list);
        this.c=c;
        this.resource=resource;
        this.list=list;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater= LayoutInflater.from(c);
        View v = inflater.inflate(resource,null,true);
        ImageView logo = (ImageView) v.findViewById(R.id.fimage);
        TextView name = (TextView) v.findViewById(R.id.fname);
        Sett item = list.get(position);
        logo.setImageResource(item.getLogo());
        name.setText(item.getName());
        return v;
    }


}
