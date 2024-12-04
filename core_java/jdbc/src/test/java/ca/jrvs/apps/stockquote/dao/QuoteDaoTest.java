package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.util.PropertiesLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuoteDaoTest {

    private Connection c;

    @BeforeEach
    public void setUp() throws SQLException {
        Properties props = PropertiesLoader.getProperties();
        String url = "jdbc:postgresql://" + props.getProperty("server") + ":" + props.getProperty("port") + "/"
                + props.getProperty("database");
        c = DriverManager.getConnection(url, props.getProperty("username"), props.getProperty("password"));
        QuoteDao dao = new QuoteDao(c);
        dao.deleteAll();

        Quote test = new Quote();
        test.setTicker("GME");
        test.setOpen(100.0);
        test.setHigh(200.0);
        test.setLow(50.0);
        test.setPrice(150.0);
        test.setVolume(1000000);
        test.setLatestTradingDay(java.sql.Date.valueOf("2024-01-01"));
        test.setPreviousClose(100.0);
        test.setChange(50.0);
        test.setChangePercent("50%");
        test.setTimestamp(Timestamp.valueOf("2024-01-01 10:10:10.0"));

        dao.save(test);
    }

    @Test
    public void testRetrieveAndSave() throws SQLException {
        Quote expected = new Quote();
        expected.setTicker("GME");
        expected.setOpen(100.0);
        expected.setHigh(200.0);
        expected.setLow(50.0);
        expected.setPrice(150.0);
        expected.setVolume(1000000);
        expected.setLatestTradingDay(java.sql.Date.valueOf("2024-01-01"));
        expected.setPreviousClose(100.0);
        expected.setChange(50.0);
        expected.setChangePercent("50%");
        expected.setTimestamp(Timestamp.valueOf("2024-01-01 10:10:10.0"));

        QuoteDao dao = new QuoteDao(c);
        Optional<Quote> result = dao.findById("GME");
        assertTrue(result.isPresent());

        Quote actual = result.get();

        assertEquals(expected, actual);
    }

    @Test
    public void testRetrieveAll() throws SQLException {
        Quote expected = new Quote();
        expected.setTicker("GME");
        expected.setOpen(100.0);
        expected.setHigh(200.0);
        expected.setLow(50.0);
        expected.setPrice(150.0);
        expected.setVolume(1000000);
        expected.setLatestTradingDay(java.sql.Date.valueOf("2024-01-01"));
        expected.setPreviousClose(100.0);
        expected.setChange(50.0);
        expected.setChangePercent("50%");
        expected.setTimestamp(Timestamp.valueOf("2024-01-01 10:10:10.0"));

        QuoteDao dao = new QuoteDao(c);
        Quote result = dao.findAll().iterator().next();

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteById() throws SQLException {
        QuoteDao dao = new QuoteDao(c);
        dao.deleteById("GME");

        Optional<Quote> result = dao.findById("GME");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteAll() throws SQLException {
        QuoteDao dao = new QuoteDao(c);
        dao.deleteAll();

        Optional<Quote> result = dao.findById("GME");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll() throws SQLException {
        Quote expected = new Quote();
        expected.setTicker("GME");
        expected.setOpen(100.0);
        expected.setHigh(200.0);
        expected.setLow(50.0);
        expected.setPrice(150.0);
        expected.setVolume(1000000);
        expected.setLatestTradingDay(java.sql.Date.valueOf("2024-01-01"));
        expected.setPreviousClose(100.0);
        expected.setChange(50.0);
        expected.setChangePercent("50%");
        expected.setTimestamp(Timestamp.valueOf("2024-01-01 10:10:10.0"));

        QuoteDao dao = new QuoteDao(c);
        Quote result = dao.findAll().iterator().next();

        assertEquals(expected, result);
    }
}