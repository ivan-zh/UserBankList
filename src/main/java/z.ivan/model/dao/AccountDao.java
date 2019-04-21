package z.ivan.model.dao;

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
        Connection connection = H2DaoManager.getDBConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE ID = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            account = new Account();
            while (rs.next()) {
                account.setId(Long.valueOf(rs.getString("id")));
                account.setAmount(Long.valueOf(rs.getString("amount")));
                account.setUserId(Long.valueOf(rs.getString("userId")));
            }
            ps.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
        return account;
    }

    public List<Account> getAccountsByUserId(final Long userId) throws DaoException {
        List<Account> accountsOfUser = new ArrayList<>();
        Connection connection = H2DaoManager.getDBConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE USERID = ?");
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            Account account;
            while (rs.next()) {
                account = new Account();
                account.setId(Long.valueOf(rs.getString("id")));
                account.setAmount(Long.valueOf(rs.getString("amount")));
                account.setUserId(Long.valueOf(rs.getString("userId")));
                accountsOfUser.add(account);
            }
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
                throw new DaoException(e);
            }
        }
        return accountsOfUser;
    }

    @Override
    public List<Account> getAll() throws DaoException {
        List<Account> allAccounts = new ArrayList<>();
        Connection connection = H2DaoManager.getDBConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ACCOUNT");
            ResultSet rs = ps.executeQuery();

            Account account;
            while (rs.next()) {
                account = new Account();
                account.setId(Long.valueOf(rs.getString("id")));
                account.setAmount(Long.valueOf(rs.getString("amount")));
                account.setUserId(Long.valueOf(rs.getString("userId")));
                allAccounts.add(account);
            }
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
                throw new DaoException(e);
            }
        }
        return allAccounts;
    }

    @Override
    public void save(final Account account) throws DaoException {
        Connection connection = H2DaoManager.getDBConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ACCOUNT (id, amount, userId) VALUES (?, ?, ?)");
            ps.setLong(1, account.getId());
            ps.setLong(2, account.getAmount());
            ps.setLong(3, account.getUserId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
                throw new DaoException(e);
            }
        }
    }

    @Override
    public void save(final Collection<Account> accountCollection) throws DaoException {
        Connection connection = H2DaoManager.getDBConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO ACCOUNT (id, amount, userId) VALUES (?, ?, ?)");
            for (Account account : accountCollection) {
                ps.setLong(1, account.getId());
                ps.setLong(2, account.getAmount());
                ps.setLong(3, account.getUserId());
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
                throw new DaoException(e);
            }
        }
    }

    @Override
    public void update(final Account account) throws DaoException {
        Connection connection = H2DaoManager.getDBConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE ACCOUNT SET amount = ?, userId = ? WHERE id = ?");
            ps.setLong(1, account.getAmount());
            ps.setLong(2, account.getUserId());
            ps.setLong(3, account.getId());
            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
                throw new DaoException(e);
            }
        }
    }

    @Override
    public void delete(final Long id) throws DaoException {
        Connection connection = H2DaoManager.getDBConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM ACCOUNT WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
                throw new DaoException(e);
            }
        }
    }
}
