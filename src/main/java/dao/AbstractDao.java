package dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.transaction.TransactionalException;
import java.util.List;

public abstract class AbstractDao<T> {

    private Class<T> entity;
    private static final Logger LOG = LogManager.getLogger(AbstractDao.class);

    public AbstractDao() {
    }

    public AbstractDao(Class<T> entity) {
        this.entity = entity;
    }

    public abstract void persist(T t);

    public abstract void merge(T t) throws TransactionalException;

    public abstract void remove(T t) throws TransactionalException;

    public abstract T find(int id) throws TransactionalException;

    public abstract List<T> findAll();
}