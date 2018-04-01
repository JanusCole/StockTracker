package com.example.janus.stocktracker.presenter;

import android.content.res.ColorStateList;
import android.view.View;

import java.text.DecimalFormat;

public interface StockQuoteContract {

    public interface View {
        void setPresenter(StockQuotePresenter stockQuotePresenter);

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

    }

    public interface Presenter {
        void loadStock();
        void addStock(String tickerSymbol);
        void deleteStock(String tickerSymbol);
    }

}
