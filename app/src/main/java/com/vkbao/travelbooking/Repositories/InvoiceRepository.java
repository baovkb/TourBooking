package com.vkbao.travelbooking.Repositories;

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
import com.vkbao.travelbooking.Models.Invoice;

import java.util.concurrent.CompletableFuture;

public class InvoiceRepository {
    private DatabaseReference reference;

    public InvoiceRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Invoice");
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
}
