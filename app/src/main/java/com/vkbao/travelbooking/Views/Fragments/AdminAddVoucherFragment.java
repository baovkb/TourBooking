package com.vkbao.travelbooking.Views.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.VoucherViewModel;
import com.vkbao.travelbooking.Views.Dialogs.ConfirmDialog;
import com.vkbao.travelbooking.databinding.FragmentAdminAddVoucherBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdminAddVoucherFragment extends Fragment {
    private FragmentAdminAddVoucherBinding binding;
    private VoucherViewModel voucherViewModel;
    private Voucher voucher;
    private Date startDate;
    private Date endDate;

    public AdminAddVoucherFragment() {
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
        binding = FragmentAdminAddVoucherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        voucherViewModel = new ViewModelProvider(requireActivity()).get(VoucherViewModel.class);

        initDiscountType();
        setupDatePickers();
        initVoucher();
        initSaveBtn();
    }

    private void initVoucher() {
        if (getArguments() != null) {
            voucher = (Voucher) getArguments().getSerializable("voucher");

            binding.voucherNameEditText.setText(voucher.getName());
            binding.descriptionEditText.setText(voucher.getDescription());
            binding.discountTypeAutoComplete.setText(voucher.getVoucher_type(), false);
            binding.discountValueEditText.setText(String.valueOf(voucher.getVoucher_value()));
            binding.quantityEditText.setText(String.valueOf(voucher.getQuantity()));
            binding.startDateEditText.setText(voucher.getStart_at());
            binding.endDateEditText.setText(voucher.getEnd_at());
            binding.activeSwitch.setChecked(voucher.getIs_active());
        }
    }

    private void initDiscountType() {
        String[] discountTypes = {"percent", "fixed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, discountTypes);
        binding.discountTypeAutoComplete.setAdapter(adapter);
    }

    private void setupDatePickers() {
        Calendar calendar = Calendar.getInstance();

        binding.startDateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        startDate = calendar.getTime();
                        binding.startDateEditText.setText(formatDate(startDate));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.endDateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        endDate = calendar.getTime();
                        binding.endDateEditText.setText(formatDate(endDate));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void initSaveBtn() {
        binding.saveButton.setOnClickListener(view -> {
            String id = voucherViewModel.createID();
            String createAt = Helper.getCurrentTimeString();
            String startAt = binding.startDateEditText.getText().toString();
            String endAt = binding.endDateEditText.getText().toString();
            String description = binding.descriptionEditText.getText().toString();
            Boolean isActive = binding.activeSwitch.isChecked();
            String voucherType = binding.discountTypeAutoComplete.getText().toString();
            String voucherName = binding.voucherNameEditText.getText().toString();
            int discountValue = Integer.parseInt(binding.discountValueEditText.getText().toString());
            int quantity = Integer.parseInt(binding.quantityEditText.getText().toString());

            if (voucher != null) {
                id = voucher.getVoucher_id();
                createAt = voucher.getCreate_at();
            }

            Voucher savedVoucher = new Voucher(id, createAt, startAt, endAt, description, isActive, voucherType, discountValue, quantity, voucherName);
            voucherViewModel.addVoucher(savedVoucher).thenAccept(isSuccessful -> {
                if (isSuccessful) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager()
                            .popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String formatDate(Date date) {
        return new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyy", java.util.Locale.getDefault())
                .format(date);
    }
}