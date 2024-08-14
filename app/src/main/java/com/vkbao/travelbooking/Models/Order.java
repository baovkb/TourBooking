package com.vkbao.travelbooking.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {
    private String order_id;
    private String account_id;
    private String timestamp;
    private int amount;
    private Map<String, OrderItem> item;

    public Order() {
    }

    public Order(String order_id, String account_id, String timestamp, int amount, Map<String, OrderItem> item) {
        this.order_id = order_id;
        this.account_id = account_id;
        this.timestamp = timestamp;
        this.amount = amount;
        this.item = item;
    }

    protected Order(Parcel in) {
        order_id = in.readString();
        account_id = in.readString();
        timestamp = in.readString();
        amount = in.readInt();
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Map<String, OrderItem> getItem() {
        return item;
    }

    public void setItem(Map<String, OrderItem> item) {
        this.item = item;
    }

    public static class OrderItem {
        private String item_id;
        private int quantity;
        private int unit_price;
        private int total_price;

        public OrderItem(String item_id, int quantity, int unit_price, int total_price) {
            this.item_id = item_id;
            this.quantity = quantity;
            this.unit_price = unit_price;
            this.total_price = total_price;
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

        public int getUnit_price() {
            return unit_price;
        }

        public void setUnit_price(int unit_price) {
            this.unit_price = unit_price;
        }

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }
    }
}

