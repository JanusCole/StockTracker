package com.example.janus.stocktracker.data.stockquotes;
import com.example.janus.stocktracker.BuildConfig;
import android.support.annotation.VisibleForTesting;

import com.example.janus.stocktracker.util.AppExecutors;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RemoteStockQuoteService implements StockQuoteService {

    private static RemoteStockQuoteService INSTANCE = null;

    private AppExecutors appExecutors;

    private String BASE_URL = "https://www.alphavantage.co/";

    private String APIKEY = BuildConfig.APIKEY;

    private interface StockQuoteInterface {
        @GET("query?function=OVERVIEW")
        Call<StockQuote> getStockQuote(@Query("symbol") String stockTicker,
                                       @Query("apikey") String apikey);
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

                StockQuoteInterface stockQuoteClient = createRetrofitClient();

                List<StockQuote> stockQuotesArrayList = new ArrayList<>();
                callRetrofitForTickerSymbolList(stockQuoteClient, stockQuotesArrayList, tickerSymbols, getStockQuoteCallback);


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

    private void callRetrofitForTickerSymbolList(StockQuoteInterface stockQuoteClient, List<StockQuote> stockQuotesArrayList, List<String> tickerSymbols, final GetStockQuotesCallback getStockQuoteCallback) {
        // This is done with a loop beacuse the source does not perform batched requests
        for (String stockTicker : tickerSymbols) {
            Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote(stockTicker, APIKEY);
            callRetrofitClient(stockQuotesArrayList, getStockQuoteCallback, stockQuoteCall);
        }
    }

    private void callRetrofitClient(List<StockQuote> stockQuotesArrayList, final GetStockQuotesCallback getStockQuoteCallback, Call<StockQuote> stockQuoteCall) {

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

    private StockQuoteInterface createRetrofitClient() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(StockQuoteInterface.class);
    }

    @VisibleForTesting
    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

}
