package com.example.janus.stocktracker;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class StockQuoteResult {

// Class to pass back stock quote results
    private Fragment destinationFragment;
    private List <StockQuote> stockQuotes;

    public StockQuoteResult(List<StockQuote> stockList, Fragment destinationFragment) {
        this.stockQuotes = stockList;
        this.destinationFragment = destinationFragment;
    }

    public Fragment getDestinationFragment() {
        return destinationFragment;
    }

    public List<StockQuote> getStockQuotes() {
        return stockQuotes;
    }
}
