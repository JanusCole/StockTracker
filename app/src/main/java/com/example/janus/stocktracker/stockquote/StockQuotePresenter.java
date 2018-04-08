package com.example.janus.stocktracker.stockquote;

import android.graphics.Color;

import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;

public class StockQuotePresenter implements StockQuoteContract.Presenter {

    private StockQuoteContract.View stockQuoteView;
    private TickerSymbolsDataSource tickerSymbolDataSource;

    private StockQuote stockQuote;

    public StockQuotePresenter(StockQuoteContract.View stockQuoteView, StockQuote stockQuote, TickerSymbolsDataSource tickerSymbolDataSource) {

        this.stockQuoteView = stockQuoteView;
        this.stockQuoteView.setPresenter(this);

        this.stockQuote = stockQuote;

        this.tickerSymbolDataSource = tickerSymbolDataSource;

    }

    @Override
    public void loadStock() {
        stockQuoteView.setTickerSymbol(stockQuote.getSymbol());
        stockQuoteView.setCompanyName(stockQuote.getCompanyName());
        stockQuoteView.setSector(stockQuote.getSector());
        stockQuoteView.setLatestPrice(stockQuote.getLatestPrice());
        stockQuoteView.setPriceChange(stockQuote.getLatestPrice() - stockQuote.getOpen());
        stockQuoteView.setOpenPrice(stockQuote.getOpen());
        stockQuoteView.setClosePrice(stockQuote.getClose());
        stockQuoteView.setLatestVolume(stockQuote.getLatestVolume());

        stockQuoteView.setPriceChangePercent(100 * (stockQuote.getLatestPrice() - stockQuote.getOpen())/stockQuote.getOpen());

        if (stockQuote.getLatestPrice() < stockQuote.getOpen()) {
            stockQuoteView.setPriceChangeColor(Color.RED);
        }

        checkStock(stockQuote.getSymbol());
    }

    @Override
    public void addStock(String tickerSymbol) {
        tickerSymbolDataSource.addTickerSymbol(tickerSymbol, new TickerSymbolsDataSource.AddTickerSymbolCallback() {
            @Override
            public void onTickerSymbolAdded() {
                stockQuoteView.showStockInPortfolio();
            }

            @Override
            public void onDataBaseError() {
                stockQuoteView.showDatabaseError();
            }
        });

    }

    @Override
    public void deleteStock(String tickerSymbol) {
        tickerSymbolDataSource.deleteTickerSymbol(tickerSymbol, new TickerSymbolsDataSource.DeleteTickerSymbolCallback() {
            @Override
            public void onTickerSymbolDeleted() {
                stockQuoteView.showStockNotInPortfolio();
            }

            @Override
            public void onDataBaseError() {
                stockQuoteView.showDatabaseError();
            }
        });
    }

    public void checkStock(String tickerSymbol) {

        tickerSymbolDataSource.getTickerSymbol(tickerSymbol, new TickerSymbolsDataSource.GetTickerSymbolCallback() {
            @Override
            public void onTickerSymbolRetrieved(String tickerSymbol) {
                if (tickerSymbol == null) {
                    stockQuoteView.showStockNotInPortfolio();
                } else {
                    stockQuoteView.showStockInPortfolio();
                }
            }

            @Override
            public void onDataNotAvailable() {
                stockQuoteView.showStockNotInPortfolio();
            }
        });
    }
}
