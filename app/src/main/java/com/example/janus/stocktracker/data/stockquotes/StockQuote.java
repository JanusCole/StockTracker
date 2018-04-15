package com.example.janus.stocktracker.data.stockquotes;

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.io.Serializable;

public class StockQuote implements Serializable {

// This takes the result of the stock quote search

    private String symbol;

    private String companyName;

    private String sector;

    private double open;

    private double close;

    private double latestPrice;

    private long latestVolume;

    public StockQuote(String symbol, String companyName, String sector, double open, double close, double latestPrice, long latestVolume) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.sector = sector;
        this.open = open;
        this.close = close;
        this.latestPrice = latestPrice;
        this.latestVolume = latestVolume;
    }

    // Getters
    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSector() {
        return sector;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public long getLatestVolume() {
        return latestVolume;
    }

}

