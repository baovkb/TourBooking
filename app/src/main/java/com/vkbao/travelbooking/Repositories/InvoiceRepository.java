package com.vkbao.travelbooking.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.vkbao.travelbooking.Clients.ZaloPayClient;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.CreateOrderResponse;
import com.vkbao.travelbooking.Models.Invoice;
import com.vkbao.travelbooking.Services.ZaloPayService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceRepository {
    private DatabaseReference reference;
    private ZaloPayService zaloPayService;

    public InvoiceRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Invoice");
        zaloPayService = ZaloPayClient.getClient().create(ZaloPayService.class);
    }

    public String createID() {
        return reference.push().getKey();
    }

    public CompletableFuture<Boolean> createInvoice(Invoice invoice) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(invoice.getInvoice_id()).setValue(invoice).addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(success -> success);
    }

    public LiveData<Boolean> waitingForPayment(String invoiceID) {
        MutableLiveData<Boolean> isPaid = new MutableLiveData<>(false);
        DatabaseReference paymentStatusRef = reference.child(invoiceID).child("payment_status");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getValue(String.class).equals(Invoice.PaymentStatus.Paid.name())) {
                        isPaid.setValue(true);
                        paymentStatusRef.removeEventListener(this);
                    } else isPaid.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isPaid.setValue(false);
            }
        };

        paymentStatusRef.addValueEventListener(listener);

        return isPaid;
    }

    public CompletableFuture<CreateOrderResponse> zalopay_AddOrder(Invoice invoice) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<CreateOrderResponse> future = new CompletableFuture<>();
            Gson gson = new Gson();
            int appid = 554;
            String MAC_KEY = "8NdU5pG5R2spGHGhyO99HN1OhD8IQJBn";
            String appuser = "demo";
            long apptime = 0;
            String apptransid = new SimpleDateFormat("yyMMdd").format(new Date()) + "_" + invoice.getInvoice_id();
            String embeddata = "{}";
            String items = "[]";

            try {
                apptime = Helper.convertToUnix(invoice.getTimestamp());
            } catch (Exception e) {
                future.complete(null);
            }

            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    appid,
                    apptransid,
                    appuser,
                    invoice.getAmount(),
                    apptime,
                    embeddata,
                    items);
            String Mac = "";

            try {
                Mac = Helper.getMac(MAC_KEY, inputHMac);
            } catch (Exception e) {
                future.complete(null);
            }

            zaloPayService.createOrder(
                    appid,
                    appuser,
                    apptime,
                    invoice.getAmount(),
                    apptransid,
                    embeddata,
                    items,
                    Mac
            ).enqueue(new Callback<CreateOrderResponse>() {
                @Override
                public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                    future.complete(response.body());
                }

                @Override
                public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                    future.complete(null);
                }
            });;

            return future;
        }).thenCompose(data -> data);
    }
}
