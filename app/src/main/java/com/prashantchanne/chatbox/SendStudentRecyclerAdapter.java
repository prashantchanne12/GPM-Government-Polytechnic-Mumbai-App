package com.prashantchanne.chatbox;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class SendStudentRecyclerAdapter extends RecyclerView.Adapter<SendStudentRecyclerAdapter.sendStudentViewHolder>{

    private FirebaseFirestore firebaseFirestore;

    private List<StudentUsers> studentList;
    private Context context;
    private SendStudentToNextYear sendStudentToNextYear;
    String currentDate;
    String nextYear;

    SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public SendStudentRecyclerAdapter(List<StudentUsers> studentList, Context context){
        this.studentList = studentList;
        this.context = context;
        sendStudentToNextYear = (SendStudentToNextYear) context;

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public sendStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item,parent,false);
        return new sendStudentViewHolder(view,sendStudentToNextYear);

    }

    @Override
    public void onBindViewHolder(@NonNull sendStudentViewHolder holder, int position) {
        //to set the Text or name
        holder.userName.setText(studentList.get(position).getName());

        //to set the User Profile
        CircleImageView circleImageView = holder.userImage;
        RequestOptions placeHolder = new RequestOptions();
        placeHolder.placeholder(R.drawable.default_image);

       // Glide.with(context).setDefaultRequestOptions(placeHolder).load(studentList.get(position).getImage()).into(circleImageView);
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


        if(sendStudentToNextYear.isInActionMode){

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
    public class sendStudentViewHolder extends RecyclerView.ViewHolder {

        private View view;

        private CircleImageView userImage;
        private TextView userName;
        private SendStudentToNextYear sendStudentToNextYear;
        private TextView userRollNumber;
        private CheckBox checkBox;
        private CardView relativeLayout;


        public sendStudentViewHolder(View itemView,final SendStudentToNextYear sendStudentToNextYear) {
            super(itemView);


            view = itemView;
            this.sendStudentToNextYear = sendStudentToNextYear;

            userImage = view.findViewById(R.id.notificationUserImageview);
            userName = view.findViewById(R.id.notificationUserTextview);
            userRollNumber = view.findViewById(R.id.studentNumberTextView);
            checkBox = view.findViewById(R.id.listCheckBox);
            relativeLayout = view.findViewById(R.id.studentListItem);


            relativeLayout.setOnLongClickListener(sendStudentToNextYear);


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

                    sendStudentToNextYear.preapareSelection(view,getAdapterPosition());

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
    public void submitStudents(ArrayList<StudentUsers> list){



        String year = sendStudentToNextYear.year;

        if(year.equals("First")){
            nextYear = "Second";
        }

        if(year.equals("Second")){
            nextYear = "Third";
        }

        if(year.equals("Third")){
            nextYear = "PassOut";
        }

        Toast.makeText(context, "Next Year "+nextYear, Toast.LENGTH_SHORT).show();

        for(final StudentUsers users : list) {

            String userId = users.getUserId();

            firebaseFirestore.collection("Users")
                    .document(userId)
                    .update("year",nextYear);

        }

        gotoSuccessful();

    }

    private void gotoSuccessful() {

        context.startActivity(new Intent(context.getApplicationContext(),MainActivity.class));

    }
}
/*
      android:screenOrientation="portrait">
<RelativeLayout

    android:layout_width="wrap_content"
    android:layout_height="wrap_content">

    <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/notificationUserImageview"
        android:layout_width="77dp"
        android:layout_height="74dp"
        android:src="@drawable/default_image" />

    <TextView
        android:id="@+id/notificationUserTextview"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@+id/listCheckBox"
        android:layout_alignStart="@+id/studentNumberTextView"
        android:layout_marginBottom="-38dp"
        android:fontFamily="@font/roboto_medium"
        android:text="Noah"
        android:textSize="18sp" />

    <TextView
        android:id="@+id/studentNumberTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignTop="@+id/listCheckBox"
        android:layout_alignParentStart="true"
        android:layout_marginStart="112dp"
        android:fontFamily="@font/roboto_medium"
        android:text="FS16IF012"
        android:textSize="16sp"
        android:textStyle="bold" />

    <android.support.v7.widget.AppCompatCheckBox
        android:id="@+id/listCheckBox"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignStart="@+id/notificationUserImageview"
        android:layout_alignParentTop="true"
        android:layout_marginLeft="250dp"
        android:layout_marginTop="35dp"
        android:background="@color/textTabBrigth"
        android:buttonTint="@color/common_google_signin_btn_text_dark_focused" />
</RelativeLayout>


 */