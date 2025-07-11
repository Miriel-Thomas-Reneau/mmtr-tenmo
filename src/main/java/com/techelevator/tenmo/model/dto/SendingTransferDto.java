package com.techelevator.tenmo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class SendingTransferDto {
    @NotBlank(message = "Recipient username cannot be blank")
    @JsonProperty("recipient_username")
    private String recipientUsername;
    @Positive(message = "Transfer amount must be greater than zero")
    @JsonProperty("transfer_amount")
    private BigDecimal transferAmount;

    public SendingTransferDto(String recipientUsername, BigDecimal transferAmount) {
        this.recipientUsername = recipientUsername;
        this.transferAmount = transferAmount;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
