package com.example.janus.stocktracker.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.model.database.AsyncTickerSymbolsDataSource;
import com.example.janus.stocktracker.model.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.model.stockquotes.AsyncStockQuoteDataSource;
import com.example.janus.stocktracker.model.stockquotes.StockQuoteDataSource;
import com.example.janus.stocktracker.model.stockquotes.StockQuotesAPI;
import com.example.janus.stocktracker.model.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.presenter.PortfolioPresenter;

// This app currently supports views for single stocks, multiple stocks (as a portfolio), and a single stock search screen.
// It uses Retrofit to access a JSON stock search API from IEX.

// The user's portfolio stocks are stored in an SQLite database

public class PortfolioActivity extends AppCompatActivity {

    private PortfolioPresenter portfolioPresenter;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity);

// Create the Portfolio Fragment
        PortfolioFragment portfolioFragment = new PortfolioFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.portfolioFrameLaout, portfolioFragment);
        fragmentTransaction.commit();

// Set up the PortfolioPresenter
        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(this);
        TickerSymbolsRepository portfolioSource = new TickerSymbolsRepository(new AsyncTickerSymbolsDataSource(portfolioDBOpenHelper));
        StockQuoteDataSource stockQuoteDataSource = new AsyncStockQuoteDataSource(new StockQuotesAPI());
        portfolioPresenter = new PortfolioPresenter(portfolioFragment, portfolioSource, stockQuoteDataSource);

// Set up the bottom navigation bar
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_search_ticker:
                                Intent stockSearchIntent = new Intent(getApplicationContext(), StockSearchActivity.class);
                                startActivity(stockSearchIntent);
                                break;
                            case R.id.action_splash_screen:
                                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainActivityIntent);
                                break;
                            case R.id.action_portfolio:
                                Intent portfolioIntent = new Intent(getApplicationContext(), PortfolioActivity.class);
                                startActivity(portfolioIntent);
                                break;
                        }
                        return false;
                    }
                });

    }

}
