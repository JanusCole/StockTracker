package com.example.janus.stocktracker.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.example.janus.stocktracker.model.stockquotes.GetStockQuotes;
import com.example.janus.stocktracker.model.stockquotes.GetStockQuotesFromIEX;
import com.example.janus.stocktracker.model.codes.ResultCode;
import com.example.janus.stocktracker.model.stockquotes.StockQuote;

public class StockSearchPresenter implements StockSearchContract.Presenter, GetStockQuotes.ProcessStockQuoteResults, PortfolioAccessContract.View {

    private StockSearchContract.View stockSearchView;
    private PortfolioAccessContract.Presenter portfolioAccessPresenter;

    public StockSearchPresenter(StockSearchContract.View stockSearchView, Context context) {
        this.stockSearchView = stockSearchView;
        this.portfolioAccessPresenter = new PortfolioAccessPresenter(context, this);
    }

    @Override
    public void searchPortfolio() {
        portfolioAccessPresenter.getPortfolio();
    }

    public void searchStocks(String stockSymbol) {
        List <String> stockSearchList = new ArrayList<>();
        stockSearchList.add(stockSymbol);
        GetStockQuotes webStockQuote = new GetStockQuotesFromIEX(this);
        webStockQuote.getStockQuotes(stockSearchList);
    }

    public void searchStocks(List<String> stockSymbols) {
        GetStockQuotes webStockQuote = new GetStockQuotesFromIEX(this);
        webStockQuote.getStockQuotes(stockSymbols);
    }


    @Override
    public void processStockQuoteResults(List<StockQuote> stockQuoteResults, ResultCode resultCode) {

        if (resultCode == ResultCode.NETWORK_ERROR) {
            stockSearchView.displayNetworkErrorMessage();
        }
        else if (resultCode == ResultCode.NOT_FOUND) {
            stockSearchView.displayNotFoundErrorMessage();
        }
        else if (stockQuoteResults.size() == 1) {
            stockSearchView.displayOneStock(stockQuoteResults.get(0));
        } else {
            stockSearchView.displayManyStocks(stockQuoteResults);
        }
    }

    @Override
    public void portfolioAccessCompleted(List<String> stockTickers, ResultCode resultCode) {

        if (resultCode == ResultCode.DATABASE_ERROR) {
            stockSearchView.displayDatabaseErrorMessage();
        }
        else if (stockTickers.size() == 0) {
            stockSearchView.displayEmptyPortfolioErrorMessage();

        } else {
            searchStocks(stockTickers);
        }

    }
}
