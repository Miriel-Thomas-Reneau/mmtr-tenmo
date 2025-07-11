package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTenmoAccountDao;
import com.techelevator.tenmo.dao.JdbcUsdAccountDao;
import com.techelevator.tenmo.model.UsdAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUsdAccountDaoTest extends BaseDaoTest{

    private JdbcTemplate jdbcTemplate;

    private JdbcUsdAccountDao dao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcUsdAccountDao(jdbcTemplate);
    }
    @Test
    void createUsdAccount() {
    }

    @Test
    void receiveFunds() {
    }

    @Test
    void getUsdAccountBalance_GiveAcct1Return0() {
        BigDecimal expected = BigDecimal.valueOf(0);
        int acctId = 1;

        BigDecimal actual = dao.getUsdAccountBalance(acctId);

        assertNotNull(actual);
        assertEquals(0,actual.compareTo(expected));
    }

    @Test
    void pullAccountInformation_GiverUser1ReturnAcct1() {
        int userId = 1;
        BigDecimal bal = BigDecimal.valueOf(0);
        UsdAccount expected = new UsdAccount(1,1,bal,userId);

        UsdAccount actual = dao.pullAccountInformation(userId);

        assertNotNull(actual);
        assertEquals(expected.getTenmoAccountId(),actual.getTenmoAccountId());
        assertEquals(expected.getUser_id(),actual.getUser_id());
        assertEquals(expected.getUsdAccountId(),actual.getUsdAccountId());
        assertEquals(0,actual.getUsdBalance().compareTo(expected.getUsdBalance()));
    }
}