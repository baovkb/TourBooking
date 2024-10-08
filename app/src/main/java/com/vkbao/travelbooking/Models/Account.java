package com.vkbao.travelbooking.Models;

public class Account {
    private String uid;
    private String email;
    private String name;
    private String role;
    private String create_at;
    private String cart_id;

    public Account() {
    }

    public String getCreate_at() {
        return create_at;
    }

    public Account(String uid, String email, String name, String role, String create_at, String cart_id) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.role = role;
        this.create_at = create_at;
        this.cart_id = cart_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
