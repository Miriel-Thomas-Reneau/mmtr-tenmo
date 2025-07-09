package com.techelevator.tenmo.dao.interfaces;

import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TenmoAccountDao {

    TenmoAccount createTenmoAccount(int userId);
        //creates new Tenmo Account and sets starting balance to 1000 TEB

    TenmoAccount updateTenmoAccount(TenmoAccount tenmoAccount);

    TenmoAccount getTenmoAccountByTenmoAccountId(int tenmoAccountId);

    BigDecimal getBalanceByUserId(int userId);

    BigDecimal getBalanceByTenmoAccountId(int tenmoAccountId);

    void updateBalances(Transfer transfer);
        //subtract transfer amount from sender account
        //add transfer amount to recipient account
}
