package org.hillel.dao;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataAcessImpl implements DBCPDataAccess {
    private final DriverManagerDataSource dataSource;

    public DBCPDataAcessImpl(DriverManagerDataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
