package com.vkbao.travelbooking.Models;

import java.util.List;
import java.util.Map;

public class Cart {
    private String cart_id;
    private String account_id;
    private Map<String, CartItem> item;

    public Cart() {
    }

    public Cart(String cart_id, String account_id, Map<String, CartItem> item) {
        this.cart_id = cart_id;
        this.account_id = account_id;
        this.item = item;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public Map<String, CartItem> getItem() {
        return item;
    }

    public void setItem(Map<String, CartItem> item) {
        this.item = item;
    }

    public static class CartItem {
        String item_id;
        int quantity;

        public CartItem() {
        }

        public CartItem(String item_id, int quantity) {
            this.item_id = item_id;
            this.quantity = quantity;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}


