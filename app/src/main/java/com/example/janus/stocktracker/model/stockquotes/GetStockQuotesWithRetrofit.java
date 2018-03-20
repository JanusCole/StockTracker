package com.example.janus.stocktracker.model.stockquotes;

import android.os.AsyncTask;

import com.example.janus.stocktracker.presenter.StockQuote;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetStockQuotesWithRetrofit extends GetStockQuotes {

    public GetStockQuotesWithRetrofit(GetStockQuotesAPI getStockQuotesAPI) {
        super(getStockQuotesAPI);
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
                    .baseUrl(getStockQuotesAPI.getIEXBaseURL())
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = retrofitBuilder.build();
            GetStockQuotesAPI.IEXStockQuoteInterface stockQuoteClient = retrofit.create(GetStockQuotesAPI.IEXStockQuoteInterface.class);

            GetStockQuotesResult.ResultCode returnCode = null;

            List<StockQuote> stockQuotesArrayList = new ArrayList<>();

            for (String stockTicker : stockTickers) {

                Call<DeserializedIEXData> stockQuoteCall = stockQuoteClient.getIEXStockQuote(stockTicker);

                Response<DeserializedIEXData> stockQuote = null;

                try {
                    stockQuote = stockQuoteCall.execute();

                } catch (Exception e) {
                    returnCode = GetStockQuotesResult.ResultCode.NETWORK_ERROR;
                    break;
                }

                if (stockQuote.code() == HttpURLConnection.HTTP_OK) {
                    StockQuote parcelableStockQuote = new StockQuote (
                            stockQuote.body().getSymbol(),
                            stockQuote.body().getCompanyName(),
                            stockQuote.body().getSector(),
                            stockQuote.body().getOpen(),
                            stockQuote.body().getClose(),
                            stockQuote.body().getLatestPrice(),
                            stockQuote.body().getLatestVolume());
                    stockQuotesArrayList.add(parcelableStockQuote);
                    returnCode = GetStockQuotesResult.ResultCode.SUCCESS;
                }
                else if (stockQuote.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    returnCode = GetStockQuotesResult.ResultCode.NOT_FOUND;
                    break;
                } else {
                    returnCode = GetStockQuotesResult.ResultCode.NETWORK_ERROR;
                    break;
                }

            }
            return new GetStockQuotesResult(stockQuotesArrayList, returnCode);
        }

        @Override
        protected void onPostExecute(GetStockQuotesResult stockSearchResult) {

            if (stockSearchResult.getResultCode() == GetStockQuotesResult.ResultCode.SUCCESS) {
                processStockQuoteResults.processStockQuoteSuccess(stockSearchResult.getStockQuotes());
            }
            else if (stockSearchResult.getResultCode() == GetStockQuotesResult.ResultCode.NOT_FOUND) {
                processStockQuoteResults.processStockQuoteNotFound();
            }
            else if (stockSearchResult.getResultCode() == GetStockQuotesResult.ResultCode.NETWORK_ERROR) {
                processStockQuoteResults.processStockQuoteFailure();
            }

        }
    }

    public static class GetStockQuotesResult {

        private List <StockQuote> stockQuotes;
        private ResultCode resultCode;

        public GetStockQuotesResult(List<StockQuote> stockQuotes, ResultCode resultCode) {
            this.stockQuotes = stockQuotes;
            this.resultCode = resultCode;
        }

        public List<StockQuote> getStockQuotes() {
            return stockQuotes;
        }

        public ResultCode getResultCode() {
            return resultCode;
        }

        public enum ResultCode {

            SUCCESS, NETWORK_ERROR, NOT_FOUND;

        }

    }
}
