package org.hillel.dao;

import org.hillel.model.Route;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteRepositoryTrainImpl implements RouteRepository {
    private final DBCPDataAccess dataSource;

    public RouteRepositoryTrainImpl(final DBCPDataAccess dataSource) {
        this.dataSource = dataSource;
    }

    public List<Route> getRoutes(String from, String to) throws java.sql.SQLException {
        List<Route> routes = new ArrayList<>();
        Route route = new Route();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        PreparedStatement preparedStatement = connection.prepareStatement("select * from routes where station_from = ? and station_to = ?");
        preparedStatement.setString(1, from);
        preparedStatement.setString(2, to);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            routes.add(parseResultSet(resultSet));
        }
        dataSource.releaseConnection(connection);
        return Collections.unmodifiableList(routes);
    }

    //to be changed. So far, made in consideration that every root runs every day.
    @Override
    public List<Route> getRoutes(String from, String to, LocalDate departure) throws SQLException {
        return getRoutes(from, to);
    }

    public List<Route> getAllRoutes() throws java.sql.SQLException {
        List<Route> routes = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from train_routes");
        while (resultSet.next()) {
            routes.add(parseResultSet(resultSet));
        }
        dataSource.releaseConnection(connection);
        return Collections.unmodifiableList(routes);
    }

    private Route parseResultSet(ResultSet resultSet) throws SQLException {
        Route route = new Route();
        route.setNumber(resultSet.getInt("number"));
        route.setStationFrom(resultSet.getString("station_from"));
        route.setStationTo(resultSet.getString("station_to"));
        route.setDepPeriod(resultSet.getString("dep_period"));
        java.sql.Time tm = resultSet.getTime("departure");
        route.setDeparture(tm.toLocalTime());
        route.setDuration(resultSet.getInt("duration"));
        route.setInfo(resultSet.getString("info"));
        return route;
    }
}
