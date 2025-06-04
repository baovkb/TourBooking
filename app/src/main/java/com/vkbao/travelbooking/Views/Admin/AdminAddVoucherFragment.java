package com.vkbao.travelbooking.Views.Admin;

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
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.ViewModels.VoucherViewModel;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AdminAddVoucherFragment extends Fragment {
    private VoucherViewModel viewModel;
    private TextInputEditText voucherCodeEditText;
    private TextInputEditText voucherNameEditText;
    private TextInputEditText descriptionEditText;
    private AutoCompleteTextView discountTypeAutoComplete;
    private TextInputEditText discountValueEditText;
    private TextInputEditText minOrderValueEditText;
    private TextInputEditText quantityEditText;
    private TextView startDateText;
    private TextView endDateText;
    private SwitchMaterial activeSwitch;
    private MaterialButton saveButton;

    private Date startDate;
    private Date endDate;

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

        // Initialize views
        initializeViews(view);

        // Setup discount type dropdown
        String[] discountTypes = {"PERCENTAGE", "FIXED_AMOUNT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, discountTypes);
        discountTypeAutoComplete.setAdapter(adapter);

        // Setup date pickers
        setupDatePickers();

        // Setup save button
        saveButton.setOnClickListener(v -> saveVoucher());

        // Observe LiveData
        viewModel.getOperationResult().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(requireContext(), "Voucher created successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigateUp();
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews(View view) {
        voucherCodeEditText = view.findViewById(R.id.voucherCodeEditText);
        voucherNameEditText = view.findViewById(R.id.voucherNameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        discountTypeAutoComplete = view.findViewById(R.id.discountTypeAutoComplete);
        discountValueEditText = view.findViewById(R.id.discountValueEditText);
        minOrderValueEditText = view.findViewById(R.id.minOrderValueEditText);
        quantityEditText = view.findViewById(R.id.quantityEditText);
        startDateText = view.findViewById(R.id.startDateText);
        endDateText = view.findViewById(R.id.endDateText);
        activeSwitch = view.findViewById(R.id.activeSwitch);
        saveButton = view.findViewById(R.id.saveButton);
    }

    private void setupDatePickers() {
        Calendar calendar = Calendar.getInstance();
        
        startDateText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        startDate = calendar.getTime();
                        startDateText.setText(formatDate(startDate));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        endDateText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        endDate = calendar.getTime();
                        endDateText.setText(formatDate(endDate));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void saveVoucher() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Create voucher object
        Voucher voucher = new Voucher(
                UUID.randomUUID().toString(),
                voucherCodeEditText.getText().toString(),
                voucherNameEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                discountTypeAutoComplete.getText().toString(),
                Double.parseDouble(discountValueEditText.getText().toString()),
                Double.parseDouble(minOrderValueEditText.getText().toString()),
                Integer.parseInt(quantityEditText.getText().toString()),
                0, // usedQuantity starts at 0
                startDate,
                endDate,
                activeSwitch.isChecked()
        );

        // Save voucher
        viewModel.createVoucher(voucher);
    }

    private boolean validateInputs() {
        if (voucherCodeEditText.getText().toString().isEmpty()) {
            voucherCodeEditText.setError("Voucher code is required");
            return false;
        }
        if (voucherNameEditText.getText().toString().isEmpty()) {
            voucherNameEditText.setError("Voucher name is required");
            return false;
        }
        if (discountTypeAutoComplete.getText().toString().isEmpty()) {
            discountTypeAutoComplete.setError("Discount type is required");
            return false;
        }
        if (discountValueEditText.getText().toString().isEmpty()) {
            discountValueEditText.setError("Discount value is required");
            return false;
        }
        if (minOrderValueEditText.getText().toString().isEmpty()) {
            minOrderValueEditText.setError("Minimum order value is required");
            return false;
        }
        if (quantityEditText.getText().toString().isEmpty()) {
            quantityEditText.setError("Quantity is required");
            return false;
        }
        if (startDate == null) {
            Toast.makeText(requireContext(), "Please select start date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endDate == null) {
            Toast.makeText(requireContext(), "Please select end date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endDate.before(startDate)) {
            Toast.makeText(requireContext(), "End date must be after start date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String formatDate(Date date) {
        return new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                .format(date);
    }
} 