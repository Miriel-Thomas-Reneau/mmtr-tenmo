package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.dao.interfaces.UsdAccountDao;
import com.techelevator.tenmo.dao.interfaces.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.UsdAccount;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.dto.ConversionDto;
import com.techelevator.tenmo.services.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;


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
    public BigDecimal convertTenmoToUsd(ConversionDto conversionDto, Principal principal) {
        String username = principal.getName();
        TenmoAccount tenmoAccount = null;
        UsdAccount usdAccount = null;
        BigDecimal conversionAmountFromTEB = conversionDto.getConversionAmount();
        BigDecimal newUsdBalance = null;
        try {
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
            }
            int userId = user.getId();
            tenmoAccount = tenmoAccountDao.getTenmoAccountByUserId(userId);
            usdAccount = usdAccountDao.pullAccountInformation(userId);
            BigDecimal tenmoAccountBalance = tenmoAccount.getTeBucksBalance();
            if (tenmoAccountBalance.compareTo(conversionAmountFromTEB) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough TE bucks in TenmoAccount");
            }
            BigDecimal conversionAmountToUSD = conversionService.conversion("EUR", "USD", conversionAmountFromTEB);
            tenmoAccountDao.removeFunds(tenmoAccount.getTeAccountId(), conversionAmountFromTEB);
            newUsdBalance = usdAccountDao.receiveConvertedFunds(usdAccount.getUsdAccountId(), conversionAmountToUSD);

        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO exception - " + e);
        }
        return newUsdBalance;
    }
    //public BigDecimal conversion ("EUR", "USD",)


}
