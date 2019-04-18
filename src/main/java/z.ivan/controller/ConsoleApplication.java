package z.ivan.controller;

import z.ivan.model.dao.DBinitializer;
import z.ivan.model.dao.Dao;
import z.ivan.model.dao.UserDao;
import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.User;

import java.util.List;

/**
 * The most simple app
 */

public class ConsoleApplication {
    public static void main(String[] args) throws DaoException {
        DBinitializer.initDataBase();
        Dao<User> userDao = new UserDao();
        List<User> allusers = userDao.getAll();
        allusers.forEach(System.out::println);

        User user2 = userDao.get(2L);
        System.out.println(user2);

    }
}
