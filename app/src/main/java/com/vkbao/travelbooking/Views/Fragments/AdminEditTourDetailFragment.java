package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.databinding.FragmentAdminEditTourDetailBinding;

public class AdminEditTourDetailFragment extends Fragment {
    private FragmentAdminEditTourDetailBinding binding;

    public AdminEditTourDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminEditTourDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initItem() {

    }

    public void loadContent() {
        /*
        //set up location spinner - edit mode
        locationViewModel.getAllLocation().observe(getViewLifecycleOwner(), locations -> {
            ArrayAdapter<Location> locationArrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.location_item, locations);
            locationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.locationSpinner.setAdapter(locationArrayAdapter);

            for (int i = 0; i < locations.size(); ++i) {
                if (locations.get(i).getLocation_id().equals(item.getLocation_id())) {
                    binding.locationSpinner.setSelection(i);
                    break;
                }
            }
        });

        //set up category spinner - edit mode
        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), categoryList -> {
            ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.location_item, categoryList);
            categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySpinner.setAdapter(categoryArrayAdapter);

            for (int i = 0; i < categoryList.size(); ++i) {
                if (categoryList.get(i).getCategory_id().equals(item.getCategory_id())) {
                    binding.locationSpinner.setSelection(i);
                    break;
                }
            }
        });
        */
    }
}