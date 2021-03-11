package org.hillel.dao;

import org.hillel.model.Route;
import java.time.LocalDate;
import java.util.List;

public interface RouteRepository {
     List<Route> getRoutes(String from, String to) throws java.sql.SQLException;
     List<Route> getRoutes(String from, String to, LocalDate date) throws java.sql.SQLException;
     List<Route> getAllRoutes() throws java.sql.SQLException;
}
