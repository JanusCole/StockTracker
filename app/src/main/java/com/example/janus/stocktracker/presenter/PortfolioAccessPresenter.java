package com.example.janus.stocktracker.presenter;

import com.example.janus.stocktracker.model.database.DatabaseAccessAsync;

import java.util.List;

public class PortfolioAccessPresenter implements PortfolioAccessContract.Presenter, DatabaseAccessAsync.OnPortfolioDBAccessCompletion {


    private PortfolioAccessContract.View portfolioAccessView;
    private DatabaseAccessAsync portfolioDatabase;

    public PortfolioAccessPresenter(DatabaseAccessAsync portfolioDatabase) {
        this.portfolioDatabase = portfolioDatabase;
        this.portfolioDatabase.setOnPortfolioDBAccessCompletion(this);
    }

    public void setPortfolioAccessView(PortfolioAccessContract.View portfolioAccessView) {
        this.portfolioAccessView = portfolioAccessView;
    }

    @Override
    public void checkStock (String tickerSymbol) {
        portfolioDatabase.getOneStockPortfolioDBAsync(tickerSymbol);
    }

    @Override
    public void addStock (String tickerSymbol) {
        portfolioDatabase.addOneStockPortfolioDBAsync(tickerSymbol);
    }

    @Override
    public void deleteStock (String tickerSymbol) {
        portfolioDatabase.deleteOneStockPortfolioDBAsync(tickerSymbol);
    }

    public void getPortfolio () {
        portfolioDatabase.getAllStocksPortfolioDBAsync();
    }

    @Override
    public void portfolioDBAccessSuccess(List<String> stockSymbols) {
        portfolioAccessView.portfolioAccessSuccess(stockSymbols);
    }

    @Override
    public void portfolioDBAccessFailure() {
        portfolioAccessView.portfolioAccessFailure();
    }
}
