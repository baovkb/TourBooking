package com.vkbao.travelbooking.Models;

import java.io.Serializable;

public class CreateOrderResponse implements Serializable {
    int returncode;
    String returnmessage;
    String orderurl;
    String zptranstoken;

    public CreateOrderResponse(int returncode, String returnmessage, String orderurl, String zptranstoken) {
        this.returncode = returncode;
        this.returnmessage = returnmessage;
        this.orderurl = orderurl;
        this.zptranstoken = zptranstoken;
    }

    public int getReturncode() {
        return returncode;
    }

    public void setReturncode(int returncode) {
        this.returncode = returncode;
    }

    public String getReturnmessage() {
        return returnmessage;
    }

    public void setReturnmessage(String returnmessage) {
        this.returnmessage = returnmessage;
    }

    public String getOrderurl() {
        return orderurl;
    }

    public void setOrderurl(String orderurl) {
        this.orderurl = orderurl;
    }

    public String getZptranstoken() {
        return zptranstoken;
    }

    public void setZptranstoken(String zptranstoken) {
        this.zptranstoken = zptranstoken;
    }
}
