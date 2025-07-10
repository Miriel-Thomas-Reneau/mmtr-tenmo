package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.dao.interfaces.TransferDao;
import com.techelevator.tenmo.dao.interfaces.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/transfer/{transfer_id}")
    public Transfer getTransfer(@PathVariable(name = "transfer_id") int transferId,
                                Principal principal) {
        Transfer transfer = null;
        try {
            boolean canSeeTransfer;
            String username = principal.getName();
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
            }
            int userId = user.getId();
            transfer = transferDao.getTransferById(transferId);
            canSeeTransfer = user.getRole().equals("ROLE_ADMIN") || transfer.getSenderAccountId() == userId
                || transfer.getRecipientAccountId() == userId;
            if (!canSeeTransfer) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Unauthorized to access transfer id " + transferId);
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return transfer;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(path = "/transfer/{transfer_id}")
    public Transfer putTransfer(@PathVariable(name = "transfer_id") int transferId,
                                @Valid @RequestBody Transfer newTransfer,
                                Principal principal) {
        Transfer transfer = null;
        try {
            String username = principal.getName();
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
            }
            int userId = user.getId();
            TenmoAccount tenmoAccount = tenmoAccountDao.getTenmoAccountByUserId(userId);
            if (tenmoAccount == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not have a Tenmo account");
            }
            transfer = transferDao.getTransferById(transferId);
            if (transfer == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer " + transferId + " does not exist");
            }
            int tenmoAccountId = tenmoAccount.getTeAccountId();
            boolean isOwnTransfer = transfer.getSenderAccountId() == tenmoAccountId
                    || transfer.getRecipientAccountId() == tenmoAccountId;
            if (!isOwnTransfer) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorized to access transfer " + transferId);
            }
            boolean isRequestTransfer = transfer.getTransferType().equals("Request");
            if (!isRequestTransfer) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer " + transferId + " is not a request transfer");
            }
            boolean isPendingTransfer = transfer.getTransferStatus().equals("Pending");
            if (!isPendingTransfer) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer " + transferId + " is not pending");
            }
            transfer.setTransferStatus(newTransfer.getTransferStatus());
            transfer = transferDao.updateTransfer(transfer);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return transfer;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "/send")
    public Transfer createSendingTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
        }
        int userId = user.getId();
        TenmoAccount tenmoAccount = tenmoAccountDao.getTenmoAccountByUserId(userId);
        transfer.setSenderAccountId(tenmoAccount.getTeAccountId());
        transfer.setTransferType("Sending");
        transfer.setTransferStatus("Approved");
        Transfer newTransfer = null;
        try {
            newTransfer = transferDao.createTransfer(transfer);

        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return newTransfer;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "/request")
    public Transfer createRequestTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User " + username + " does not exist");
        }
        int userId = user.getId();
        TenmoAccount tenmoAccount = tenmoAccountDao.getTenmoAccountByUserId(userId);
        transfer.setRecipientAccountId(tenmoAccount.getTeAccountId());
        transfer.setTransferType("Request");
        Transfer newTransfer = null;
        try {
            newTransfer = transferDao.createTransfer(transfer);

        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return newTransfer;
    }
}
