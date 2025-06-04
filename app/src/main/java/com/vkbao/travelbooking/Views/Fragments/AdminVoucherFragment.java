package com.vkbao.travelbooking.Views.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.travelbooking.Adapters.VoucherAdminAdapter;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.VoucherViewModel;
import com.vkbao.travelbooking.Views.Dialogs.ConfirmDialog;
import com.vkbao.travelbooking.databinding.FragmentAdminVoucherBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AdminVoucherFragment extends Fragment implements VoucherAdminAdapter.OnVoucherActionListener {

    private FragmentAdminVoucherBinding binding;
    private VoucherViewModel viewModel;
    private VoucherAdminAdapter adapter;
    private String keyword = "";
    private String tabName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminVoucherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        TabLayout.Tab selectedTab = binding.tabLayout.getTabAt(binding.tabLayout.getSelectedTabPosition());
        if (selectedTab != null) {
            tabName = selectedTab.getText().toString();
            handleTabSelection();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(VoucherViewModel.class);

        // Setup RecyclerView
        adapter = new VoucherAdminAdapter(new ArrayList<>(), this);
        binding.voucherRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.voucherRecyclerView.setAdapter(adapter);

        // Setup search
        binding.searchVoucher.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyword = s.toString().trim();
                viewModel.getVoucherByName(keyword);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        binding.addVoucherFab.setOnClickListener(v -> {
            startAddVoucherFragment(null);
        });

        //setup tab
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabName = tab.getText().toString();
                handleTabSelection();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Observe vouchers
        viewModel.getFilterVouchers().observe(getViewLifecycleOwner(), this::updateVouchers);
    }

    private void handleTabSelection() {
        switch (tabName) {
            case "Active":
                viewModel.getActiveVouchersByName(keyword);
                break;
            case "Expired":
                viewModel.getExpiredVouchersByName(keyword);
                break;
            default:
                viewModel.getVoucherByName(keyword);
        }
    }

    private void updateVouchers(List<Voucher> vouchers) {
        showLoading(false);
        adapter.updateVouchers(vouchers);
    }

    private void showLoading(boolean isLoading) {
        binding.adminVoucherLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.voucherRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onEditClick(Voucher voucher) {
        startAddVoucherFragment(voucher);
    }

    @Override
    public void onDeleteClick(Voucher voucher) {
        confirmDeleteDialog(voucher.getVoucher_id());
    }

    private void confirmDeleteDialog(String voucherId) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setMessage(requireActivity().getString(R.string.dialog_delete_message));

        confirmDialog.setPositiveBtn(() -> {
            viewModel.deleteVoucher(voucherId).thenAccept(isSuccessful -> {
                if (isSuccessful) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        confirmDialog.show(getChildFragmentManager(), null);
    }

    private void startAddVoucherFragment(Voucher voucher) {
        FragmentManager fragmentManager = getParentFragment().getParentFragmentManager();
        AdminAddVoucherFragment fragment = new AdminAddVoucherFragment();

        if (voucher != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("voucher", voucher);
            fragment.setArguments(bundle);
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }
}