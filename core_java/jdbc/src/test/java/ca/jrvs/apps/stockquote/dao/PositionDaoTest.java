package ca.jrvs.apps.stockquote.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.stockquote.util.PropertiesLoader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PositionDaoTest {

    private PositionDao dao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        Properties props = PropertiesLoader.getProperties();
        String url = "jdbc:postgresql://" + props.getProperty("server") + ":" + props.getProperty("port") + "/"
                + props.getProperty("database");
        connection = DriverManager.getConnection(url, props.getProperty("username"), props.getProperty("password"));

        QuoteDao quoteDao = new QuoteDao(connection);
        quoteDao.deleteAll();
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

        quoteDao.save(test);

        dao = new PositionDao(connection);
        dao.deleteAll();

        Position entity = new Position();
        entity.setTicker("GME");
        entity.setNumOfShares(10);
        entity.setValuePaid(100.0);
        dao.save(entity);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dao.deleteAll();
        QuoteDao quoteDao = new QuoteDao(connection);
        quoteDao.deleteById("GME");
    }

    @Test
    public void testRetrieveAndSave() {
        Position testPosition = new Position();
        testPosition.setTicker("GME");
        testPosition.setNumOfShares(10);
        testPosition.setValuePaid(100.0);

        Optional<Position> actualPosition = dao.findById("GME");
        assertTrue(actualPosition.isPresent());
        assertEquals(testPosition, actualPosition.get());
    }

    @Test
    public void testDeleteById() {
        dao.deleteById("GME");
        Optional<Position> actualPosition = dao.findById("GME");
        assertTrue(actualPosition.isEmpty());
    }

    @Test
    public void testDeleteAll() {
        dao.deleteAll();
        Optional<Position> actualPosition = dao.findById("GME");
        assertTrue(actualPosition.isEmpty());
    }

    @Test
    public void testFindAll() {
        Position testPosition = new Position();
        testPosition.setTicker("GME");
        testPosition.setNumOfShares(10);
        testPosition.setValuePaid(100.0);

        Iterable<Position> retrievedEntities = dao.findAll();
        List<Position> entities = new ArrayList<>();
        retrievedEntities.forEach(entities::add);

        assertEquals(1, entities.size());
        assertEquals(testPosition, entities.get(0));
    }
}