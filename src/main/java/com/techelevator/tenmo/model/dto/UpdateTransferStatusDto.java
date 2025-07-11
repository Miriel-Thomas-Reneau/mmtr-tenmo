package com.techelevator.tenmo.model.dto;

import jakarta.validation.constraints.Pattern;

public class UpdateTransferStatusDto {
    @Pattern(regexp = "Approved|Rejected", message = "New status must be approved or rejected")
    private String status;

    public UpdateTransferStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
