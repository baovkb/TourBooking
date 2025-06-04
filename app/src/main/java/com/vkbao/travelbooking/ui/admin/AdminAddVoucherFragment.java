package com.vkbao.travelbooking.ui.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.model.Voucher;
import com.vkbao.travelbooking.viewmodel.VoucherViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminAddVoucherFragment extends Fragment {
    private VoucherViewModel viewModel;
    private TextInputEditText voucherCodeEditText;
    private TextInputEditText voucherNameEditText;
    private TextInputEditText descriptionEditText;
    private AutoCompleteTextView discountTypeAutoComplete;
    private TextInputEditText discountValueEditText;
    private TextInputEditText minOrderValueEditText;
    private TextInputEditText quantityEditText;
    private TextInputEditText startDateEditText;
    private TextInputEditText endDateEditText;
    private SwitchMaterial activeSwitch;
    private MaterialButton saveButton;
    private View progressBar;

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VoucherViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_add_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupDiscountTypeDropdown();
        setupDatePickers();
        setupSaveButton();
        observeViewModel();
    }

    private void initializeViews(View view) {
        voucherCodeEditText = view.findViewById(R.id.voucherCodeEditText);
        voucherNameEditText = view.findViewById(R.id.voucherNameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        discountTypeAutoComplete = view.findViewById(R.id.discountTypeAutoComplete);
        discountValueEditText = view.findViewById(R.id.discountValueEditText);
        minOrderValueEditText = view.findViewById(R.id.minOrderValueEditText);
        quantityEditText = view.findViewById(R.id.quantityEditText);
        startDateEditText = view.findViewById(R.id.startDateEditText);
        endDateEditText = view.findViewById(R.id.endDateEditText);
        activeSwitch = view.findViewById(R.id.activeSwitch);
        saveButton = view.findViewById(R.id.saveButton);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupDiscountTypeDropdown() {
        String[] discountTypes = {"PERCENTAGE", "FIXED_AMOUNT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            discountTypes
        );
        discountTypeAutoComplete.setAdapter(adapter);
    }

    private void setupDatePickers() {
        startDateEditText.setOnClickListener(v -> showDatePicker(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePicker(endDateEditText));
    }

    private void showDatePicker(TextInputEditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(dateFormat.format(calendar.getTime()));
        };

        new DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveVoucher();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (voucherCodeEditText.getText().toString().trim().isEmpty()) {
            voucherCodeEditText.setError("Voucher code is required");
            isValid = false;
        }

        if (voucherNameEditText.getText().toString().trim().isEmpty()) {
            voucherNameEditText.setError("Voucher name is required");
            isValid = false;
        }

        if (discountTypeAutoComplete.getText().toString().trim().isEmpty()) {
            discountTypeAutoComplete.setError("Discount type is required");
            isValid = false;
        }

        if (discountValueEditText.getText().toString().trim().isEmpty()) {
            discountValueEditText.setError("Discount value is required");
            isValid = false;
        }

        if (minOrderValueEditText.getText().toString().trim().isEmpty()) {
            minOrderValueEditText.setError("Minimum order value is required");
            isValid = false;
        }

        if (quantityEditText.getText().toString().trim().isEmpty()) {
            quantityEditText.setError("Quantity is required");
            isValid = false;
        }

        if (startDateEditText.getText().toString().trim().isEmpty()) {
            startDateEditText.setError("Start date is required");
            isValid = false;
        }

        if (endDateEditText.getText().toString().trim().isEmpty()) {
            endDateEditText.setError("End date is required");
            isValid = false;
        }

        return isValid;
    }

    private void saveVoucher() {
        showLoading(true);

        Voucher voucher = new Voucher();
        voucher.setVoucherCode(voucherCodeEditText.getText().toString().trim());
        voucher.setVoucherName(voucherNameEditText.getText().toString().trim());
        voucher.setDescription(descriptionEditText.getText().toString().trim());
        voucher.setDiscountType(discountTypeAutoComplete.getText().toString().trim());
        voucher.setDiscountValue(Double.parseDouble(discountValueEditText.getText().toString().trim()));
        voucher.setMinOrderValue(Double.parseDouble(minOrderValueEditText.getText().toString().trim()));
        voucher.setTotalQuantity(Integer.parseInt(quantityEditText.getText().toString().trim()));
        voucher.setStartDate(parseDate(startDateEditText.getText().toString().trim()));
        voucher.setEndDate(parseDate(endDateEditText.getText().toString().trim()));
        voucher.setActive(activeSwitch.isChecked());

        viewModel.createVoucher(voucher);
    }

    private Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    private void observeViewModel() {
        viewModel.getOperationResult().observe(getViewLifecycleOwner(), success -> {
            showLoading(false);
            if (success) {
                Toast.makeText(requireContext(), "Voucher created successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            } else {
                Toast.makeText(requireContext(), "Failed to create voucher", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        saveButton.setEnabled(!isLoading);
    }
} 