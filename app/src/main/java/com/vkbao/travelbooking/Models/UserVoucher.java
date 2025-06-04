package com.vkbao.travelbooking.Models;

import java.util.Date;

public class UserVoucher {
    private String userVoucherId;
    private String userId;
    private String voucherId;
    private boolean isUsed;
    private Date assignedDate;
    private Date usedDate;

    // Default constructor for Firebase
    public UserVoucher() {}

    // Constructor with all fields
    public UserVoucher(String userVoucherId, String userId, String voucherId) {
        this.userVoucherId = userVoucherId;
        this.userId = userId;
        this.voucherId = voucherId;
        this.isUsed = false;
        this.assignedDate = new Date();
        this.usedDate = null;
    }

    // Getters and Setters
    public String getUserVoucherId() { return userVoucherId; }
    public void setUserVoucherId(String userVoucherId) { this.userVoucherId = userVoucherId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVoucherId() { return voucherId; }
    public void setVoucherId(String voucherId) { this.voucherId = voucherId; }

    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }

    public Date getAssignedDate() { return assignedDate; }
    public void setAssignedDate(Date assignedDate) { this.assignedDate = assignedDate; }

    public Date getUsedDate() { return usedDate; }
    public void setUsedDate(Date usedDate) { this.usedDate = usedDate; }

    // Helper method to mark voucher as used
    public void markAsUsed() {
        this.isUsed = true;
        this.usedDate = new Date();
    }
} 