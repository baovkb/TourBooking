package com.vkbao.travelbooking.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Helper.Helper;
import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.Models.Role;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountRepository {
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private final String TAG = "AccountRepository";

    public AccountRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Account");
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<List<Account>> getAllAccount() {
        MutableLiveData<List<Account>> allAccount = new MutableLiveData<>(new ArrayList<>());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Account> accountList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot != null) {
                        accountList.add(dataSnapshot.getValue(Account.class));
                    }
                }

                allAccount.setValue(accountList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return allAccount;
    }

    public LiveData<Account> getAccountByUID(String uid) {
        MutableLiveData<Account> accountMutableLiveData = new MutableLiveData<>();
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    accountMutableLiveData.setValue(snapshot.getValue(Account.class));
                } else accountMutableLiveData.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return accountMutableLiveData;
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser.setValue(mAuth.getCurrentUser());
            }
        });
        return currentUser;
    }

    public void signupUser(String email, String password, Callback<String> callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String create_at = Helper.getCurrentTimeString();
                        Account account = new Account(user.getUid(), user.getEmail(), "", Role.User.name(), create_at);
                        reference.child(user.getUid()).setValue(account)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful())
                                        callback.onComplete("200");
                                    else {
                                        Exception exception = task1.getException();
                                        if (exception instanceof FirebaseAuthException) {
                                            String error_code = ((FirebaseAuthException) exception).getErrorCode();

                                            Log.d(TAG, exception.toString());
                                            callback.onComplete(error_code);
                                        } else {
                                            callback.onComplete(task1.getException().getMessage());
                                        }
                                    }
                                });

                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthException) {
                            String error_code = ((FirebaseAuthException) exception).getErrorCode();

                            Log.d(TAG, exception.toString());
                            callback.onComplete(error_code);
                        } else {
                            callback.onComplete(task.getException().getMessage());
                        }
                    }
                });
    }

    public void loginUser(String email, String password, Callback<Boolean> callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> callback.onComplete(task.isSuccessful()));
    }

    public void logoutUser() {
        mAuth.signOut();
    }
}