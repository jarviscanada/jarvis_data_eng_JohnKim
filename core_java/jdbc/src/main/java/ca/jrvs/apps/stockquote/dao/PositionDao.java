package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

    private Connection c;

    public PositionDao(Connection c) {
        this.c = c;
    }

    public Position save(Position entity) throws IllegalArgumentException {
        if (existsById(entity.getTicker())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private Position insert(Position entity) {
        String insert = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = c.prepareStatement(insert);) {
            stmt.setString(1, entity.getTicker());
            stmt.setInt(2, entity.getNumOfShares());
            stmt.setDouble(3, entity.getValuePaid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to insert position: " + e.getMessage(), e);
        }
        return entity;
    }

    private Position update(Position entity) {
        String update = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(update);) {
            stmt.setInt(1, entity.getNumOfShares());
            stmt.setDouble(2, entity.getValuePaid());
            stmt.setString(3, entity.getTicker());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to update position: " + e.getMessage(), e);
        }
        return entity;
    }

    private boolean existsById(String id) {
        String query = "SELECT 1 FROM position WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(query);) {
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Position> findById(String id) throws IllegalArgumentException {
        String find = "SELECT * FROM position WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(find);) {
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                Position position = new Position();
                position.setTicker(result.getString("symbol"));
                position.setNumOfShares(result.getInt("number_of_shares"));
                position.setValuePaid(result.getDouble("value_paid"));
                return Optional.of(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Iterable<Position> findAll() {
        String findAll = "SELECT * FROM position;";
        List<Position> positions = new ArrayList<>();
        try (PreparedStatement stmt = c.prepareStatement(findAll);) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Position position = new Position();
                position.setTicker(result.getString("symbol"));
                position.setNumOfShares(result.getInt("number_of_shares"));
                position.setValuePaid(result.getDouble("value_paid"));
                positions.add(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }

    public void deleteById(String id) throws IllegalArgumentException {
        String delete = "DELETE FROM position WHERE symbol = ?;";
        try (PreparedStatement stmt = c.prepareStatement(delete);) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to delete position: " + e.getMessage(), e);
        }
    }

    public void deleteAll() {
        String deleteAll = "DELETE FROM position;";
        try (PreparedStatement stmt = c.prepareStatement(deleteAll);) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}