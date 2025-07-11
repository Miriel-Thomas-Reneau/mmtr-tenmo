package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.dao.interfaces.TenmoAccountDao;
import com.techelevator.tenmo.dao.interfaces.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private final TenmoAccountDao tenmoAccountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, TenmoAccountDao tenmoAccountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.tenmoAccountDao = tenmoAccountDao;
    }

    @Override
    public Transfer getTransferById(Integer transferId) {
        Transfer transfer = null;

        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE transfer_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfer;
    }


    @Override
    public List<Transfer> getTransfersBySenderAcct(Integer senderAcctId) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE sender_account_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, senderAcctId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfers;
    }


    @Override
    public List<Transfer> getTransfersByRecipientAcct(Integer recipientAcctId) {

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE recipient_account_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, recipientAcctId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;

    }

    @Override
    public List<Transfer> getAllTransfers() {

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM transfer " +
                "ORDER BY transfer_id;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;

    }

    @Override
    public List<Transfer> getTransfersByStatus(String transferStatus) {

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE transfer_status = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferStatus);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByStatusAndUserId(String transferStatus, int tenmoAccountId) {
        List<Transfer> transfers = new ArrayList<>();
        List<Transfer> transfersByStatusUserId = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE (sender_account_id = ? OR recipient_account_id = ?) AND transfer_status = ? " +
                "ORDER BY sender_account_id, recipient_account_id, transfer_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, tenmoAccountId, tenmoAccountId, transferStatus);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfersByStatusUserId;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {

        Transfer newTransfer = null;

        String createTransferSql = "INSERT INTO transfer " +
                "(sender_account_id, recipient_account_id, transfer_amount, transfer_status, transfer_type) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING transfer_id;";

        try {
                Integer newTransferId = jdbcTemplate.queryForObject(createTransferSql, int.class, transfer.getSenderAccountId(), transfer.getRecipientAccountId(), transfer.getTransferAmount(), transfer.getTransferStatus(), transfer.getTransferType());

                newTransfer = getTransferById(newTransferId);

                if (newTransfer.getTransferStatus().equals("Approved")) {
                    tenmoAccountDao.updateBalances(newTransfer);
                }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

       return newTransfer;
    }

    @Override
    public Transfer updateTransfer(Transfer transfer) {
        Transfer updatedTransfer = null;
        int transferId = transfer.getTransferId();

        String transferExistsSql = "SELECT COUNT (*) " +
                "FROM transfer " +
                "WHERE transfer_id = ?;";
        String transferIsPendingSql = "SELECT COUNT (*) " +
                "FROM transfer " +
                "WHERE transfer_id = ? AND transfer_status = 'Pending';";
        String updateSql = "UPDATE transfer " +
                "SET transfer_status = ? " +
                "WHERE transfer_id = ?;";

        try {
            int transferIdRowCount = jdbcTemplate.queryForObject(transferExistsSql, Integer.class, transferId);
            if (transferIdRowCount == 0) {
                throw new DaoException("Invalid transfer ID: " + transferId);
            }

            int transferIsPendingRowCount = jdbcTemplate.queryForObject(transferIsPendingSql, Integer.class, transferId);
            if (transferIsPendingRowCount == 0) {
                throw new DaoException("Transfer not pending, cannot be updated");
            }

            jdbcTemplate.update(updateSql, transfer.getTransferStatus(), transfer.getTransferId());

            updatedTransfer = getTransferById(transfer.getTransferId());

            if (updatedTransfer.getTransferStatus().equals("Approved")) {
                tenmoAccountDao.updateBalances(updatedTransfer);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

            return updatedTransfer;

}

private Transfer mapRowToTransfer(SqlRowSet results) {
    Transfer transfer = new Transfer();
    transfer.setTransferId(results.getInt("transfer_id"));
    transfer.setSenderAccountId(results.getInt("sender_account_id"));
    transfer.setRecipientAccountId(results.getInt("recipient_account_id"));
    transfer.setTransferAmount(results.getBigDecimal("transfer_amount"));
    transfer.setTransferStatus(results.getString("transfer_status"));
    transfer.setTransferType(results.getString("transfer_type"));


    return transfer;
}
}
