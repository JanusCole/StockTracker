package com.example.janus.stocktracker.data.stockquotes;

import com.example.janus.stocktracker.BuildConfig;
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

    AppExecutors ioThreads;

    public interface StockQuoteInterface {
        @GET("/1.0/stock/{stockTicker}/quote")
        Call<StockQuote> getStockQuote(@Path("stockTicker") String stockTicker);
    }

    // Private constructor for singleton
    private RemoteStockQuoteService() {
        ioThreads = new AppExecutors();
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
                        .baseUrl(BuildConfig.BASEURL)
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
                            ioThreads.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getStockQuoteCallback.onDataNotAvailable();
                                }
                            });

                        }


                    } catch (Exception e) {
                        ioThreads.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                getStockQuoteCallback.onDataNotAvailable();
                            }
                        });
                    }

                }

                final List<StockQuote> stockQuotesSearchResult = new ArrayList<>(stockQuotesArrayList);

                ioThreads.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        getStockQuoteCallback.onStockQuotesLoaded(stockQuotesSearchResult);
                    }
                });

            }
        };

        ioThreads.networkIO().execute(runnable);

    }

}
