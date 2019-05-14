package z.ivan.controller;

import z.ivan.model.dao.AccountDao;
import z.ivan.model.dao.DBinitializer;
import z.ivan.model.dao.Dao;
import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.Account;
import z.ivan.model.entity.User;
import z.ivan.service.Service;
import z.ivan.service.impl.AccountService;
import z.ivan.service.impl.UserService;

import java.util.List;

/**
 * The most simple app
 */

public class ConsoleApplication {
    public static void main(String[] args) throws DaoException {
        DBinitializer.initDataBase();
        checkServices();
    }

    private static void checkServices() throws DaoException {

        Service<User> userService = UserService.getInstance();
        Service<Account> accountService = AccountService.getInstance();

        System.out.println("User #2:");
        User user2 = userService.get(2L);
        System.out.println(user2);

        System.out.println("Users:");
        List<User> allUsers = userService.getAll();
        allUsers.forEach(System.out::println);

        System.out.println("Accounts:");
        List<Account> allAccounts = accountService.getAll();
        allAccounts.forEach(System.out::println);


    }
}
