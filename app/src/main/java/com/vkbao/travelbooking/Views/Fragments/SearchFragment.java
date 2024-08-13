package com.vkbao.travelbooking.Views.Fragments;

import static com.vkbao.travelbooking.Helper.Helper.removeAccent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkbao.travelbooking.Adapters.SearchItemAdapter;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.ItemViewModel;
import com.vkbao.travelbooking.databinding.FragmentSearchBinding;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private ItemViewModel itemViewModel;

    private MutableLiveData<List<Item>> itemSearchList;

    private String keyword;

    public SearchFragment() {
        itemSearchList = new MutableLiveData<>(new ArrayList<>());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        initSearchList();
        setEvent();
        initKeyword();
    }

    public void initKeyword() {
        if (getArguments() != null) {
            String keyword = getArguments().getString("keyword", "");
            this.keyword = keyword.toLowerCase();
            binding.searchInput.setText(keyword);
        }
    }

    public void initSearchList() {
        SearchItemAdapter adapter = new SearchItemAdapter(new ArrayList<>());
        adapter.setOnItemClick(item -> {
            startTourDetailFragment(item);
        });

        itemSearchList.observe(requireActivity(), itemList -> {
            adapter.setItemList(itemList);
        });

        binding.recyclerSearch.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerSearch.setAdapter(adapter);

        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), itemList -> {
            updateKeyword(itemList, keyword);
        });
    }

    private void updateKeyword(List<Item> itemList, String keyword) {
        keyword = keyword.trim().toLowerCase();
        if (!keyword.isEmpty()) {
            this.keyword = removeAccent(keyword);
            List<Item> tmpList = new ArrayList<>();

            for (Item item: itemList) {
                String address = item.getAddress().toLowerCase();
                address = removeAccent(address);
                String title = item.getTitle().toLowerCase();
                title = removeAccent(title);

                if (address.contains(this.keyword) || title.contains(this.keyword)) {
                    tmpList.add(item);
                }
            }
            itemSearchList.setValue(tmpList);
        } else {
            itemSearchList.setValue(new ArrayList<>());
        }
    }

    private void startTourDetailFragment(Item item) {
        FragmentManager fragmentManager = getParentFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        TourDetailFragment tourDetailFragment = new TourDetailFragment();
        tourDetailFragment.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(R.id.main, tourDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    public void setEvent() {
        this.binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        this.binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String keyword = charSequence.toString().trim();
                updateKeyword(itemViewModel.getAllItems().getValue(), keyword);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}