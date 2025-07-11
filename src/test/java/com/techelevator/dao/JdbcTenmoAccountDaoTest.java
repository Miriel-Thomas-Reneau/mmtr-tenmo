package com.techelevator.dao;

import com.techelevator.dao.TestingDatabaseConfig;
import com.techelevator.tenmo.dao.JdbcTenmoAccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
//@JdbcTest
//@ContextConfiguration(classes = TestingDatabaseConfig.class)
//@SpringBootTest(classes = TestingDatabaseConfig.class)
class JdbcTenmoAccountDaoTest extends BaseDaoTest {


    private JdbcTemplate jdbcTemplate;

    private JdbcTenmoAccountDao dao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTenmoAccountDao(jdbcTemplate);
    }

    @Test
    void createTenmoAccount_createsAccount_whenUserIsValid() {
        // Given
        int userId = 10; // Add a new user (non-admin)
        jdbcTemplate.update("into );

        // When
        TenmoAccount account = dao.createTenmoAccount(userId);

        // Then
        assertNotNull(account);
        assertEquals(userId, account.getUserId());
        assertEquals(new BigDecimal("1000.00"), account.getTeBucksBalance());
    }
}
