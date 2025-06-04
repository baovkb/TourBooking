package com.vkbao.travelbooking.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.vkbao.travelbooking.model.User;
import com.vkbao.travelbooking.model.UserVoucher;
import com.vkbao.travelbooking.model.Voucher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoucherRepository {
    private final FirebaseFirestore db;
    private static final String VOUCHERS_COLLECTION = "vouchers";
    private static final String USER_VOUCHERS_COLLECTION = "user_vouchers";
    private static final String USERS_COLLECTION = "users";

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public VoucherRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void createVoucher(Voucher voucher, Callback<Boolean> callback) {
        voucher.setCreatedAt(new Date());
        voucher.setUpdatedAt(new Date());
        
        db.collection(VOUCHERS_COLLECTION)
            .document(voucher.getVoucherId())
            .set(voucher)
            .addOnSuccessListener(aVoid -> callback.onSuccess(true))
            .addOnFailureListener(callback::onError);
    }

    public void getAllVouchers(Callback<List<Voucher>> callback) {
        db.collection(VOUCHERS_COLLECTION)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Voucher> vouchers = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Voucher voucher = document.toObject(Voucher.class);
                    vouchers.add(voucher);
                }
                callback.onSuccess(vouchers);
            })
            .addOnFailureListener(callback::onError);
    }

    public void getVoucherById(String voucherId, Callback<Voucher> callback) {
        db.collection(VOUCHERS_COLLECTION)
            .document(voucherId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Voucher voucher = documentSnapshot.toObject(Voucher.class);
                    callback.onSuccess(voucher);
                } else {
                    callback.onError(new Exception("Voucher not found"));
                }
            })
            .addOnFailureListener(callback::onError);
    }

    public void updateVoucher(Voucher voucher, Callback<Boolean> callback) {
        voucher.setUpdatedAt(new Date());
        
        db.collection(VOUCHERS_COLLECTION)
            .document(voucher.getVoucherId())
            .set(voucher)
            .addOnSuccessListener(aVoid -> callback.onSuccess(true))
            .addOnFailureListener(callback::onError);
    }

    public void deleteVoucher(String voucherId, Callback<Boolean> callback) {
        db.collection(VOUCHERS_COLLECTION)
            .document(voucherId)
            .delete()
            .addOnSuccessListener(aVoid -> callback.onSuccess(true))
            .addOnFailureListener(callback::onError);
    }

    public void getAllUsers(Callback<List<User>> callback) {
        db.collection(USERS_COLLECTION)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<User> users = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    User user = document.toObject(User.class);
                    users.add(user);
                }
                callback.onSuccess(users);
            })
            .addOnFailureListener(callback::onError);
    }

    public void assignVoucherToUsers(String voucherId, List<String> userIds, Callback<Boolean> callback) {
        List<UserVoucher> userVouchers = new ArrayList<>();
        Date now = new Date();

        for (String userId : userIds) {
            UserVoucher userVoucher = new UserVoucher();
            userVoucher.setUserId(userId);
            userVoucher.setVoucherId(voucherId);
            userVoucher.setAssignedDate(now);
            userVoucher.setUsed(false);
            userVouchers.add(userVoucher);
        }

        // Use batch write to ensure atomicity
        db.runTransaction(transaction -> {
            // Get the voucher to check quantity
            Voucher voucher = transaction.get(db.collection(VOUCHERS_COLLECTION)
                .document(voucherId)).toObject(Voucher.class);

            if (voucher == null) {
                throw new Exception("Voucher not found");
            }

            if (voucher.getUsedQuantity() + userIds.size() > voucher.getTotalQuantity()) {
                throw new Exception("Not enough vouchers available");
            }

            // Update voucher used quantity
            transaction.update(db.collection(VOUCHERS_COLLECTION).document(voucherId),
                "usedQuantity", voucher.getUsedQuantity() + userIds.size());

            // Create user vouchers
            for (UserVoucher userVoucher : userVouchers) {
                transaction.set(db.collection(USER_VOUCHERS_COLLECTION).document(),
                    userVoucher);
            }

            return null;
        })
        .addOnSuccessListener(aVoid -> callback.onSuccess(true))
        .addOnFailureListener(callback::onError);
    }

    public void validateVoucher(String voucherCode, String userId, Callback<Voucher> callback) {
        db.collection(VOUCHERS_COLLECTION)
            .whereEqualTo("voucherCode", voucherCode)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.isEmpty()) {
                    callback.onError(new Exception("Invalid voucher code"));
                    return;
                }

                Voucher voucher = queryDocumentSnapshots.getDocuments().get(0).toObject(Voucher.class);
                if (voucher == null) {
                    callback.onError(new Exception("Invalid voucher"));
                    return;
                }

                // Check if voucher is active
                if (!voucher.isActive()) {
                    callback.onError(new Exception("Voucher is inactive"));
                    return;
                }

                // Check if voucher is expired
                if (voucher.getEndDate().before(new Date())) {
                    callback.onError(new Exception("Voucher has expired"));
                    return;
                }

                // Check if user has this voucher
                db.collection(USER_VOUCHERS_COLLECTION)
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("voucherId", voucher.getVoucherId())
                    .whereEqualTo("isUsed", false)
                    .get()
                    .addOnSuccessListener(userVoucherSnapshots -> {
                        if (userVoucherSnapshots.isEmpty()) {
                            callback.onError(new Exception("You don't have this voucher"));
                        } else {
                            callback.onSuccess(voucher);
                        }
                    })
                    .addOnFailureListener(callback::onError);
            })
            .addOnFailureListener(callback::onError);
    }

    public void useVoucher(String userVoucherId, Callback<Boolean> callback) {
        db.collection(USER_VOUCHERS_COLLECTION)
            .document(userVoucherId)
            .update("isUsed", true, "usedDate", new Date())
            .addOnSuccessListener(aVoid -> callback.onSuccess(true))
            .addOnFailureListener(callback::onError);
    }
} 