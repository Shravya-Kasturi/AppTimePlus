package com.wise.bottombar.sample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeP extends AppCompatActivity {
    EditText email,oldpass,newpass;
    String es,os,ns;
    Button ok;
    String un;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_p);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                un=dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("username").getValue(String.class);
                ((TextView)findViewById(R.id.welcome)).setText("Hi "+un);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        email=(EditText)findViewById(R.id.emailc);
        oldpass=(EditText)findViewById(R.id.oldpass);
        newpass=(EditText)findViewById(R.id.newpass);
        ok=(Button)findViewById(R.id.reauth);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                es=email.getText().toString();
                os=oldpass.getText().toString();
                ns=newpass.getText().toString();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(es, os);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(ns).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ChangeP.this,"Changed Successfully",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ChangeP.this,FiveColorChangingTabsActivity.class));
                                            } else {
                                                Toast.makeText(ChangeP.this,"Error: Please Try Again!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                }
                            }
                        });
            }
        });
    }
}
