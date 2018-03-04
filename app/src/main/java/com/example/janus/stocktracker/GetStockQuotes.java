package com.example.janus.stocktracker;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class GetStockQuotes implements Runnable {

    private Handler callingThreadHandler;
    private StockQuoteRequest stockQuoteRequest;
    private WebStockQuote webStockQuote;

    public GetStockQuotes(Handler callingThreadHandler, StockQuoteRequest stockQuoteRequest, WebStockQuote webStockQuote) {
        this.callingThreadHandler = callingThreadHandler;
        this.stockQuoteRequest = stockQuoteRequest;
        this.webStockQuote = webStockQuote;

    }

    @Override
    public void run() {

        List<StockQuote> stockQuotesArrayList = new ArrayList<>();

        for (String stockTicker: stockQuoteRequest.getStocksToBeSearched()) {

            StockQuote stockQuote = webStockQuote.getRetrofitStockQuote(stockTicker);

            if (stockQuote != null) {
                stockQuotesArrayList.add(stockQuote);
            }

        }

// Send the results back to the MainActivity using the  Handler
        Message returnStockQuote = Message.obtain();

        returnStockQuote.obj = new StockQuoteResult(stockQuotesArrayList, stockQuoteRequest.getDestinationFragment());
        callingThreadHandler.sendMessage(returnStockQuote);

    }

}
