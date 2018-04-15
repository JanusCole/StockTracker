package com.example.janus.stocktracker.data.stockquotes;

import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;
import com.example.janus.stocktracker.portfolio.PortfolioContract;

import java.util.List;

// This class returns a List of StockQuotes corresponding to the user's collection of portfolio stocks.
// It first accesses the portfolio database to get the list of stock ticker symbols and then passes them
// to the StockQuote Service

public class PortfolioQuoteService {

    private TickerSymbolsDataSource tickerSymbolDataSource;
    private StockQuoteService stockQuoteDataSource;

    public PortfolioQuoteService(TickerSymbolsDataSource tickerSymbolDataSource, StockQuoteService stockQuoteDataSource) {
        this.tickerSymbolDataSource = tickerSymbolDataSource;
        this.stockQuoteDataSource = stockQuoteDataSource;
    }

    public interface GetPortfolioQuotesCallback {
        void onDataBaseError();
        void onStockQuotesLoaded(List<StockQuote> stockQuotes);
        void onDataNotAvailable();
    }

    public void getPortfolioQuotes(final GetPortfolioQuotesCallback callback) {

        tickerSymbolDataSource.getAllTickerSymbols(new TickerSymbolsDataSource.LoadTickerSymbolsCallback() {
            @Override
            public void onTickerSymbolsLoaded(List<String> tickerSymbols) {
                stockQuoteDataSource.getStockQuotes(tickerSymbols, new StockQuoteService.GetStockQuotesCallback() {
                    @Override
                    public void onStockQuotesLoaded(List<StockQuote> stockQuotes) {
                        callback.onStockQuotesLoaded(stockQuotes);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }

            @Override
            public void onDataBaseError() {
                callback.onDataBaseError();
            }
        });
    }

}
