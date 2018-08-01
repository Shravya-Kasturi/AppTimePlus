package com.wise.bottombar.sample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FriendProfile extends AppCompatActivity {
    private Button btnChoose, btnUpload;
    private ImageView imageView;

    android.support.v7.widget.Toolbar th;
    ListView listView;
    ArrayList<String> badg;
    ArrayList<Integer> piclogo;
    ArrayList<Sett> ar;
    String un;
    Bitmap bitmap;
    private Bitmap mBitmap;
    DatabaseReference databaseReference;
    Button blockB;
    String fbl;
    FirebaseStorage storage;
    StorageReference storageReference;
    String fu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        listView = (ListView) findViewById(R.id.mybadgesf);
        badg = new ArrayList<String>();
        ar = new ArrayList<Sett>();
        piclogo = new ArrayList<Integer>();
        blockB=(Button)findViewById(R.id.blockbf);
        piclogo.add(R.mipmap.fbbadge);
        piclogo.add(R.mipmap.whatsappbadge);
        piclogo.add(R.mipmap.instabadge);
        piclogo.add(R.mipmap.twitterbadge);
        piclogo.add(R.mipmap.chromebadge);
        piclogo.add(R.mipmap.hikebadge);
        piclogo.add(R.mipmap.utubebadge);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                for (int k = 0; k < 7; k++) {
                    badg.add("     " + dataSnapshot.child(FrList.frkey).child("badges").child(String.valueOf(k)).getValue(String.class));
                    Sett i = new Sett(piclogo.get(k), badg.get(k));
                    ar.add(i);
                }
                listView.setAdapter(new FreindsAdapter(FriendProfile.this, R.layout.fri, ar));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                 fu=  dataSnapshot.child(FrList.frkey).child("username").getValue(String.class);

                th = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar4f);
                th.setTitle(fu);
                setSupportActionBar(th);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        imageView = (ImageView) findViewById(R.id.imgViewf);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Glide.with(FriendProfile.this)
                .using(new FirebaseImageLoader())
                .load(storageReference.child("images").child(FrList.frkey))
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(FriendProfile.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
                mBitmap = resource;

            }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                un = dataSnapshot.child(FrList.frkey).child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        blockB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        databaseReference.removeEventListener(this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(FriendProfile.this);
                        builder.setCancelable(true);
                        builder.setTitle("Block");
                        builder.setMessage("Remove from Followers list?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("friends").child(FrList.frkey).removeValue();
                                        Toast.makeText(FriendProfile.this,"Removed Sucessfully!",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(FriendProfile.this,FrList.class));
                                    }
                                });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
            }

    }