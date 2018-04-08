package com.example.janus.stocktracker.portfolio;

import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteDataSource;

import java.util.List;

public class PortfolioPresenter implements PortfolioContract.Presenter  {

    private PortfolioContract.View portfolioView;
    private TickerSymbolsDataSource tickerSymbolDataSource;
    private StockQuoteDataSource stockQuoteDataSource;

    public PortfolioPresenter(PortfolioContract.View portfolioView, TickerSymbolsDataSource tickerSymbolDataSource, StockQuoteDataSource stockQuoteDataSource) {

        this.portfolioView = portfolioView;
        this.portfolioView.setPresenter(this);

        this.tickerSymbolDataSource = tickerSymbolDataSource;

        this.stockQuoteDataSource = stockQuoteDataSource;

    }

    @Override
    public void loadStocks() {

        portfolioView.showLoadingIndicator();

        tickerSymbolDataSource.getAllTickerSymbols(new TickerSymbolsDataSource.LoadTickerSymbolsCallback() {
            @Override
            public void onTickerSymbolsLoaded(List<String> tickerSymbols) {
                if (tickerSymbols.isEmpty()) {
                    portfolioView.showEmptyPortfolioMessage();
                } else {
                    getStockQuotes(tickerSymbols);
                }
            }

            @Override
            public void onDataNotAvailable() {
                portfolioView.showLoadingError();
            }
        });
    }

    @Override
    public void selectIndividualStockQuote(StockQuote stockQuote) {
        portfolioView.showStockQuote(stockQuote);

    }

    private void getStockQuotes (List<String> tickerSymbols) {

        stockQuoteDataSource.getStockQuotes(tickerSymbols, new StockQuoteDataSource.GetStockQuotesCallback() {
            @Override
            public void onStockQuotesLoaded(List<StockQuote> stockQuotes) {
                portfolioView.showStocks(stockQuotes);
            }

            @Override
            public void onDataNotAvailable() {
                portfolioView.showLoadingError();
            }
        });

    }

}
