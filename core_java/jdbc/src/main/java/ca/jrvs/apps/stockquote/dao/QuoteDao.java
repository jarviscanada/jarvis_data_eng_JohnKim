package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;

    public QuoteDao(Connection c) {
        this.c = c;
    }

    public Quote save(Quote entity) throws IllegalArgumentException {
        String insert = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, timestamp, change_percent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = c.prepareStatement(insert);) {
            stmt.setString(1, entity.getTicker());
            stmt.setDouble(2, entity.getOpen());
            stmt.setDouble(3, entity.getHigh());
            stmt.setDouble(4, entity.getLow());
            stmt.setDouble(5, entity.getPrice());
            stmt.setInt(6, entity.getVolume());
            stmt.setDate(7, entity.getLatestTradingDay());
            stmt.setDouble(8, entity.getPreviousClose());
            stmt.setDouble(9, entity.getChange());
            stmt.setTimestamp(10, entity.getTimestamp());
            stmt.setString(11, entity.getChangePercent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return entity;
    }

    public Optional<Quote> findById(String id) throws IllegalArgumentException {
        String find = "SELECT * FROM quote WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(find);) {
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                Quote quote = new Quote();
                quote.setTicker(result.getString("symbol"));
                quote.setOpen(result.getDouble("open"));
                quote.setHigh(result.getDouble("high"));
                quote.setLow(result.getDouble("low"));
                quote.setPrice(result.getDouble("price"));
                quote.setVolume(result.getInt("volume"));
                quote.setLatestTradingDay(result.getDate("latest_trading_day"));
                quote.setPreviousClose(result.getDouble("previous_close"));
                quote.setChange(result.getDouble("change"));
                quote.setChangePercent(result.getString("change_percent"));
                quote.setTimestamp(result.getTimestamp("timestamp"));
                return Optional.of(quote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Iterable<Quote> findAll() {
        String findAll = "SELECT * FROM quote";
        Iterable<Quote> quotes = new ArrayList<Quote>();
        try (PreparedStatement stmt = c.prepareStatement(findAll);) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Quote quote = new Quote();
                quote.setTicker(result.getString("symbol"));
                quote.setOpen(result.getDouble("open"));
                quote.setHigh(result.getDouble("high"));
                quote.setLow(result.getDouble("low"));
                quote.setPrice(result.getDouble("price"));
                quote.setVolume(result.getInt("volume"));
                quote.setLatestTradingDay(result.getDate("latest_trading_day"));
                quote.setPreviousClose(result.getDouble("previous_close"));
                quote.setChange(result.getDouble("change"));
                quote.setChangePercent(result.getString("change_percent"));
                quote.setTimestamp(result.getTimestamp("timestamp"));
                ((ArrayList<Quote>) quotes).add(quote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return quotes;
    }

    public void deleteById(String id) throws IllegalArgumentException {
        String delete = "DELETE FROM quote WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(delete);) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        String deleteAll = "DELETE FROM quote;";
        try (PreparedStatement stmt = c.prepareStatement(deleteAll);) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/stock_quote";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "password");
        try (Connection conn = DriverManager.getConnection(url, props)) {
            QuoteHttpHelper httpHelper = new QuoteHttpHelper();
            Quote test = httpHelper.getStockQuote("GME");
            QuoteDao dao = new QuoteDao(conn);
            dao.deleteAll();
            dao.save(test);
            Optional<Quote> optionalQuote = dao.findById("GME");
            optionalQuote.ifPresent(quote -> {
                // Access methods of the Quote object
                System.out.println("Quote: " + quote.getPrice());
            });
            Iterator<Quote> quotes = dao.findAll().iterator();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

}