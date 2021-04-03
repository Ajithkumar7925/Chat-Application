package com.ajiiilove.project1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_page extends AppCompatActivity {

    EditText memail,mpassword;
    Button mloginbtn;
    TextView mcreatebtn,mforgotpassword;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        memail=findViewById(R.id.email_logintext);
        mpassword=findViewById(R.id.pass_logintext);
        progressBar=findViewById(R.id.progressBar2);
        fAuth=FirebaseAuth.getInstance();
        mloginbtn=findViewById(R.id.loginbutton);
        mcreatebtn=findViewById(R.id.signup_loginView);
        mforgotpassword=findViewById(R.id.forgotpass_loginview);

        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = memail.getText().toString();
                String password=mpassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    memail.setError("Email is required");
                }

                if(TextUtils.isEmpty(password)){
                    mpassword.setError("Password is required");
                }

                if(password.length()<6){
                    mpassword.setError("Password must be of 6 characters");
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){

                             Toast.makeText(Login_page.this,"Logged in Successfully",Toast.LENGTH_LONG).show();
                             startActivity(new Intent(getApplicationContext(),MainActivity.class));
                         }
                         else {
                             Toast.makeText(Login_page.this,"Error!" +task.getException().getMessage(),Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(View.GONE);
                         }
                    }
                });
            }
        });

        mcreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Signup_page.class));
            }
        });

        mforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText resetmail=new EditText(view.getContext());
                AlertDialog.Builder passwordresetdialog=new AlertDialog.Builder(view.getContext());
                passwordresetdialog.setTitle("Reset Password");
                passwordresetdialog.setMessage("Enter the registered mail to send your reset password link");
                passwordresetdialog.setView(resetmail);

                passwordresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String email=resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login_page.this,"Reset password link sent to your mail",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login_page.this,"Error!Reset Link is not sent" +e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwordresetdialog.create().show();
            }
        });
    }
}
