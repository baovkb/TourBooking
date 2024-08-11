package com.vkbao.travelbooking.Views.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.vkbao.travelbooking.Adapters.SearchItemAdapter;
import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Location;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.CategoryViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.LocationViewModel;
import com.vkbao.travelbooking.databinding.FragmentAdminToursBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdminToursFragment extends Fragment {
    FragmentAdminToursBinding binding;

    ItemViewModel itemViewModel;
    LocationViewModel locationViewModel;
    CategoryViewModel categoryViewModel;
    String locationID;
    String categoryID;
    Observer<List<Item>> itemByLocationCategoryObserver;
    MutableLiveData<List<Item>> itemListByLocationCategory;

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminToursBinding.inflate(inflater, container, false);
        context = container.getContext();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationID = "";
        categoryID = "";
        itemListByLocationCategory = new MutableLiveData<>(new ArrayList<>());
        itemByLocationCategoryObserver = itemList -> {
            itemListByLocationCategory.setValue(itemList);
        };

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        initLocation();
        initCategory();
        initTourList();
    }

    private void initLocation() {
        binding.spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i-1;
                //find location id
                if (position == -1) {
                    locationID = "";
                    updateItemByCategory();
                } else {
                    locationViewModel.getAllLocation().observe(getViewLifecycleOwner(), locationList -> {
                        if (locationList.get(position) != null) {
                            locationID = locationList.get(position).getLocation_id();

                            updateItemByCategory();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        locationViewModel.getAllLocation().observe(getViewLifecycleOwner(), locationList -> {
            List<Location> allLocation = new ArrayList<>();
            allLocation.add(new Location("", "All"));
            allLocation.addAll(locationList);

            ArrayAdapter<Location> adapter = new ArrayAdapter<>(requireActivity(), R.layout.location_item, allLocation);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerLocation.setAdapter(adapter);
        });
    }

    private void initCategory() {
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i-1;
                //find location id
                if (position == -1) {
                    categoryID = "";
                    updateItemByCategory();
                } else {
                    categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), categoryList -> {
                        if (categoryList.get(position) != null) {
                            categoryID = categoryList.get(position).getCategory_id();
                            updateItemByCategory();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), categories -> {
            List<Category> allCategory = new ArrayList<>();
            allCategory.add(new Category("", "All", ""));
            allCategory.addAll(categories);

            ArrayAdapter<Category> adapter = new ArrayAdapter<>(requireActivity(), R.layout.location_item, allCategory);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerCategory.setAdapter(adapter);
        });
    }

    private void initTourList() {
        itemListByLocationCategory.observe(getViewLifecycleOwner(), itemList -> {
            SearchItemAdapter adapter = new SearchItemAdapter(itemList);
            adapter.setOnItemClick(item -> {
                startAdminTourDetailFragment(item);
                /*
                String itemLocationID = item.getLocation_id();
                String itemCategoryID = item.getCategory_id();

                CompletableFuture<Location> locationCompletableFuture = CompletableFuture.supplyAsync(() -> {
                    CompletableFuture<Location> future = new CompletableFuture<>();
                    locationViewModel.getLocationByID(itemLocationID, location -> {
                        future.complete(location);
                    });
                    return future.join();
                });

                CompletableFuture<Category> itemCompletableFuture = CompletableFuture.supplyAsync(() -> {
                    CompletableFuture<Category> future = new CompletableFuture<>();
                    categoryViewModel.getCategoryByID(itemCategoryID, (category) -> {
                        future.complete(category);
                    });
                    return future.join();
                });

                CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(locationCompletableFuture, itemCompletableFuture);
                combinedFuture.thenRun(() -> {

                });

                 */

            });

            binding.recyclerViewItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            binding.recyclerViewItem.setAdapter(adapter);

            binding.progressBar.setVisibility(View.GONE);
        });
    }

    private void updateItemByCategory() {
        itemViewModel.getItemsByLocationIdCategoryId(this.locationID, this.categoryID).removeObserver(itemByLocationCategoryObserver);
        itemViewModel.getItemsByLocationIdCategoryId(this.locationID, this.categoryID).observe(getViewLifecycleOwner(), itemByLocationCategoryObserver);
    }

    private void startAdminTourDetailFragment(Item item) {
        FragmentManager fragmentManager = getParentFragment().getParentFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        AdminTourDetailFragment adminTourDetailFragment = new AdminTourDetailFragment();
        adminTourDetailFragment.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main, adminTourDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}