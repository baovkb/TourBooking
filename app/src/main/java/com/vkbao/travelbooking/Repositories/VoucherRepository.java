package com.vkbao.travelbooking.Repositories;

import com.vkbao.travelbooking.Models.Voucher;
import com.vkbao.travelbooking.Models.UserVoucher;
import java.util.List;

public interface VoucherRepository {
    // CRUD operations for vouchers
    void createVoucher(Voucher voucher, Callback<Boolean> callback);
    void getAllVouchers(Callback<List<Voucher>> callback);
    void getVoucherById(String voucherId, Callback<Voucher> callback);
    void updateVoucher(Voucher voucher, Callback<Boolean> callback);
    void deleteVoucher(String voucherId, Callback<Boolean> callback);
    
    // Voucher assignment and user operations
    void assignVoucherToUser(String voucherId, String userId, Callback<Boolean> callback);
    void getUserVouchers(String userId, Callback<List<UserVoucher>> callback);
    void getVoucherByCode(String voucherCode, Callback<Voucher> callback);
    
    // Voucher usage and validation
    void useVoucher(String userVoucherId, Callback<Boolean> callback);
    void validateVoucher(String voucherCode, String userId, Callback<Voucher> callback);
    
    // Search and filter operations
    void searchVouchers(String query, Callback<List<Voucher>> callback);
    void getActiveVouchers(Callback<List<Voucher>> callback);
    void getExpiredVouchers(Callback<List<Voucher>> callback);
    
    // Callback interface for async operations
    interface Callback<T> {
        void onSuccess(T result);
        void onError(String error);
    }
} 