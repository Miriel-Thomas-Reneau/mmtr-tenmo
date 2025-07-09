package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.dao.interfaces.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Override
    public Transfer getTransferById(Integer transferId) {
        return null;
    }

    @Override
    public List<Transfer> getTransfersBySenderAcct(Integer senderAcctId) {
        return null;
    }

    @Override
    public List<Transfer> getTransfersByRecipientAcct(Integer recipientAcctId) {
        return null;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return null;
    }

    @Override
    public List<Transfer> getTransfersByStatus(String transferStatus) {
        return null;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        return null;
    }

    @Override
    public Transfer updateTransfer(Integer transferId, String newTransferStatus) {
        return null;
    }
    //TODO create mapRowToTransfer method
}
