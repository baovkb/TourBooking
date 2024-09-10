package com.vkbao.travelbooking.Services;

import com.vkbao.travelbooking.Models.CreateOrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ZaloPayService {
    @FormUrlEncoded
    @POST("/v001/tpe/createorder")
    Call<CreateOrderResponse> createOrder(
            @Field("appid") int appid,
            @Field("appuser") String appuser,
            @Field("apptime") long apptime,
            @Field("amount") long amount,
            @Field("apptransid") String apptransid,
            @Field("embeddata") String embeddata,
            @Field("item") String item,
            @Field("mac") String mac
            );
}
