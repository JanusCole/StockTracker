package com.example.janus.stocktracker;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class StockQuoteRequest {

    private Fragment destinationFragment;
    private List <String> stocks;

    public StockQuoteRequest(List<String> stocks, Fragment destinationFragment) {
        this.stocks = stocks;
        this.destinationFragment = destinationFragment;
    }

    public Fragment getDestinationFragment() {
        return destinationFragment;
    }

    public List<String> getStocks() {
        return stocks;
    }
}
