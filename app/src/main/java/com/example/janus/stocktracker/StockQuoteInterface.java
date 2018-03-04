package com.example.janus.stocktracker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StockQuoteInterface {

// This interface defines the syntax for calling the EIX stock quote API
    @GET("/1.0/stock/{stockTicker}/quote")
    Call<StockQuote> getIEXStockQuote(@Path("stockTicker") String stockTicker);

}
