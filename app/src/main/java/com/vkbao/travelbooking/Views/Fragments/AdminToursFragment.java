package com.vkbao.travelbooking.Views.Fragments;

import static com.vkbao.travelbooking.Helper.Helper.removeAccent;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.vkbao.travelbooking.Adapters.SearchItemAdapter;
import com.vkbao.travelbooking.Helper.UserItemTouch;
import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Location;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.CategoryViewModel;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.ViewModels.LocationViewModel;
import com.vkbao.travelbooking.Views.Dialogs.ConfirmDialog;
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
    MutableLiveData<List<Item>> itemsSearch;
    private final String TAG = "AdminToursFragment";

    String searchKeyword = "";

    private ItemTouchHelper itemTouchHelper;

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
        itemsSearch = new MutableLiveData<>(new ArrayList<>());

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        initLocation();
        initCategory();
        initTourList();
        initSearch();
        setUpAddBtn();
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

    public void updateSearchKeyword(String newKeyword) {
        this.searchKeyword = removeAccent(newKeyword.toLowerCase().trim());
        List<Item> itemList = itemListByLocationCategory.getValue();

        if (this.searchKeyword.isEmpty()) {
            this.itemsSearch.setValue(itemList);
        } else {
            List<Item> tmpList = new ArrayList<>();
            for (Item item: itemList) {
                String title = removeAccent(item.getTitle().toLowerCase());
                String address = removeAccent(item.getAddress().toLowerCase());

                if (title.contains(searchKeyword) || address.contains(searchKeyword)) {
                    tmpList.add(item);
                }
            }
            itemsSearch.setValue(tmpList);
        }
    }

    private void initTourList() {
        itemListByLocationCategory.observe(getViewLifecycleOwner(), itemList -> {
            updateSearchKeyword(this.searchKeyword);
        });

        itemsSearch.observe(getViewLifecycleOwner(), itemList -> {
            SearchItemAdapter adapter = new SearchItemAdapter(itemList);
            adapter.setOnItemClick(item -> {
                startAdminTourDetailFragment(item);
            });

            //detach item touch
            if (itemTouchHelper != null) {
                itemTouchHelper.attachToRecyclerView(null);
            }
            UserItemTouch userItemTouch = new UserItemTouch(context, adapter);
            userItemTouch.setOnItemSwipeListener(position -> {
                confirmDeleteDialog(itemList.get(position), position);
            });
            itemTouchHelper = new ItemTouchHelper(userItemTouch);
            itemTouchHelper.attachToRecyclerView(binding.recyclerViewItem);

            binding.recyclerViewItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            binding.recyclerViewItem.setAdapter(adapter);

            binding.progressBar.setVisibility(View.GONE);
        });
    }

    public void initSearch() {
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSearchKeyword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
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

    private void setUpAddBtn() {
        binding.addTourBtn.setOnClickListener(view -> {
            getParentFragment().getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new AdminAddTourFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    public void confirmDeleteDialog(Item item, int position) {
        ConfirmDialog confirmDialog = new ConfirmDialog();

        confirmDialog.setMessage(requireActivity().getString(R.string.dialog_delete_message));

        confirmDialog.setPositiveBtn(() -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            CompletableFuture<Boolean> deleteBannerFuture = itemViewModel.deleteBannerByID(item.getItem_id());
            CompletableFuture<Boolean> deleteItemFuture = itemViewModel.deleteItemByID(item.getItem_id());

            deleteBannerFuture
                    .thenCombine(deleteItemFuture, (doneBanner, doneItem) -> doneItem)
                    .thenAccept(done -> {
                        if (done) fragmentManager.popBackStack();
                        else {
                            Log.d(TAG, "delete item is not successful");
                        }
                    });
        });

        confirmDialog.show(getChildFragmentManager(), null);
    }
}