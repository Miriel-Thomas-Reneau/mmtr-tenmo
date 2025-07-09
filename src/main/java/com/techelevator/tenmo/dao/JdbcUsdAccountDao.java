package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.dao.interfaces.UsdAccountDao;
import com.techelevator.tenmo.model.UsdAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcUsdAccountDao implements UsdAccountDao {
    @Override
    public UsdAccount createUsdAccount() {
        return null;
    }

    @Override
    public UsdAccount receiveFunds(BigDecimal conversionAmount) {
        return null;
    }

    @Override
    public BigDecimal getUsdAccountBalance(int userId) {
        return null;
    }

    //TODO: mapRowToUsdAccount method
}
