package com.example.janus.stocktracker.portfolio;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;

import java.util.List;

public interface PortfolioContract {

    public interface View {
        void setPresenter(PortfolioContract.Presenter portfolioPresenter);
        void showStocks(List<StockQuote> stockQuotes);
        void showLoadingIndicator();
        void dismissLoadingIndicator();
        void showLoadingError();
        void showStockQuote(StockQuote stockQuote);
        void showEmptyPortfolioMessage();
    }

    public interface Presenter {
        void loadStocks();
        void selectIndividualStockQuote(StockQuote stockQuote);
    }

}
