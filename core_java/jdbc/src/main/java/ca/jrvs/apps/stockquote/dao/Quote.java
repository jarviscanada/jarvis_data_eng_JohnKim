package ca.jrvs.apps.stockquote.dao;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quote {

    @JsonProperty("01. symbol") // id
    private String ticker;
    @JsonProperty("02. open")
    private double open;
    @JsonProperty("03. high")
    private double high;
    @JsonProperty("04. low")
    private double low;
    @JsonProperty("05. price")
    private double price;
    @JsonProperty("06. volume")
    private int volume;
    @JsonProperty("07. latest trading day")
    private Date latestTradingDay;
    @JsonProperty("08. previous close")
    private double previousClose;
    @JsonProperty("09. change")
    private double change;
    @JsonProperty("10. change percent")
    private String changePercent;

    private Timestamp timestamp; // time when the info was pulled

    // Setters
    public void setTimestamp() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setLatestTradingDay(Date latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    // Getters for testings
    public String getTicker() {
        return ticker;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public Date getLatestTradingDay() {
        return latestTradingDay;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public double getChange() {
        return change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}