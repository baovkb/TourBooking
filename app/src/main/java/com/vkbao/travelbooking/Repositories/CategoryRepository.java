package com.vkbao.travelbooking.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.Models.Location;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private DatabaseReference reference;

    public CategoryRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Category");
    }

    public MutableLiveData<List<Category>> getCategoryLiveData() {
        MutableLiveData<List<Category>> categoryLiveData = new MutableLiveData<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> categoryList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (snapshot.exists()) {
                        categoryList.add(dataSnapshot.getValue(Category.class));
                    }
                }
                categoryLiveData.setValue(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return categoryLiveData;
    }

    public void getCategoryByID(String categoryID, Callback<Category> callback) {
        reference.child(categoryID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) callback.onComplete(snapshot.getValue(Category.class));
                else callback.onComplete(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
