package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.databinding.FragmentAdminBinding;

public class AdminFragment extends Fragment {
    FragmentAdminBinding binding;
    private int chipNavID = -1;

    public AdminFragment() {
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
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initChipNavigation();
    }

    public void initChipNavigation() {
        binding.chipNavigation.setOnItemSelectedListener(i -> {
            chipNavID = i;
            FragmentManager fragmentManager = getChildFragmentManager();

            if (i == R.id.tours) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_screen_fragment, new AdminToursFragment())
                        .commit();
            } else if (i == R.id.users) {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_screen_fragment, new ManageUserFragment())
                        .commit();
            } else if (i == R.id.profile) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_screen_fragment, new ProfileFragment())
                        .commit();
            } else if (i == R.id.voucher) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_screen_fragment, new AdminVoucherFragment())
                        .commit();
            }
        });

        if (chipNavID == -1) binding.chipNavigation.setItemSelected(R.id.tours, true);
        else binding.chipNavigation.setItemSelected(chipNavID, true);
    }
}