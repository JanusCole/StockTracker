package com.example.janus.stocktracker.presenter;

import com.example.janus.stocktracker.model.stockquotes.StockQuote;

import java.util.List;

public interface PortfolioContract {

    public interface View {
        void setPresenter(PortfolioPresenter portfolioPresenter);
        void showStocks(List<StockQuote> stockQuotes);
        void showLoadingIndicator();
        void showLoadingError();
        void showStockQuote(StockQuote stockQuote);
        void showEmptyPortfolioMessage();
    }

    public interface Presenter {
        void loadStocks();
        void selectIndividualStockQuote(StockQuote stockQuote);
    }

}
