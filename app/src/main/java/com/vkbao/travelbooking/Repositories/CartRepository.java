package com.vkbao.travelbooking.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Cart;
import com.vkbao.travelbooking.Models.Order;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class CartRepository {
    private DatabaseReference reference;

    public CartRepository() {
        reference = FirebaseDatabase.getInstance().getReference("Cart");
    }

    public String createID() {
        return reference.push().getKey();
    }

    public CompletableFuture<Boolean> createCart(Cart cart) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(cart.getCart_id()).setValue(cart).addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(future -> future);
    }

    public CompletableFuture<Cart> getCartByIDFuture(String cartID) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Cart> future = new CompletableFuture<>();

            reference.child(cartID).orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) future.complete(snapshot.getValue(Cart.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    future.complete(null);
                }
            });

            return future;
        }).thenCompose(cart -> cart);
    }

    public LiveData<Cart> getCartByID(String cartID) {
        MutableLiveData<Cart> cartMutableLiveData = new MutableLiveData<>();

        reference.child(cartID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String account_id = snapshot.child("account_id").getValue(String.class);
                    String cart_id = snapshot.child("cart_id").getValue(String.class);
                    Map<String, Cart.CartItem> cartItemMap = snapshot.child("item").getValue(new GenericTypeIndicator<Map<String, Cart.CartItem>>() {});

                    if (cartItemMap != null) {
                        Map<String, Cart.CartItem> sortedCartItemMap = new TreeMap<>(cartItemMap);
                        cartMutableLiveData.setValue(new Cart(cart_id, account_id, sortedCartItemMap));
                    } else {
                        cartMutableLiveData.setValue(new Cart(cart_id, account_id, null));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return cartMutableLiveData;
    }

    public CompletableFuture<Boolean> addCartItem(String cartID, Cart.CartItem cartItem) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            DatabaseReference childRef = reference.child(cartID).child("item").child(cartItem.getItem_id());

            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Cart.CartItem oldCartItem = snapshot.getValue(Cart.CartItem.class);
                        oldCartItem.setQuantity(oldCartItem.getQuantity() + cartItem.getQuantity());

                        childRef.setValue(oldCartItem).addOnCompleteListener(task -> {
                            future.complete(task.isSuccessful());
                        });
                    } else {
                        childRef.setValue(cartItem).addOnCompleteListener(task -> {
                            future.complete(task.isSuccessful());
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    future.complete(false);
                }
            });

            return future;
        }).thenCompose(success -> success);
    }

    public CompletableFuture<Boolean> updateCartItem(String cartID, Cart.CartItem cartItem) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            DatabaseReference childRef = reference.child(cartID).child("item").child(cartItem.getItem_id());

            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        childRef.setValue(cartItem).addOnCompleteListener(task -> {
                            future.complete(task.isSuccessful());
                        });
                    } else {
                        future.complete(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    future.complete(false);
                }
            });

            return future;
        }).thenCompose(success -> success);
    }

    public CompletableFuture<Boolean> deleteCartItem(String cartID, String itemID) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(cartID).child("item").child(itemID).removeValue().addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(success -> success);
    }

    public CompletableFuture<Boolean> deleteCart(String cartID) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();

            reference.child(cartID).removeValue().addOnCompleteListener(task -> {
                future.complete(task.isSuccessful());
            });

            return future;
        }).thenCompose(success -> success);
    }
}
