package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;

    public Quote save(Quote entity) throws IllegalArgumentException {
        return null;
    }

    public Optional<Quote> findById(String id) throws IllegalArgumentException {
        return null;
    }

    public Iterable<Quote> findAll() {
        return null;
    }

    public void deleteById(String id) throws IllegalArgumentException {
    }

    public void deleteAll() {
    }

}