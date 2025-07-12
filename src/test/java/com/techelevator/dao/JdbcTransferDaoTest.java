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
    void getTransferById_Give1000ReturnNull() {
        int id = 1000;

        Transfer actual = dao.getTransferById(id);

        assertNull(actual);
    }

    @Test
    void getTransfersBySenderAcct_Give5Return1ListSizeAndRightTransfer() {
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
    void getTransferBySenderAcct_Give1000ReturnEmptyList () {
        List<Transfer> actual = dao.getTransfersBySenderAcct(1000);

        assertEquals(0,actual.size());
    }

    @Test
    void getTransfersByRecipientAcct_Give2Return1ListSizeAndTransfer() {
        List<Transfer> expected = new ArrayList<>();
        Transfer addToList = new Transfer(1,1,2,BigDecimal.valueOf(150.01),"Approved", "Sending");
        expected.add(addToList);

        List<Transfer> actual = dao.getTransfersByRecipientAcct(2);

        assertEquals(expected.size(),actual.size());
        assertEquals(expected.get(0).getTransferType(),actual.get(0).getTransferType());
        assertEquals(expected.get(0).getTransferAmount(),actual.get(0).getTransferAmount());
        assertEquals(expected.get(0).getTransferId(),actual.get(0).getTransferId());
        assertEquals(expected.get(0).getSenderAccountId(),actual.get(0).getSenderAccountId());
        assertEquals(expected.get(0).getRecipientAccountId(),actual.get(0).getRecipientAccountId());
        assertEquals(expected.get(0).getTransferStatus(),actual.get(0).getTransferStatus());
    }

    @Test
    void getTransfersByRecipientAcct_Give1000Return0Size () {
        List<Transfer> actual = dao.getTransfersByRecipientAcct(1000);

        assertEquals(0,actual.size());
    }

    @Test
    void getAllTransfers() {
        List<Transfer> expected = new ArrayList<>();
        Transfer transfer1 = new Transfer(1,1,2,BigDecimal.valueOf(150.01),"Approved","Sending");
        Transfer transfer2 = new Transfer(2,5,3,BigDecimal.valueOf(75.01),"Pending","Request");
        Transfer transfer3 = new Transfer(3,4,7,BigDecimal.valueOf(100.01),"Approved","Request");
        Transfer transfer4 = new Transfer(4,6,3,BigDecimal.valueOf(15.01),"Pending","Sending");
        Transfer transfer5 = new Transfer(5,2,1,BigDecimal.valueOf(100.01),"Rejected","Request");
        Transfer transfer6 = new Transfer(6,3,1,BigDecimal.valueOf(50.01),"Approved","Sending");
        Transfer transfer7 = new Transfer(7,3,1,BigDecimal.valueOf(85.01),"Pending","Request");
        Transfer transfer8 = new Transfer(8,1,6,BigDecimal.valueOf(100.01),"Pending","Request");
        expected.add(transfer1);
        expected.add(transfer2);
        expected.add(transfer3);
        expected.add(transfer4);
        expected.add(transfer5);
        expected.add(transfer6);
        expected.add(transfer7);
        expected.add(transfer8);

        List<Transfer> actual = dao.getAllTransfers();

        assertEquals(expected.size(),actual.size());

        for (int i = 0 ; i < expected.size() ; i++) {
            assertEquals(expected.get(i).getTransferType(),actual.get(i).getTransferType());
            assertEquals(expected.get(i).getTransferAmount(),actual.get(i).getTransferAmount());
            assertEquals(expected.get(i).getTransferId(),actual.get(i).getTransferId());
            assertEquals(expected.get(i).getSenderAccountId(),actual.get(i).getSenderAccountId());
            assertEquals(expected.get(i).getRecipientAccountId(),actual.get(i).getRecipientAccountId());
            assertEquals(expected.get(i).getTransferStatus(),actual.get(i).getTransferStatus());
        }
    }

    @Test
    void getTransfersByStatus_GiveRejectedReturnOne() {
        List<Transfer> expected = new ArrayList<>();
        Transfer transfer5 = new Transfer(5, 2, 1, BigDecimal.valueOf(100.01), "Rejected", "Request");
        expected.add(transfer5);

        List<Transfer> actual = dao.getTransfersByStatus("Rejected");

        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getTransferType(), actual.get(i).getTransferType());
            assertEquals(expected.get(i).getTransferAmount(), actual.get(i).getTransferAmount());
            assertEquals(expected.get(i).getTransferId(), actual.get(i).getTransferId());
            assertEquals(expected.get(i).getSenderAccountId(), actual.get(i).getSenderAccountId());
            assertEquals(expected.get(i).getRecipientAccountId(), actual.get(i).getRecipientAccountId());
            assertEquals(expected.get(i).getTransferStatus(), actual.get(i).getTransferStatus());
        }
    }

    @Test
    void getTransfersByStatus_GiveApprovedReturn3 () {
        List<Transfer> expected = new ArrayList<>();
        Transfer transfer1 = new Transfer(1,1,2,BigDecimal.valueOf(150.01),"Approved","Sending");
        Transfer transfer3 = new Transfer(3,4,7,BigDecimal.valueOf(100.01),"Approved","Request");
        Transfer transfer6 = new Transfer(6,3,1,BigDecimal.valueOf(50.01),"Approved","Sending");
        expected.add(transfer1);
        expected.add(transfer3);
        expected.add(transfer6);

        List<Transfer> actual = dao.getTransfersByStatus("Approved");

        assertEquals(expected.size(),actual.size());

        for (int i = 0 ; i < expected.size() ; i++) {
            assertEquals(expected.get(i).getTransferType(),actual.get(i).getTransferType());
            assertEquals(expected.get(i).getTransferAmount(),actual.get(i).getTransferAmount());
            assertEquals(expected.get(i).getTransferId(),actual.get(i).getTransferId());
            assertEquals(expected.get(i).getSenderAccountId(),actual.get(i).getSenderAccountId());
            assertEquals(expected.get(i).getRecipientAccountId(),actual.get(i).getRecipientAccountId());
            assertEquals(expected.get(i).getTransferStatus(),actual.get(i).getTransferStatus());
        }
    }

    @Test
    void getTransferByStatus_GivePendingReturn4 () {
        List<Transfer> expected = new ArrayList<>();
        Transfer transfer2 = new Transfer(2,5,3,BigDecimal.valueOf(75.01),"Pending","Request");
        Transfer transfer4 = new Transfer(4,6,3,BigDecimal.valueOf(15.01),"Pending","Sending");
        Transfer transfer7 = new Transfer(7,3,1,BigDecimal.valueOf(85.01),"Pending","Request");
        Transfer transfer8 = new Transfer(8,1,6,BigDecimal.valueOf(100.01),"Pending","Request");
        expected.add(transfer2);
        expected.add(transfer4);
        expected.add(transfer7);
        expected.add(transfer8);

        List<Transfer> actual = dao.getTransfersByStatus("Pending");

        assertEquals(expected.size(),actual.size());

        for (int i = 0 ; i < expected.size() ; i++) {
            assertEquals(expected.get(i).getTransferType(),actual.get(i).getTransferType());
            assertEquals(expected.get(i).getTransferAmount(),actual.get(i).getTransferAmount());
            assertEquals(expected.get(i).getTransferId(),actual.get(i).getTransferId());
            assertEquals(expected.get(i).getSenderAccountId(),actual.get(i).getSenderAccountId());
            assertEquals(expected.get(i).getRecipientAccountId(),actual.get(i).getRecipientAccountId());
            assertEquals(expected.get(i).getTransferStatus(),actual.get(i).getTransferStatus());
        }
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