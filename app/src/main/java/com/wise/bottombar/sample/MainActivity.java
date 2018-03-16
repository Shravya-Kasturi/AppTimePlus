package com.wise.bottombar.sample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog progressDialog;
    EditText user;
    EditText pass;
    Button login;
    TextView fp;
    TextView sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(MainActivity.this);
        user=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        login=(Button)findViewById(R.id.loginbut);
        fp=(TextView)findViewById(R.id.forgotpass);
        sp=(TextView) findViewById(R.id.already);
/*firebase authentication listemer for one time login and new login */
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    finish();
                    startActivity(new Intent(MainActivity.this, FiveColorChangingTabsActivity.class));
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(user.getText().toString().trim()))
                {
                    Toast.makeText(MainActivity.this, "Please enter a valid Emaild id", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(pass.getText().toString().trim()))
                {
                    Toast.makeText(MainActivity.this,"Please enter the password",Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    progressDialog.setMessage("Hold on for a sec...");
                    progressDialog.show();
                    String u=user.getText().toString().trim();
                    String p=pass.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(u,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {
                                finish();
                                startActivity(new Intent(MainActivity.this,CheckUser.class));
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(user.getText().toString().trim()))
                {
                    Toast.makeText(MainActivity.this, "Please enter a valid Emaild id", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    progressDialog.setMessage("Hold on for a sec...");
                    progressDialog.show();
                    String u=user.getText().toString().trim();
                    mAuth.sendPasswordResetEmail(u).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this,"Reset mail sent successfully",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Failed to send reset mail",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });
    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }).create().show();
    }
}