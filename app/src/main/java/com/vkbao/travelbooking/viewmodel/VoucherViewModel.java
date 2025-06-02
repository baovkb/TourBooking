package com.vkbao.travelbooking.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vkbao.travelbooking.model.User;
import com.vkbao.travelbooking.model.Voucher;
import com.vkbao.travelbooking.repository.VoucherRepository;

import java.util.ArrayList;
import java.util.List;

public class VoucherViewModel extends ViewModel {
    private final VoucherRepository voucherRepository;
    private final MutableLiveData<List<Voucher>> vouchersLiveData;
    private final MutableLiveData<List<User>> usersLiveData;
    private final MutableLiveData<Boolean> operationResultLiveData;
    private List<User> allUsers;

    public VoucherViewModel() {
        voucherRepository = new VoucherRepository();
        vouchersLiveData = new MutableLiveData<>(new ArrayList<>());
        usersLiveData = new MutableLiveData<>(new ArrayList<>());
        operationResultLiveData = new MutableLiveData<>();
        allUsers = new ArrayList<>();
    }

    public LiveData<List<Voucher>> getVouchers() {
        return vouchersLiveData;
    }

    public LiveData<List<User>> getUsers() {
        return usersLiveData;
    }

    public LiveData<Boolean> getOperationResult() {
        return operationResultLiveData;
    }

    public void loadVouchers() {
        voucherRepository.getAllVouchers(new VoucherRepository.Callback<List<Voucher>>() {
            @Override
            public void onSuccess(List<Voucher> result) {
                vouchersLiveData.postValue(result);
            }

            @Override
            public void onError(Exception e) {
                operationResultLiveData.postValue(false);
            }
        });
    }

    public void loadUsers() {
        voucherRepository.getAllUsers(new VoucherRepository.Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                allUsers = result;
                usersLiveData.postValue(result);
            }

            @Override
            public void onError(Exception e) {
                operationResultLiveData.postValue(false);
            }
        });
    }

    public void filterUsers(String query) {
        if (query.isEmpty()) {
            usersLiveData.setValue(allUsers);
            return;
        }

        List<User> filteredUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        usersLiveData.setValue(filteredUsers);
    }

    public void createVoucher(Voucher voucher) {
        voucherRepository.createVoucher(voucher, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(true);
                loadVouchers(); // Refresh the list
            }

            @Override
            public void onError(Exception e) {
                operationResultLiveData.postValue(false);
            }
        });
    }

    public void updateVoucher(Voucher voucher) {
        voucherRepository.updateVoucher(voucher, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(true);
                loadVouchers(); // Refresh the list
            }

            @Override
            public void onError(Exception e) {
                operationResultLiveData.postValue(false);
            }
        });
    }

    public void deleteVoucher(String voucherId) {
        voucherRepository.deleteVoucher(voucherId, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(true);
                loadVouchers(); // Refresh the list
            }

            @Override
            public void onError(Exception e) {
                operationResultLiveData.postValue(false);
            }
        });
    }

    public void assignVoucherToUsers(String voucherId, List<String> userIds) {
        voucherRepository.assignVoucherToUsers(voucherId, userIds, new VoucherRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                operationResultLiveData.postValue(true);
            }

            @Override
            public void onError(Exception e) {
                operationResultLiveData.postValue(false);
            }
        });
    }

    public void validateVoucher(String voucherCode, String userId) {
        voucherRepository.validateVoucher(voucherCode, userId, new VoucherRepository.Callback<Voucher>() {
            @Override
            public void onSuccess(Voucher result) {
                // Handle successful validation
            }

            @Override
            public void onError(Exception e) {
                // Handle validation error
            }
        });
    }
} 