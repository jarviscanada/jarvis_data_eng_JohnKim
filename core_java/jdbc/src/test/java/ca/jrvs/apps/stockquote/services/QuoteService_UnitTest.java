package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class QuoteService_UnitTest {

    private QuoteService quoteService;
    private QuoteDao mockQuoteDao;
    private QuoteHttpHelper mockHttpHelper;

    @BeforeEach
    public void setUp() {
        mockQuoteDao = mock(QuoteDao.class);
        mockHttpHelper = mock(QuoteHttpHelper.class);
        quoteService = new QuoteService(mockQuoteDao, mockHttpHelper);
    }

    @Test
    public void testFetchQuoteDataFromAPI() {
        // Create a Quote object to return from the mock HttpHelper
        Quote mockQuote = new Quote();
        mockQuote.setTicker("AAPL");
        mockQuote.setOpen(150.0);
        mockQuote.setHigh(155.0);
        mockQuote.setLow(148.0);
        mockQuote.setPrice(152.0);
        mockQuote.setVolume(1000000);
        mockQuote.setLatestTradingDay(java.sql.Date.valueOf("2024-01-01"));
        mockQuote.setPreviousClose(151.0);
        mockQuote.setChange(1.0);
        mockQuote.setChangePercent("0.66%");

        // Set up the mock behavior
        when(mockHttpHelper.getStockQuote(anyString())).thenReturn(mockQuote);

        // Call the method to test
        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("AAPL");

        // Verify the results
        assertTrue(result.isPresent());
        assertEquals(mockQuote, result.get());
    }

    @Test
    public void testFetchQuoteDataFromAPI_NotFound() {
        // Set up the mock behavior to return null
        when(mockHttpHelper.getStockQuote(anyString())).thenReturn(null);

        // Call the method to test
        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI("INVALID");

        // Verify the results
        assertTrue(result.isEmpty());

    }
}