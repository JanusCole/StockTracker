package com.example.janus.stocktracker.presenter;

import java.util.List;

public interface PortfolioAccessContract {

    public interface View {
        void portfolioAccessSuccess(List<String> stockTickers);
        void portfolioAccessFailure();
    }

    public interface Presenter {
        void setPortfolioAccessView(PortfolioAccessContract.View portfolioAccessView);
        void addStock(String tickerSymbol);
        void deleteStock(String tickerSymbol);
        void checkStock(String tickerSymbol);
        void getPortfolio ();
    }

}
