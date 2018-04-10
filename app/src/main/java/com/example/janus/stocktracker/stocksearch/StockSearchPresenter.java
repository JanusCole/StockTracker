package com.example.janus.stocktracker.stocksearch;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;

public class StockSearchPresenter implements StockSearchContract.Presenter {

    private StockSearchContract.View stockSearchView;
    private StockQuoteService stockQuoteDataSource;

    public StockSearchPresenter(StockSearchContract.View stockSearchView, StockQuoteService stockQuoteDataSource) {

        this.stockSearchView = stockSearchView;
        this.stockSearchView.setPresenter(this);

        this.stockQuoteDataSource = stockQuoteDataSource;

    }

    @Override
    public void searchStock(String tickerSymbol) {

        stockSearchView.showLoadingIndicator();

        stockQuoteDataSource.getStockQuote(tickerSymbol, new StockQuoteService.GetStockQuoteCallback() {
            @Override
            public void onStockQuoteLoaded(StockQuote stockQuote) {

                if (stockQuote == null) {
                    stockSearchView.dismissLoadingIndicator();
                    stockSearchView.showNotFoundError();
                } else {
                    stockSearchView.dismissLoadingIndicator();
                    stockSearchView.showStockQuoteUI(stockQuote);
                }

            }

            @Override
            public void onDataNotAvailable() {
                stockSearchView.dismissLoadingIndicator();
                stockSearchView.showLoadingError();
            }
        });

    }
}
