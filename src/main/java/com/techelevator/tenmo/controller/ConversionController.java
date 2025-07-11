package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.dao.interfaces.UsdAccountDao;
import com.techelevator.tenmo.dao.interfaces.UserDao;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.UsdAccount;
import com.techelevator.tenmo.services.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
public class ConversionController {
    private UserDao userDao;
    private TenmoAccountDao tenmoAccountDao;
    private UsdAccountDao usdAccountDao;
    private ConversionService conversionService;

    public ConversionController(UserDao userDao, TenmoAccountDao tenmoAccountDao, UsdAccountDao usdAccountDao, ConversionService conversionService) {
        this.userDao = userDao;
        this.tenmoAccountDao = tenmoAccountDao;
        this.usdAccountDao = usdAccountDao;
        this.conversionService = conversionService;

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(path = "/convert")
    public BigDecimal conversion ("EUR", "USD",)


}
