package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TenmoAccount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTenmoAccountDaoTest {

    private JdbcTenmoAccountDao dao;

    @Test
    public void creatingANewTenmoAccount() {
        TenmoAccount account = dao.createTenmoAccount(9);
        assertNotNull(account);

        TenmoAccount actualUser = dao.getTenmoAccountByTenmoAccountId(account.getTeAccountId());
        assertNotNull(actualUser);

        account.setUserId(actualUser.getTeAccountId());
        assertEquals(account,actualUser);
    }

}