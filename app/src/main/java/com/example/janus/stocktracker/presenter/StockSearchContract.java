package com.example.janus.stocktracker.presenter;

import java.util.List;

public interface StockSearchContract {

    public interface View {
        void displayStocks(List<StockQuote> quotes);
        void displayDatabaseErrorMessage();
        void displayEmptyPortfolioErrorMessage();
        void displayNotFoundErrorMessage();
        void displayNetworkErrorMessage();

    }

    public interface Presenter {
        void setStockSearchView(StockSearchContract.View stockSearchView);
        void searchStocks(List<String> stockSymbols);
        void searchPortfolio();
    }

}
