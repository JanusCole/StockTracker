package com.example.janus.stocktracker.stocksearch;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;

import java.util.ArrayList;
import java.util.List;

// This class performs the functionality behind the stock search view. It receives a stock ticker as a string
// and calls a service to get a quote

public class StockSearchPresenter implements StockSearchContract.Presenter {

    private StockSearchContract.View stockSearchView;
    private StockQuoteService stockQuoteService;

    public StockSearchPresenter(StockSearchContract.View stockSearchView, StockQuoteService stockQuoteService) {

        this.stockSearchView = stockSearchView;
        this.stockSearchView.setPresenter(this);

        this.stockQuoteService = stockQuoteService;

    }

    // The main method that searches for stocks using a stock quote service
    @Override
    public void searchStock(String tickerSymbol) {

        stockSearchView.showLoadingIndicator();

        List<String> searchTickerSymbols = new ArrayList<>();
        searchTickerSymbols.add(tickerSymbol);

        // Call the stock quote service for the ticker symbol
        stockQuoteService.getStockQuotes(searchTickerSymbols, new StockQuoteService.GetStockQuotesCallback() {
            @Override
            public void onStockQuotesLoaded(List<StockQuote> stockQuotes) {
                returnStockQuoteResult(stockQuotes);
            }

            // Tell the view to display a message if a network error occurred
            @Override
            public void onDataNotAvailable() {
                stockSearchView.dismissLoadingIndicator();
                stockSearchView.showLoadingError();
            }
        });

    }

    private void returnStockQuoteResult(List<StockQuote> stockQuotes) {
        // Empty List returned means the stock ticker was not found
        if ((stockQuotes.size() == 0) || (stockQuotes.get(0).getSymbol() == null)) {
            stockSearchView.dismissLoadingIndicator();
            stockSearchView.showNotFoundError();
        } else {
            stockSearchView.dismissLoadingIndicator();
            stockSearchView.showStockQuoteUI(stockQuotes.get(0));
        }
    }
}
