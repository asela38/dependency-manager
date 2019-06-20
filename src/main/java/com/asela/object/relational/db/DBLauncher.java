package com.asela.object.relational.db;

import org.h2.tools.Server;

import java.sql.SQLException;
import java.util.logging.Logger;

public class DBLauncher {
    public static void main(String[] args) {
        try {
            Server.main();
            Logger.getGlobal().info("DB Launched");

        } catch (SQLException e) {
            Logger.getGlobal().throwing(DBLauncher.class.getCanonicalName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        }
    }
}
