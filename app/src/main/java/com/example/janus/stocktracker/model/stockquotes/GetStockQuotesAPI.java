package com.example.janus.stocktracker.model.stockquotes;

import android.support.annotation.VisibleForTesting;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class GetStockQuotesAPI {

    private String IEXBaseURL = "https://api.iextrading.com";

    public interface IEXStockQuoteInterface {
// This interface defines the syntax for calling the IEX stock quote API
        @GET("/1.0/stock/{stockTicker}/quote")
        Call<DeserializedIEXData> getIEXStockQuote(@Path("stockTicker") String stockTicker);
    }

    @VisibleForTesting
    public void setIEXBaseURL(String IEXBaseURL) {
        this.IEXBaseURL = IEXBaseURL;
    }

    public String getIEXBaseURL() {
        return IEXBaseURL;
    }
}
