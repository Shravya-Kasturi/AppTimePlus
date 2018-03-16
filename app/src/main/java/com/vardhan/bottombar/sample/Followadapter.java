package com.vardhan.bottombar.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vardhan on 1/30/2018.
 */

public class Followadapter extends ArrayAdapter{
   Context c;
   int res;
   ArrayList<String> as;
   List<String> fr;
    FirebaseStorage storage;
    StorageReference storageReference;


    public Followadapter(@NonNull Context context, int resource, ArrayList as, List<String> fr) {
        super(context, resource,as);
        this.c=context;
        this.res=resource;
        this.as=as;
        this.fr=fr;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(res, null, true);

        final ImageView logo = (ImageView) v.findViewById(R.id.fimage);
        TextView name = (TextView) v.findViewById(R.id.fname);

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
        name.setText(as.get(position));
        return v;
    }
}
