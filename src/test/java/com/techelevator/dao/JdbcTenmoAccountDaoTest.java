package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTenmoAccountDao;
import com.techelevator.tenmo.model.TenmoAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
class JdbcTenmoAccountDaoTest extends BaseDaoTest {


    private JdbcTemplate jdbcTemplate;

    private JdbcTenmoAccountDao dao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTenmoAccountDao(jdbcTemplate);
    }

    @Test
    void getBalanceByUserId_Input2ShouldReturn1000() {
        BigDecimal expected = BigDecimal.valueOf(1000);
        int userId = 2;

        BigDecimal actual = dao.getBalanceByUserId(userId);

        assertNotNull(actual);
        assertEquals(0,actual.compareTo(expected));
    }


}
