package z.ivan.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;


public class DBinitializer extends H2DaoManager {
    private static final Logger LOGGER = Logger.getLogger(DBinitializer.class.getName());


    private static boolean isInitialized = false;

    public static void initDataBase() {
        if (!isInitialized) {
            String createAccountTable = "CREATE TABLE `ACCOUNT` (`id` INT, `amount` INT, `userId` INT)";
            String insertAccount = "INSERT INTO ACCOUNT (id, amount, userId) VALUES (?, ?, ?)";

            String createUserTable = "CREATE TABLE USER (id INT, name varchar(255), surname varchar(255))";
            String insertUser = "INSERT INTO USER (id, name, surname) VALUES (?, ?, ?)";

            Connection connection = getDBConnection();

            PreparedStatement createAccountsPS = null;
            PreparedStatement insertAccountPS = null;
            PreparedStatement createUserTablePS = null;
            PreparedStatement insertUserPS = null;
            try {
                connection.setAutoCommit(false);
                //CREATE TABLE `ACCOUNT`
                createAccountsPS = connection.prepareStatement(createAccountTable);
                createAccountsPS.executeUpdate();
                createAccountsPS.close();

                //CREATE TABLE USER
                createUserTablePS = connection.prepareStatement(createUserTable);
                createUserTablePS.executeUpdate();
                createUserTablePS.close();

                //Add 10 accounts
                for (int i = 1; i < 11; i++) {

                    insertAccountPS = connection.prepareStatement(insertAccount);
                    insertAccountPS.setInt(1, i);
                    insertAccountPS.setInt(2, 100 + i * 10);

                    //Accounts 1-3 belong to User #1 etc...
                    int uID;
                    if (i > 0 && i < 4) {
                        uID = 1;
                    } else if (i > 3 && i < 7) {
                        uID = 2;
                    } else uID = 3;

                    insertAccountPS.setInt(3, uID);
                    insertAccountPS.executeUpdate();
                    insertAccountPS.close();
                }

                //Add user#1
                insertUserPS = connection.prepareStatement(insertUser);
                insertUserPS.setInt(1, 1);
                insertUserPS.setString(2, "John");
                insertUserPS.setString(3, "Johnson");
                insertUserPS.executeUpdate();
                insertUserPS.close();

                //Add user#2
                insertUserPS = connection.prepareStatement(insertUser);
                insertUserPS.setInt(1, 2);
                insertUserPS.setString(2, "Jack");
                insertUserPS.setString(3, "Jackson");
                insertUserPS.executeUpdate();
                insertUserPS.close();

                //Add user#3
                insertUserPS = connection.prepareStatement(insertUser);
                insertUserPS.setInt(1, 3);
                insertUserPS.setString(2, "Harry");
                insertUserPS.setString(3, "Harrison");
                insertUserPS.executeUpdate();
                insertUserPS.close();

                connection.commit();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.warning(e.getMessage());
                }
            }
        }
    }


    //Next code is just for test
    public static void main(String[] args) {
        initDataBase();
        printTableACCOUNT();
        printTableUSER();
    }

    private static void printTableUSER() {
        String selectQuery = "select * from USER";
        PreparedStatement selectAll = null;
        Connection connection = getDBConnection();
        try {
            connection.setAutoCommit(false);
            selectAll = connection.prepareStatement(selectQuery);

            ResultSet rs = selectAll.executeQuery();
            System.out.println("Table USER");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));
            }
            selectAll.close();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    private static void printTableACCOUNT() {
        String selectQuery = "select * from ACCOUNT";
        PreparedStatement selectAll = null;
        Connection connection = getDBConnection();
        try {
            connection.setAutoCommit(false);
            selectAll = connection.prepareStatement(selectQuery);

            ResultSet rs = selectAll.executeQuery();
            System.out.println("Table ACCOUNT");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3));
            }
            selectAll.close();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.warning(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

}
