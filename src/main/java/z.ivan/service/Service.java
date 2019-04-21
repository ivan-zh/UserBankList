package z.ivan.service;

import z.ivan.model.dao.exception.DaoException;
import z.ivan.model.entity.User;

import java.util.Collection;
import java.util.List;

public interface Service<T> {
    T get(Long id) throws DaoException;

    List<T> getAll() throws DaoException;

    void save(T t) throws DaoException;

    void save(Collection<T> t) throws DaoException;

    void update(T t) throws DaoException;

    void delete(Long id) throws DaoException;

}
