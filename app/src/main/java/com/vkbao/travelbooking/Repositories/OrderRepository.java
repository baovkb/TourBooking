package com.vkbao.travelbooking.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.vkbao.travelbooking.Clients.ZaloPayClient;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.CreateOrderResponse;
import com.vkbao.travelbooking.Models.Invoice;
import com.vkbao.travelbooking.Models.Order;
import com.vkbao.travelbooking.Services.ZaloPayService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
