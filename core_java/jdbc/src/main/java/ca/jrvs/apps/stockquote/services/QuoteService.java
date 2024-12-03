package ca.jrvs.apps.stockquote.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

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
        dao.save(quote);
        return Optional.of(quote);
    }

    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "password");
        String url = "jdbc:postgresql://localhost:5432/stock_quote";
        Connection c = DriverManager.getConnection(url, props);
        QuoteDao dao = new QuoteDao(c);
        QuoteHttpHelper httpHelper = new QuoteHttpHelper();
        QuoteService service = new QuoteService(dao, httpHelper);
        service.fetchQuoteDataFromAPI("AAPL");
    }

}