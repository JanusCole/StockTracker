package com.example.janus.stocktracker.data.stockquotes;

import android.support.annotation.VisibleForTesting;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class StockQuotesAPI {

    private String BASE_URL = "https://api.iextrading.com";

    public interface StockQuoteInterface {
// This interface defines the syntax for calling the IEX stock quote API
        @GET("/1.0/stock/{stockTicker}/quote")
        Call<StockQuote> getStockQuote(@Path("stockTicker") String stockTicker);
    }

    @VisibleForTesting
    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }
}
