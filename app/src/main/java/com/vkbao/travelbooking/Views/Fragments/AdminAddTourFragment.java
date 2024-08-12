package com.vkbao.travelbooking.Views.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Location;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.CategoryViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.LocationViewModel;
import com.vkbao.travelbooking.databinding.FragmentAdminEditTourDetailBinding;

public class AdminAddTourFragment extends Fragment {
    private FragmentAdminEditTourDetailBinding binding;

    private LocationViewModel locationViewModel;
    private CategoryViewModel categoryViewModel;
    private ItemViewModel itemViewModel;

    private String categoryID;
    private String locationID;
    private Uri bannerUri;
    public static final String KEY_RESULT_FRAGMENT = "key_new_item_add_tour";
    private final int PICK_IMG_CODE = 2;
    private final String TAG = "AdminAddTourFragment";

    public AdminAddTourFragment() {
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

        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        locationID = "";
        categoryID = "";

        loadContent();
        setUpEvent();
    }

    public void loadContent() {
        //set up location spinner
        locationViewModel.getAllLocation().observe(getViewLifecycleOwner(), locations -> {
            ArrayAdapter<Location> locationArrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.location_item, locations);
            locationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.locationSpinner.setAdapter(locationArrayAdapter);

            binding.locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (locations.get(i) != null) {
                        locationID = locations.get(i).getLocation_id();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });

        //set up category spinner
        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), categoryList -> {
            ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.location_item, categoryList);
            categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySpinner.setAdapter(categoryArrayAdapter);

            binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (categoryList.get(i) != null) {
                        categoryID = categoryList.get(i).getCategory_id();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });
    }

    private void setUpEvent() {
        binding.backBtn.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });

        binding.editBanner.setOnClickListener(view -> {
            chooseImage();
        });

        binding.save.setOnClickListener(view -> {
            //get value
            String title = binding.title.getText().toString();
            String address = binding.address.getText().toString();
            String duration = binding.valDuration.getText().toString();
            String dateTour = binding.valStartDate.getText().toString();
            String price = binding.price.getText().toString();
            String description = binding.description.getText().toString();
            String tourGuideName = binding.tourGuideName.getText().toString();
            String tourGuidePhone = binding.tourGuidePhone.getText().toString();

            String itemID = itemViewModel.createID();
            Item newItem = new Item(itemID,
                    categoryID,
                    locationID,
                    address,
                    "",
                    dateTour,
                    description,
                    duration,
                    price,
                    title,
                    tourGuideName,
                    tourGuidePhone,
                    "");

            if (bannerUri != null) {
                itemViewModel.uploadBanner(bannerUri, itemID)
                        .thenCompose(url -> {
                            if (url != null) newItem.setBanner(url);
                            return itemViewModel.updateItem(newItem);
                        })
                        .thenAccept(success -> {
                            Log.d(TAG, "save: " + success);
                            if (success) popFragment(newItem);
                        });
            } else {
                itemViewModel.updateItem(newItem)
                        .thenAccept(success -> {
                            if (success) popFragment(newItem);
                        });
            }
        });
    }

    private void popFragment(Item newItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", newItem);
        getParentFragmentManager().setFragmentResult(KEY_RESULT_FRAGMENT, bundle);
        getParentFragmentManager().popBackStack();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMG_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG_CODE && resultCode == RESULT_OK){
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    binding.banner.setImageBitmap(bitmap);
                    bannerUri = uri;
                } catch (Exception e) {
                    e.printStackTrace();
                    bannerUri = null;
                }
            }
        }
    }
}