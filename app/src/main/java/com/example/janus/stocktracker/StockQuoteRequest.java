package com.example.janus.stocktracker;

import android.support.v4.app.Fragment;

import java.util.List;

public class StockQuoteRequest {

// The app has just one class than handles all stock search requests. However, the results can be displayed by a number of
// different kinds of fragments. So this class is used to combine a stock search request and its intended destination fragment.

    private List <String> stocksToBeSearched;
    private Fragment destinationFragment;

    public StockQuoteRequest(List<String> stocksToBeSearched, Fragment destinationFragment) {
        this.stocksToBeSearched = stocksToBeSearched;
        this.destinationFragment = destinationFragment;
    }

    public Fragment getDestinationFragment() {
        return destinationFragment;
    }

    public List<String> getStocksToBeSearched() {
        return stocksToBeSearched;
    }
}
