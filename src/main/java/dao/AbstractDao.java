package dao;

import java.util.List;

public interface AbstractDao<T> {

    void insert(T t);

    void update(T t);

    void delete(T t);

    T find(int id);

    List<T> findAll();
}