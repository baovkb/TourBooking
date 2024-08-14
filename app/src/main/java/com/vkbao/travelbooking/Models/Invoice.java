package com.vkbao.travelbooking.Models;

public class Invoice {
    private String invoice_id;
    private String order_id;
    private String payment_status;
    private String timestamp;
    private int amount;

    public enum PaymentStatus {
        Pending,
        Rejected,
        Paid
    }

    public Invoice() {
    }

    public Invoice(Invoice another) {
        this.invoice_id = another.invoice_id;
        this.order_id = another.order_id;
        this.payment_status = another.payment_status;
        this.timestamp = another.timestamp;
        this.amount = another.amount;
    }

    public Invoice(String invoice_id, String order_id, String payment_status, String timestamp, int amount) {
        this.invoice_id = invoice_id;
        this.order_id = order_id;
        this.payment_status = payment_status;
        this.timestamp = timestamp;
        this.amount = amount;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
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
}
