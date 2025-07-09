package com.techelevator.tenmo.dao.interfaces;


import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransferById(Integer transferId);

    List<Transfer> getTransfersBySenderAcct(Integer senderAcctId);

    List<Transfer> getTransfersByRecipientAcct (Integer recipientAcctId);

    List<Transfer> getAllTransfers ();

    List<Transfer> getTransfersByStatus(String transferStatus);

    Transfer createTransfer(Transfer transfer);

    Transfer updateTransfer(Integer transferId, String newTransferStatus);
}
