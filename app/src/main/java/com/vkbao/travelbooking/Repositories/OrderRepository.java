package com.vkbao.travelbooking.Repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vkbao.travelbooking.Models.Order;

import java.util.concurrent.CompletableFuture;

public class OrderRepository {
    private DatabaseReference reference;

    public OrderRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Order");
    }

    public CompletableFuture<Boolean> addOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(order.getOrder_id()).setValue(order).addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(success -> success);
    }

    public String createID() {
        return reference.push().getKey();
    }
}