package z.ivan.model.dao.impl;

import z.ivan.model.dao.Dao;
import z.ivan.model.dao.config.H2DaoManager;
import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class UserDao implements Dao<User> {
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    private static final String SELECT_BY_ID = "SELECT * FROM USER WHERE ID = ?";
    private static final String SELECT_ALL = "SELECT * FROM USER";
    private static final String INSERT_VALUES = "INSERT INTO USER (id, name, surname) VALUES (?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE USER SET name = ?, surname = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM USER WHERE id = ?";

    private static final String COLUMN_LABEL_ID = "id";
    private static final String COLUMN_LABEL_NAME = "name";
    private static final String COLUMN_LABEL_SURNAME = "surname";

    private static volatile UserDao instance;

    public static UserDao getInstance() {
        if (instance == null) {
            synchronized (UserDao.class) {
                if (instance == null) {
                    instance = new UserDao();
                }
            }
        }
        return instance;
    }

    private UserDao() {
    }

    @Override
    public User get(final Long id) throws DaoException {
        User user;
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            user = new User();
            while (rs.next()) {
                user.setId(Long.parseLong(rs.getString(COLUMN_LABEL_ID)));
                user.setName(rs.getString(COLUMN_LABEL_NAME));
                user.setSurname(rs.getString(COLUMN_LABEL_SURNAME));
                user.setAccounts(AccountDao.getInstance().getAccountsByUserId(user.getId()));
            }
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }

        return user;
    }

    @Override
    public List<User> getAll() throws DaoException {
        List<User> allUsers = new ArrayList<>();
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL)
        ) {
            connection.setAutoCommit(false);
            ResultSet rs = ps.executeQuery();

            User user;
            while (rs.next()) {
                user = new User();

                user.setId(Long.parseLong(rs.getString(COLUMN_LABEL_ID)));
                user.setName(rs.getString(COLUMN_LABEL_NAME));
                user.setSurname(rs.getString(COLUMN_LABEL_SURNAME));

                user.setAccounts(AccountDao.getInstance().getAccountsByUserId(user.getId()));

                allUsers.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }

        return allUsers;
    }

    @Override
    public void save(final User user) throws DaoException {
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_VALUES)
        ) {
            ps.setLong(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getSurname());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void save(final Collection<User> userCollection) throws DaoException {
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_VALUES)
        ) {
            connection.setAutoCommit(false);
            for (User user : userCollection) {
                ps.setLong(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getSurname());
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(final User user) throws DaoException {
        try (
                Connection connection = H2DaoManager.getConnection();
                PreparedStatement ps = connection.prepareStatement(UPDATE_USER)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setLong(3, user.getId());
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
