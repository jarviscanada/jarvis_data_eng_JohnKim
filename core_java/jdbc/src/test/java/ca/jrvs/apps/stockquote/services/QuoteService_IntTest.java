package ca.jrvs.apps.stockquote.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.util.PropertiesLoader;

public class QuoteService_IntTest {

    private Connection c;
    private QuoteDao dao;
    private PositionDao positionDao;
    private QuoteService service;
    private QuoteHttpHelper helper;

    @BeforeEach
    public void setUp() throws SQLException {
        Properties props = PropertiesLoader.getProperties();
        String url = "jdbc:postgresql://" + props.getProperty("server") + ":" + props.getProperty("port") + "/"
                + props.getProperty("database");
        c = DriverManager.getConnection(url, props.getProperty("username"), props.getProperty("password"));
        dao = new QuoteDao(c);
        positionDao = new PositionDao(c);
        helper = new QuoteHttpHelper();
        positionDao.deleteAll();
        dao.deleteAll();
        service = new QuoteService(dao, helper);
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
        assertEquals(null, quoteOptional.get().getTicker());
    }

}
