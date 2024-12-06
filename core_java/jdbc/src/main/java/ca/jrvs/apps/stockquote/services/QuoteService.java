package ca.jrvs.apps.stockquote.services;

import java.util.Optional;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;

public class QuoteService {

    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
        this.dao = dao;
    }

    /**
     * Fetches latest quote data from endpoint
     * 
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        Quote quote = httpHelper.getStockQuote(ticker);
        if (quote == null) {
            return Optional.empty();
        }
        try {
            dao.save(quote);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        return Optional.of(quote);
    }
}