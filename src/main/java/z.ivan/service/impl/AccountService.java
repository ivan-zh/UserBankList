package z.ivan.service.impl;

import z.ivan.model.dao.AccountDao;
import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.Account;
import z.ivan.service.Service;

import java.util.Collection;
import java.util.List;

public class AccountService implements Service<Account> {

    private AccountDao accountDao;

    private static volatile AccountService instance;

    public static AccountService getInstance() {
        if (instance == null) {
            synchronized (AccountService.class) {
                if (instance == null) {
                    instance = new AccountService();
                }
            }
        }
        return instance;
    }

    private AccountService() {
        this.accountDao = AccountDao.getInstance();
    }

    @Override
    public Account get(Long id) throws DaoException {
        return accountDao.get(id);
    }

    @Override
    public List<Account> getAll() throws DaoException {
        return accountDao.getAll();
    }

    @Override
    public void save(Account account) throws DaoException {
        accountDao.save(account);
    }

    @Override
    public void save(Collection<Account> accounts) throws DaoException {
        accountDao.save(accounts);
    }

    @Override
    public void update(Account account) throws DaoException {
        accountDao.update(account);
    }

    @Override
    public void delete(Long id) throws DaoException {
        accountDao.delete(id);
    }
}
