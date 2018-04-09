package com.example.janus.stocktracker.stocksearch;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteDataSource;

public class StockSearchPresenter implements StockSearchContract.Presenter {

    private StockSearchContract.View stockSearchView;
    private StockQuoteDataSource stockQuoteDataSource;

    public StockSearchPresenter(StockSearchContract.View stockSearchView, StockQuoteDataSource stockQuoteDataSource) {

        this.stockSearchView = stockSearchView;
        this.stockSearchView.setPresenter(this);

        this.stockQuoteDataSource = stockQuoteDataSource;

    }

    @Override
    public void searchStock(String tickerSymbol) {

        stockSearchView.showLoadingIndicator();

        stockQuoteDataSource.getStockQuote(tickerSymbol, new StockQuoteDataSource.GetStockQuoteCallback() {
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
