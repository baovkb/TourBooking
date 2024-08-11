package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AccountViewModel accountViewModel;
    private static final String TAG = "LoginFragment";

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLogin();
    }

    public void initLogin() {
        binding.email.setOnFocusChangeListener((view, b) -> {
            Log.d("test", binding.email.getText().toString());
            if (b) binding.email.setBackgroundResource(R.drawable.login_field_input_focus);
            else binding.email.setBackgroundResource(R.drawable.login_field_input);
            binding.loginFailNotify.setVisibility(View.GONE);
        });
        binding.password.setOnFocusChangeListener((view, b) -> {
            if (b) binding.password.setBackgroundResource(R.drawable.login_field_input_focus);
            else binding.password.setBackgroundResource(R.drawable.login_field_input);
            binding.loginFailNotify.setVisibility(View.GONE);
        });

        binding.loginButton.setOnClickListener(view -> {
            login();
        });

        binding.signup.setOnClickListener(view -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(binding.main.getId(), new SignupFragment())
                    .addToBackStack(null)
                    .commit();
        });

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
    }

    public void login() {
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        if (!email.equals("") && !password.equals("")) {
            accountViewModel.loginUser(email, password, (isSuccessful) -> {
                if (isSuccessful) {
                    Log.d(TAG, "login successfully");
                } else {
                    binding.email.setBackgroundResource(R.drawable.red_bg);
                    binding.password.setBackgroundResource(R.drawable.red_bg);
                    binding.loginFailNotify.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}