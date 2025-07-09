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
import java.security.Principal;

@Component
public class JdbcUsdAccountDao implements UsdAccountDao {

    private final String UsdAccountSqlSelect = "SELECT usd.account_id, usd.tenmo_account_id, " +
            "usd.te_bucks_balance, usd.user_id FROM USD_account AS usd";

    private JdbcTemplate jdbcTemplate;

    public JdbcUsdAccountDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public UsdAccount createUsdAccount(UsdAccount newUsdAccount) {
        UsdAccount usdAccount = null;
        String insertUsdAccountSql = "INSERT INTO USD_account" +
                "(tenmo_account_id,usd_bucks_balance,user_id)" +
                "VALUES (?,?.?)" +
                "Returning USD_account_id";
        try {
            Integer usdAccountId = jdbcTemplate.queryForObject(insertUsdAccountSql, int.class,
                    newUsdAccount.getTenmoAccountId(), newUsdAccount.getUsdBucksBalance(), newUsdAccount.getUser_id());
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
        String insertUsdAccountSql = "SELECT SUM(usd_bucks_balance + ?)" +
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
        String insertUsdAccountSql = "SELECT usd_bucks_balance FROM USD_account" +
                "WHERE USD_account_id = ?";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(insertUsdAccountSql, usdAccountId);
            
            if (result.next()) {
                balanceAmount = (BigDecimal) result;
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balanceAmount;
    }

    private UsdAccount mapRowToUsdAccount(SqlRowSet rs) {
        UsdAccount usdAccount = new UsdAccount();
        usdAccount.setUsdAccountId(rs.getInt("USD_account_id"));
        usdAccount.setTenmoAccountId(rs.getInt("tenmo_account_id"));
        usdAccount.setUsdBucksBalance(rs.getBigDecimal("usd_bucks_balance"));
        usdAccount.setUser_id(rs.getInt("user_id"));
        return usdAccount;
    }
}
