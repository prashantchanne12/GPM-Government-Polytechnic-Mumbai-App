package com.prashantchanne.chatbox;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationUserAdapterList extends RecyclerView.Adapter<NotificationUserAdapterList.ViewHolder> {

    private List<NotificationUsers> notList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private String name;
    private FirebaseAuth firebaseAuth;
    String DateString;

    public  NotificationUserAdapterList(List<NotificationUsers> notList, Context context){

        this.notList = notList;
        this.context = context;

    }

    @NonNull
    @Override
    public NotificationUserAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_users_iems,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        String from_id = notList.get(position).getFrom();

        holder.userMessage.setText(notList.get(position).getMessage());

        final String message = notList.get(position).getMessage();

        firebaseAuth = FirebaseAuth.getInstance();


            //Date
           if(firebaseAuth!=null) {
               try {

                   long millisecond = notList.get(position).getTimeStamp().getTime();
                   DateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
                   holder.setTime(DateString);


                   SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                   String s = dateFormat.format(notList.get(position).getTimeStamp());
                   holder.time.setText(s);




               }catch (Exception e){

               }
           }

        if(firebaseAuth!=null) {
            firebaseFirestore.collection("Users").document(from_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    name = documentSnapshot.getString("name");
                    String image = documentSnapshot.getString("image");

                    holder.userName.setText(name);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.default_image);

                   // Glide.with(context).setDefaultRequestOptions(requestOptions).load(image).into(holder.circleImageView);

                    if (image!=null) {

                        if (image.equals("captain")) {
                            Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.captainamerica).into(holder.circleImageView);
                        }

                        if (image.equals("ww")) {
                            Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.wonderwoman).into(holder.circleImageView);
                        }

                        if (image.equals("flash")) {
                            Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.theflash).into(holder.circleImageView);
                        }

                        if (image.equals("spiderman")) {
                            Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.spiderman).into(holder.circleImageView);
                        }

                        if (image.equals("incredibles")) {
                            Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.incredibles).into(holder.circleImageView);
                        }

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(context, "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent NotificationDetailsActivity = new Intent(context,NotificationDetails.class);
                NotificationDetailsActivity.putExtra("from_message",message);
                NotificationDetailsActivity.putExtra("from_name",name);
                NotificationDetailsActivity.putExtra("date",DateString);
                context.startActivity(NotificationDetailsActivity);

            }
        });

    }



    public void deleteItem(int position){
        NotificationUsers notificationUsers = notList.get(position);

        Toast.makeText(context, "Msg "+notificationUsers.getDoc(), Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("notification")
                .document(notificationUsers.getDoc())
                .delete();

        //notifyDataSetChanged();

    }



    @Override
    public int getItemCount() {
        return notList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        return  position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userMessage;
        private TextView userName;
        private CircleImageView circleImageView;
        private TextView dateText;
        private View view;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            userMessage = view.findViewById(R.id.notificationUserMessage);
            userName = view.findViewById(R.id.notificationUserName);
            circleImageView = view.findViewById(R.id.notificationUserImageview);
            dateText = view.findViewById(R.id.notificationDateTextView);
            time = view.findViewById(R.id.notificationTime);
        }


        public void setTime(String date){
            dateText.setText(date);
        }
    }
}
