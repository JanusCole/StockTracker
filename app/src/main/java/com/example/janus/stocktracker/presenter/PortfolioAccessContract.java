package com.example.janus.stocktracker.presenter;

import java.util.List;
import com.example.janus.stocktracker.model.codes.ResultCode;

public interface PortfolioAccessContract {

    public interface View {
        void portfolioAccessCompleted(List<String> stockTickers, ResultCode resultCode);
    }

    public interface Presenter {
        void addStock(String tickerSymbol);
        void deleteStock(String tickerSymbol);
        void checkStock(String tickerSymbol);
        void getPortfolio ();
    }

}
