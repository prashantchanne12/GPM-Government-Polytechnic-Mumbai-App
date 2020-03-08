package com.prashantchanne.chatbox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationFirestoreAdapter extends FirestoreRecyclerAdapter<NotificationModel, NotificationFirestoreAdapter.NotificationViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotificationFirestoreAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull NotificationModel model) {
        holder.fromUserName.setText(model.getFrom());
        holder.msg.setText(model.getTitle());
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_user_card,parent,false);
        return new NotificationViewHolder(view);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView fromUserName;
        TextView msg;
        CircleImageView circleImageView;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            fromUserName = itemView.findViewById(R.id.notification_user_name);
            msg = itemView.findViewById(R.id.notification_user_message);
            circleImageView = itemView.findViewById(R.id.notification_user_image);

        }
    }

}
