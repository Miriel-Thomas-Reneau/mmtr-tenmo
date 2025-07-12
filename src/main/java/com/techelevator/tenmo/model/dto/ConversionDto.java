package com.techelevator.tenmo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ConversionDto {
    @Positive(message = "Conversion amount must be greater than zero")
    @JsonProperty("conversion_amount")
    private BigDecimal conversionAmount;

    public ConversionDto() {}

    public ConversionDto(BigDecimal conversionAmount) {
        this.conversionAmount = conversionAmount;
    }

    public BigDecimal getConversionAmount() {
        return conversionAmount;
    }

    public void setConversionAmount(BigDecimal conversionAmount) {
        this.conversionAmount = conversionAmount;
    }
}
