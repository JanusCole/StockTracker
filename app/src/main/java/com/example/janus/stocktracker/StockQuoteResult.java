package com.example.janus.stocktracker;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class StockQuoteResult {

// The app has just one class than handles all stock search requests. However, the results can be displayed by a number of
// different kinds of fragments. So this class is used to combine a stock search results and their intended destination fragment.

    private List <StockQuote> stockQuotes;
    private Fragment destinationFragment;

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
