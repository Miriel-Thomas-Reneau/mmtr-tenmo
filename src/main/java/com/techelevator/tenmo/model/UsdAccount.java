package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class UsdAccount {

    private int usdAccountId;
    private int tenmoAccountId;
    private BigDecimal usdBucksBalance;
    private int user_id;

    public UsdAccount() {}

    public UsdAccount(int usdAccountId, int teAccountId, BigDecimal teBucksBalance, int user_id) {
        this.usdAccountId = usdAccountId;
        this.tenmoAccountId = teAccountId;
        this.usdBucksBalance = teBucksBalance;
        this.user_id = user_id;
    }

    public int getUsdAccountId() {
        return usdAccountId;
    }

    public void setUsdAccountId(int usdAccountId) {
        this.usdAccountId = usdAccountId;
    }

    public int getTenmoAccountId() {
        return tenmoAccountId;
    }

    public void setTenmoAccountId(int tenmoAccountId) {
        this.tenmoAccountId = tenmoAccountId;
    }

    public BigDecimal getUsdBucksBalance() {
        return usdBucksBalance;
    }

    public void setUsdBucksBalance(BigDecimal usdBucksBalance) {
        this.usdBucksBalance = usdBucksBalance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
