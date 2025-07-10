package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TenmoAccount {

    private int tenmoAccountId;
    private int userId;
    private BigDecimal teBucksBalance;


    public TenmoAccount() {}

    public TenmoAccount(int teAccountId, int userId, BigDecimal teBucksBalance) {
        this.tenmoAccountId = teAccountId;
        this.userId = userId;
        this.teBucksBalance = teBucksBalance;
    }

    public TenmoAccount(int userId, BigDecimal teBucksBalance) {
        this.userId = userId;
        this.teBucksBalance = teBucksBalance;
    }

    public int getTeAccountId() {
        return tenmoAccountId;
    }

    public void setTeAccountId(int teAccountId) {
        this.tenmoAccountId = teAccountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTeBucksBalance() {
        return teBucksBalance;
    }

    public void setTeBucksBalance(BigDecimal teBucksBalance) {
        this.teBucksBalance = teBucksBalance;
    }
}
