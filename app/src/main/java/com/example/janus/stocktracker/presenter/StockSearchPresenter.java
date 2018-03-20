package com.example.janus.stocktracker.presenter;

import java.util.ArrayList;
import java.util.List;

import com.example.janus.stocktracker.model.stockquotes.GetStockQuotes;

public class StockSearchPresenter implements StockSearchContract.Presenter, GetStockQuotes.ProcessStockQuoteResults, PortfolioAccessContract.View {

    private StockSearchContract.View stockSearchView;
    private PortfolioAccessContract.Presenter portfolioSource;
    private GetStockQuotes stockQuoteSource;

    public StockSearchPresenter(StockSearchContract.View stockSearchView, PortfolioAccessContract.Presenter portfolioSource, GetStockQuotes stockQuoteSource) {
        this.stockSearchView = stockSearchView;
        this.portfolioSource = portfolioSource;
        this.portfolioSource.setPortfolioAccessView(this);
        this.stockQuoteSource = stockQuoteSource;
        this.stockQuoteSource.setProcessStockQuoteResults(this);
    }

    public void setStockSearchView(StockSearchContract.View stockSearchView) {
        this.stockSearchView = stockSearchView;
    }

    @Override
    public void searchPortfolio() {
        portfolioSource.getPortfolio();
    }

    public void searchStocks(String stockSymbol) {
        List <String> stockSearchList = new ArrayList<>();
        stockSearchList.add(stockSymbol);
        stockQuoteSource.getStockQuotes(stockSearchList);
    }

    public void searchStocks(List<String> stockSymbols) {
        stockQuoteSource.getStockQuotes(stockSymbols);
    }

    @Override
    public void processStockQuoteSuccess(List<StockQuote> stockQuoteResults) {
        stockSearchView.displayStocks(stockQuoteResults);
    }

    @Override
    public void processStockQuoteFailure() {
        stockSearchView.displayNetworkErrorMessage();
    }

    @Override
    public void processStockQuoteNotFound() {
        stockSearchView.displayNotFoundErrorMessage();
    }

    @Override
    public void portfolioAccessSuccess(List<String> stockTickers) {

        if (stockTickers.size() == 0) {
            stockSearchView.displayEmptyPortfolioErrorMessage();

        } else {
            searchStocks(stockTickers);
        }
    }

    @Override
    public void portfolioAccessFailure() {
        stockSearchView.displayDatabaseErrorMessage();
    }
}
