package com.vkbao.travelbooking.Views.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.Models.Role;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.Views.Fragments.AdminFragment;
import com.vkbao.travelbooking.Views.Fragments.LoginFragment;
import com.vkbao.travelbooking.Views.Fragments.UserFragment;
import com.vkbao.travelbooking.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private AccountViewModel accountViewModel;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        FragmentManager fragmentManager = getSupportFragmentManager();

        accountViewModel.getCurrentUser().observe(this, firebaseUser -> {
            for (Fragment fragment: fragmentManager.getFragments()) {
                fragmentManager.popBackStackImmediate();
            }

            if (firebaseUser == null) {
                fragmentManager
                        .beginTransaction()
                        .replace(binding.main.getId(), new LoginFragment())
                        .commit();
            } else {
                String uid = firebaseUser.getUid();
                accountViewModel.getAccountByUID(uid).observe(this, account -> {
                    if (account != null) {
                        if (account.getRole().equals(Role.User.name())) {
                            Log.d(TAG, "user is logged in");

                            fragmentManager
                                    .beginTransaction()
                                    .replace(binding.main.getId(), new UserFragment())
                                    .commit();
                        } else if (account.getRole().equals(Role.Admin.name())) {
                            Log.d(TAG, "admin is logged in");

                            fragmentManager
                                    .beginTransaction()
                                    .replace(binding.main.getId(), new AdminFragment())
                                    .commit();
                        }
                    } else {
                        Log.d(TAG, "account is not exist");
                    }
                });
            }
        });
    }
}