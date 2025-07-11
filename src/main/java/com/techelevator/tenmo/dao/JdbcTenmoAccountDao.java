package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Component
public class JdbcTenmoAccountDao implements TenmoAccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTenmoAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TenmoAccount createTenmoAccount(int userId) {
        String sql = "INSERT INTO tenmo_account " +
                "(user_id, te_bucks_balance) " +
                "VALUES (?, 1000) " +
                "RETURNING tenmo_account_id;";
        TenmoAccount newTenmoAccount = null;
        try {
            int newTenmoAccountId = jdbcTemplate.queryForObject(sql, int.class, userId);
            newTenmoAccount = getTenmoAccountByTenmoAccountId(newTenmoAccountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newTenmoAccount;
    }

    @Override
    public TenmoAccount updateTenmoAccount(TenmoAccount tenmoAccount) {
        String sql = "UPDATE tenmo_account " +
                "SET user_id = ?, te_bucks_balance = ? " +
                "WHERE tenmo_account_id = ?;";
        TenmoAccount newTenmoAccount;
        try {
            int numberOfRows = jdbcTemplate.update(sql, tenmoAccount.getUserId(), tenmoAccount.getTeBucksBalance(), tenmoAccount.getTeAccountId());
            if (numberOfRows == 0) {
                throw new DaoException("Tried to update non-existent tenmo_account_id: " + tenmoAccount.getTeAccountId());
            }
            newTenmoAccount = getTenmoAccountByTenmoAccountId(tenmoAccount.getTeAccountId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newTenmoAccount;
    }

    @Override
    public TenmoAccount getTenmoAccountByTenmoAccountId(int tenmoAccountId) {
        String sql = "SELECT * " +
                "FROM tenmo_account " +
                "WHERE tenmo_account_id = ?";
        TenmoAccount tenmoAccount = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, tenmoAccountId);
            if (results.next()) {
                tenmoAccount = mapRowToTenmoAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return tenmoAccount;
    }

    @Override
    public TenmoAccount getTenmoAccountByUserId(int userId) {
        String sql = "SELECT * " +
                "FROM tenmo_account " +
                "WHERE user_id = ?";
        TenmoAccount tenmoAccount = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                tenmoAccount = mapRowToTenmoAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return tenmoAccount;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        String sql = "SELECT te_bucks_balance " +
                "FROM tenmo_account " +
                "WHERE user_id = ?;";
        BigDecimal balance = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("te_bucks_balance");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balance;
    }

    @Override
    public BigDecimal getBalanceByTenmoAccountId(int tenmoAccountId) {
        String sql = "SELECT te_bucks_balance " +
                "FROM tenmo_account " +
                "WHERE tenmo_account_id = ?;";
        BigDecimal balance = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, tenmoAccountId);
            if (results.next()) {
                balance = results.getBigDecimal("te_bucks_balance");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balance;
    }

    @Override
    public void updateBalances(Transfer transfer) {
        TenmoAccount senderAccount = getTenmoAccountByTenmoAccountId(transfer.getSenderAccountId());
        TenmoAccount recipientAccount = getTenmoAccountByTenmoAccountId(transfer.getRecipientAccountId());
        int senderAccountId = senderAccount.getTeAccountId();
        int recipientAccountId = recipientAccount.getTeAccountId();
        if (senderAccountId == recipientAccountId) {
            throw new DaoException("Unable to update balances: sender and recipient are the same");
        }
        BigDecimal senderBalance = getBalanceByTenmoAccountId(senderAccountId);
        BigDecimal recipientBalance = getBalanceByTenmoAccountId(recipientAccountId);
        BigDecimal transferAmount = transfer.getTransferAmount();
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DaoException("Unable to update balances: transfer amount is zero or negative");
        }
        if (senderBalance.compareTo(transferAmount) < 0) {
            throw new DaoException("Unable to update balances: sender balance is too low");
        }
        BigDecimal newSenderBalance = senderBalance.subtract(transferAmount);
        BigDecimal newRecipientBalance = recipientBalance.add(transferAmount);
        senderAccount.setTeBucksBalance(newSenderBalance);
        recipientAccount.setTeBucksBalance(newRecipientBalance);
        try {
            updateTenmoAccount(senderAccount);
            updateTenmoAccount(recipientAccount);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    public TenmoAccount mapRowToTenmoAccount(SqlRowSet results) {
        TenmoAccount tenmoAccount = new TenmoAccount();
        tenmoAccount.setTeAccountId(results.getInt("tenmo_account_id"));
        tenmoAccount.setUserId(results.getInt("user_id"));
        tenmoAccount.setTeBucksBalance(results.getBigDecimal("te_bucks_balance"));
        return tenmoAccount;
    }

}
