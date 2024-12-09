package ca.jrvs.apps.stockquote.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.util.PropertiesLoader;

public class PositionService_IntTest {

    private Connection c;
    private PositionDao dao;
    private QuoteDao quoteDao;
    private QuoteHttpHelper helper;
    private QuoteService service;

    @BeforeEach
    public void setUp() throws SQLException {
        Properties props = PropertiesLoader.getProperties();
        String url = "jdbc:postgresql://" + props.getProperty("server") + ":" + props.getProperty("port") + "/"
                + props.getProperty("database");
        c = DriverManager.getConnection(url, props.getProperty("username"), props.getProperty("password"));
        quoteDao = new QuoteDao(c);
        quoteDao.deleteAll();
        helper = new QuoteHttpHelper(props.getProperty("api-key"));
        service = new QuoteService(quoteDao, helper);
        service.fetchQuoteDataFromAPI("AAPL");
        dao = new PositionDao(c);
        dao.deleteAll();
    }

    @Test
    public void testBuy() {
        PositionService service = new PositionService(dao);
        Position position = service.buy("AAPL", 10, 100);
        assertEquals("AAPL", position.getTicker());
        assertEquals(10, position.getNumOfShares());
        assertEquals(1000, position.getValuePaid());
    }

    @Test
    public void testSell() {
        PositionService service = new PositionService(dao);
        Position position = service.buy("AAPL", 10, 100);
        assertEquals("AAPL", position.getTicker());
        assertEquals(10, position.getNumOfShares());
        assertEquals(1000, position.getValuePaid());
        service.sell("AAPL");
        assertTrue(dao.findById("AAPL").isEmpty());
    }
}