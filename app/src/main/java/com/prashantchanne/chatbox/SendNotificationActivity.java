package com.prashantchanne.chatbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity {

    private EditText msgText;
    private Spinner spinner;
    private Button sendNotificationButton;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private Spinner shiftSpinner;
    private Spinner yearSpinner;
    private Spinner adYearSpinner;

    private ProgressDialog progressDialog;

    private int studentCount =0;
    private boolean isSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        msgText = findViewById(R.id.enterEditText);
        spinner = findViewById(R.id.spinnerSendNotification);
        sendNotificationButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressNoti);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        shiftSpinner = findViewById(R.id.dropdownShift);
        yearSpinner = findViewById(R.id.dropdownYear);
        adYearSpinner = findViewById(R.id.messageAddYearSpinner);

        progressDialog = new ProgressDialog(SendNotificationActivity.this);


        //------------ Select Department ------------//
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SendNotificationActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        //------------ Select Shift ------------//
        ArrayAdapter<String> shiftArrayAdapter = new ArrayAdapter<>(SendNotificationActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.shifts));
        shiftArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shiftSpinner.setAdapter(shiftArrayAdapter);

        //------------ Select Year ------------//
        ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<>(SendNotificationActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year));
        yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearArrayAdapter);

        //------------ Select Admission Year ------------//
        ArrayAdapter<String> addYearArrayAdapter = new ArrayAdapter<>(SendNotificationActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.yearNumber2));
        addYearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adYearSpinner.setAdapter(addYearArrayAdapter);


        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String messageText = msgText.getText().toString();
                final String deptText = spinner.getSelectedItem().toString();
                String shiftText = shiftSpinner.getSelectedItem().toString();
                String yearText = yearSpinner.getSelectedItem().toString();
                String adYear = adYearSpinner.getSelectedItem().toString();

                final String currentUser = firebaseAuth.getCurrentUser().getUid();

                if (deptText.contentEquals("Select your Department")
                        || shiftText.contentEquals("Select your Shift")
                        || yearText.contentEquals("Select your Year")
                        || adYear.contentEquals("Select Admission Year")){

                    Toast.makeText(SendNotificationActivity.this, "Please select all the fields", Toast.LENGTH_SHORT).show();
                    return;

                }
                else {

                        progressBar.setVisibility(View.VISIBLE);

                        progressDialog.setTitle("Sending Notification");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCanceledOnTouchOutside(false); // process will not terminate even if the user touches outside the dialog
                        progressDialog.setMessage("Sending Message..");

                        //progressDialog.show();
                         studentCount=0;
                         sendMessageToReciver(deptText, shiftText, yearText, messageText, currentUser,adYear);

                         //progressBar.setVisibility(View.INVISIBLE);
                    }

            }
        });

    }

    private void sendMessageToReciver(final String deptText, String shiftText, String yearText, final String messageText, final String currentUser,String adYear) {

        final String temp = Long.toString(System.currentTimeMillis());

        Toast.makeText(SendNotificationActivity.this, "Selected! " + deptText, Toast.LENGTH_SHORT).show();
        studentCount=0;
        firebaseFirestore.collection("Users")
                .whereEqualTo("dept",deptText)
                .whereEqualTo("add_year",adYear)
                .whereEqualTo("shift",shiftText)
                .whereEqualTo("year",yearText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.show();
                        Map<String, Object> notMap = new HashMap<>();
                        notMap.put("message",messageText);
                        notMap.put("from",currentUser);
                        notMap.put("timeStamp", FieldValue.serverTimestamp());
                        notMap.put("doc",temp);

                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.VISIBLE);


                            for(DocumentSnapshot doc: task.getResult()){

                                studentCount++;
                                if (doc!=null)
                                firebaseFirestore.collection("Users").document(doc.getId())
                                        .collection("notification")
                                        .document(temp)
                                        .set(notMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                studentCount++;
                                                msgText.setText(" ");


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SendNotificationActivity.this, "Sending Failed.."+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            progressDialog.dismiss();
                            Toast.makeText(SendNotificationActivity.this, "Message is sent successfully to "+studentCount+" students", Toast.LENGTH_SHORT).show();
                            studentCount=0;
                        }else{
                            progressDialog.dismiss();
                            progressBar.setVisibility(View.INVISIBLE);
                            String message = task.getException().getMessage();
                            Toast.makeText(SendNotificationActivity.this, "Error Occur! "+message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
