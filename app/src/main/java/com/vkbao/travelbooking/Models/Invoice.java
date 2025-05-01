package com.vkbao.travelbooking.Models;

import java.util.Collections;
import java.util.List;

public class Invoice {
    private String invoice_id;
    private String order_id;
    private String payment_status;
    private String timestamp;
    private int subtotal;
    private int total;
    private List<String> voucher_ids;

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getVoucher_ids() {
        return voucher_ids;
    }

    public void setVoucher_ids(List<String> voucher_ids) {
        this.voucher_ids = voucher_ids;
    }

    public Invoice(
            String invoice_id,
            String order_id,
            String payment_status,
            String timestamp,
            int subtotal,
            int total,
            List<String> voucher_ids
    ) {
        this.invoice_id = invoice_id;
        this.order_id = order_id;
        this.payment_status = payment_status;
        this.timestamp = timestamp;
        this.subtotal = subtotal;
        this.total = total;
        this.voucher_ids = voucher_ids;
    }

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
        this.subtotal = another.subtotal;
        this.total = another.total;
        Collections.copy(another.voucher_ids, this.voucher_ids);
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
}
