package com.vkbao.travelbooking.Models;

import androidx.annotation.Nullable;

import com.vkbao.travelbooking.R;

import java.io.Serializable;

public class Voucher implements Serializable {
    private String voucher_id;
    private String create_at;
    private String start_at;
    private String end_at;
    private String description;
    private Boolean is_active;
    private String voucher_type;
    private int voucher_value;
    private int quantity;
    private String name;

    public Voucher() {}

    public Voucher(
            String voucher_id,
            String create_at,
            String start_at,
            String end_at,
            String description,
            Boolean is_active,
            String voucher_type,
            int voucher_value,
            int quantity,
            String name
    ) {
        this.voucher_id = voucher_id;
        this.create_at = create_at;
        this.start_at = start_at;
        this.end_at = end_at;
        this.description = description;
        this.is_active = is_active;
        this.voucher_type = voucher_type;
        this.voucher_value = voucher_value;
        this.quantity = quantity;
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof Voucher)) return false;

        Voucher v = (Voucher) obj;
        return v.getVoucher_id().equals(this.voucher_id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
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

