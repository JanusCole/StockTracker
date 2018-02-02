package com.example.janus.stocktracker;

public interface AccessPortfolio {

    void addToPortfolio(String stockTicker);
    void removeFromPortfolio(String stockTicker);

    boolean checkPortfolio(String stockTicker);

}
