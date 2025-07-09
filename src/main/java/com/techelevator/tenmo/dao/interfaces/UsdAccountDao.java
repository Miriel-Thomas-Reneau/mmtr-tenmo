package com.techelevator.tenmo.dao.interfaces;

import com.techelevator.tenmo.model.UsdAccount;

import java.math.BigDecimal;

public interface UsdAccountDao {

    UsdAccount createUsdAccount();

    UsdAccount receiveFunds(BigDecimal conversionAmount);

    BigDecimal getUsdAccountBalance(int userId);
}
