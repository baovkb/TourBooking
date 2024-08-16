package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.databinding.CartItemBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<ItemPaymentAdapter.ItemOrderPayment> itemCartList;
    private Context context;
    private List<ItemPaymentAdapter.ItemOrderPayment> selectedItemList;
    private OnAmountChangedListener<ItemPaymentAdapter.ItemOrderPayment, Integer> listener;

    public CartAdapter(List<ItemPaymentAdapter.ItemOrderPayment> itemCartList) {
        this.itemCartList = itemCartList;
        this.selectedItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ItemPaymentAdapter.ItemOrderPayment itemCart = itemCartList.get(position);
        Item item = itemCart.getItem();

        holder.setBanner(item.getBanner());
        holder.setTitle(item.getTitle());
        holder.setAddress(item.getAddress());
        holder.setPrice(itemCart.getUnit_price());
        holder.setNumTicket(itemCart.getQuantity());
        holder.setTotalPrice(itemCart.getTotal_price());

        holder.binding.getRoot().setOnClickListener(view -> {
            holder.binding.checkbox.setChecked(!holder.binding.checkbox.isChecked());
        });

        holder.binding.checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedItemList.add(itemCartList.get(position));
            } else {
                selectedItemList.remove(itemCartList.get(position));
            }
        });

        holder.binding.addBtn.setOnClickListener(view -> {
            int numTicket = Integer.parseInt(holder.binding.numTicket.getText().toString());
            holder.binding.numTicket.setText(String.valueOf(numTicket+1));
            if (listener != null) listener.onChanged(itemCartList.get(position), numTicket+1);
        });

        holder.binding.subtractBtn.setOnClickListener(view -> {
            int numTicket = Integer.parseInt(holder.binding.numTicket.getText().toString());
            if (numTicket > 0) {
                holder.binding.numTicket.setText(String.valueOf(numTicket-1));
                if (listener != null) listener.onChanged(itemCartList.get(position), numTicket-1);
            }
        });

        holder.binding.numTicket.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (listener != null)
                            listener.onChanged(itemCartList.get(position), holder.getNumTicket());
                        return true;
                }
                return false;
            }
        });
    }

    public void setOnAmountChangedListener(OnAmountChangedListener<ItemPaymentAdapter.ItemOrderPayment, Integer> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return itemCartList.size();
    }

    public List<ItemPaymentAdapter.ItemOrderPayment> getSelectedItemList() {
        return selectedItemList;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CartItemBinding binding;

        public CartViewHolder(@NonNull CartItemBinding binding) {
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
            binding.numTicket.setText(String.valueOf(quantity));
        }

        private void setTotalPrice(int totalPrice) {
            binding.amount.setText(String.valueOf(totalPrice));
        }

        private int getNumTicket() {
            return Integer.parseInt(binding.numTicket.getText().toString());
        }
    }

    public interface OnAmountChangedListener<O, T> {
        public void onChanged(O item, T value);
    }
}
