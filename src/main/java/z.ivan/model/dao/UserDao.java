package z.ivan.model.dao;

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
        Connection connection = H2DaoManager.getDBConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER WHERE ID = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            user = new User();
            while (rs.next()) {
                user.setId(Long.parseLong(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
            }
            ps.close();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public List<User> getAll() throws DaoException {
        List<User> allUsers = new ArrayList<>();
        Connection connection = H2DaoManager.getDBConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER");
            ResultSet rs = ps.executeQuery();

            User user;
            while (rs.next()) {
                user = new User();
                user.setId(Long.parseLong(rs.getString("id")));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                allUsers.add(user);
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
        return allUsers;
    }

    @Override
    public void save(final User user) throws DaoException {
        Connection connection = H2DaoManager.getDBConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USER (id, name, surname) VALUES (?, ?, ?)");
            ps.setLong(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getSurname());
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
    public void save(final Collection<User> userCollection) throws DaoException {
        Connection connection = H2DaoManager.getDBConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USER (id, name, surname) VALUES (?, ?, ?)");
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
    public void update(final User user) throws DaoException {
        Connection connection = H2DaoManager.getDBConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE USER SET name = ?, surname = ? WHERE id = ?");
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setLong(3, user.getId());
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
            PreparedStatement ps = connection.prepareStatement("DELETE FROM USER WHERE id = ?");
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
