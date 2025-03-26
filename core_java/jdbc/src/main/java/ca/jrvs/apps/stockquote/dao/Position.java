package ca.jrvs.apps.stockquote.dao;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {

    @JsonProperty("01. symbol") // id
    private String ticker;
    @JsonProperty("02. number_of_shares")
    private int numOfShares;
    @JsonProperty("03. value_paid")
    private double valuePaid;

    // Getters and setters
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getNumOfShares() {
        return numOfShares;
    }

    public void setNumOfShares(int numOfShares) {
        this.numOfShares = numOfShares;
    }

    public double getValuePaid() {
        return valuePaid;
    }

    public void setValuePaid(double valuePaid) {
        this.valuePaid = valuePaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        return numOfShares == position.numOfShares &&
                Double.compare(position.valuePaid, valuePaid) == 0 &&
                Objects.equals(ticker, position.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, numOfShares, valuePaid);
    }

    @Override
    public String toString() {
        return "Position{" +
                "ticker='" + ticker + '\'' +
                ", numOfShares=" + numOfShares +
                ", valuePaid=" + valuePaid +
                '}';
    }
}