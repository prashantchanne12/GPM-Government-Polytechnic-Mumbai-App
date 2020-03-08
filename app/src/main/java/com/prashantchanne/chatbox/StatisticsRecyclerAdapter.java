package com.prashantchanne.chatbox;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatisticsRecyclerAdapter extends FirestoreRecyclerAdapter<StatisticsModel,StatisticsRecyclerAdapter.ViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    int totalLectures;
    int attendedLectures;
    int total;




    public StatisticsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<StatisticsModel> options, Context context,int totalLectures) {
        super(options);
        this.context = context;
        this.totalLectures = totalLectures;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull StatisticsModel model) {

        holder.rollNum.setText(model.getRoll());
        holder.username.setText(model.getName());
        holder.allAttendedLectures.setText(model.getAttended());
        holder.allTotalLectures.setText(String.valueOf(totalLectures));

        String image = model.getProfile();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.default_image);

       // Glide.with(context).setDefaultRequestOptions(requestOptions).load(image).into(holder.profile);


        if (image!=null) {

            if (image.equals("captain")) {
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.captainamerica).into(holder.profile);
            }

            if (image.equals("ww")) {
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.wonderwoman).into(holder.profile);
            }

            if (image.equals("flash")) {
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.theflash).into(holder.profile);
            }

            if (image.equals("spiderman")) {
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.spiderman).into(holder.profile);
            }

            if (image.equals("incredibles")) {
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.incredibles).into(holder.profile);
            }

        }

        attendedLectures = Integer.parseInt(model.getAttended());
        int percen = (attendedLectures * 100) / totalLectures;

        if(percen>75){
            holder.percentage.setTextColor(Color.rgb(0,206,0));
        }else{
            holder.percentage.setTextColor(Color.rgb(209,0,0));
        }

        holder.percentage.setText(percen+"%");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_user_item,parent,false);
        return new ViewHolder(view);

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile;
        TextView username;
        TextView rollNum;
        TextView percentage;
        TextView allAttendedLectures;
        TextView allTotalLectures;

        public ViewHolder(View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.static_user_image);
            username = itemView.findViewById(R.id.static_user_name);
            rollNum = itemView.findViewById(R.id.static_roll_number);
            percentage = itemView.findViewById(R.id.percentage);
            allAttendedLectures = itemView.findViewById(R.id.static_attended);
            allTotalLectures = itemView.findViewById(R.id.static_total);
        }
    }


}
