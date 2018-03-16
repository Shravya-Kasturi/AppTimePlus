package com.vardhan.bottombar.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {

    String ud;
    ListView listView;
    ArrayList<Sett> arrayList;
    Toolbar th;
    DatabaseReference databaseReference;
    ArrayList<String> ar;
    ArrayList<String > br;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow);
        listView=(ListView)findViewById(R.id.followlist);
        th=(Toolbar)findViewById(R.id.toolbar5);
        th.setTitle("Friend Requests");
        setSupportActionBar(th);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ar=new ArrayList<String>();
        br=new ArrayList<String>();
       databaseReference= FirebaseDatabase.getInstance().getReference("users");
       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               databaseReference.removeEventListener(this);
               for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("requests").getChildren()) {
                String s=   d.getValue(String.class);
                br.add(s);
                ar.add(dataSnapshot.child(s).child("username").getValue(String.class));

               }Followadapter q=new Followadapter(Friends.this,R.layout.fri,ar,br);
               listView.setAdapter(q);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String u=ar.get(position);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Friends.this);
                        builder.setCancelable(true);
                        builder.setTitle("Follow Request");
                        builder.setMessage("I wanna follow U! Can I?");
                        builder.setPositiveButton("Accept",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(DataSnapshot d:dataSnapshot.getChildren()){
                                            if(d.child("username").getValue(String.class).equals(u)){
                                                ud=d.child("uid").getValue(String.class);
                                                databaseReference.child(d.getKey()).child("friends").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
                                                databaseReference.child(FirebaseAuth.getInstance().getUid()).child("requests").child(ud).removeValue();
                                                ar.remove(position);
                                                br.remove(position);
                                                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                                                Toast.makeText(Friends.this,"Accepted",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child(FirebaseAuth.getInstance().getUid()).child("requests").child(ud).removeValue();
                                ar.remove(position);
                                br.remove(position);
                                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                                Toast.makeText(Friends.this,"Rejected",Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}