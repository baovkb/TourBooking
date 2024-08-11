package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.databinding.SliderContainerBinding;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<String> sliderItemsList;
    private int itemLength = 0;
    private Context context;
    private ViewPager2 viewPager2;
    private OnItemClick<Integer> listener;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItemsList.addAll(sliderItemsList);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(List<String> sliderItemsList, ViewPager2 viewPager2) {
        this.sliderItemsList = sliderItemsList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        SliderContainerBinding sliderContainerBinding = SliderContainerBinding.inflate(LayoutInflater.from(context), parent, false);

        return new SliderViewHolder(sliderContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImageView(sliderItemsList.get(holder.getAdapterPosition()));
        if (position >= sliderItemsList.size() - 2) {
            viewPager2.post(runnable);
        }

        holder.binding.getRoot().setOnClickListener(view -> {
            if (listener != null) listener.onItemClick(position % itemLength);
        });
    }

    public void setSliderItemsList(List<String> sliderItemsList) {
        this.sliderItemsList.clear();
        this.sliderItemsList.addAll(sliderItemsList);
        itemLength = sliderItemsList.size();
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick<Integer> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return sliderItemsList.size();
    }

    final class SliderViewHolder extends RecyclerView.ViewHolder {
        SliderContainerBinding binding;

        public SliderViewHolder(@NonNull SliderContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setImageView(String sliderItem) {
            Glide.with(context)
                    .load(sliderItem)
                    .into(binding.sliderImage);
        }
    }

    public interface OnItemClick<T> {
        public void onItemClick(T position);
    }
}
