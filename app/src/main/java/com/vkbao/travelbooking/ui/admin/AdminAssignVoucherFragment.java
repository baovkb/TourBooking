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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.adapter.UserSelectionAdapter;
import com.vkbao.travelbooking.viewmodel.VoucherViewModel;

import java.util.ArrayList;

public class AdminAssignVoucherFragment extends Fragment implements UserSelectionAdapter.OnUserSelectionListener {
    private VoucherViewModel viewModel;
    private UserSelectionAdapter adapter;
    private TextInputEditText searchEditText;
    private RecyclerView userRecyclerView;
    private MaterialButton assignButton;
    private View progressBar;
    private String voucherId;

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

        initializeViews(view);
        setupRecyclerView();
        setupSearch();
        setupAssignButton();
        observeViewModel();
        loadUsers();
    }

    private void initializeViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        assignButton = view.findViewById(R.id.assignButton);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new UserSelectionAdapter(new ArrayList<>(), this);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        userRecyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupAssignButton() {
        assignButton.setOnClickListener(v -> {
            if (voucherId != null) {
                assignVoucher();
            }
        });
    }

    private void loadUsers() {
        showLoading(true);
        viewModel.loadUsers();
    }

    private void filterUsers(String query) {
        viewModel.filterUsers(query);
    }

    private void assignVoucher() {
        showLoading(true);
        viewModel.assignVoucherToUsers(voucherId, adapter.getSelectedUserIds());
    }

    private void observeViewModel() {
        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            showLoading(false);
            adapter.updateUsers(users);
        });

        viewModel.getOperationResult().observe(getViewLifecycleOwner(), success -> {
            showLoading(false);
            if (success) {
                Toast.makeText(requireContext(), "Voucher assigned successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            } else {
                Toast.makeText(requireContext(), "Failed to assign voucher", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        assignButton.setEnabled(!isLoading);
    }

    @Override
    public void onUserSelectionChanged(String userId, boolean isSelected) {
        // Handle user selection if needed
    }
} 