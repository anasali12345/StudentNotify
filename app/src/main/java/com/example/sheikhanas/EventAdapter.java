package com.example.sheikhanas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sheikhanas.EventModel.EventModel;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    ArrayList<EventModel> list;

    public EventAdapter(ArrayList<EventModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventModel eventModel = list.get(position);
        Picasso.get().load(eventModel.getImageUrl()).into(holder.eventImage);
        holder.eventDescription.setText(eventModel.getEventDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        ImageView eventImage;
        MaterialTextView eventDescription;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_show_img);
            eventDescription = itemView.findViewById(R.id.show_description_event);
        }
    }
}
