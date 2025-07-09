package com.techelevator.tenmo.dao.interfaces;

import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TenmoAccountDao {

    TenmoAccount createTenmoAccount ();
        //creates new Tenmo Account and sets starting balance to 1000 TEB

    BigDecimal getBalanceByUserId (int userId);



    void updateBalances(Transfer transfer);
        //subtract transfer amount from sender account
        //add transfer amount to recipient account
}
