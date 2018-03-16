package com.example.bottombar.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FrList extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<String> fr,ar;
    ListView l;
    String po;
    android.support.v7.widget.Toolbar th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr_list);
        th=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar3);
        th.setTitle("Friends");
        setSupportActionBar(th);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        l=(ListView)findViewById(R.id.frun);
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        fr=new ArrayList<String>();
        ar=new ArrayList<String>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren()){
                    ar.add(d.getValue(String.class));
                }
                for (String s:ar){
                    fr.add(dataSnapshot.child(s).child("username").getValue(String.class));
                }
                Followadapter arrayAdapter=new Followadapter(FrList.this,R.layout.fri,fr,ar);
                l.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               final String u=fr.get(position);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(FrList.this);
                        builder.setCancelable(true);
                        builder.setTitle("Block");
                        builder.setMessage("Remove from Followers list?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (DataSnapshot d:dataSnapshot.getChildren()){
                                            if(d.child("username").getValue(String.class).equals(u)){
                                                po=d.child("uid").getValue(String.class);
                                            }
                                        }
                                        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("friends").child(po).removeValue();
                                        fr.remove(position);
                                        ar.remove(position);
                                        ((BaseAdapter) l.getAdapter()).notifyDataSetChanged();
                                        Toast.makeText(FrList.this,"Removed Sucessfully!",Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
