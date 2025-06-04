package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vkbao.travelbooking.Adapters.VoucherSelectionAdapter;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.VoucherViewModel;
import com.vkbao.travelbooking.databinding.FragmentVoucherSelectionBinding;

import java.util.ArrayList;
import java.util.List;

public class VoucherSelectionFragment extends Fragment {
    private FragmentVoucherSelectionBinding binding;
    private VoucherViewModel voucherViewModel;
    private List<Voucher> selectedVouchers = new ArrayList<>();
    private final String TAG = "VoucherSelectionFragment";

    public VoucherSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVoucherSelectionBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        //handle back
                        handleBackAction();
                        setEnabled(false);
                    }
                });

        voucherViewModel = new ViewModelProvider(requireActivity()).get(VoucherViewModel.class);
        selectedVouchers = voucherViewModel.getSelectedVoucher();

        initVouchers();
        initGesture();
    }

    public void initVouchers() {
        voucherViewModel.getAvailableVouchers().observe(requireActivity(), vouchers -> {
            VoucherSelectionAdapter adapter = new VoucherSelectionAdapter(vouchers, voucherViewModel.getSelectedVoucher(), v -> {
                Log.d(TAG, "selected vouchers have been changed, num = " + v.size());
                selectedVouchers = v;
            });
            binding.recyclerViewVoucher.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerViewVoucher.setAdapter(adapter);
        });
    }

    public void initGesture() {
        binding.backBtn.setOnClickListener(view -> {
            handleBackAction();
        });

        binding.submitBtn.setOnClickListener(view -> {
//            Log.d("before backing from voucher selection", "selected voucher size: " + selectedVouchers.size());
            voucherViewModel.setSelectedVoucher(selectedVouchers);
            handleBackAction();
        });
    }

    private void handleBackAction() {
        Bundle result = new Bundle();
        result.putBoolean("should_clear_voucher", false);
        getParentFragmentManager().setFragmentResult("selected_vouchers", result);
        getParentFragmentManager().popBackStack();
    }
}