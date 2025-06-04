package com.vkbao.travelbooking.Views.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.travelbooking.Adapters.UserSelectionAdapter;
import com.vkbao.travelbooking.Models.User;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.VoucherViewModel;
import java.util.ArrayList;
import java.util.List;

public class AdminAssignVoucherFragment extends Fragment implements UserSelectionAdapter.OnUserSelectionListener {
    private VoucherViewModel viewModel;
    private UserSelectionAdapter adapter;
    private TextInputEditText searchEditText;
    private TextView voucherInfoText;
    private MaterialButton assignButton;
    private String voucherId;
    private Voucher currentVoucher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VoucherViewModel.class);
        voucherId = getArguments() != null ? getArguments().getString("voucherId") : null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_assign_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        searchEditText = view.findViewById(R.id.searchEditText);
        voucherInfoText = view.findViewById(R.id.voucherInfoText);
        assignButton = view.findViewById(R.id.assignButton);

        // Setup RecyclerView
        adapter = new UserSelectionAdapter(new ArrayList<>(), this);
        view.findViewById(R.id.userRecyclerView).setLayoutManager(new LinearLayoutManager(requireContext()));
        view.findViewById(R.id.userRecyclerView).setAdapter(adapter);

        // Setup search
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                // Implement user search functionality
                // viewModel.searchUsers(s.toString());
            }
        });

        // Setup assign button
        assignButton.setOnClickListener(v -> assignVoucher());

        // Load voucher details
        if (voucherId != null) {
            viewModel.getVoucherById(voucherId, new VoucherRepository.Callback<Voucher>() {
                @Override
                public void onSuccess(Voucher voucher) {
                    currentVoucher = voucher;
                    updateVoucherInfo();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Observe LiveData
        viewModel.getOperationResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "Voucher assigned successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigateUp();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // Load users
        loadUsers();
    }

    private void updateVoucherInfo() {
        if (currentVoucher != null) {
            String info = String.format("Assigning voucher: %s\nDiscount: %s %s",
                    currentVoucher.getVoucherName(),
                    currentVoucher.getDiscountValue(),
                    currentVoucher.getDiscountType().equals("PERCENTAGE") ? "%" : "đ");
            voucherInfoText.setText(info);
        }
    }

    private void loadUsers() {
        // Implement user loading functionality
        // viewModel.loadUsers();
    }

    private void assignVoucher() {
        List<String> selectedUserIds = adapter.getSelectedUserIds();
        if (selectedUserIds.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one user", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assign voucher to selected users
        for (String userId : selectedUserIds) {
            viewModel.assignVoucherToUser(voucherId, userId);
        }
    }

    @Override
    public void onUserSelectionChanged(String userId, boolean isSelected) {
        // Handle user selection change if needed
    }
} 