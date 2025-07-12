package com.techelevator.tenmo.dao.interfaces;

import com.techelevator.tenmo.model.UsdAccount;

import java.math.BigDecimal;
public interface UsdAccountDao {

    UsdAccount createUsdAccount(UsdAccount newUsdAccount);

    BigDecimal receiveFunds(BigDecimal conversionAmount, int UsdAccountId);

    BigDecimal getUsdAccountBalance(int usdAccountId);

    UsdAccount pullAccountInformation(int user_id);

    BigDecimal receiveConvertedFunds(int usdAccountId, BigDecimal usd);
    }

