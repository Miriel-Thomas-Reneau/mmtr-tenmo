package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.dao.interfaces.UsdAccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.UsdAccount;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class JdbcUsdAccountDao implements UsdAccountDao {

    private final String UsdAccountSqlSelect = "SELECT usd.account_id, usd.tenmo_account_id, " +
            "usd.usd_balance, usd.user_id FROM USD_account AS usd";

    private JdbcTemplate jdbcTemplate;

    public JdbcUsdAccountDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public UsdAccount createUsdAccount(UsdAccount newUsdAccount) {
        UsdAccount usdAccount = null;
        String insertUsdAccountSql = "INSERT INTO USD_account " +
                "(tenmo_account_id,usd_balance,user_id) " +
                "VALUES (?, ?, ?) " +
                "Returning USD_account_id";
        try {
            int usdAccountId = jdbcTemplate.queryForObject(insertUsdAccountSql, int.class,
                    newUsdAccount.getTenmoAccountId(), newUsdAccount.getUsdBalance(), newUsdAccount.getUser_id());
            newUsdAccount.setUsdAccountId(usdAccountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newUsdAccount;
    }

    @Override
    public BigDecimal receiveFunds(BigDecimal conversionAmount, int usdAccountId) {
        BigDecimal balanceAmount = null;
        String insertUsdAccountSql = "SELECT SUM(usd_balance + ?) " +
                "FROM USD_account WHERE USD_account_id = ?";
        try {
            balanceAmount = jdbcTemplate.queryForObject(insertUsdAccountSql, BigDecimal.class
            , conversionAmount, usdAccountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balanceAmount;
    }

    @Override
    public BigDecimal getUsdAccountBalance(int usdAccountId) {
        BigDecimal balanceAmount = null;
        String insertUsdAccountSql = "SELECT usd_balance FROM USD_account " +
                "WHERE USD_account_id = ?";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(insertUsdAccountSql, usdAccountId);
            
            if (result.next()) {
                balanceAmount = result.getBigDecimal("usd_balance");
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balanceAmount;
    }

    @Override
    public UsdAccount pullAccountInformation(int user_id) {
        UsdAccount usdAccount = null;
        String insertUsdAccountSql = "SELECT * FROM USD_account " +
                "WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(insertUsdAccountSql,user_id);
            if (results.next()) {
                usdAccount = mapRowToUsdAccount(results);
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or data", e);
        }
        return usdAccount;
    }

    @Override
    public BigDecimal receiveConvertedFunds(int usdAccountId, BigDecimal usd) {

        BigDecimal currentUSDBalance = getUsdAccountBalance(usdAccountId);
        BigDecimal updatedUSDBalance = null;
        BigDecimal amountToReceive = usd;
        BigDecimal newBalance = currentUSDBalance.add(amountToReceive);
            String sql = "UPDATE usd_account " +
                    "SET usd_balance = ? " +
                    "WHERE usd_account_id = ?;";


        try {


             updatedUSDBalance = jdbcTemplate.update(sql, newBalance, usdAccountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }


        return updatedUSDBalance;
    }


    private UsdAccount mapRowToUsdAccount(SqlRowSet rs) {
        UsdAccount usdAccount = new UsdAccount();
        usdAccount.setUsdAccountId(rs.getInt("USD_account_id"));
        usdAccount.setTenmoAccountId(rs.getInt("tenmo_account_id"));
        usdAccount.setUsdBalance(rs.getBigDecimal("usd_balance"));
        usdAccount.setUser_id(rs.getInt("user_id"));
        return usdAccount;
    }
}
