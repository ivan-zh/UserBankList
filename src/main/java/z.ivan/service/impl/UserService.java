package z.ivan.service.impl;

import z.ivan.model.dao.impl.AccountDao;
import z.ivan.model.dao.impl.UserDao;
import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.Account;
import z.ivan.model.entity.User;
import z.ivan.service.Service;

import java.util.Collection;
import java.util.List;

public class UserService implements Service<User> {

    private UserDao userDao;
    private AccountDao accountDao;

    private static volatile UserService instance;

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    private UserService() {
        this.userDao = UserDao.getInstance();
        this.accountDao = AccountDao.getInstance();
    }

    @Override
    public User get(final Long id) throws DaoException {
        User user = userDao.get(id);
        List<Account> accounts= accountDao.getAccountsByUserId(id);
        user.setAccounts(accounts);

        return user;
    }

    @Override
    public List<User> getAll() throws DaoException {
        return userDao.getAll();
    }

    @Override
    public void save(final User user) throws DaoException {
        userDao.save(user);
    }

    @Override
    public void save(Collection<User> users) throws DaoException {
        userDao.save(users);
    }

    @Override
    public void update(final User user) throws DaoException {
        userDao.update(user);
    }

    @Override
    public void delete(final Long id) throws DaoException {
        userDao.delete(id);
    }
}
