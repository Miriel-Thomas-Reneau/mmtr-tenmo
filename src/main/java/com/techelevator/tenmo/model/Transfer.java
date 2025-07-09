package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int senderAccountId;
    private int recipientAccountId;
    private BigDecimal amountTransfer;
    private String transferStatus;
    private String transferType;

    public Transfer () {}

    public Transfer(int transferId, int senderAccountId, int recipientAccountId, BigDecimal amountTransfer, String transferStatus, String transferType) {
        this.transferId = transferId;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.amountTransfer = amountTransfer;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public int getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(int recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public BigDecimal getAmountTransfer() {
        return amountTransfer;
    }

    public void setAmountTransfer(BigDecimal amountTransfer) {
        this.amountTransfer = amountTransfer;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
