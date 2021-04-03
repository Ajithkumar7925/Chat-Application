package com.ajiiilove.project1;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;



import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {


    TextView pemail,pname,paddress,pphone,pverifytext;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button verifybtn,messageButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        pphone = findViewById(R.id.profilephoneView);
        pname = findViewById(R.id.profilenameView);
        pemail = findViewById(R.id.profileemailView);
        paddress = findViewById(R.id.profileaddressView);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        messageButton=findViewById(R.id.button_message);
        verifybtn=findViewById(R.id.verifybutton);
        pverifytext=findViewById(R.id.verifytext);
        userID = fAuth.getCurrentUser().getUid();
        final FirebaseUser user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()){
            verifybtn.setVisibility(View.VISIBLE);
            pverifytext.setVisibility(View.VISIBLE);

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(),"Verification Link has been sent",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","onFailure:Email not sent"+e.getMessage());
                        }
                    });
                }
            });
        }

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists()) {
                    pphone.setText(documentSnapshot.getString("phone"));
                    paddress.setText(documentSnapshot.getString("address"));
                    pemail.setText(documentSnapshot.getString("email"));
                    pname.setText(documentSnapshot.getString("fname"));
                } else {
                    Log.d("tag","onEvent:Document donot exixts");
                }
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MessageActivity.class));
            }
        });

    }



    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login_page.class));
        finish();
    }


}