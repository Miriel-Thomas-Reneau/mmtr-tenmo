package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTenmoAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUsdAccountDao;
import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTransferDaoTest extends BaseDaoTest{

    private JdbcTemplate jdbcTemplate;
    private TenmoAccountDao tenmoDao;
    private JdbcTransferDao dao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransferDao(jdbcTemplate,tenmoDao);
    }

    @Test
    void getTransferById_Give1ReturnCorrectValues() {
        int transferId = 1;
        int senderId = 1;
        int recieverId = 2;
        BigDecimal amountToTransfer = BigDecimal.valueOf(150.01);
        String status = "Approved";
        String type = "Sending";
        Transfer expected = new Transfer(transferId,senderId,recieverId,amountToTransfer,status,type);
        
        Transfer actual = dao.getTransferById(transferId);
        
        assertNotNull(actual);
        assertEquals(expected.getTransferStatus(),actual.getTransferStatus());
        assertEquals(expected.getRecipientAccountId(),actual.getRecipientAccountId());
        assertEquals(expected.getSenderAccountId(),actual.getSenderAccountId());
        assertEquals(expected.getTransferId(),actual.getTransferId());
        assertEquals(expected.getTransferAmount(),actual.getTransferAmount());
        assertEquals(expected.getTransferType(),actual.getTransferType());
    }

    @Test
    void getTransfersBySenderAcct() {
        List<Transfer> expected = new ArrayList<>();
        Transfer transfer = new Transfer(2,5,3,BigDecimal.valueOf(75.01),"Pending","Request");
        expected.add(transfer);

        List<Transfer> actual = dao.getTransfersBySenderAcct(5);

        assertEquals(expected.size(),actual.size());
        assertEquals(expected.get(0).getTransferType(),actual.get(0).getTransferType());
        assertEquals(expected.get(0).getTransferAmount(),actual.get(0).getTransferAmount());
        assertEquals(expected.get(0).getTransferId(),actual.get(0).getTransferId());
        assertEquals(expected.get(0).getSenderAccountId(),actual.get(0).getSenderAccountId());
        assertEquals(expected.get(0).getRecipientAccountId(),actual.get(0).getRecipientAccountId());
        assertEquals(expected.get(0).getTransferStatus(),actual.get(0).getTransferStatus());
    }

    @Test
    void getTransfersByRecipientAcct() {
    }

    @Test
    void getAllTransfers() {
    }

    @Test
    void getTransfersByStatus() {
    }

    @Test
    void getTransfersByStatusAndUserId() {
    }

    @Test
    void createTransfer() {
    }

    @Test
    void updateTransfer() {
    }
}