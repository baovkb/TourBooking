package com.vkbao.travelbooking.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Ticket;
import com.vkbao.travelbooking.databinding.MyTicketItemBinding;

import java.util.List;

public class MyTicketAdapter extends RecyclerView.Adapter<MyTicketAdapter.MyTicketViewHolder> {
    private List<MyTicket> myTicketList;
    private OnItemClick<Ticket> listener;

    public MyTicketAdapter(List<MyTicket> myTicketList) {
        this.myTicketList = myTicketList;
    }

    @NonNull
    @Override
    public MyTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyTicketItemBinding binding = MyTicketItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyTicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTicketViewHolder holder, int position) {
        Item item = myTicketList.get(position).item;
        Ticket ticket = myTicketList.get(position).ticket;

        holder.setTitle(item.getTitle());
        holder.setDateTour(item.getDateTour());
        holder.setDuration(item.getDuration());
        holder.setTourGuideName(item.getTourGuideName());
        holder.setTourGuidePhone(item.getTourGuidePhone());
        holder.setPrice(item.getPrice());

        holder.binding.getRoot().setOnClickListener(view -> {
            if (listener != null) listener.onClick(ticket);
        });
    }

    @Override
    public int getItemCount() {
        return myTicketList.size();
    }

    public void setOnItemClick(OnItemClick<Ticket> listener) {
        this.listener = listener;
    }

    static class MyTicketViewHolder extends RecyclerView.ViewHolder {
        private MyTicketItemBinding binding;

        public MyTicketViewHolder(@NonNull MyTicketItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setTitle(String title) {
            binding.ticketTitle.setText(title);
        }

        public void setDateTour(String dateTour) {
            binding.dateTour.setText(dateTour);
        }

        public void setDuration(String duration) {
            binding.duration.setText(duration);
        }

        public void setTourGuideName(String tourGuideName) {
            binding.tourGuideName.setText(tourGuideName);
        }

        public void setTourGuidePhone(String tourGuidePhone) {
            binding.tourGuidePhone.setText(tourGuidePhone);
        }

        public void setPrice(int price) {
            binding.price.setText(String.valueOf(price));
        }
    }

    public static class MyTicket {
        private Item item;
        private Ticket ticket;

        public MyTicket(Item item, Ticket ticket) {
            this.item = item;
            this.ticket = ticket;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public Ticket getTicket() {
            return ticket;
        }

        public void setTicket(Ticket ticket) {
            this.ticket = ticket;
        }
    }

    public interface OnItemClick<T>{
        public void onClick(T ticket);
    }
}
