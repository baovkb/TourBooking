package com.vkbao.travelbooking.Repositories;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VoucherRepository {
    private DatabaseReference reference;

    public VoucherRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Voucher");
    }
}
