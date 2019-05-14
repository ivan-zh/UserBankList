package z.ivan.model.dao.impl;

import z.ivan.model.dao.Dao;
import z.ivan.model.dao.config.H2DaoManager;
import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class AccountDao implements Dao<Account> {
    private static final Logger LOGGER = Logger.getLogger(AccountDao.class.getName());

    private static final String SELECT_BY_ID = "SELECT * FROM ACCOUNT WHERE ID = ?";
    private static final String SELECT_BY_USERID = "SELECT * FROM ACCOUNT WHERE USERID = ?";
    private static final String SELECT_ALL = "SELECT * FROM ACCOUNT";
    private static final String INSERT_VALUES = "INSERT INTO ACCOUNT (id, amount, userId) VALUES (?, ?, ?)";
    private static final String SET_AMOUNT = "UPDATE ACCOUNT SET amount = ?, userId = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM ACCOUNT WHERE id = ?";

    private static final String COLUMN_LABEL_ID = "id";
    private static final String COLUMN_LABEL_AMOUNT = "amount";
    private static final String COLUMN_LABEL_USER_ID = "userId";

    private static volatile AccountDao instance;

    public static AccountDao getInstance() {
        if (instance == null) {
            synchronized (AccountDao.class) {
                if (instance == null) {
                    instance = new AccountDao();
                }
            }
        }
        return instance;
    }

    private AccountDao() {
    }

    @Override
    public Account get(final Long id) throws DaoException {
        Account account;
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            account = new Account();
            while (rs.next()) {
                account.setId(Long.valueOf(rs.getString(COLUMN_LABEL_ID)));
                account.setAmount(Long.valueOf(rs.getString(COLUMN_LABEL_AMOUNT)));
                account.setUserId(Long.valueOf(rs.getString(COLUMN_LABEL_USER_ID)));
            }
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }

        return account;
    }

    public List<Account> getAccountsByUserId(final Long userId) throws DaoException {
        List<Account> accountsOfUser = new ArrayList<>();
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_BY_USERID)
        ) {
            connection.setAutoCommit(false);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            Account account;
            while (rs.next()) {
                account = new Account();
                account.setId(Long.valueOf(rs.getString(COLUMN_LABEL_ID)));
                account.setAmount(Long.valueOf(rs.getString(COLUMN_LABEL_AMOUNT)));
                account.setUserId(Long.valueOf(rs.getString(COLUMN_LABEL_USER_ID)));
                accountsOfUser.add(account);
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }

        return accountsOfUser;
    }

    @Override
    public List<Account> getAll() throws DaoException {
        List<Account> allAccounts = new ArrayList<>();
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL)
        ) {
            connection.setAutoCommit(false);
            ResultSet rs = ps.executeQuery();

            Account account;
            while (rs.next()) {
                account = new Account();
                account.setId(Long.valueOf(rs.getString(COLUMN_LABEL_ID)));
                account.setAmount(Long.valueOf(rs.getString(COLUMN_LABEL_AMOUNT)));
                account.setUserId(Long.valueOf(rs.getString(COLUMN_LABEL_USER_ID)));
                allAccounts.add(account);
            }

            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }

        return allAccounts;
    }

    @Override
    public void save(final Account account) throws DaoException {
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection
                        .prepareStatement(INSERT_VALUES)
        ) {
            fillStatement(ps, account);
//            ps.setLong(1, account.getId());
//            ps.setLong(2, account.getAmount());
//            ps.setLong(3, account.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
    }

    private void fillStatement(PreparedStatement ps, Account account) throws DaoException {
        try {
            ps.setLong(1, account.getId());
            ps.setLong(2, account.getAmount());
            ps.setLong(3, account.getUserId());
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void save(final Collection<Account> accountCollection) throws DaoException {
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection
                        .prepareStatement(INSERT_VALUES)
        ) {
            connection.setAutoCommit(false);
            for (Account account : accountCollection) {
                fillStatement(ps, account);
//                ps.setLong(1, account.getId());
//                ps.setLong(2, account.getAmount());
//                ps.setLong(3, account.getUserId());
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(final Account account) throws DaoException {
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection
                        .prepareStatement(SET_AMOUNT)
        ) {
            ps.setLong(1, account.getAmount());
            ps.setLong(2, account.getUserId());
            ps.setLong(3, account.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(final Long id) throws DaoException {
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID)
        ) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
    }
}
