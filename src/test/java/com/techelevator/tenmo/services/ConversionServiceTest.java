package com.techelevator.tenmo.services;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ConversionServiceTest {

    ConversionService conversionService = new ConversionService();

    //exchange rate is 1.1698
    @Test
    public void te13TousdShouldReturn15_21(){
        String from = "EUR";
        String to = "USD";
        BigDecimal amount = BigDecimal.valueOf(13);
        BigDecimal expected = BigDecimal.valueOf(15.21);
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

    @Test
    public void te21ToUsdShouldReturn24_57(){
        String from = "EUR";
        String to = "USD";
        BigDecimal amount = BigDecimal.valueOf(21);
        BigDecimal expected = BigDecimal.valueOf(24.57);
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

    @Test
    public void te420ToUsdShouldReturn491_32(){
        String from = "EUR";
        String to = "USD";
        BigDecimal amount = BigDecimal.valueOf(420);
        BigDecimal expected = BigDecimal.valueOf(491.32);
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

    @Test
    public void usd24ToTeShouldReturn20_52() {
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = BigDecimal.valueOf(24);
        BigDecimal expected = BigDecimal.valueOf(20.52);
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

    @Test
    public void usd13ToTeShouldReturn11_11() {
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = BigDecimal.valueOf(13);
        BigDecimal expected = BigDecimal.valueOf(11.11);
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

    @Test
    public void usd69ToTeShouldReturn58_98(){
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = BigDecimal.valueOf(69);
        BigDecimal expected = BigDecimal.valueOf(58.98);
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

    @Test
    public void invalidFromReturn0(){
        String from = "ABC";
        String to = "EUR";
        BigDecimal amount = BigDecimal.valueOf(13);
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

    @Test
    public void invalidToReturn0(){
        String from = "EUR";
        String to = "ABC";
        BigDecimal amount = BigDecimal.valueOf(13);
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal actual = conversionService.conversion(from,to,amount);
        assertEquals(expected,actual);
    }

}