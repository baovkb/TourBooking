package com.vkbao.travelbooking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.model.Voucher;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VoucherAdminAdapter extends RecyclerView.Adapter<VoucherAdminAdapter.VoucherViewHolder> {
    private List<Voucher> vouchers;
    private final OnVoucherActionListener listener;
    private final SimpleDateFormat dateFormat;

    public interface OnVoucherActionListener {
        void onEditClick(Voucher voucher);
        void onDeleteClick(Voucher voucher);
        void onAssignClick(Voucher voucher);
    }

    public VoucherAdminAdapter(List<Voucher> vouchers, OnVoucherActionListener listener) {
        this.vouchers = vouchers;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voucher_admin_item, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);
        holder.bind(voucher);
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public void updateVouchers(List<Voucher> newVouchers) {
        this.vouchers = newVouchers;
        notifyDataSetChanged();
    }

    class VoucherViewHolder extends RecyclerView.ViewHolder {
        private final TextView voucherNameText;
        private final TextView voucherCodeText;
        private final TextView discountText;
        private final TextView validityText;
        private final Chip statusChip;
        private final ImageButton editButton;
        private final ImageButton assignButton;
        private final ImageButton deleteButton;

        VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            voucherNameText = itemView.findViewById(R.id.voucherNameText);
            voucherCodeText = itemView.findViewById(R.id.voucherCodeText);
            discountText = itemView.findViewById(R.id.discountText);
            validityText = itemView.findViewById(R.id.validityText);
            statusChip = itemView.findViewById(R.id.statusChip);
            editButton = itemView.findViewById(R.id.editButton);
            assignButton = itemView.findViewById(R.id.assignButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        void bind(Voucher voucher) {
            voucherNameText.setText(voucher.getVoucherName());
            voucherCodeText.setText(voucher.getVoucherCode());
            
            String discountValue = voucher.getDiscountType().equals("PERCENTAGE") 
                ? voucher.getDiscountValue() + "% off"
                : "$" + voucher.getDiscountValue() + " off";
            discountText.setText(discountValue);
            
            String validity = "Valid until: " + dateFormat.format(voucher.getEndDate());
            validityText.setText(validity);
            
            statusChip.setText(voucher.isActive() ? "Active" : "Inactive");
            statusChip.setChipBackgroundColorResource(
                voucher.isActive() ? R.color.success : R.color.error
            );

            editButton.setOnClickListener(v -> listener.onEditClick(voucher));
            assignButton.setOnClickListener(v -> listener.onAssignClick(voucher));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(voucher));
        }
    }
} 