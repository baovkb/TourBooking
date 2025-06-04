package com.vkbao.travelbooking.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VoucherRepository {
    private DatabaseReference reference;

    private MutableLiveData<List<Voucher>> filterVouchers;

    public VoucherRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Voucher");
        filterVouchers = new MutableLiveData<>(new ArrayList<>());
    }

    public String createID() {
        return reference.push().getKey();
    }

    public LiveData<List<Voucher>> getAllVouchers() {
        MutableLiveData<List<Voucher>> allVouchers = new MutableLiveData<>(new ArrayList<>());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Voucher> voucherList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        voucherList.add(dataSnapshot.getValue(Voucher.class));
                    }
                }

                allVouchers.setValue(voucherList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                allVouchers.setValue(new ArrayList<>());
            }
        });

        return allVouchers;
    }

    public void getVoucherByName(String name) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Voucher> voucherList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Voucher voucher = dataSnapshot.getValue(Voucher.class);
                        //filter
                        if (voucher.getName().toLowerCase().contains(name.toLowerCase())) {
                            voucherList.add(voucher);
                        }
                    }
                }

                filterVouchers.setValue(voucherList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                filterVouchers.setValue(new ArrayList<>());
            }
        });
    }

    public void getActiveFilterVouchers(String name) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Voucher> voucherList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Voucher voucher = dataSnapshot.getValue(Voucher.class);
                        //filter
                        if (voucher.getIs_active() && voucher.getName().toLowerCase().contains(name.toLowerCase())) {
                            voucherList.add(voucher);
                        }
                    }
                }

                filterVouchers.setValue(voucherList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                filterVouchers.setValue(new ArrayList<>());
            }
        });
    }

    public void getExpiredFilterVouchers(String name) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Voucher> voucherList = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Voucher voucher = dataSnapshot.getValue(Voucher.class);
                        //filter
                        if (Helper.compareTimeStrings(Helper.getCurrentTimeString(), voucher.getEnd_at()) > 0 && voucher.getName().toLowerCase().contains(name.toLowerCase())) {
                            voucherList.add(voucher);
                        }
                    }
                }

                filterVouchers.setValue(voucherList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                filterVouchers.setValue(new ArrayList<>());
            }
        });
    }

    public LiveData<List<Voucher>> getFilterVouchers() {
        return this.filterVouchers;
    }

    public LiveData<List<Voucher>> getAvailableVouchers() {
        MediatorLiveData<List<Voucher>> liveDataMerger = new MediatorLiveData<>();

        liveDataMerger.addSource(getAllVouchers(), allVouchers -> {
            List<Voucher> availableVouchers = new ArrayList<>();

            allVouchers.forEach(voucher -> {
                String startTime = voucher.getStart_at();
                String endTime = voucher.getEnd_at();
                String currentTime = Helper.getCurrentTimeString();

                Boolean isValid = Helper.compareTimeStrings(currentTime, startTime) > 0 && Helper.compareTimeStrings(currentTime, endTime) < 0;
                if (voucher.getIs_active() && isValid) {
                    availableVouchers.add(voucher);
                }
            });

            liveDataMerger.setValue(availableVouchers);
        });

        return liveDataMerger;
    }

    public CompletableFuture<Boolean> addVoucher(Voucher voucher) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(voucher.getVoucher_id()).setValue(voucher).addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });
            return future;
        }).thenCompose(success -> success);
    }

    public CompletableFuture<Boolean> deleteVoucher(String voucherId) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(voucherId).removeValue().addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });
            return future;
        }).thenCompose(success -> success);
    }
}
