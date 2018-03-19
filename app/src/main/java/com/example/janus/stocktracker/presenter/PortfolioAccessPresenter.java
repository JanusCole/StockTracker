package com.example.janus.stocktracker.presenter;

import android.content.Context;

import com.example.janus.stocktracker.model.database.DatabaseAccessAsync;
import com.example.janus.stocktracker.model.codes.ResultCode;

import java.util.List;

public class PortfolioAccessPresenter implements PortfolioAccessContract.Presenter, DatabaseAccessAsync.OnPortfolioDBAccessCompletion {


    private PortfolioAccessContract.View portfolioAccessView;
    private DatabaseAccessAsync userPortfolio;

    public PortfolioAccessPresenter(Context context, PortfolioAccessContract.View portfolioAccessView) {
        this.userPortfolio = new DatabaseAccessAsync(context, this);
        this.portfolioAccessView = portfolioAccessView;
    }

    @Override
    public void checkStock (String tickerSymbol) {
        userPortfolio.getOneStockPortfolioDBAsync(tickerSymbol);
    }

    @Override
    public void addStock (String tickerSymbol) {
        userPortfolio.addOneStockPortfolioDBAsync(tickerSymbol);
    }

    @Override
    public void deleteStock (String tickerSymbol) {
        userPortfolio.deleteOneStockPortfolioDBAsync(tickerSymbol);
    }

    public void getPortfolio () {
        userPortfolio.getAllStocksPortfolioDBAsync();
    }

    @Override
    public void portfolioDBAccessCompleted(ResultCode resultCode, List<String> stockSymbols) {
        portfolioAccessView.portfolioAccessCompleted(stockSymbols, resultCode);
    }

}
