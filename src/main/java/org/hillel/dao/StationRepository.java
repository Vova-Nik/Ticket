package org.hillel.dao;

import org.hillel.model.Station;
import java.util.List;

public interface StationRepository {
    List<Station> getAll();
    Station getByName(String name);
    boolean exist(String name);
}
