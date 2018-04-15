package com.example.janus.stocktracker.portfolio;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;

import java.util.List;

// This defines the relationship between the Portfolio View and it's Presenter

public interface PortfolioContract {

    interface View {
        void setPresenter(PortfolioContract.Presenter portfolioPresenter);
        void showStocks(List<StockQuote> stockQuotes);
        void showLoadingIndicator();
        void dismissLoadingIndicator();
        void showLoadingError();
        void showIndividualStockQuote(StockQuote stockQuote);
        void showEmptyPortfolioMessage();
    }

    interface Presenter {
        void loadPortfolio();
        void selectIndividualStockQuote(StockQuote stockQuote);
    }

}
