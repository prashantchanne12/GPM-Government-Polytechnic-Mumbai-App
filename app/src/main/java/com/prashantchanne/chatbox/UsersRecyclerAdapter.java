package com.prashantchanne.chatbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    List<Users> usersList;
    Context context;

    public UsersRecyclerAdapter(List<Users> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }


    @NonNull
    @Override
    public UsersRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecyclerAdapter.ViewHolder holder, int position) {

        //to set the Text or name
        holder.userName.setText(usersList.get(position).getName());

        //to set the User Profile
        CircleImageView circleImageView = holder.userImage;
        Glide.with(context).load(usersList.get(position).getImage()).into(circleImageView);


        final String userName = usersList.get(position).getName();
        final String userId = usersList.get(position).UserId;

        // the view represents the complete view of our single list item
       /* holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendToMain = new Intent(context, SendActivity.class);
                sendToMain.putExtra("user_id", userId);
                sendToMain.putExtra("user_name", userName);
                context.startActivity(sendToMain);

            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private View view;

        private CircleImageView userImage;
        private  TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            userImage = view.findViewById(R.id.notificationUserImageview);
            userName = view.findViewById(R.id.notificationUserTextview);


        }
    }

}
