package com.example.janus.stocktracker;

public interface WebStockQuote {

    StockQuote getRetrofitStockQuote(String stockTicker);

}
