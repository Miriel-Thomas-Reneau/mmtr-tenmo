package com.techelevator.tenmo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class RequestTransferDto {
    @NotBlank(message = "Recipient username cannot be blank")
    @JsonProperty("sender_username")
    private String senderUsername;
    @Positive(message = "Transfer amount must be greater than zero")
    @JsonProperty("transfer_amount")
    private BigDecimal transferAmount;

    public RequestTransferDto(String senderUsername, BigDecimal transferAmount) {
        this.senderUsername = senderUsername;
        this.transferAmount = transferAmount;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
