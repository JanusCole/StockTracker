package com.example.janus.stocktracker;

public interface AccessPortfolio {

// The interface has the methods needed to access the app's portfolio database

    void addToPortfolio(String stockTicker);
    void removeFromPortfolio(String stockTicker);

    boolean checkPortfolio(String stockTicker);

}
