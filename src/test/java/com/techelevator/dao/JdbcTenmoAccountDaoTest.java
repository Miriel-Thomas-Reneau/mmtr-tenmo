package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTenmoAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
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

    @Test
    void getBalanceByUserId_Input1000ShouldReturnNull() {
        int userId = 1000;

        BigDecimal actual = dao.getBalanceByUserId(userId);

        assertNull(actual);
    }

    @Test
    void getBalanceByAccountId_Input1ShouldReturn1000() {
        BigDecimal expected = BigDecimal.valueOf(1000);
        int accountId = 1;

        BigDecimal actual = dao.getBalanceByTenmoAccountId(accountId);

        assertNotNull(actual);
        assertEquals(0,actual.compareTo(expected));
    }

    @Test
    void getBalanceByAccountId_Input1000ShouldReturnNull(){
        int accountId = 1000;

        BigDecimal actual = dao.getBalanceByTenmoAccountId(accountId);

        assertNull(actual);
    }

    @Test
    void getTenmoAccountByUserId_Input2ShouldReturnBal1000AndActId2() {
        int userId = 2;
        BigDecimal bal = BigDecimal.valueOf(1000);
        TenmoAccount expected = new TenmoAccount(2,2,bal);

        TenmoAccount actual = dao.getTenmoAccountByUserId(userId);

        assertNotNull(actual);
        assertEquals(expected.getTeAccountId(),actual.getTeAccountId());
        assertEquals(0,actual.getTeBucksBalance().compareTo(expected.getTeBucksBalance()));
        assertEquals(expected.getUserId(),actual.getUserId());
    }

    @Test
    void getTenmoAccountByUserId_Input1000ShouldReturnNull () {
        int userId = 1000;

        TenmoAccount actual = dao.getTenmoAccountByUserId(userId);

        assertNull(actual);
    }

    @Test
    void getTenmoAccountByAccountId_Input1ShouldReturnBal1000AndActId1() {
        int accountId = 1;
        BigDecimal bal = BigDecimal.valueOf(1000);
        TenmoAccount expected = new TenmoAccount(1,1,bal);

        TenmoAccount actual = dao.getTenmoAccountByTenmoAccountId(accountId);

        assertNotNull(actual);
        assertEquals(expected.getUserId(),actual.getUserId());
        assertEquals(0, actual.getTeBucksBalance().compareTo(expected.getTeBucksBalance()));
        assertEquals(expected.getTeAccountId(),actual.getTeAccountId());
    }

    @Test
    void getTenmoAccountByAccountId_Input1000ShouldReturnNull () {
        int accountId = 1000;

        TenmoAccount actual = dao.getTenmoAccountByTenmoAccountId(accountId);

        assertNull(actual);
    }

    @Test
    void updateTenmoAccount_UpdateUserIdTo1AndBalanceTo10 () {
        BigDecimal bal = BigDecimal.valueOf(10);
        int userId = 1;
        int accountId = 1;
        TenmoAccount expected = new TenmoAccount(accountId,userId,bal);

        TenmoAccount actual = dao.updateTenmoAccount(expected);

        assertNotNull(actual);
        assertEquals(expected.getTeAccountId(),actual.getTeAccountId());
        assertEquals(0,actual.getTeBucksBalance().compareTo(expected.getTeBucksBalance()));
        assertEquals(expected.getUserId(),actual.getUserId());
    }

    @Test
    void createTenmoAccount_CreateId11AcctId8Bal1000() {
        User newUser = new User("User11","password","ROLE_USER");
        JdbcUserDao userDao = new JdbcUserDao(jdbcTemplate);
        userDao.createUser(newUser);
        BigDecimal bal = BigDecimal.valueOf(1000);
        int userId = 11;
        int acctId = 8;
        TenmoAccount expected = new TenmoAccount(acctId,userId,bal);

        TenmoAccount actual = dao.createTenmoAccount(11);

        assertNotNull(actual);
        assertEquals(expected.getTeAccountId(),actual.getTeAccountId());
        assertEquals(0,actual.getTeBucksBalance().compareTo(expected.getTeBucksBalance()));
        assertEquals(expected.getUserId(),actual.getUserId());
    }

    @Test
    void updateBalances_User1Sends150ToUser2Recieves (){
        int senderAccount = 1;
        int recievingAcct = 2;
        BigDecimal sending = BigDecimal.valueOf(300);
        BigDecimal sendingOriginal = dao.getBalanceByTenmoAccountId(senderAccount);
        BigDecimal sendingExpected = sendingOriginal.subtract(sending);
        BigDecimal receivingOriginal = dao.getBalanceByTenmoAccountId(recievingAcct);
        BigDecimal receivingExpected = receivingOriginal.add(sending);

        Transfer transfer = new Transfer(senderAccount,recievingAcct,sending,"Pending","Sending");
        JdbcTransferDao transferDao = new JdbcTransferDao(jdbcTemplate, dao);
        Transfer transfers = transferDao.createTransfer(transfer);

        dao.updateBalances(transfers);

        assertEquals(0,dao.getBalanceByTenmoAccountId(1).compareTo(sendingExpected));
        assertEquals(0,dao.getBalanceByTenmoAccountId(2).compareTo(receivingExpected));
    }


}
