package com.example.janus.stocktracker.data.stockquotes;

import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.util.Log;

import com.example.janus.stocktracker.util.AppExecutors;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RemoteStockQuoteService implements StockQuoteService {

    private static RemoteStockQuoteService INSTANCE = null;

    private AppExecutors appExecutors;

    private String BASE_URL = "https://api.iextrading.com";

    private interface StockQuoteInterface {
        @GET("/1.0/stock/{stockTicker}/quote")
        Call<StockQuote> getStockQuote(@Path("stockTicker") String stockTicker);
    }

    // Private constructor for singleton
    private RemoteStockQuoteService() {
        appExecutors = new AppExecutors();
    }

    // Public getInstance for singleton
    public static RemoteStockQuoteService getInstance () {
        if (INSTANCE == null) {
            INSTANCE = new RemoteStockQuoteService();
        }

        return INSTANCE;
    }

    @Override
    public void getStockQuotes(final List<String> tickerSymbols, final GetStockQuotesCallback getStockQuoteCallback) {

        // Performed in a thread because the source does not perform batched requests and so for multiple stock searches,
        // it needs to run synchronously in a loop
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Create the Retrofit client
                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = retrofitBuilder.build();
                StockQuoteInterface stockQuoteClient = retrofit.create(StockQuoteInterface.class);

                List<StockQuote> stockQuotesArrayList = new ArrayList<>();

                // This is done with a loop beacuse the source does not perform batched requests
                for (String stockTicker : tickerSymbols) {

                    Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote(stockTicker);

                    try {
                        final Response<StockQuote> stockQuote = stockQuoteCall.execute();
                        if ((stockQuote.code() == HttpURLConnection.HTTP_OK) || (stockQuote.code() == HttpURLConnection.HTTP_NOT_FOUND)) {
                            if (stockQuote.body() != null) {
                                stockQuotesArrayList.add(stockQuote.body());
                            }
                        } else {
                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getStockQuoteCallback.onDataNotAvailable();
                                }
                            });

                        }


                    } catch (Exception e) {
                        appExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                getStockQuoteCallback.onDataNotAvailable();
                            }
                        });
                    }

                }

                final List<StockQuote> stockQuotesSearchResult = new ArrayList<>(stockQuotesArrayList);

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        getStockQuoteCallback.onStockQuotesLoaded(stockQuotesSearchResult);
                    }
                });



            }
        };

        appExecutors.networkIO().execute(runnable);

    }

    @VisibleForTesting
    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

}
