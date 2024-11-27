package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

    private Connection c;

    public Position save(Position entity) throws IllegalArgumentException {
        return null;
    }

    public Optional<Position> findById(String id) throws IllegalArgumentException {
        return null;
    }

    public Iterable<Position> findAll() {
        return null;
    }

    public void deleteById(String id) throws IllegalArgumentException {
    }

    public void deleteAll() {
    }

}