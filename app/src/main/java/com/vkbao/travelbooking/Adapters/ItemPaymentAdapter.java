package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.databinding.PaymentItemBinding;

import java.util.List;

public class ItemPaymentAdapter extends RecyclerView.Adapter<ItemPaymentAdapter.ItemPaymentViewHolder> {
    private List<ItemOrderPayment> itemOrderPaymentList;
    private Context context;

    public ItemPaymentAdapter(List<ItemOrderPayment> itemOrderPaymentList) {
        this.itemOrderPaymentList = itemOrderPaymentList;
    }

    @NonNull
    @Override
    public ItemPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        PaymentItemBinding binding = PaymentItemBinding.inflate(LayoutInflater.from(context), parent, false );

        return new ItemPaymentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPaymentViewHolder holder, int position) {
        ItemOrderPayment itemOrderPayment = itemOrderPaymentList.get(position);
        Item item = itemOrderPayment.getItem();

        holder.setBanner(item.getBanner());
        holder.setTitle(item.getTitle());
        holder.setAddress(item.getAddress());
        holder.setPrice(itemOrderPayment.getUnit_price());
        holder.setNumTicket(itemOrderPayment.getQuantity());
        holder.setTotalPrice(itemOrderPayment.getTotal_price());
    }

    @Override
    public int getItemCount() {
        return itemOrderPaymentList.size();
    }

    public class ItemPaymentViewHolder extends RecyclerView.ViewHolder {
        private PaymentItemBinding binding;

        public ItemPaymentViewHolder(@NonNull PaymentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBanner(String url) {
            Glide.with(context)
                    .load(url)
                    .into(binding.imageViewHolder);
        }

        public void setTitle(String title) {
            binding.titleViewHolder.setText(title);
        }

        public void setAddress(String address) {
            binding.address.setText(address);
        }

        public void setPrice(int price) {
            binding.price.setText(String.valueOf(price));
        }

        public void setNumTicket(int quantity) {
            binding.quantity.setText(String.valueOf(quantity));
        }

        private void setTotalPrice(int totalPrice) {
            binding.amount.setText(String.valueOf(totalPrice));
        }
    }

    public static class ItemOrderPayment {
        private Item item;
        private int quantity;
        private int unit_price;
        private int total_price;

        public ItemOrderPayment(Item item, int quantity, int unit_price, int total_price) {
            this.item = item;
            this.quantity = quantity;
            this.unit_price = unit_price;
            this.total_price = total_price;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getUnit_price() {
            return unit_price;
        }

        public void setUnit_price(int unit_price) {
            this.unit_price = unit_price;
        }

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }
    }
}
