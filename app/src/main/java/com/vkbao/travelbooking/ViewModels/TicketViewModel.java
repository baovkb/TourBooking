package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vkbao.travelbooking.Models.Ticket;
import com.vkbao.travelbooking.Repositories.TicketRepository;

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
}
