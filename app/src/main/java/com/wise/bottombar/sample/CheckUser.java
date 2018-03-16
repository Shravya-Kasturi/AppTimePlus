package com.wise.bottombar.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckUser extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    static EditText username,fullname;
    static Button submit,gv;
    static String uname;
    static EditText fname;
    static String fn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");
        progressDialog=new ProgressDialog(CheckUser.this);
        submit=(Button)findViewById(R.id.okc);
        fname=(EditText)findViewById(R.id.fullnamec);
        username=(EditText)findViewById(R.id.usernamec);
        progressDialog.setMessage("Checking your identity...Hold on!!");
        submit.setEnabled(false);
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(firebaseUser.getUid()))
                {
                    progressDialog.dismiss();
                    databaseReference.removeEventListener(this);
                    startActivity(new Intent(CheckUser.this,FiveColorChangingTabsActivity.class));
                }
                else
                {
                    databaseReference.removeEventListener(this);
                    progressDialog.dismiss();
                    submit.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname=username.getText().toString();
                fn=fname.getText().toString();
                if(TextUtils.isEmpty(uname))
                {
                    Toast.makeText(CheckUser.this,"Enter a unique username",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int flag=0;
                            for(DataSnapshot us:dataSnapshot.getChildren())
                            {
                                databaseReference.removeEventListener(this);
                                User user=us.getValue(User.class);
                                if(user.getUsername().equals(uname))
                                {
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==1)
                            {
                                Toast.makeText(CheckUser.this,"Username alredy exists!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ArrayList<String> temp=new ArrayList<String>();
                                for (int i=0;i<7;i++){
                                    temp.add(String.valueOf(0));
                                }
                                User user=new User(firebaseUser.getUid(),fn,uname,null,null,null,temp,String.valueOf(0));
                                databaseReference.child(firebaseUser.getUid()).setValue(user);
                                startActivity(new Intent(CheckUser.this,FiveColorChangingTabsActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

}