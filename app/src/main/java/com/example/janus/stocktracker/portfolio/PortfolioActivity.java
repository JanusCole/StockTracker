package com.example.janus.stocktracker.portfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.janus.stocktracker.BaseActivity;
import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.stockquotes.PortfolioQuoteService;
import com.example.janus.stocktracker.data.database.TickerSymbolsLocalDataSource;
import com.example.janus.stocktracker.data.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.RemoteStockQuoteService;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.splashscreen.SplashScreen;
import com.example.janus.stocktracker.stockquote.StockQuoteActivity;
import com.example.janus.stocktracker.stocksearch.StockSearchActivity;
import com.example.janus.stocktracker.util.BottomNavigationMap;
import com.example.janus.stocktracker.util.FormattedMessages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// This displays current stock quotes for the user's portfolio

public class PortfolioActivity extends BaseActivity implements StocksRecyclerViewAdapter.OnItemSelectedListener, PortfolioContract.View {

    private PortfolioContract.Presenter portfolioPresenter;

    private StocksRecyclerViewAdapter stocksRecyclerViewAdapter;

    private AlertDialog networkActivityDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity);

        setUpStockQuotesRecyclerView();

        networkActivityDialog = FormattedMessages.getNetworkActivityAlert(getLayoutInflater(), this);

        setUpPortfolioPresenter();
    }

    private void setUpPortfolioPresenter() {
        // Get a portfolio ticker symbol data source
        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(this);
        TickerSymbolsRepository portfolioSource = TickerSymbolsRepository.getInstance(TickerSymbolsLocalDataSource.getInstance(portfolioDBOpenHelper));

        // Get a source for stock quotes
        StockQuoteService stockQuoteDataSource = RemoteStockQuoteService.getInstance();
        PortfolioQuoteService portfolioQuoteService = new PortfolioQuoteService(portfolioSource, stockQuoteDataSource);

        // Set up the PortfolioPresenter
        portfolioPresenter = new PortfolioPresenter(this, portfolioQuoteService);
        portfolioPresenter.loadPortfolio();
    }

    private void setUpStockQuotesRecyclerView() {

        stocksRecyclerViewAdapter = new StocksRecyclerViewAdapter(new ArrayList<StockQuote>(), this);

        RecyclerView stockRecyclerView = (RecyclerView) findViewById(R.id.displayStocksRecyclerView);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockRecyclerView.setAdapter(stocksRecyclerViewAdapter);

    }

    @Override
    public void setPresenter(PortfolioContract.Presenter portfolioPresenter) {
        this.portfolioPresenter = portfolioPresenter;
    }

    // Called when the Presenter has successfully retrieved both the portfolio ticker symbols and their quotes
    @Override
    public void showStocks(List<StockQuote> stockQuotes) {
        stocksRecyclerViewAdapter.addStocks(stockQuotes);
    }

    // Display a message during network and database activity
    @Override
    public void showLoadingIndicator() {
        networkActivityDialog.show();
    }

    @Override
    public void dismissLoadingIndicator() {
        networkActivityDialog.dismiss();
    }

    // Display error messages
    @Override
    public void showLoadingError() {
        FormattedMessages.displayErrorMessageAlertDialog(getString(R.string.portfolio_loading_error_message), getLayoutInflater(), this);
    }

    @Override
    public void showEmptyPortfolioMessage() {
        FormattedMessages.displayErrorMessageAlertDialog(getString(R.string.empty_portfolio_message), getLayoutInflater(), this);
    }

    // Process OnClick Of An Item From The RecyclerView
    @Override
    public void onItemSelected(StockQuote stockQuote) {
        portfolioPresenter.selectIndividualStockQuote(stockQuote);
    }

    // Pass control to the Stock Quote display and send the StockQuote through the Intent
    @Override
    public void showIndividualStockQuote(StockQuote stockQuote) {
        Intent intent = new Intent(this, StockQuoteActivity.class);
        intent.putExtra(StockQuoteActivity.STOCK_QUOTE, (Serializable) stockQuote);
        startActivity(intent);
    }

    @VisibleForTesting
    public void setPortfolioPresenter(PortfolioContract.Presenter portfolioPresenter) {
        this.portfolioPresenter = portfolioPresenter;
    }

    @VisibleForTesting
    public PortfolioContract.View getPortfolioView() {
        return this;
    }

}
