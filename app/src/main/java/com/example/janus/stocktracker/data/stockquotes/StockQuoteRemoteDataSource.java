package com.example.janus.stocktracker.data.stockquotes;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockQuoteRemoteDataSource implements StockQuoteDataSource {

    private static StockQuoteRemoteDataSource INSTANCE = null;

    protected StockQuotesAPI stockQuotesAPI;

    private StockQuoteRemoteDataSource(StockQuotesAPI stockQuotesAPI) {
        this.stockQuotesAPI = stockQuotesAPI;
    }

    public static StockQuoteRemoteDataSource getInstance (StockQuotesAPI stockQuotesAPI) {
        if (INSTANCE == null) {
            INSTANCE = new StockQuoteRemoteDataSource(stockQuotesAPI);
        }

        return INSTANCE;
    }

    @Override
    public void getStockQuotes(List<String> tickerSymbols, final GetStockQuotesCallback getStockQuoteCallback) {

        new AsyncTask<List<String>, Void, List<StockQuote>>() {

            @Override
            protected List<StockQuote> doInBackground(List<String>... params) {

                List<String> stockTickers = params[0];

                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(stockQuotesAPI.getBASE_URL())
                        .addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = retrofitBuilder.build();
                StockQuotesAPI.StockQuoteInterface stockQuoteClient = retrofit.create(StockQuotesAPI.StockQuoteInterface.class);

                List<StockQuote> stockQuotesArrayList = new ArrayList<>();

                for (String stockTicker : stockTickers) {

                    if (!isCancelled()) {

                        Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote(stockTicker);

                        Response<StockQuote> stockQuote = null;

                        try {
                            stockQuote = stockQuoteCall.execute();

                        } catch (Exception e) {
                            cancel(true);
                        }

                        if (stockQuote.code() == HttpURLConnection.HTTP_OK) {
                            stockQuotesArrayList.add(stockQuote.body());
                        } else {
                            cancel(true);
                        }
                    }

                }
                return stockQuotesArrayList;

            }

            @Override
            protected void onPostExecute(List<StockQuote> stockQuotes) {
                getStockQuoteCallback.onStockQuotesLoaded(stockQuotes);
            }

            @Override
            protected void onCancelled() {
                getStockQuoteCallback.onDataNotAvailable();
            }
        }.execute(tickerSymbols);

    }

    @Override
    public void getStockQuote(String tickerSymbol, final GetStockQuoteCallback getStockQuoteCallback) {

        new AsyncTask<String, Void, StockQuote> () {

            @Override
            protected StockQuote doInBackground(String... params) {

                String stockTicker = params[0];

                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(stockQuotesAPI.getBASE_URL())
                        .addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = retrofitBuilder.build();
                StockQuotesAPI.StockQuoteInterface stockQuoteClient = retrofit.create(StockQuotesAPI.StockQuoteInterface.class);

                Call<StockQuote> stockQuoteCall = stockQuoteClient.getStockQuote(stockTicker);

                Response<StockQuote> stockQuote = null;

                try {
                    stockQuote = stockQuoteCall.execute();

                } catch (Exception e) {
                    cancel(true);
                }

                if ((stockQuote.code() != HttpURLConnection.HTTP_OK) && (stockQuote.code() != HttpURLConnection.HTTP_NOT_FOUND)){
                    cancel(true);
                }

                return stockQuote.body();

            }

            @Override
            protected void onPostExecute(StockQuote stockQuote) {
                getStockQuoteCallback.onStockQuoteLoaded(stockQuote);
            }

            @Override
            protected void onCancelled() {
                getStockQuoteCallback.onDataNotAvailable();
            }
        }.execute(tickerSymbol);

    }


}
