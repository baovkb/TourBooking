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
import com.vkbao.travelbooking.Models.Ticket;

import java.util.ArrayList;
import java.util.List;
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

    public CompletableFuture<List<Ticket>> getTicketsByUIDFuture(String uid) {
        return CompletableFuture.supplyAsync(() -> {
            List<Ticket> ticketList = new ArrayList<>();
            CompletableFuture<List<Ticket>> ticketListFuture = new CompletableFuture<>();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Ticket ticket = dataSnapshot.getValue(Ticket.class);
                            if (ticket != null && ticket.getAccount_id().equals(uid)) ticketList.add(ticket);
                        }
                        ticketListFuture.complete(ticketList);
                    } else {
                        ticketListFuture.complete(ticketList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    ticketListFuture.complete(ticketList);
                }
            });

            return ticketListFuture;
        }).thenCompose(ticketList -> ticketList);
    }

    public LiveData<List<Ticket>> getTicketsByUID(String uid) {
        MutableLiveData<List<Ticket>> allTicket = new MutableLiveData<>(new ArrayList<>());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Ticket> ticketList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Ticket ticket = dataSnapshot.getValue(Ticket.class);
                        if (ticket != null && ticket.getAccount_id().equals(uid)) ticketList.add(ticket);
                    }

                    allTicket.setValue(ticketList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return allTicket;
    }

    public CompletableFuture<Boolean> deleteTicketByID(String ticketID) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(ticketID).removeValue().addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(success -> success);
    }
}
