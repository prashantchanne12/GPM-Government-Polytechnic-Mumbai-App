package com.prashantchanne.chatbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.transition.Transition;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.StudentViewHolder>{

    List<StudentUsers> studentList;
    Context context;
    StudentList studentListclass;

    SparseBooleanArray itemStateArray = new SparseBooleanArray();

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    String currentDate;

    List<String> rollNumbersList;

    String rollNumId;
    int count = 0;

   String lectures;

   private String shiftText;
   private String addYearText;
   private String depText;
   private String tempShift;

   private CardView card;
    //SubjectAndPractical subjectAndPractical;

    public StudentRecyclerAdapter(List<StudentUsers> studentList, Context context,String lectures,String shiftText,String addYearText,String deptText) {
        this.studentList = studentList;
        this.context = context;
        studentListclass = (StudentList)context;
        this.lectures = lectures;

        this.addYearText = addYearText;
        this.depText = deptText;
        this.shiftText = shiftText;

        if(shiftText.equals("First")){
            tempShift = "FS";
        }

        if (shiftText.equals("Second")){
            tempShift = "SS";
        }

        rollNumId = tempShift + addYearText +deptText;

        Toast.makeText(context, ""+rollNumId, Toast.LENGTH_SHORT).show();

        rollNumbersList = new ArrayList<>();
    }



    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item,parent,false);
        return new StudentViewHolder(view,studentListclass);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        //to set the Text or name
        holder.userName.setText(studentList.get(position).getName());

        //to set the User Profile
        CircleImageView circleImageView = holder.userImage;
        RequestOptions placeHolder = new RequestOptions();
        placeHolder.placeholder(R.drawable.default_image);

        String image = studentList.get(position).getImage();

        if (image!=null) {

            if (image.equals("captain")) {
                Glide.with(context).setDefaultRequestOptions(placeHolder).load(R.drawable.captainamerica).into(circleImageView);
            }

            if (image.equals("ww")) {
                Glide.with(context).setDefaultRequestOptions(placeHolder).load(R.drawable.wonderwoman).into(circleImageView);
            }

            if (image.equals("flash")) {
                Glide.with(context).setDefaultRequestOptions(placeHolder).load(R.drawable.theflash).into(circleImageView);
            }

            if (image.equals("spiderman")) {
                Glide.with(context).setDefaultRequestOptions(placeHolder).load(R.drawable.spiderman).into(circleImageView);
            }

            if (image.equals("incredibles")) {
                Glide.with(context).setDefaultRequestOptions(placeHolder).load(R.drawable.incredibles).into(circleImageView);
            }

        }


        //Roll number
        holder.userRollNumber.setText(studentList.get(position).getRoll());


        if(studentListclass.isInActionMode){

            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);

        }else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.INVISIBLE);

        }

        Calendar calendar = Calendar.getInstance();
        currentDate = java.text.DateFormat.getDateInstance().format(calendar.getTime());


        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }


    //ViewHolder
    public class StudentViewHolder extends RecyclerView.ViewHolder{

        private View view;

        private CircleImageView userImage;
        private TextView userName;
        private StudentList studentListclass;
        private TextView userRollNumber;
        CheckBox checkBox;
        RelativeLayout relativeLayout;

        public StudentViewHolder(View itemView, final StudentList studentList){
            super(itemView);

            view = itemView;

            userImage = view.findViewById(R.id.notificationUserImageview);
            userName = view.findViewById(R.id.notificationUserTextview);
            userRollNumber = view.findViewById(R.id.studentNumberTextView);
            this.studentListclass = studentList;
            checkBox = view.findViewById(R.id.listCheckBox);
            card = view.findViewById(R.id.studentListItem);

            card.setOnLongClickListener(studentListclass);


            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    if (!itemStateArray.get(adapterPosition, false)) {
                        checkBox.setChecked(true);
                        itemStateArray.put(adapterPosition, true);
                    }
                    else  {

                        checkBox.setChecked(false);
                        itemStateArray.put(adapterPosition, false);
                    }
                    studentListclass.preapareSelection(view,getAdapterPosition());
                }
            });
        }

         void bind(int position){
            if(!itemStateArray.get(position,false)){
                checkBox.setChecked(false);
            }else {
                checkBox.setChecked(true);
            }
        }

    }

    public void submitStudentAttendance(final ArrayList<StudentUsers> list, ArrayList<StudentUsers> notList){

        final Map<String,Object> map = new HashMap<>();
        map.put("Attended",lectures);
        map.put("type","student");

        final Map<String,Object> updateMap = new HashMap<>();


        final Map<String,String> totalMap = new HashMap<>();
        totalMap.put("total_lectures",lectures);


        final Map<String,String> startMap = new HashMap<>();
        startMap.put("Attended","0");


        final DocumentReference documentReference1 =  firebaseFirestore.collection("Attendance")
                .document(studentListclass.tempdept)
                .collection(studentListclass.yearText)
                .document(studentListclass.shiftText)
                .collection(studentListclass.addmissionYearText)
                .document(studentListclass.subject)
                .collection(studentListclass.lop)
                .document("total");


                documentReference1.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                DocumentSnapshot documentSnapshot = task.getResult();

                                if(documentSnapshot.exists()){

                                    int total = Integer.parseInt(documentSnapshot.getString("total_lectures"));
                                    total+=Integer.parseInt(lectures);

                                    documentReference1.update("total_lectures",total+"");


                                }else{

                                    documentReference1.set(totalMap);

                                }

                            }
                        });



                for ( StudentUsers users : list) {

                    String rollNum = users.getRoll();

                    updateMap.put("lectures", studentListclass.lectures);

                    final DocumentReference documentReference = firebaseFirestore.collection("Attendance")
                            .document(studentListclass.tempdept)
                            .collection(studentListclass.yearText)
                            .document(studentListclass.shiftText)
                            .collection(studentListclass.addmissionYearText)
                            .document(studentListclass.subject)
                            .collection(studentListclass.lop)
                            .document(rollNum);



                    documentReference.get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if(task.isSuccessful()){

                                        DocumentSnapshot documentSnapshot = task.getResult();

                                        if(documentSnapshot.exists()){

                                            int attend = Integer.parseInt(documentSnapshot.getString("Attended"));
                                            attend+=Integer.parseInt(lectures);

                                            updateMap.put("Attended",attend+"");

                                            documentReference.update(updateMap);

                                        }
                                    }else{
                                        Toast.makeText(context, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                }
                gotoSuccessful();

             }



    private void gotoSuccessful() {

        Intent successfulActivityIntent = new Intent(context, SuccessfulActivity.class);
        successfulActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(successfulActivityIntent);


    }



}