package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vkbao.travelbooking.Helper.FormValidation;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;

    private AccountViewModel accountViewModel;

    private final String TAG = "ForgotPasswordFragment";

    public ForgotPasswordFragment() {
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
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        initForgotPassword();
        initLoginBtn();
    }

    public void initForgotPassword() {
        FormValidation formValidation = new FormValidation(requireContext());

        binding.forgotPasswordButton.setOnClickListener(view -> {
            String checkEmail = formValidation.checkEmail(binding.email, binding.forgotPasswordNotify);
            if (checkEmail == null) {
                String email = binding.email.getText().toString().trim();
                accountViewModel.forgotPassword(email)
                        .thenAccept(result -> {
                            Log.d(TAG, result);

                            switch (result) {
                                case "SUCCESS":
                                    Toast.makeText(getContext(), getString(R.string.forgot_password_send_email_success), Toast.LENGTH_SHORT).show();
                                    getParentFragmentManager().popBackStack();
                                    break;
                            }
                        });
            }
        });
    }

    public void initLoginBtn() {
        binding.login.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });
    }
}