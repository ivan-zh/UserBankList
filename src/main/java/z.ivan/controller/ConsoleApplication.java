package z.ivan.controller;

import z.ivan.model.dao.AccountDao;
import z.ivan.model.dao.DBinitializer;
import z.ivan.model.dao.Dao;
import z.ivan.model.dao.UserDao;
import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.Account;
import z.ivan.model.entity.User;
import z.ivan.service.Service;
import z.ivan.service.impl.UserService;

import java.util.List;

/**
 * The most simple app
 */

public class ConsoleApplication {
    public static void main(String[] args) throws DaoException {
        DBinitializer.initDataBase();
        checkUserService();
    }

    private static void checkUserDao() throws DaoException {
        Dao<User> userDao = UserDao.getInstance();

        List<User> allUsers = userDao.getAll();
        allUsers.forEach(System.out::println);

        System.out.println("---");

        User user2 = userDao.get(2L);
        System.out.println(user2);
    }

    private static void checkAccountDao() throws DaoException {
        Dao<Account> accountDao = AccountDao.getInstance();
        List<Account> allAccounts = accountDao.getAll();
        allAccounts.forEach(System.out::println);

        System.out.println("---");

        Account account2 = accountDao.get(2L);
        System.out.println(account2);
    }

    private static void checkUserService() throws DaoException {
        Service<User> service = UserService.getInstance();
        User user = service.get(1L);
        System.out.println(user);



    }
}
