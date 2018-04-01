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
import com.example.janus.stocktracker.model.stockquotes.StockQuote;
import com.example.janus.stocktracker.presenter.StockQuotePresenter;
import com.example.janus.stocktracker.model.database.TickerSymbolsRepository;

public class StockQuoteActivity extends AppCompatActivity {

    public static final String STOCK_QUOTE = "stock_quote";

    private StockQuotePresenter stockQuotePresenter;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_quote_activity);

        // Get the requested stock quote
        StockQuote stockQuote = (StockQuote) getIntent().getSerializableExtra(STOCK_QUOTE);

// Create the StockSearch Fragment
        StockQuoteFragment stockQuoteFragment = new StockQuoteFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.stockQuoteFrameLaout, stockQuoteFragment);
        fragmentTransaction.commit();

// Set up the StockSearchPresenter
        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(this);
        TickerSymbolsRepository portfolioSource = new TickerSymbolsRepository(new AsyncTickerSymbolsDataSource(portfolioDBOpenHelper));
        stockQuotePresenter = new StockQuotePresenter(stockQuoteFragment, stockQuote, portfolioSource);

// Set up the bottom navigation bar
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

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