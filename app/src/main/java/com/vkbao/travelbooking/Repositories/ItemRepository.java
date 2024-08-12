package com.vkbao.travelbooking.Repositories;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemRepository {
    private DatabaseReference reference;
    private StorageReference storageReference;

    public ItemRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Items");
        storageReference = FirebaseStorage.getInstance().getReference("Banner");
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

    public String createID() {
        return reference.push().getKey();
    }

    public CompletableFuture<String> uploadBanner(Uri imageUri, String bannerID) {
        return CompletableFuture.supplyAsync(() -> {
            StorageReference childRef = storageReference.child(bannerID);
            CompletableFuture<String> result = new CompletableFuture<>();

            childRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        childRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            result.complete(uri.toString());
                        });

                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        result.complete(null);
                    });

            return result;
        }).thenCompose(url -> url);
    }

    public CompletableFuture<Boolean> updateItem(Item newItem) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> result = new CompletableFuture<>();

            reference.child(newItem.getItem_id()).setValue(newItem)
                    .addOnCompleteListener(task -> {
                        result.complete(task.isSuccessful());
                    });
            return result;
        }).thenCompose(result -> result);
    }

    public CompletableFuture<Boolean> deleteItemByID(String itemID) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(itemID).removeValue().addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(result -> result);
    }

    public CompletableFuture<Boolean> deleteBannerByID(String itemID) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            storageReference.child(itemID).delete().addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(result -> result);
    }
}
