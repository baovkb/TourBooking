package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.Repositories.VoucherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VoucherViewModel extends AndroidViewModel {
    private VoucherRepository voucherRepository;
    private List<Voucher> selectedVoucher;

    public VoucherViewModel(@NonNull Application application) {
        super(application);
        voucherRepository = new VoucherRepository();
        selectedVoucher = new ArrayList<>();
    }

    public String createID() {
        return voucherRepository.createID();
    }

    public CompletableFuture<Boolean> addVoucher(Voucher voucher) {
        return voucherRepository.addVoucher(voucher);
    }

    public LiveData<List<Voucher>> getAllVouchers() {
        return voucherRepository.getAllVouchers();
    }

    public void getVoucherByName(String name) {
        voucherRepository.getVoucherByName(name);
    }

    public void getActiveVouchersByName(String name) {
        voucherRepository.getActiveFilterVouchers(name);
    }

    public void getExpiredVouchersByName(String name) {
        voucherRepository.getExpiredFilterVouchers(name);
    }

    public LiveData<List<Voucher>> getFilterVouchers() {
        return voucherRepository.getFilterVouchers();
    }

    public LiveData<List<Voucher>> getAvailableVouchers() {
        return voucherRepository.getAvailableVouchers();
    }

    public List<Voucher> getSelectedVoucher() {
        return selectedVoucher;
    }

    public void setSelectedVoucher(List<Voucher> selectedVoucher) {
        this.selectedVoucher = selectedVoucher;
    }

    public CompletableFuture<Boolean> deleteVoucher(String voucherId) {
        return voucherRepository.deleteVoucher(voucherId);
    }
}
