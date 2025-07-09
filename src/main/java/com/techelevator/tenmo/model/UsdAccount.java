package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class UsdAccount {

    private int usdAccountId;
    private int teAccountId;
    private BigDecimal teBucksBalance;
    private int user_id;

    public UsdAccount () {}

    public UsdAccount(int usdAccountId, int teAccountId, BigDecimal teBucksBalance, int user_id) {
        this.usdAccountId = usdAccountId;
        this.teAccountId = teAccountId;
        this.teBucksBalance = teBucksBalance;
        this.user_id = user_id;
    }

    public int getUsdAccountId() {
        return usdAccountId;
    }

    public void setUsdAccountId(int usdAccountId) {
        this.usdAccountId = usdAccountId;
    }

    public int getTeAccountId() {
        return teAccountId;
    }

    public void setTeAccountId(int teAccountId) {
        this.teAccountId = teAccountId;
    }

    public BigDecimal getTeBucksBalance() {
        return teBucksBalance;
    }

    public void setTeBucksBalance(BigDecimal teBucksBalance) {
        this.teBucksBalance = teBucksBalance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
