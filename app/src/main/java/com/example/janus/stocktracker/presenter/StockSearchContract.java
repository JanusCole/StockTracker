package com.example.janus.stocktracker.presenter;

import com.example.janus.stocktracker.model.stockquotes.StockQuote;

public interface StockSearchContract {

    public interface View {
        void setPresenter(StockSearchPresenter stockSearchPresenter);
        void showStockQuoteUI(StockQuote stockQuote);
        void showLoadingIndicator();
        void showNotFoundError();
        void showLoadingError();
    }

    public interface Presenter {
        void searchStock(String tickerSymbol);
    }

}
