package com.vkbao.travelbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.R;
import com.vkbao.travelbooking.databinding.VoucherSelectionItemBinding;

import java.util.List;

public class VoucherSelectionAdapter extends RecyclerView.Adapter<VoucherSelectionAdapter.VoucherSelectionViewHolder>{
    private final List<Voucher> vouchers;
    private final List<Voucher> selectedVouchers;
    private final OnItemChange<List<Voucher>> onItemChange;
    private Context context;

    public VoucherSelectionAdapter(List<Voucher> vouchers, List<Voucher> selectedVouchers, OnItemChange<List<Voucher>> onItemChange) {
        this.vouchers = vouchers;
        this.selectedVouchers = selectedVouchers;
        this.onItemChange = onItemChange;
    }

    @NonNull
    @Override
    public VoucherSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        VoucherSelectionItemBinding binding = VoucherSelectionItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new VoucherSelectionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherSelectionViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);

        String value = "";
        String bannerValue = "";
        if (voucher.getVoucher_type().equals("percent")) {
            value = String.format(context.getString(R.string.discount_percent), String.valueOf(voucher.getVoucher_value()));
            bannerValue = String.format(context.getString(R.string.voucher_banner_percent), String.valueOf(voucher.getVoucher_value()));
        } else {
            value = String.format(context.getString(R.string.discount_value), String.valueOf(voucher.getVoucher_value()));
            bannerValue = String.format(context.getString(R.string.voucher_banner_fixed), String.valueOf(voucher.getVoucher_value()));
        }
        holder.setTitle(value);
        holder.setBannerValue(bannerValue);
        holder.setStartDate(voucher.getStart_at());
        holder.setEndDate(voucher.getEnd_at());

        boolean checked = selectedVouchers.contains(voucher);
        holder.binding.checkbox.setChecked(checked);
        holder.binding.checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                selectedVouchers.add(voucher);
            } else {
                selectedVouchers.remove(voucher);
            }

            if (onItemChange != null) {
                onItemChange.onChange(selectedVouchers);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public class VoucherSelectionViewHolder extends RecyclerView.ViewHolder {
        private final VoucherSelectionItemBinding binding;

        public VoucherSelectionViewHolder(@NonNull VoucherSelectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBannerValue(String value) {
            binding.bannerValue.setText(value);
        }

        public void setTitle(String title) {
            binding.voucherTitle.setText(title);
        }

        public void setStartDate(String startDate) {
            String str = String.format(context.getString(R.string.start_date), startDate);
            binding.startDate.setText(str);
        }

        public void setEndDate(String endDate) {
            String str = String.format(context.getString(R.string.end_date), endDate);
            binding.endDate.setText(str);
        }
    }

    public interface OnItemChange<T>{
        public void onChange(T value);
    }
}
