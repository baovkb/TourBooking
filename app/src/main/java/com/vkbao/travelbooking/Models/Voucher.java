package com.vkbao.travelbooking.Models;

import java.util.Date;

public class Voucher {
    private String voucherId;
    private String voucherCode;
    private String voucherName;
    private String description;
    private String discountType; // "PERCENTAGE" or "FIXED_AMOUNT"
    private double discountValue;
    private double minOrderValue;
    private int totalQuantity;
    private int usedQuantity;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    // Default constructor for Firebase
    public Voucher() {}

    // Constructor with all fields
    public Voucher(String voucherId, String voucherCode, String voucherName, String description,
                  String discountType, double discountValue, double minOrderValue,
                  int totalQuantity, int usedQuantity, Date startDate, Date endDate,
                  boolean isActive) {
        this.voucherId = voucherId;
        this.voucherCode = voucherCode;
        this.voucherName = voucherName;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderValue = minOrderValue;
        this.totalQuantity = totalQuantity;
        this.usedQuantity = usedQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getVoucherId() { return voucherId; }
    public void setVoucherId(String voucherId) { this.voucherId = voucherId; }

    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    public String getVoucherName() { return voucherName; }
    public void setVoucherName(String voucherName) { this.voucherName = voucherName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }

    public double getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(double minOrderValue) { this.minOrderValue = minOrderValue; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public int getUsedQuantity() { return usedQuantity; }
    public void setUsedQuantity(int usedQuantity) { this.usedQuantity = usedQuantity; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isValid() {
        Date now = new Date();
        return isActive && 
               now.after(startDate) && 
               now.before(endDate) && 
               usedQuantity < totalQuantity;
    }

    public double calculateDiscount(double orderValue) {
        if (orderValue < minOrderValue) return 0;
        
        if (discountType.equals("PERCENTAGE")) {
            return orderValue * (discountValue / 100);
        } else {
            return Math.min(discountValue, orderValue);
        }
    }
} 