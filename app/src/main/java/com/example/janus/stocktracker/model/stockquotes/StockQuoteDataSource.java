package com.example.janus.stocktracker.model.stockquotes;

import java.util.List;

public interface StockQuoteDataSource {

    interface GetStockQuotesCallback {

        void onStockQuotesLoaded(List<StockQuote> stockQuotes);

        void onDataNotAvailable();
    }

    interface GetStockQuoteCallback {

        void onStockQuoteLoaded(StockQuote stockQuote);

        void onDataNotAvailable();
    }

    void getStockQuotes(List<String> tickerSymbol, GetStockQuotesCallback getStockQuoteCallback);

    void getStockQuote(String tickerSymbol, GetStockQuoteCallback getStockQuoteCallback);

}
