package com.asela.object.relational.provider;

import com.asela.object.relational.anno.Provides;

import javax.xml.ws.WebServiceProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionProvider {

    @Provides
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:h2:/Users/asela/Workspace/github/object-relational-mapper/db-files/db-persons",
                "sa", "");
    }
}
