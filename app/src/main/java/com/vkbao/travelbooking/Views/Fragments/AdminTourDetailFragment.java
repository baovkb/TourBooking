package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Location;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.CategoryViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.LocationViewModel;
import com.vkbao.travelbooking.databinding.FragmentAdminTourDetailBinding;
import com.vkbao.travelbooking.databinding.FragmentAdminToursBinding;
import com.vkbao.travelbooking.databinding.FragmentTourDetailBinding;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdminTourDetailFragment extends Fragment {
    private FragmentAdminTourDetailBinding binding;

    private LocationViewModel locationViewModel;
    private CategoryViewModel categoryViewModel;

    private Item item = null;

    public AdminTourDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminTourDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        initItem();
        setUpEvent();
    }

    private void initItem() {
        if ( getArguments() != null) {
             item = getArguments().getParcelable("item");
        }

        if (item != null) {
            loadContent(item);
        }
    }

    private void loadContent(Item item) {
        Glide.with(requireActivity())
                .load(item.getBanner())
                .into(binding.banner);
        binding.title.setText(item.getTitle());
        binding.address.setText(item.getAddress());
        binding.valDuration.setText(item.getDuration());
        binding.valStartDate.setText(item.getDateTour());
        binding.price.setText(item.getPrice());
        binding.description.setText(item.getDescription());
        binding.tourGuideName.setText(item.getTourGuideName());
        binding.tourGuidePhone.setText(item.getTourGuidePhone());
        Glide.with(requireActivity())
                .load(item.getTourGuidePic())
                .into(binding.tourGuideAvatar);


        //set location name - view mode
        locationViewModel.getLocationByID(item.getLocation_id(), result -> {
            if (result != null) {
                binding.locationName.setText(result.getLocation());
            }
        });

        //set category name - view mode
        categoryViewModel.getCategoryByID(item.getCategory_id(), result -> {
            if (result != null) {
                Glide.with(requireActivity())
                        .load(result.getImagePath())
                        .into(binding.categoryIcon);
                binding.categoryName.setText(result.getName());
            }
        });
    }

    private void setUpEvent() {
        binding.backBtn.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        binding.edit.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("item", item);
            AdminEditTourDetailFragment fragment = new AdminEditTourDetailFragment();
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}