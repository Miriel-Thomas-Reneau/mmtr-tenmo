package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTenmoAccountDao;
import com.techelevator.tenmo.dao.JdbcUsdAccountDao;
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
    void getUsdAccountBalance() {
        BigDecimal expected = BigDecimal.valueOf(0);
        int acctId = 1;
        
        BigDecimal actual = dao.getUsdAccountBalance(acctId);

        assertNotNull(actual);
        assertEquals(0,actual.compareTo(expected));
    }

    @Test
    void pullAccountInformation() {
    }
}