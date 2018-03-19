package com.example.janus.stocktracker.model.stockquotes;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.example.janus.stocktracker.model.codes.ResultCode;

public class GetStockQuotesFromIEX extends GetStockQuotes {

    private final String IEXBaseURL = "https://api.iextrading.com";

    public GetStockQuotesFromIEX(ProcessStockQuoteResults processStockQuoteResults) {
        super(processStockQuoteResults);
    }

    private interface IEXStockQuoteInterface {
// This interface defines the syntax for calling the IEX stock quote API
        @GET("/1.0/stock/{stockTicker}/quote")
        Call<StockQuote> getIEXStockQuote(@Path("stockTicker") String stockTicker);
    }

    @Override
    public void getStockQuotes(List<String> stockTickers) {

        new SearchStockQuotesAsync().execute(stockTickers);

    }

    private class SearchStockQuotesAsync extends AsyncTask <List<String>, Void, GetStockQuotesResult> {
        @Override
        protected GetStockQuotesResult doInBackground(List<String>... params) {

            List<String> stockTickers = params[0];

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(IEXBaseURL)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = retrofitBuilder.build();
            IEXStockQuoteInterface stockQuoteClient = retrofit.create(IEXStockQuoteInterface.class);

            ResultCode returnCode = null;

            List<StockQuote> stockQuotesArrayList = new ArrayList<>();

            for (String stockTicker : stockTickers) {

                Call<StockQuote> stockQuoteCall = stockQuoteClient.getIEXStockQuote(stockTicker);

                Response<StockQuote> stockQuote = null;

                try {
                    stockQuote = stockQuoteCall.execute();

                } catch (Exception e) {
                    returnCode = ResultCode.NETWORK_ERROR;
                    break;
                }

                if (stockQuote.code() == HttpURLConnection.HTTP_OK) {
                    stockQuotesArrayList.add(stockQuote.body());
                    returnCode = ResultCode.SUCCESS;
                }
                else if (stockQuote.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    returnCode = ResultCode.NOT_FOUND;
                    break;
                } else {
                    returnCode = ResultCode.NETWORK_ERROR;
                    break;
                }

            }
            return new GetStockQuotesResult(stockQuotesArrayList, returnCode);
        }

        @Override
        protected void onPostExecute(GetStockQuotesResult stockSearchResult) {
            processStockQuoteResults.processStockQuoteResults(stockSearchResult.getStockQuotes(), stockSearchResult.getResultCode());
        }
    }
}
