package com.asela.object.relational.entity.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2EntityManager<T> extends AbstractEntityManger<T> {

    public H2EntityManager(Class<T> clss) {
        super(clss);
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:/Users/asela/Workspace/github/object-relational-mapper/db-files/db-persons",
                "sa", "");
    }
}
