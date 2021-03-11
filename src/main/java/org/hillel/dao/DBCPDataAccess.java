package org.hillel.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBCPDataAccess {
    Connection getConnection() throws SQLException;
    void releaseConnection(Connection connection) throws SQLException;
}
