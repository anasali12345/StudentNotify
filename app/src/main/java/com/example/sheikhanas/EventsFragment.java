package com.example.sheikhanas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sheikhanas.EventModel.EventModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    RecyclerView recyclerViewEvent;
    ArrayList<EventModel> list;
    EventAdapter eventAdaptor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewEvent = view.findViewById(R.id.event_recycler_view);
        recyclerViewEvent.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewEvent.setLayoutManager(linearLayoutManager);

        list = new ArrayList<>();

        eventAdaptor = new EventAdapter(list);

        recyclerViewEvent.setAdapter(eventAdaptor);

        RetrieveData();
    }

    private void RetrieveData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Event");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String EventImage = snapshot.child("imageUrl").getValue().toString();
                    String EventDescription = snapshot.child("eventDiscription").getValue().toString();
                    list.add(new EventModel(EventDescription, EventImage));
                }

                eventAdaptor.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
