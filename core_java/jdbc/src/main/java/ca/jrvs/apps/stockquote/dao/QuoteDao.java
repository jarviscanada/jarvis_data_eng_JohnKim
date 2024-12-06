package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;

    public QuoteDao(Connection c) {
        this.c = c;
    }

    public Quote save(Quote entity) throws IllegalArgumentException {
        if (entity.getTicker() == null) {
            throw new IllegalArgumentException("Ticker cannot be null");
        }

        if (existsById(entity.getTicker())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private Quote insert(Quote entity) {
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
        }
        return entity;
    }

    private Quote update(Quote entity) {
        String update = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, timestamp = ?, change_percent = ? WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(update);) {
            stmt.setDouble(1, entity.getOpen());
            stmt.setDouble(2, entity.getHigh());
            stmt.setDouble(3, entity.getLow());
            stmt.setDouble(4, entity.getPrice());
            stmt.setInt(5, entity.getVolume());
            stmt.setDate(6, entity.getLatestTradingDay());
            stmt.setDouble(7, entity.getPreviousClose());
            stmt.setDouble(8, entity.getChange());
            stmt.setTimestamp(9, entity.getTimestamp());
            stmt.setString(10, entity.getChangePercent());
            stmt.setString(11, entity.getTicker());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private boolean existsById(String id) {
        String query = "SELECT 1 FROM quote WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(query);) {
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
}