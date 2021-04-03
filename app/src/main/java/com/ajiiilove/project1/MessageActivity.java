package com.ajiiilove.project1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MessageActivity extends AppCompatActivity {
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        myDatabase = FirebaseDatabase.getInstance().getReference("Message");

        final TextView myText = findViewById(R.id.textbox);
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                String[] Messages = snapshot.getValue().toString().split(",");

                myText.setText(""); // Cleaning the text Area

                for (int i=1; i<Messages.length;i++){
                    String[] finalMsg = Messages[i].split("=");
                    myText.append(finalMsg[1] + "\n");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                myText.setText("Cancelled");

            }
        });
    }

    public void sendMessage(View view){
        EditText myEditText = findViewById(R.id.editText);

        myDatabase.child(Long.toString(System.currentTimeMillis())).setValue(myEditText.getText().toString());
        myEditText.setText("");
    }
}