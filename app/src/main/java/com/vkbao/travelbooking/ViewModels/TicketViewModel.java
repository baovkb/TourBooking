package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.travelbooking.Models.Ticket;
import com.vkbao.travelbooking.Repositories.TicketRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TicketViewModel extends AndroidViewModel {
    private TicketRepository ticketRepository;

    public TicketViewModel(@NonNull Application application) {
        super(application);

        ticketRepository = new TicketRepository();
    }

    public CompletableFuture<Boolean> addTicket(Ticket ticket) {
        return ticketRepository.addTicket(ticket);
    }

    public String createID() {
        return ticketRepository.createID();
    }

    public CompletableFuture<List<Ticket>> getTicketsByUIDFuture(String uid) {
        return ticketRepository.getTicketsByUIDFuture(uid);
    }

    public LiveData<List<Ticket>> getTicketsByUID(String uid) {
        return ticketRepository.getTicketsByUID(uid);
    }

    public CompletableFuture<Boolean> deleteTicketByID(String ticketID) {
        return ticketRepository.deleteTicketByID(ticketID);
    }

}
