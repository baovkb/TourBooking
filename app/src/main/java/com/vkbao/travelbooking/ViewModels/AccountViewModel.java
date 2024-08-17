package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseUser;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.Repositories.AccountRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;
    private LiveData<List<Account>> allAccount;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository();
        allAccount = accountRepository.getAllAccount();
    }

    public LiveData<List<Account>> getAllAccount() {
        return allAccount;
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return accountRepository.getCurrentUser();
    }

    public void signupUser(String email, String password, Callback<String> callback) {
        accountRepository.signupUser(email, password, callback);
    }

    public LiveData<Account> getAccountByUID(String uid) {
        return accountRepository.getAccountByUID(uid);
    }

    public void getAccountByUID(String uid, Callback<Account> callback) {
        accountRepository.getAccountByUID(uid, callback);
    }

    public CompletableFuture<Account> getAccountByUIDFuture(String uid) {
        return accountRepository.getAccountByUIDFuture(uid);
    }

    public void loginUser(String email, String password, Callback<Boolean> callback) {
        accountRepository.loginUser(email, password, callback);
    }

    public void logoutUser() {
        accountRepository.logoutUser();
    }

    public void deleteUser(String accountID, Callback<Boolean> callback) {
        accountRepository.deleteUser(accountID, callback);
    }

    public CompletableFuture<Boolean> updateUser(Account newAccount) {
        return accountRepository.updateUser(newAccount);
    }

    public FirebaseUser getUser() {
        return accountRepository.getUser();
    }

    public CompletableFuture<String> forgotPassword(String email) {
        return accountRepository.forgotPassword(email);
    }
}
