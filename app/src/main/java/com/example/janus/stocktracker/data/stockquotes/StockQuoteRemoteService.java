package com.example.janus.stocktracker.data.stockquotes;

import android.os.AsyncTask;

import com.example.janus.stocktracker.util.AppExecutors;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockQuoteRemoteService implements StockQuoteService {

    private static StockQuoteRemoteService INSTANCE = null;

    private StockQuotesWebClient stockQuotesAPI;

    AppExecutors ioThread;
    List<StockQuote> stockQuotesArrayList = new ArrayList<>();

    private StockQuoteRemoteService(StockQuotesWebClient stockQuotesAPI) {
        this.stockQuotesAPI = stockQuotesAPI;
        ioThread = new AppExecutors();
    }

    public static StockQuoteRemoteService getInstance (StockQuotesWebClient stockQuotesAPI) {
        if (INSTANCE == null) {
            INSTANCE = new StockQuoteRemoteService(stockQuotesAPI);
        }

        return INSTANCE;
    }

    @Override
    public void getStockQuotes(final List<String> tickerSymbols, final GetStockQuotesCallback getStockQuoteCallback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(stockQuotesAPI.getBASE_URL())
                        .addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = retrofitBuilder.build();
                StockQuotesWebClient.StockQuoteInterface stockQuoteClient = retrofit.create(StockQuotesWebClient.StockQuoteInterface.class);

                stockQuotesArrayList.clear();

                for (String stockTicker : tickerSymbols) {

                    Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote(stockTicker);

                    try {
                        final Response<StockQuote> stockQuote = stockQuoteCall.execute();
                        if (stockQuote.code() == HttpURLConnection.HTTP_OK) {
                            stockQuotesArrayList.add(stockQuote.body());
                        } else {
                            ioThread.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getStockQuoteCallback.onDataNotAvailable();
                                }
                            });

                        }


                    } catch (Exception e) {
                        ioThread.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                getStockQuoteCallback.onDataNotAvailable();
                            }
                        });
                    }

                }

                ioThread.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        getStockQuoteCallback.onStockQuotesLoaded(stockQuotesArrayList);
                    }
                });

            }
        };

        ioThread.networkIO().execute(runnable);

    }

    @Override
    public void getStockQuote(final String tickerSymbol, final GetStockQuoteCallback getStockQuoteCallback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(stockQuotesAPI.getBASE_URL())
                        .addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = retrofitBuilder.build();
                StockQuotesWebClient.StockQuoteInterface stockQuoteClient = retrofit.create(StockQuotesWebClient.StockQuoteInterface.class);

                Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote(tickerSymbol);

                try {
                    final Response<StockQuote> stockQuote = stockQuoteCall.execute();

                    ioThread.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if ((stockQuote.code() != HttpURLConnection.HTTP_OK) && (stockQuote.code() != HttpURLConnection.HTTP_NOT_FOUND)) {
                                getStockQuoteCallback.onDataNotAvailable();
                            } else {
                                getStockQuoteCallback.onStockQuoteLoaded(stockQuote.body());
                            }
                        }
                    });

                } catch (IOException e) {
                    ioThread.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            getStockQuoteCallback.onDataNotAvailable();
                        }
                    });
                }

            }
        };

        ioThread.networkIO().execute(runnable);

    }


}
