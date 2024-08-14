package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.databinding.ItemViewHolderBinding;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private Context context;
    private OnItemClick<Item> listener;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemViewHolderBinding binding = ItemViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.setImage(item.getBanner());
        holder.setTitle(item.getTitle());
        holder.setAddress(item.getAddress());
        holder.setPrice(item.getPrice());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onItemClick(itemList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<Item> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick<Item> listener) {
        this.listener = listener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemViewHolderBinding binding;

        public ItemViewHolder(@NonNull ItemViewHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setImage(String url) {
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
    }

    public interface OnItemClick<T>{
        void onItemClick(T item);
    }
}
