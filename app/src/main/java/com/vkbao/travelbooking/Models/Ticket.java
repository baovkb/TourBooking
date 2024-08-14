package com.vkbao.travelbooking.Models;

public class Ticket {
    private String ticket_id;
    private String order_id;
    private String item_id;
    private String account_id;

    public Ticket(String ticket_id, String order_id, String item_id, String account_id) {
        this.ticket_id = ticket_id;
        this.order_id = order_id;
        this.item_id = item_id;
        this.account_id = account_id;
    }

    public Ticket() {
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }
}
