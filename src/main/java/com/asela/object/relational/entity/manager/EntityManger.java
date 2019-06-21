package com.asela.object.relational.entity.manager;

import java.sql.SQLException;

public interface EntityManger<T> {


    void persist(T saman) throws SQLException, IllegalAccessException;

    T find(Object i) throws Exception;
}
