package com.prashantchanne.chatbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationDetails extends AppCompatActivity {

    private TextView fromText;
    private TextView messageText;
    private TextView dateText;

    private String fromUser;
    private String message;
    private String date;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        fromText = findViewById(R.id.fromTextView);
        messageText = findViewById(R.id.messageTextView);
        dateText = findViewById(R.id.dateTextView);

        firebaseFirestore = FirebaseFirestore.getInstance();

        message = getIntent().getStringExtra("from_message");
        fromUser = getIntent().getStringExtra("from_name");
        date = getIntent().getStringExtra("date");

        /*firebaseFirestore.collection("Users").document(fromUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                 Username = documentSnapshot.getString("name");

            }
        });
*/

        fromText.setText(fromUser);
        dateText.setText(date);
        messageText.setText(message);


    }
}
