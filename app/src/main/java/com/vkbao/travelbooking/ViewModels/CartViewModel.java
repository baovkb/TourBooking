package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.vkbao.travelbooking.Models.Cart;
import com.vkbao.travelbooking.Repositories.CartRepository;

import java.util.concurrent.CompletableFuture;

public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository();
    }

    public String createID() {
        return cartRepository.createID();
    }

    public CompletableFuture<Boolean> createCart(Cart cart) {
        return cartRepository.createCart(cart);
    }

    public CompletableFuture<Cart> getCartByID(String cartID) {
        return cartRepository.getCartByID(cartID);
    }

    public CompletableFuture<Boolean> addCartItem(String cartID, Cart.CartItem cartItem) {
        return cartRepository.addCartItem(cartID, cartItem);
    }

    public CompletableFuture<Boolean> deleteCart(String cartID) {
        return cartRepository.deleteCart(cartID);
    }
}
