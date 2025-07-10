package com.techelevator.tenmo.dao.interfaces;


import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransferById(Integer transferId);

    List<Transfer> getTransfersBySenderAcct(Integer senderAcctId);

    List<Transfer> getTransfersByRecipientAcct (Integer recipientAcctId);

    List<Transfer> getAllTransfers ();

    List<Transfer> getTransfersByStatus(String transferStatus);

    List<Transfer> getTransfersByStatusAndUserId (String transferStatus, String userId);

    Transfer createTransfer(Transfer transfer);

    Transfer updateTransfer(Transfer transfer);
}
