package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Helper.FormValidation;
import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.Models.Cart;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.AccountViewModel;
import com.vkbao.travelbooking.ViewModels.CartViewModel;
import com.vkbao.travelbooking.databinding.FragmentSignupBinding;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class SignupFragment extends Fragment {
    private FragmentSignupBinding binding;
    private AccountViewModel accountViewModel;
    private CartViewModel cartViewModel;

    public SignupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        initSignUp();
    }

    public void initSignUp() {
        FormValidation formValidation = new FormValidation(getContext());

        binding.email.setOnFocusChangeListener((view, b) -> {
            binding.emailNotify.setVisibility(View.GONE);
            if (b) {
                binding.email.setBackgroundResource(R.drawable.login_field_input_focus);
                binding.emailNotify.setVisibility(View.GONE);
            }
            else {
                formValidation.checkEmail(binding.email, binding.emailNotify);
            }
        });

        binding.password.setOnFocusChangeListener((view, b) -> {
            if (b){
                binding.password.setBackgroundResource(R.drawable.login_field_input_focus);
                binding.passwordNotify.setVisibility(View.GONE);
            }
            else {
                formValidation.checkMinLength(binding.password, binding.passwordNotify, 6);
            }
        });

        binding.confirmPassword.setOnFocusChangeListener((view, b) -> {
            if (b) {
                binding.confirmPassword.setBackgroundResource(R.drawable.login_field_input_focus);
                binding.confirmPasswordNotify.setVisibility(View.GONE);
            } else {
                String checkMinLength = formValidation.checkMinLength(binding.confirmPassword, binding.confirmPasswordNotify, 6);
                if (checkMinLength == null) {
                    formValidation.checkMatch(binding.password, binding.confirmPassword, binding.confirmPasswordNotify);
                }
            }
        });


        binding.signupButton.setOnClickListener(view -> {
            if (isFormValid(formValidation)) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                accountViewModel.signupUser(email, password, result -> {
                    switch (result) {
                        case "SUCCESS":
                            FirebaseUser user = accountViewModel.getUser();
                            String cartID = cartViewModel.createID();

                            CompletableFuture<Account> getAccountFuture = accountViewModel.getAccountByUIDFuture(user.getUid());
                            CompletableFuture<Boolean> createCartFuture = cartViewModel.createCart(new Cart(cartID, user.getUid(), new HashMap<>()));

                            getAccountFuture.thenCombine(createCartFuture, (account, success) -> {
                                if (account != null && success) {
                                    account.setCart_id(cartID);
                                    accountViewModel.updateUser(account);
                                }

                                return null;
                            });

                            break;
                    }
                });
            }
        });

        binding.login.setOnClickListener(view -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
        });
    }

    private boolean isFormValid(FormValidation formValidation) {
        String checkEmail = formValidation.checkEmail(binding.email, binding.emailNotify);
        String checkPassword = formValidation.checkMinLength(binding.password, binding.passwordNotify, 6);
        String checkMinLengthConfirmPassword = formValidation.checkMinLength(binding.confirmPassword, binding.confirmPasswordNotify, 6);
        String checkMatchPassword = null;
        if (checkMinLengthConfirmPassword == null) {
            checkMatchPassword = formValidation.checkMatch(binding.password, binding.confirmPassword, binding.confirmPasswordNotify);
        }
        return checkEmail == null && checkPassword == null && checkMinLengthConfirmPassword == null && checkMatchPassword == null;
    }
}