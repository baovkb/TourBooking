package com.vkbao.travelbooking.Views.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;
    private int chipNavID = -1;

    public UserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initChipNavigation();
    }

    public void initChipNavigation() {
        binding.chipNavigation.setOnItemSelectedListener(i -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            chipNavID = i;

            if (i == R.id.home) {
                fragmentManager
                        .beginTransaction()
                        .replace(binding.mainScreenFragment.getId(), new HomeFragment())
                        .commit();
            } else if (i == R.id.cart) {
                fragmentManager
                        .beginTransaction()
                        .replace(binding.mainScreenFragment.getId(), new CartFragment())
                        .commit();
            } else if (i == R.id.profile) {
                fragmentManager
                        .beginTransaction()
                        .replace(binding.mainScreenFragment.getId(), new ProfileFragment())
                        .commit();
            }
        });

        if (chipNavID == -1) binding.chipNavigation.setItemSelected(R.id.home, true);
        else binding.chipNavigation.setItemSelected(chipNavID, true);
    }
}