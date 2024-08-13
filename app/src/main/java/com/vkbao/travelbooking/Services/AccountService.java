package com.vkbao.travelbooking.Services;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface AccountService {
    @DELETE("/deleteUser")
    Call<Void> deleteAccount(@Query("uid") String uid);
}