package com.example.janus.stocktracker;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitStockQuote implements WebStockQuote {

    private String jsonBaseURL;

    public RetrofitStockQuote(String jsonBaseURL) {
        this.jsonBaseURL = jsonBaseURL;
    }

    @Override
    public StockQuote getRetrofitStockQuote(String stockTicker) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(jsonBaseURL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        StockQuoteInterface stockQuoteClient = retrofit.create(StockQuoteInterface.class);

        Call<StockQuote> stockQuoteCall = stockQuoteClient.getIEXStockQuote(stockTicker);

        StockQuote stockQuote = null;

        try {
            stockQuote =  stockQuoteCall.execute().body();

        } catch (IOException e) {
            Log.d("I/O ERROR", "Retrofit " + e.getMessage());
        }

        return stockQuote;

    }
}
