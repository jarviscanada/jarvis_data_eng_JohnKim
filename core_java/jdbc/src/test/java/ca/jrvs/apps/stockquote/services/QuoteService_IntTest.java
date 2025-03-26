package ca.jrvs.apps.stockquote.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.util.PropertiesLoader;

public class QuoteService_IntTest {

    private static Connection c;
    private static QuoteDao dao;
    private static PositionDao positionDao;
    private static QuoteService service;
    private static QuoteHttpHelper helper;

    @BeforeAll
    public static void setUpAll() throws SQLException {
        Properties props = PropertiesLoader.getProperties();
        String url = "jdbc:postgresql://" + props.getProperty("server") + ":" + props.getProperty("port") + "/"
                + props.getProperty("database");
        c = DriverManager.getConnection(url, props.getProperty("username"), props.getProperty("password"));
        dao = new QuoteDao(c);
        positionDao = new PositionDao(c);
        helper = new QuoteHttpHelper(props.getProperty("api-key"));
        positionDao.deleteAll();
        dao.deleteAll();
        service = new QuoteService(dao, helper);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        dao.deleteAll();
    }

    @Test
    public void testFetchQuoteDataFromAPI() {
        Optional<Quote> quoteOptional = service.fetchQuoteDataFromAPI("AAPL");
        assert (quoteOptional.isPresent());
        Quote quote = quoteOptional.get();
        assertEquals("AAPL", quote.getTicker());
    }

    @Test
    public void testFetchQuoteDataFromAPI_Null() {
        Optional<Quote> quoteOptional = service.fetchQuoteDataFromAPI("AAPL2");
        assertTrue(quoteOptional.isEmpty());
    }

}
