package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TeAccount {

    private int teAccountId;
    private int userId;
    private BigDecimal teBucksBalance;

    public TeAccount() {}

    public TeAccount(int teAccountId, int userId, BigDecimal teBucksBalance) {
        this.teAccountId = teAccountId;
        this.userId = userId;
        this.teBucksBalance = teBucksBalance;
    }

    public int getTeAccountId() {
        return teAccountId;
    }

    public void setTeAccountId(int teAccountId) {
        this.teAccountId = teAccountId;
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
