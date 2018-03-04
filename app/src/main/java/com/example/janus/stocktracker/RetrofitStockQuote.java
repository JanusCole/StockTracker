package com.example.janus.stocktracker;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitStockQuote implements WebStockQuote {

// This is the class that actually implements Retrofit
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
// TODO Need to handle Retrofit errors elegantly. At present, the user will get a message indicating the stooc was not found for
// TODO all errors. That works, but it would be betterif the error message was more helpful.
        }

        return stockQuote;

    }
}
