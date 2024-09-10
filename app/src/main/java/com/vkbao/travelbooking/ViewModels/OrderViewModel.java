package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vkbao.travelbooking.Models.Order;
import com.vkbao.travelbooking.Repositories.OrderRepository;

import java.util.concurrent.CompletableFuture;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository orderRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);

        orderRepository = new OrderRepository();
    }

    public CompletableFuture<Boolean> addOrder(Order order) {
        return orderRepository.addOrder(order);
    }

    public String createID() {
        return orderRepository.createID();
    }
}