package z.ivan.model.dao.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbInitializer {

    public static final String CREATE_TABLE_USER_SQL = "CREATE TABLE USER (id INT, name varchar(255), surname varchar(255))";
    public static final String CREATE_TABLE_ACCOUNT_SQL = "CREATE TABLE ACCOUNT (`id` INT, `amount` INT, `userId` INT)";

    public static final String INSERT_INTO_USER_SQL = "INSERT INTO USER (id, name, surname) VALUES (?, ?, ?)";
    public static final String INSERT_INTO_ACCOUNT_SQL = "INSERT INTO ACCOUNT (id, amount, userId) VALUES (?, ?, ?)";

    private static final Logger LOG = Logger.getLogger(DbInitializer.class.getName());

    private static boolean isInitialized = false;

    public static void init() {
        if(!isInitialized) {
            try (Connection connection = H2DaoManager.getConnection()) {
                initUsers(connection);
                fillUsers(connection);

                initAccounts(connection);
                fillAccounts(connection);

                isInitialized = true;
            } catch (SQLException e) {
                LOG.warning(e.getMessage());
            }
        }
    }

    private static void initUsers(final Connection connection) {
        try(PreparedStatement ps = connection.prepareStatement(CREATE_TABLE_USER_SQL)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        }
    }

    private static void fillUsers(final Connection connection) {
        try(PreparedStatement ps = connection.prepareStatement(INSERT_INTO_USER_SQL)) {
            for(int i = 0; i < 3; i++) {
                ps.setInt(1, i+1);
                ps.setString(2, "John #" + (i + 1));
                ps.setString(3, "Johnson #" + (i +1));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        }
    }

    private static void initAccounts(final Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE_TABLE_ACCOUNT_SQL)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        }
    }

    private static void fillAccounts(final Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_INTO_ACCOUNT_SQL)) {
            for (int i = 0; i < 10; i++) {
                ps.setInt(1, i + 1);
                ps.setInt(2, (int) (Math.random() * 900) + 100);
                //Accounts 1-3 belong to User #1 etc...
                int uID;
                if (i < 4) {
                    uID = 1;
                } else if (i < 7) {
                    uID = 2;
                } else uID = 3;
                ps.setInt(3, uID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        }
    }
}
