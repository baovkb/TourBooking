package com.vkbao.travelbooking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.travelbooking.Adapters.VoucherAdminAdapter;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.VoucherViewModel;
import java.util.ArrayList;

public class AdminVoucherFragment extends Fragment implements VoucherAdminAdapter.OnVoucherActionListener {
    private VoucherViewModel viewModel;
    private VoucherAdminAdapter adapter;
    private TextInputEditText searchEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VoucherViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        searchEditText = view.findViewById(R.id.searchEditText);
        FloatingActionButton addVoucherFab = view.findViewById(R.id.addVoucherFab);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Setup RecyclerView
        adapter = new VoucherAdminAdapter(new ArrayList<>(), this);
        view.findViewById(R.id.voucherRecyclerView).setLayoutManager(new LinearLayoutManager(requireContext()));
        view.findViewById(R.id.voucherRecyclerView).setAdapter(adapter);

        // Setup search
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                viewModel.searchVouchers(s.toString());
            }
        });

        // Setup FAB
        addVoucherFab.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_adminVoucherFragment_to_adminAddVoucherFragment));

        // Setup tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // All vouchers
                        viewModel.loadVouchers();
                        break;
                    case 1: // Active vouchers
                        viewModel.getActiveVouchers();
                        break;
                    case 2: // Expired vouchers
                        viewModel.getExpiredVouchers();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Observe LiveData
        viewModel.getVouchers().observe(getViewLifecycleOwner(), vouchers -> {
            adapter.updateVouchers(vouchers);
        });

        viewModel.getOperationResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "Operation successful", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // Load initial data
        viewModel.loadVouchers();
    }

    @Override
    public void onEditClick(Voucher voucher) {
        Bundle args = new Bundle();
        args.putString("voucherId", voucher.getVoucherId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_adminVoucherFragment_to_adminEditVoucherFragment, args);
    }

    @Override
    public void onDeleteClick(Voucher voucher) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Voucher")
                .setMessage("Are you sure you want to delete this voucher?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteVoucher(voucher.getVoucherId());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onAssignClick(Voucher voucher) {
        Bundle args = new Bundle();
        args.putString("voucherId", voucher.getVoucherId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_adminVoucherFragment_to_adminAssignVoucherFragment, args);
    }
} 