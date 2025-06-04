package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;

import java.util.List;

public class VoucherAdminAdapter extends RecyclerView.Adapter<VoucherAdminAdapter.VoucherViewHolder> {
    private List<Voucher> vouchers;
    private final OnVoucherActionListener listener;
    private Context context;

    public interface OnVoucherActionListener {
        void onEditClick(Voucher voucher);
        void onDeleteClick(Voucher voucher);
    }

    public VoucherAdminAdapter(List<Voucher> vouchers, OnVoucherActionListener listener) {
        this.vouchers = vouchers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_admin_item, parent, false);

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
        private final TextView discountText;
        private final TextView validityText;
        private final Chip statusChip;
        private final ImageView editButton;
        private final ImageView deleteButton;

        VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            voucherNameText = itemView.findViewById(R.id.voucherNameText);
            discountText = itemView.findViewById(R.id.discountText);
            validityText = itemView.findViewById(R.id.validityText);
            statusChip = itemView.findViewById(R.id.statusChip);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        void bind(Voucher voucher) {
            String value = "";
            if (voucher.getVoucher_type().equals("percent")) {
                value = String.format(context.getString(R.string.discount_percent), String.valueOf(voucher.getVoucher_value()));
            } else {
                value = String.format(context.getString(R.string.discount_value), String.valueOf(voucher.getVoucher_value()));
            }

            voucherNameText.setText(voucher.getName());
            discountText.setText(value);

            String validity = "Valid until: " + voucher.getEnd_at();
            validityText.setText(validity);

            statusChip.setText(voucher.getIs_active() ? "Active" : "Inactive");
            statusChip.setChipBackgroundColorResource(
                    voucher.getIs_active() ? R.color.white : R.color.light_gray
            );

            editButton.setOnClickListener(v -> listener.onEditClick(voucher));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(voucher));
        }
    }
}
