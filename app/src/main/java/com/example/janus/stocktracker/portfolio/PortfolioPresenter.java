package com.example.janus.stocktracker.portfolio;

import com.example.janus.stocktracker.data.stockquotes.PortfolioQuoteService;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;

import java.util.List;

// This is the Presenter for the Portfoio display.It gets called when the View starts. It calls a
// service that gets a list of stock tickers from the portfolio database which, in turn, calls a stock quote
// service for each one. A list of StockQuote objects is returned representing to the user's portfolio

public class PortfolioPresenter implements PortfolioContract.Presenter  {

    private PortfolioContract.View portfolioView;

    private PortfolioQuoteService portfolioQuoteService;

    public PortfolioPresenter(PortfolioContract.View portfolioView, PortfolioQuoteService portfolioQuoteService) {

        this.portfolioView = portfolioView;
        this.portfolioView.setPresenter(this);

        this.portfolioQuoteService = portfolioQuoteService;

    }

    @Override
    public void loadPortfolio() {

        portfolioView.showLoadingIndicator();

        // Call the portfolio stock quotes service. This returns a List of StockQuote objects
        portfolioQuoteService.getPortfolioQuotes(new PortfolioQuoteService.GetPortfolioQuotesCallback() {
            @Override
            public void onStockQuotesLoaded(List<StockQuote> stockQuotes) {

                if (stockQuotes.isEmpty()) {
                    portfolioView.dismissLoadingIndicator();
                    portfolioView.showEmptyPortfolioMessage();
                } else {
                    portfolioView.dismissLoadingIndicator();
                    portfolioView.showStocks(stockQuotes);
                }
            }

            // Display error if the database was not available
            @Override
            public void onDataBaseError() {
                portfolioView.dismissLoadingIndicator();
                portfolioView.showLoadingError();
            }

            // Display an error message of the stock quote service had a networking error
            @Override
            public void onDataNotAvailable() {
                portfolioView.dismissLoadingIndicator();
                portfolioView.showLoadingError();
            }
        });

    }

    // Handle event where the user selects an individual stock from the portfolio for display
    @Override
    public void selectIndividualStockQuote(StockQuote stockQuote) {
        portfolioView.showIndividualStockQuote(stockQuote);

    }

}
