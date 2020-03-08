package com.prashantchanne.chatbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class NoticeAdapter extends FirestoreRecyclerAdapter<NoticeModel, NoticeAdapter.NoticeViewHolder> {

    public OnItemClickListener listener;

    public NoticeAdapter(@NonNull FirestoreRecyclerOptions<NoticeModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoticeViewHolder holder, int position, @NonNull NoticeModel model) {

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());

        try {

            long millisecond = model.getTimestamp().getTime();
            String DateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            holder.priority.setText(DateString);

          SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
          String s = dateFormat.format(model.getTimestamp());
          holder.time.setText(s);


        }catch (Exception e){

        }

        if(model.isTeacher()){
            holder.from.setText("Sent to: "+model.getFrom());
        }
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_board_cards,parent,false);
        return new NoticeViewHolder(v);
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,priority,from,time;

        public NoticeViewHolder(final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextView);
            description = itemView.findViewById(R.id.descTextView);
            priority = itemView.findViewById(R.id.priorityTextView);
            from = itemView.findViewById(R.id.fromTextView);
            time = itemView.findViewById(R.id.timeTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(position!= RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });

        }

    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void SetOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
