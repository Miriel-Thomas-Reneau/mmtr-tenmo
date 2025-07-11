package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.dao.interfaces.UsdAccountDao;
import com.techelevator.tenmo.dao.interfaces.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.UsdAccount;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class UserController {
    private final UserDao userDao;
    private final TenmoAccountDao tenmoAccountDao;
    private final UsdAccountDao usdAccountDao;

    public UserController(UserDao userDao, TenmoAccountDao tenmoAccountDao, UsdAccountDao usdAccountDao) {
        this.userDao = userDao;
        this.tenmoAccountDao = tenmoAccountDao;
        this.usdAccountDao = usdAccountDao;
    }

    //TODO fix this to account for admin consuming this endpoint
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/balance/tenmo")
    public BigDecimal getTenmoAccountBalance(Principal principal) {
        BigDecimal balance = null;
        String username = principal.getName();
        try {
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
            }
            int userId = user.getId();
            balance = tenmoAccountDao.getBalanceByUserId(userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
        return balance;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/balance/usd")
    public BigDecimal getUsdAccountBalance(Principal principal) {
        BigDecimal balance = null;
        String username = principal.getName();
        try {
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
            }
            int userId = user.getId();
            UsdAccount usdAccount = usdAccountDao.pullAccountInformation(userId);
            balance = usdAccount.getUsdBalance();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
        return balance;
    }
}
