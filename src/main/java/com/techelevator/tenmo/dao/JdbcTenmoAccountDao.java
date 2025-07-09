package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcTenmoAccountDao implements TenmoAccountDao {
    @Override
    public TenmoAccount createTenmoAccount() {
        return null;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        return null;
    }

    @Override
    public void updateBalances(Transfer transfer) {

    }

    //TODO: create mapRowToTenmoAccount method

}
