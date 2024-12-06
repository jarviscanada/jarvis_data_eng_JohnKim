package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;

import java.util.Optional;

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
        Optional<Position> existingPositionOpt = dao.findById(ticker);
        Position position;
        if (existingPositionOpt.isPresent()) {
            position = existingPositionOpt.get();
            position.setNumOfShares(position.getNumOfShares() + numberOfShares);
            position.setValuePaid(position.getValuePaid() + (price * numberOfShares));
        } else {
            position = new Position();
            position.setTicker(ticker);
            position.setNumOfShares(numberOfShares);
            position.setValuePaid(price * numberOfShares);
        }
        try {
            return dao.save(position);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Sells all shares of the given ticker symbol
     * 
     * @param ticker
     */
    public void sell(String ticker) {
        dao.deleteById(ticker);
    }
}