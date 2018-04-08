package com.example.janus.stocktracker.stockquote;

import com.example.janus.stocktracker.stockquote.StockQuotePresenter;

public interface StockQuoteContract {

    interface View {
        void setPresenter(StockQuoteContract.Presenter stockQuotePresenter);

        void setTickerSymbol(String tickerSymbol);
        void setCompanyName (String companyName);
        void setSector(String sector);
        void setLatestPrice(double latestPrice);
        void setPriceChange (double priceChange);
        void setOpenPrice (double openPrice);
        void setClosePrice (double closePrice);
        void setLatestVolume (double latstVolume);
        void setPriceChangePercent (double priceChangePercent);
        void setPriceChangeColor (int color);

        void showStockNotInPortfolio();
        void showStockInPortfolio();

        void showDatabaseError();

    }

    interface Presenter {
        void loadStock();
        void addStock(String tickerSymbol);
        void deleteStock(String tickerSymbol);
        void checkStock(String tickerSymbol);
    }

}
