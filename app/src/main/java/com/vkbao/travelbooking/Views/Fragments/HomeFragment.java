package com.vkbao.travelbooking.Views.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.vkbao.travelbooking.Adapters.CategoryAdapter;
import com.vkbao.travelbooking.Adapters.ItemAdapter;
import com.vkbao.travelbooking.Adapters.SliderAdapter;
import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Location;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.ViewModels.CategoryViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.LocationViewModel;
import com.vkbao.travelbooking.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    private LocationViewModel locationViewModel;
    private CategoryViewModel categoryViewModel;
    private ItemViewModel itemViewModel;

    private String locationID;
    private String categoryID;
    private MutableLiveData<List<Item>> itemListByLocationCategory;
    private Observer<List<Item>> itemByLocationCategoryObserver;
    private Context context;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = requireContext();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemListByLocationCategory = new MutableLiveData<>(new ArrayList<>());
        categoryID = "";
        locationID = "";
        itemByLocationCategoryObserver = itemList -> {
            itemListByLocationCategory.setValue(itemList);
        };

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

        initLocation();
        initBanner();
        initCategory();
        initItems();
        initSearch();
    }

    private void initLocation() {
        //handle click item spinner
        binding.spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //find location id
                locationViewModel.getAllLocation().observe(getViewLifecycleOwner(), locationList -> {
                    if (locationList.get(i) != null) {
                        locationID = locationList.get(i).getLocation_id();

                        updateItemByCategory();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        locationViewModel.getAllLocation().observe(getViewLifecycleOwner(), (locationsList) -> {
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.location_item, locationsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerLocation.setAdapter(adapter);
        });
    }

    private void initBanner() {
        List<Item> itemList = new ArrayList<>();

        SliderAdapter sliderAdapter = new SliderAdapter(new ArrayList<>(), binding.viewPagerSlider);
        sliderAdapter.setOnItemClick(position -> {
            Item clickedItem = itemList.get(position);
            if (clickedItem != null) {
                startTourDetailFragment(clickedItem);
            }
        });

        binding.viewPagerSlider.setAdapter(sliderAdapter);
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer pageTransformer = new CompositePageTransformer();
        pageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPagerSlider.setPageTransformer(pageTransformer);

        itemListByLocationCategory.observe(getViewLifecycleOwner(), items -> {
            List<String> sliderItemsList = new ArrayList<>();
            itemList.clear();

            for (Item item : items) {
                if (item != null && item.getBanner() != null) {
                    sliderItemsList.add(item.getBanner());
                    itemList.add(item);
                }
            }
            sliderAdapter.setSliderItemsList(sliderItemsList);
            binding.progressBarBanner.setVisibility(View.GONE);
        });
    }

    private void initCategory() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(new ArrayList<>());
        categoryAdapter.setOnItemClick(category -> {
            this.categoryID = category != null ? category.getCategory_id() : "";
            updateItemByCategory();
        });

        binding.recyclerViewCategory.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewCategory.setAdapter(categoryAdapter);

        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), categories -> {
            binding.progressBarCategory.setVisibility(View.GONE);

            categoryAdapter.setCategoryList(categories);
        });
    }

    private void updateItemByCategory() {
        itemViewModel.getItemsByLocationIdCategoryId(this.locationID, this.categoryID).removeObserver(itemByLocationCategoryObserver);
        itemViewModel.getItemsByLocationIdCategoryId(this.locationID, this.categoryID).observe(getViewLifecycleOwner(), itemByLocationCategoryObserver);
    }

    private void initItems() {
        ItemAdapter itemAdapter = new ItemAdapter(new ArrayList<>());
        itemAdapter.setOnItemClick(item -> startTourDetailFragment(item));

        binding.recyclerViewPopularDestination.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewPopularDestination.setAdapter(itemAdapter);

        itemListByLocationCategory.observe(getViewLifecycleOwner(), itemList -> {
            binding.progressBarPopularDestination.setVisibility(View.GONE);

            itemAdapter.setItemList(itemList);
        });
    }

    private void initSearch() {
        binding.searchBtn.setOnClickListener(view -> {
            startSearchFragment(binding.searchInput.getText().toString());
        });
    }

    private void startTourDetailFragment(Item item) {
        FragmentManager fragmentManager = getParentFragment().getParentFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        TourDetailFragment tourDetailFragment = new TourDetailFragment();
        tourDetailFragment.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main, tourDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startSearchFragment(String keyword) {
        FragmentManager fragmentManager = getParentFragment().getParentFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);

        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main, searchFragment)
                .addToBackStack(null)
                .commit();
    }
}