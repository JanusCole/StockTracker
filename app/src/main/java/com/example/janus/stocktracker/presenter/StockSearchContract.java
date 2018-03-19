package com.example.janus.stocktracker.presenter;

import java.util.List;
import com.example.janus.stocktracker.model.stockquotes.StockQuote;

public interface StockSearchContract {

    public interface View {
        void displayOneStock(StockQuote quote);
        void displayManyStocks(List<StockQuote> quotes);
        void displayDatabaseErrorMessage();
        void displayEmptyPortfolioErrorMessage();
        void displayNotFoundErrorMessage();
        void displayNetworkErrorMessage();

    }

    public interface Presenter {
        void searchStocks(List<String> stockSymbols);
        void searchPortfolio();
    }

}
