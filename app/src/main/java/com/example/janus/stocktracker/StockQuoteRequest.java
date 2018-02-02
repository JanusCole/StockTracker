package com.example.janus.stocktracker;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class StockQuoteRequest {

    private Fragment destinationFragment;
    private ArrayList <String> stocks;

    public StockQuoteRequest(ArrayList<String> stocks, Fragment destinationFragment) {
        this.stocks = stocks;
        this.destinationFragment = destinationFragment;
    }

    public Fragment getDestinationFragment() {
        return destinationFragment;
    }

    public ArrayList<String> getStocks() {
        return stocks;
    }
}
