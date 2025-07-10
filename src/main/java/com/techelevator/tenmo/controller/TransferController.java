package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.dao.interfaces.TransferDao;
import com.techelevator.tenmo.dao.interfaces.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransferController {
    private UserDao userDao;
    private TransferDao transferDao;
    private TenmoAccountDao tenmoAccountDao;

    public TransferController(UserDao userDao, TransferDao transferDao, TenmoAccountDao tenmoAccountDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.tenmoAccountDao = tenmoAccountDao;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/transfer")
    public List<Transfer> getTransfersByUser(
            @RequestParam(name = "status", defaultValue = "") String transferStatus,
            Principal principal) {
        List<Transfer> transfers = new ArrayList<>();
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
        }
        try {
            if (user.getRole().equals("ROLE_ADMIN")) {
                transfers = transferDao.getAllTransfers();
            }
            else {
                TenmoAccount tenmoAccount = tenmoAccountDao.getTenmoAccountByUserId(user.getId());
                int tenmoAccountId = tenmoAccount.getTeAccountId();
                if (transferStatus.isEmpty()) {
                    transfers = transferDao.getTransfersBySenderAcct(tenmoAccountId);
                    transfers.addAll(transferDao.getTransfersByRecipientAcct(tenmoAccountId));
                } else {
                    // TODO need transferDao methods to get transfers by both tenmoAccountId and status
                }
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return transfers;
    }
}
