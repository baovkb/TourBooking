package com.vkbao.travelbooking.Repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vkbao.travelbooking.Models.Cart;

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

    public CompletableFuture<Cart> getCartByID(String cartID) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Cart> future = new CompletableFuture<>();

            reference.child(cartID).addValueEventListener(new ValueEventListener() {
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

    public CompletableFuture<Boolean> addCartItem(String cartID, Cart.CartItem cartItem) {
        return CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            reference.child(cartID).child(cartItem.getItem_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Cart.CartItem oldCartItem = snapshot.getValue(Cart.CartItem.class);
                        oldCartItem.setQuantity(oldCartItem.getQuantity() + cartItem.getQuantity());

                        reference.child(cartID).child(oldCartItem.getItem_id()).setValue(oldCartItem).addOnCompleteListener(task -> {
                            future.complete(task.isSuccessful());
                        });
                    } else {
                        reference.child(cartID).child(cartItem.getItem_id()).setValue(cartItem).addOnCompleteListener(task -> {
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
