package com.vkbao.travelbooking.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {
    private DatabaseReference reference;

    public LocationRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Location");
    }

    public LiveData<List<Location>> getAllLocation() {
        MutableLiveData<List<Location>> locationListLiveData = new MutableLiveData<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Location> locationList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        locationList.add(dataSnapshot.getValue(Location.class));
                    }
                }
                locationListLiveData.setValue(locationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return locationListLiveData;
    }

    public void getLocationByID(String locationID, Callback<Location> callback) {
        reference.child(locationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onComplete(snapshot.getValue(Location.class));
                } else callback.onComplete(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
