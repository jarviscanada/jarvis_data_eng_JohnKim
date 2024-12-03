package ca.jrvs.apps.stockquote.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;

public class PositionService {

    private PositionDao dao;

    public PositionService(PositionDao dao) {
        this.dao = dao;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * 
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        Position position = new Position();
        position.setTicker(ticker);
        position.setNumOfShares(numberOfShares);
        position.setValuePaid(price * numberOfShares);
        return dao.save(position);
    }

    /**
     * Sells all shares of the given ticker symbol
     * 
     * @param ticker
     */
    public void sell(String ticker) {
        dao.deleteById(ticker);
    }

    public static void main(String[] args) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stock_quote", "postgres",
                "password");
        PositionDao dao = new PositionDao(c);
        PositionService service = new PositionService(dao);
        service.buy("AAPL", 10, 100);
    }

}