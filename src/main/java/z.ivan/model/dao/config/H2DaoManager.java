package z.ivan.model.dao.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class H2DaoManager {

    private static final Logger LOG = Logger.getLogger(H2DaoManager.class.getName());

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private H2DaoManager() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            final String msg = "Driver has not been found.";
            LOG.warning(msg);
            throw new SQLException(msg);
        }
    }
}
