package com.vkbao.travelbooking.Repositories;

import android.util.Log;

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
import com.vkbao.travelbooking.Models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private DatabaseReference reference;

    public ItemRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Items");
    }

    public LiveData<List<Item>> getItemsByLocationID(String locationID) {
        MutableLiveData<List<Item>> itemByIDLiveData = new MutableLiveData<>(new ArrayList<>());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> itemList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot != null) {
                        Item item = dataSnapshot.getValue(Item.class);
                        if (item.getLocation_id().equals(locationID)) itemList.add(item);
                    }
                }

                itemByIDLiveData.setValue(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return itemByIDLiveData;
    }

    public LiveData<List<Item>> getItemsByLocationIdCategoryId(@NonNull String locationId, @NonNull String categoryID) {
        MutableLiveData<List<Item>> itemLiveData = new MutableLiveData<>(new ArrayList<>());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> itemList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot != null) {
                        Item item = dataSnapshot.getValue(Item.class);

                        if (locationId.isEmpty() && categoryID.isEmpty()) {
                            itemList.add(item);
                        } else if (locationId.isEmpty()) {
                            if (item.getCategory_id().equals(categoryID)) itemList.add(item);
                        } else if (categoryID.isEmpty()) {
                            if (item.getLocation_id().equals(locationId)) itemList.add(item);
                        } else if (item.getLocation_id().equals(locationId) && item.getCategory_id().equals(categoryID)) {
                            itemList.add(item);
                        }
                    }
                }
                itemLiveData.setValue(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return itemLiveData;
    }

    public LiveData<List<Item>> getAllItems() {
        MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>(new ArrayList<>());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> itemList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot != null) {
                        Item item = dataSnapshot.getValue(Item.class);
                        itemList.add(item);
                    }
                }

                itemsLiveData.setValue(itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return itemsLiveData;
    }

    public void getItemByID(String itemID, Callback<Item> callback) {
        reference.child(itemID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) callback.onComplete(snapshot.getValue(Item.class));
                else callback.onComplete(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
