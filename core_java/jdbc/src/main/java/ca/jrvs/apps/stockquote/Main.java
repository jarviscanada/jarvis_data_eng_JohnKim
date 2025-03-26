package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.*;
import ca.jrvs.apps.stockquote.services.*;
import ca.jrvs.apps.stockquote.util.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties = PropertiesLoader.getProperties();

        try {
            Class.forName(properties.getProperty("db-class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:postgresql://" + properties.getProperty("server") + ":" + properties.getProperty("port")
                + "/" + properties.getProperty("database");
        try (Connection c = DriverManager.getConnection(url, properties.getProperty("username"),
                properties.getProperty("password"))) {
            QuoteDao qRepo = new QuoteDao(c);
            PositionDao pRepo = new PositionDao(c);
            QuoteHttpHelper rcon = new QuoteHttpHelper(properties.getProperty("api-key"));
            QuoteService sQuote = new QuoteService(qRepo, rcon);
            PositionService sPos = new PositionService(pRepo);
            StockQuoteController con = new StockQuoteController(sQuote, sPos);
            con.initClient();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}