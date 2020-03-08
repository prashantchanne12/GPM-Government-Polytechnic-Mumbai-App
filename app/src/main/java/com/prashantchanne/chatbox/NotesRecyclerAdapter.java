package com.prashantchanne.chatbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;


public class NotesRecyclerAdapter extends FirestoreRecyclerAdapter<NotesModel,NotesRecyclerAdapter.NotesViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     *
     */

    NotesModel notesModel;
    ArrayList<String> urls = new ArrayList<>();
    Context context;

    public NotesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<NotesModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull NotesModel model) {
        holder.textView.setText(model.getTitle());
        urls.add(model.getUrl());
        holder.uploadByName.setText(model.getName());
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_items,parent,false);
        return new NotesViewHolder(view);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView textView,uploadByName;

        public NotesViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.notesNameTextView);
            uploadByName = itemView.findViewById(R.id.uploaded_by_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    int position = getAdapterPosition();  //it will return the position of item we clicked..
                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);  //denotes that we are going to view something..
                    intent.setData(Uri.parse(urls.get(position)));
                    context.startActivity(intent);

                }
            });


        }
    }

}
