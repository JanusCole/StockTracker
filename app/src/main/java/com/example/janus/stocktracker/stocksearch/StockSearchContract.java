package com.example.janus.stocktracker.stocksearch;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.stocksearch.StockSearchPresenter;

// This defines the relationship between the Stock Search View and its Presenter

public interface StockSearchContract {

    interface View {
        void setPresenter(StockSearchContract.Presenter stockSearchPresenter);
        void showStockQuoteUI(StockQuote stockQuote);
        void showLoadingIndicator();
        void dismissLoadingIndicator();
        void showNotFoundError();
        void showLoadingError();
    }

    interface Presenter {
        void searchStock(String tickerSymbol);
    }

}
