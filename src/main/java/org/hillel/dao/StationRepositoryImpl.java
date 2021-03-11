package org.hillel.dao;

import org.hillel.model.Station;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//to do - separate DB Table for stations
public class StationRepositoryImpl implements StationRepository {
    private final List<Station> stations = new ArrayList<>();

    public StationRepositoryImpl(final DBCPDataAccess dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select distinct station_from from routes;");
        while (resultSet.next()) {
            stations.add(new Station(resultSet.getString("station_from"),""));
        }
        dataSource.releaseConnection(connection);
        System.out.println("StationListRepositoryImpl construcnor OK");
    }

    @Override
    public List<Station> getAll() {
        return new ArrayList<>(stations);
    }

    @Override
    public Station getByName(String name) {
        return (stations
                .stream()
                .filter(s -> s.getName().equals(name))
                .findFirst())
                .orElse(new Station());
    }

    @Override
    public boolean exist(String name) {
        return stations
                .stream()
                .anyMatch(s -> s.getName().equals(name));
    }
}

