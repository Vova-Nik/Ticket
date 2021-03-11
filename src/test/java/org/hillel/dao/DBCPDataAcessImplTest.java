package org.hillel.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DBCPDataAcessImplTest {

    @Test
    void getConnection() {
        DBCPDataAcessImpl da;
        Connection connection = null;
        ResultSet resultSet;
        Statement statement;
        String res;
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("common-beans.xml");
        try {
            da =(DBCPDataAcessImpl)applicationContext.getBean("DBCPDataAccess","org.hillel.dao.DBCPDataAcessImpl");
            connection = da.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from test");
            resultSet.next();
            res = resultSet.getString("name");
            System.out.println("res = " + res);
            assertEquals("ticket project test table", res);
        } catch (java.sql.SQLException e) {
            fail(e.getMessage());
        }
        try {
            connection.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}