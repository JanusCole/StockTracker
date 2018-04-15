package com.example.janus.stocktracker.data.stockquotes;

import java.util.List;

public interface StockQuoteService {

    interface GetStockQuotesCallback {
        void onStockQuotesLoaded(List<StockQuote> stockQuotes);
        void onDataNotAvailable();
    }

    void getStockQuotes(List<String> tickerSymbol, GetStockQuotesCallback getStockQuoteCallback);
}
