package com.vkbao.travelbooking.Models;

public class Vouchers {
    private String voucher_id;
    private String create_at;
    private String start_at;
    private String end_at;
    private String description;
    private String is_active;
    private String voucher_type;
    private int voucher_value;
    private int quantity;

    public Vouchers() {}

    public Vouchers(String voucher_id, String create_at, String start_at, String end_at, String description, String is_active, String voucher_type, int voucher_value, int quantity) {
        this.voucher_id = voucher_id;
        this.create_at = create_at;
        this.start_at = start_at;
        this.end_at = end_at;
        this.description = description;
        this.is_active = is_active;
        this.voucher_type = voucher_type;
        this.voucher_value = voucher_value;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVoucher_value() {
        return voucher_value;
    }

    public void setVoucher_value(int voucher_value) {
        this.voucher_value = voucher_value;
    }

    public String getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(String voucher_type) {
        this.voucher_type = voucher_type;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getVoucher_id() {
        return voucher_id;
    }

    public void setVoucher_id(String voucher_id) {
        this.voucher_id = voucher_id;
    }
}

