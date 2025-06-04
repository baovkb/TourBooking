package com.vkbao.travelbooking.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.Models.UserVoucher;
import com.vkbao.travelbooking.Repositories.VoucherRepository;
import java.util.List;

public class VoucherViewModel extends ViewModel {
    private final VoucherRepository voucherRepository;
    private final MutableLiveData<List<Voucher>> vouchersLiveData;
    private final MutableLiveData<Boolean> operationResultLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<Voucher> selectedVoucherLiveData;

    public VoucherViewModel(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
        this.vouchersLiveData = new MutableLiveData<>();
        this.operationResultLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.selectedVoucherLiveData = new MutableLiveData<>();
    }

    // CRUD Operations
    public void createVoucher(Voucher voucher) {
        voucherRepository.createVoucher(voucher, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(result);
                loadVouchers(); // Refresh the list
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    public void updateVoucher(Voucher voucher) {
        voucherRepository.updateVoucher(voucher, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(result);
                loadVouchers(); // Refresh the list
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    public void deleteVoucher(String voucherId) {
        voucherRepository.deleteVoucher(voucherId, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(result);
                loadVouchers(); // Refresh the list
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    // Load vouchers
    public void loadVouchers() {
        voucherRepository.getAllVouchers(new VoucherRepository.Callback<List<Voucher>>() {
            @Override
            public void onSuccess(List<Voucher> vouchers) {
                vouchersLiveData.postValue(vouchers);
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    // Search and filter
    public void searchVouchers(String query) {
        voucherRepository.searchVouchers(query, new VoucherRepository.Callback<List<Voucher>>() {
            @Override
            public void onSuccess(List<Voucher> vouchers) {
                vouchersLiveData.postValue(vouchers);
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    public void getActiveVouchers() {
        voucherRepository.getActiveVouchers(new VoucherRepository.Callback<List<Voucher>>() {
            @Override
            public void onSuccess(List<Voucher> vouchers) {
                vouchersLiveData.postValue(vouchers);
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    // Voucher assignment
    public void assignVoucherToUser(String voucherId, String userId) {
        voucherRepository.assignVoucherToUser(voucherId, userId, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(result);
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    // Voucher validation
    public void validateVoucher(String voucherCode, String userId) {
        voucherRepository.validateVoucher(voucherCode, userId, new VoucherRepository.Callback<Voucher>() {
            @Override
            public void onSuccess(Voucher voucher) {
                selectedVoucherLiveData.postValue(voucher);
            }

            @Override
            public void onError(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    // LiveData getters
    public LiveData<List<Voucher>> getVouchers() {
        return vouchersLiveData;
    }

    public LiveData<Boolean> getOperationResult() {
        return operationResultLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Voucher> getSelectedVoucher() {
        return selectedVoucherLiveData;
    }
} 