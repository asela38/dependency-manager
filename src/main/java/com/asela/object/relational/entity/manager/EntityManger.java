package com.asela.object.relational.entity.manager;

import java.sql.SQLException;

public interface EntityManger<T> {

    static <T> EntityManger<T> of(Class<T> clss) {
        return new H2EntityManager<>(clss);
    }

    void persist(T saman) throws SQLException, IllegalAccessException;

    T find(Object i) throws Exception;
}
