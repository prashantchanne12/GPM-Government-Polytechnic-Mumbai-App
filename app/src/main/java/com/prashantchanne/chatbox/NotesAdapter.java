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

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    public void update(String fileName, String url){
        items.add(fileName);
        urls.add(url);
        notifyDataSetChanged(); // this refreshes the recylerview automatically..
    }

    public NotesAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  //to create views for recycler view item
        View view = LayoutInflater.from(context).inflate(R.layout.notes_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //initialize the elements of indivisual items
        holder.textView.setText(items.get(position));

    }

    @Override
    public int getItemCount() {  //return the number of items count
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{  //represents the indivisual list items

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            View view  = itemView;

            textView = view.findViewById(R.id.notesNameTextView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = recyclerView.getChildAdapterPosition(view);  //it will return the position of item we clicked..
                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);  //denotes that we are going to view something..
                    intent.setData(Uri.parse(urls.get(position)));
                    context.startActivity(intent);



                }
            });

        }
    }

}
