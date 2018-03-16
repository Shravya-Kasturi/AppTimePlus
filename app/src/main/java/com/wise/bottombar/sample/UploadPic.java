package com.wise.bottombar.sample;

import android.app.ProgressDialog;
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

public class UploadPic extends AppCompatActivity {
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    ImageButton imageButton;
    android.support.v7.widget.Toolbar th;
    private Uri filePath;
    ListView listView;
    ArrayList<String> badg;
    ArrayList<Integer> piclogo;
    ArrayList<Sett> ar;
    String un;
    Bitmap bitmap;
    private Bitmap mBitmap;
    DatabaseReference databaseReference;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic);
        //startActivity(new Intent(UploadPic.this,Detection.class));
        listView=(ListView)findViewById(R.id.mybadges);
        badg=new ArrayList<String>();
        ar=new ArrayList<Sett>();
        piclogo=new ArrayList<Integer>();
        piclogo.add(R.mipmap.fbbadge);
        piclogo.add(R.mipmap.whatsappbadge);
        piclogo.add(R.mipmap.instabadge);
        piclogo.add(R.mipmap.twitterbadge);
        piclogo.add(R.mipmap.chromebadge);
        piclogo.add(R.mipmap.hikebadge);
        piclogo.add(R.mipmap.utubebadge);

        databaseReference=FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                for(int k=0;k<7;k++) {
                    badg.add("     "+dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("badges").child(String.valueOf(k)).getValue(String.class));
                    Sett i=new Sett(piclogo.get(k),badg.get(k));
                    ar.add(i);
                }
                listView.setAdapter(new FreindsAdapter(UploadPic.this,R.layout.fri,ar));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        th=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar4);
        th.setTitle("My Profile");
        setSupportActionBar(th);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        imageView = (ImageView) findViewById(R.id.imgView);
        imageButton=(ImageButton)findViewById(R.id.imageButton);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Glide.with(UploadPic.this)
                .using(new FirebaseImageLoader())
                .load(storageReference.child("images").child(FirebaseAuth.getInstance().getUid()))
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(UploadPic.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
                mBitmap=resource;
                detect();
            }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                un=dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("username").getValue(String.class);
                ((TextView)findViewById(R.id.welcomep)).setText("Hello "+un+" !");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       imageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               chooseImage();
           }
       });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ FirebaseAuth.getInstance().getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadPic.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadPic.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String getEmotion(Emotion emotion)
    {
        String emotionType = "";
        double emotionValue = 0.0;
        if (emotion.anger > emotionValue)
        {
            emotionValue = emotion.anger;
            emotionType = "Anger";
        }
        if (emotion.contempt > emotionValue)
        {
            emotionValue = emotion.contempt;
            emotionType = "Contempt";
        }
        if (emotion.disgust > emotionValue)
        {
            emotionValue = emotion.disgust;
            emotionType = "Disgust";
        }
        if (emotion.fear > emotionValue)
        {
            emotionValue = emotion.fear;
            emotionType = "Fear";
        }
        if (emotion.happiness > emotionValue)
        {
            emotionValue = emotion.happiness;
            emotionType = "Happiness";
        }
        if (emotion.neutral > emotionValue)
        {
            emotionValue = emotion.neutral;
            emotionType = "Neutral";
        }
        if (emotion.sadness > emotionValue)
        {
            emotionValue = emotion.sadness;
            emotionType = "Sadness";
        }
        if (emotion.surprise > emotionValue)
        {
            emotionValue = emotion.surprise;
            emotionType = "Surprise";
        }
        return String.format("%s", emotionType);
    }

    private void setUiAfterDetection(Face[] result, boolean succeed) {

        if (succeed) {
            String detectionResult="";
            if (result != null) {
                detectionResult = result.length + " face"
                        + (result.length != 1 ? "s" : "") + " detected";
                if(Arrays.asList(result).size()>0) {
                    Face face = Arrays.asList(result).get(0);
                    String em = getEmotion(face.faceAttributes.emotion);
                    TextView textView = (TextView) findViewById(R.id.emotiontext);
                    textView.setText(em);
                }

            } else {
                detectionResult = "0 face detected";
            }
        }

        mBitmap = null;
    }

    public void detect() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        new UploadPic.DetectionTask().execute(inputStream);
    }

     class DetectionTask extends AsyncTask<InputStream, String, Face[]> {
        private boolean mSucceed = true;

        @Override
        protected Face[] doInBackground(InputStream... params) {
            FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
            try {
                return faceServiceClient.detect(
                        params[0],
                        false,
                        false,
                        new FaceServiceClient.FaceAttributeType[]{
                                FaceServiceClient.FaceAttributeType.Emotion
                        });
            } catch (Exception e) {
                mSucceed = false;
                return null;
            }
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(Face[] result) {
            setUiAfterDetection(result, mSucceed);
        }
    }

}