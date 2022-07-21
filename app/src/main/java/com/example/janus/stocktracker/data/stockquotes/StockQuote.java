package com.example.janus.stocktracker.data.stockquotes;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class StockQuote implements Serializable {

// This takes the result of the stock quote search

    @SerializedName("Symbol")
    private String symbol;

    @SerializedName("Name")
    private String companyName;

    @SerializedName("Sector")
    private String sector;

    @SerializedName("52WeekLow")
    private double open;

    @SerializedName("52WeekHigh")
    private double close;

    @SerializedName("50DayMovingAverage")
    private double latestPrice;

    @SerializedName("SharesOutstanding")
    private long latestVolume;

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

