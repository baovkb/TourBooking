package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.databinding.CategoryItemBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<Category> categoryList;
    private Context context;
    private OnItemClick<Category> listener;
    private int selectedPosition = -1;
    private int lastPosition = -2;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemBinding binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        this.context = parent.getContext();
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.setTitle(categoryList.get(position).getName());
        holder.setImage(categoryList.get(position).getImagePath());

        holder.binding.getRoot().setOnClickListener(view -> {
            lastPosition = selectedPosition;
            selectedPosition = position;
            if (lastPosition >= 0)
                notifyItemChanged(lastPosition);
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onItemClick(lastPosition == selectedPosition ? null : categoryList.get(selectedPosition));
            }

        });

        if (lastPosition == selectedPosition) {
            lastPosition = -2;
            selectedPosition = -1;
            holder.binding.title.setVisibility(View.GONE);
            holder.binding.getRoot().setBackgroundResource(R.drawable.gray_bg);
            holder.binding.title.setTextColor(context.getColor(R.color.white));
        } else if (position == selectedPosition) {
            holder.binding.title.setVisibility(View.VISIBLE);
            holder.binding.getRoot().setBackgroundResource(R.drawable.blue_bg);
            holder.binding.title.setTextColor(context.getColor(R.color.white));
        } else {
            holder.binding.title.setVisibility(View.GONE);
            holder.binding.getRoot().setBackgroundResource(R.drawable.gray_bg);
            holder.binding.title.setTextColor(context.getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList.clear();
        this.categoryList.addAll(categoryList);
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick<Category> listener) {
        this.listener = listener;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final CategoryItemBinding binding;

        public CategoryViewHolder(@NonNull CategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setImage(String url) {
            Glide.with(context)
                    .load(url)
                    .into(binding.image);
        }

        public void setTitle(String title) {
            binding.title.setText(title);
        }
    }

    public interface OnItemClick<T> {
        public void onItemClick(T category);
    }
}
