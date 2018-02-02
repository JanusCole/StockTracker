package com.example.janus.stocktracker;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class StockQuoteResult {

    private Fragment destinationFragment;
    private ArrayList <StockQuote> stockQuotes;

    public StockQuoteResult(ArrayList<StockQuote> stockList, Fragment destinationFragment) {
        this.stockQuotes = stockList;
        this.destinationFragment = destinationFragment;
    }

    public Fragment getDestinationFragment() {
        return destinationFragment;
    }

    public ArrayList<StockQuote> getStockQuotes() {
        return stockQuotes;
    }
}
