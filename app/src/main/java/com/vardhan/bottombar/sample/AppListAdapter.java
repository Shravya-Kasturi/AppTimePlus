package com.vardhan.bottombar.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class AppListAdapter extends ArrayAdapter<EachAppItem> {
    int resource;
    Context c;
    List<EachAppItem> list;
    List<String> fr;
    FirebaseStorage storage;
    StorageReference storageReference;
    int lr;

    public AppListAdapter(Context c, int resource, List<EachAppItem> list, List<String> fr, int lr){
        super(c,resource,list);
        this.c=c;
        this.resource=resource;
        this.list=list;
        this.fr=fr;
        this.lr=lr;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater= LayoutInflater.from(c);
        View v = inflater.inflate(resource,null,true);

        final ImageView logo = (ImageView) v.findViewById(R.id.app_logo);
        TextView name = (TextView) v.findViewById(R.id.app_name);
        TextView time = (TextView) v.findViewById(R.id.app_time);
        TextView badge=(TextView)v.findViewById(R.id.bad);
        ImageView picL=(ImageView)v.findViewById(R.id.picL);

        EachAppItem item = list.get(position);
       // logo.setImageResource(item.getLogo());
        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(storageReference.child("images").child(fr.get(position)))
                .asBitmap().centerCrop().into(new BitmapImageViewTarget(logo) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                logo.setImageDrawable(circularBitmapDrawable);
            }
        });
        name.setText(item.getName());
        time.setText(item.getTime());
        badge.setText(item.getBad());
        picL.setImageResource(lr);
        return v;
    }
}
