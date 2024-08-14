package com.vkbao.travelbooking.Repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vkbao.travelbooking.Models.Ticket;

import java.util.concurrent.CompletableFuture;

public class TicketRepository {
    private DatabaseReference reference;

    public TicketRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Ticket");
    }

    public CompletableFuture<Boolean> addTicket(Ticket ticket) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(ticket.getTicket_id()).setValue(ticket).addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(success -> success);
    }

    public String createID() {
        return reference.push().getKey();
    }
}
