package com.example.janus.stocktracker;

import android.os.Handler;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class GetStockQuotesBuilder {

    private Handler callingThreadHandler;

    private StockQuoteRequest stockQuoteRequest;

    private WebStockQuote webStockQuote;

    public GetStockQuotesBuilder() {}

    public GetStockQuotesBuilder setCallingThreadHandler(Handler callingThreadHandler) {
        this.callingThreadHandler = callingThreadHandler;
        return this;
    }

    public GetStockQuotesBuilder setStockQuoteRequest(StockQuoteRequest stockQuoteRequest) {
        this.stockQuoteRequest = stockQuoteRequest;
        return this;
    }

    public GetStockQuotesBuilder setWebStockQuote(WebStockQuote webStockQuote) {
        this.webStockQuote = webStockQuote;
        return this;
    }

    public GetStockQuotes build() {
        return new GetStockQuotes(callingThreadHandler,
                stockQuoteRequest,
                webStockQuote);
    }
}
