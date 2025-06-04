package com.vkbao.travelbooking.ui.admin;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.adapter.VoucherAdminAdapter;
import com.vkbao.travelbooking.model.Voucher;
import com.vkbao.travelbooking.viewmodel.VoucherViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminVoucherFragment extends Fragment implements VoucherAdminAdapter.OnVoucherActionListener {
    private VoucherViewModel viewModel;
    private VoucherAdminAdapter adapter;
    private TextInputEditText searchEditText;
    private RecyclerView voucherRecyclerView;
    private View progressBar;

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
        voucherRecyclerView = view.findViewById(R.id.voucherRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        FloatingActionButton addVoucherFab = view.findViewById(R.id.addVoucherFab);

        // Setup RecyclerView
        adapter = new VoucherAdminAdapter(new ArrayList<>(), this);
        voucherRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        voucherRecyclerView.setAdapter(adapter);

        // Setup search
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterVouchers(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Setup FAB
        addVoucherFab.setOnClickListener(v -> 
            Navigation.findNavController(view)
                .navigate(R.id.action_adminVoucherFragment_to_adminAddVoucherFragment));

        // Observe vouchers
        viewModel.getVouchers().observe(getViewLifecycleOwner(), this::updateVouchers);

        // Load vouchers
        loadVouchers();
    }

    private void loadVouchers() {
        showLoading(true);
        viewModel.loadVouchers();
    }

    private void updateVouchers(List<Voucher> vouchers) {
        showLoading(false);
        adapter.updateVouchers(vouchers);
    }

    private void filterVouchers(String query) {
        List<Voucher> filteredList = new ArrayList<>();
        for (Voucher voucher : viewModel.getVouchers().getValue()) {
            if (voucher.getVoucherName().toLowerCase().contains(query.toLowerCase()) ||
                voucher.getVoucherCode().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(voucher);
            }
        }
        adapter.updateVouchers(filteredList);
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        voucherRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
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
                showLoading(true);
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