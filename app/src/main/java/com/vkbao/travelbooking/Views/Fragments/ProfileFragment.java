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

import com.google.firebase.auth.FirebaseUser;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;

    AccountViewModel accountViewModel;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        loadContent();
        setUpItem();
    }

    public void setUpItem() {
        binding.myProfile.setOnClickListener(view -> {

        });

        binding.changePassword.setOnClickListener(view -> {

        });

        binding.changeLanguage.setOnClickListener(view -> {

        });

        binding.logout.setOnClickListener(view -> {
            try {
                accountViewModel.logoutUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadContent() {
        accountViewModel.getCurrentUser().observe(requireActivity(), user -> {
            if (user != null) {
                accountViewModel.getAccountByUID(user.getUid(), result -> {
                    if (result != null) binding.name.setText(result.getName());
                });
            }
        });
    }
}