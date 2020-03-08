package com.prashantchanne.chatbox;


import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NoticeModel {
    private String title;
    private String description;
    private @ServerTimestamp Date timestamp;
    private String from;
    private String fromuserid;
    private boolean teacher;

    public NoticeModel(){

    }

    public NoticeModel(String title, String description,Date timestamp,String from,String fromuserid,boolean teacher) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.from = from;
        this.fromuserid = fromuserid;
        this.teacher = teacher;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public String getFromuserid() {
        return fromuserid;
    }
}

/*
final Map<String,Object> initialMap = new HashMap<>();
                    initialMap.put("Attended","0");

                    firebaseFirestore
                            .collection("Users")
                            .whereEqualTo("add_year",addMissionYear)
                            .whereEqualTo("dept",deptText)
                            .whereEqualTo("year",yearText)
                            .whereEqualTo("shift",shiftText)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    for(final DocumentSnapshot doc: queryDocumentSnapshots){

                                        firebaseFirestore.collection("Users")
                                                .document(doc.getId())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(final DocumentSnapshot documentSnapshot) {

                                                        final String number = documentSnapshot.getString("roll");
                                                        final String profile = documentSnapshot.getString("image");
                                                        final String name = documentSnapshot.getString("name");

                                                        final DocumentReference docRef =  firebaseFirestore.collection("Attendance")
                                                                .document(tempDept)
                                                                .collection(yearText)
                                                                .document(shiftText)
                                                                .collection(addMissionYear)
                                                                .document(subject)
                                                                .collection(lop)
                                                                .document(number);

                                                        docRef.get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                        if(task.isSuccessful()){

                                                                            DocumentSnapshot docSnap = task.getResult();

                                                                            if(!docSnap.exists()){


                                                                                initialMap.put("name",name);
                                                                                initialMap.put("profile",profile);
                                                                                initialMap.put("roll",number);
                                                                                initialMap.put("type","student");

                                                                                docRef.set(initialMap);

                                                                            }

                                                                        }else{
                                                                            Toast.makeText(getApplicationContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });


                                                    }
                                                });

                                    }

                                }
                            });

 */