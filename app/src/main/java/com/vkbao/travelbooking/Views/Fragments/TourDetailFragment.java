package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.databinding.FragmentTourDetailBinding;

public class TourDetailFragment extends Fragment {
    FragmentTourDetailBinding binding;

    public TourDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTourDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initItem();
        setEvent();
    }

    public void initItem() {
        Item item = null;
        if (getArguments() != null) {
            item = getArguments().getParcelable("item");
        }

        if (item != null) {
            Glide.with(requireContext())
                            .load(item.getBanner())
                                    .into(binding.banner);
            binding.title.setText(item.getTitle());
            binding.address.setText(item.getAddress());
            binding.description.setText(item.getDescription());
            binding.price.setText(item.getPrice());
            binding.valDuration.setText(item.getDuration());
            binding.valStartDate.setText(item.getDateTour());
        }
    }

    public void setEvent() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }
}