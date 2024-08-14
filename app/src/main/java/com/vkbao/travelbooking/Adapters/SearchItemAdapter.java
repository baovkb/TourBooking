package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.databinding.SearchItemBinding;

import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder> {
    private List<Item> itemList;
    private Context context;
    private OnItemClick<Item> listener;

    public SearchItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        SearchItemBinding binding = SearchItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.setBanner(item.getBanner());
        holder.setTitle(item.getTitle());
        holder.setAddress(item.getAddress());
        holder.setPrice(item.getPrice());

        holder.binding.getRoot().setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(itemList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick<Item> listener) {
        this.listener = listener;
    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder {
        private SearchItemBinding binding;

        public SearchItemViewHolder(@NonNull SearchItemBinding binding) {
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
    }

    public interface OnItemClick<T>{
        void onItemClick(T item);
    }
}
