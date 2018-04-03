package com.example.janus.stocktracker.stocksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteRemoteDataSource;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteDataSource;
import com.example.janus.stocktracker.data.stockquotes.StockQuotesAPI;
import com.example.janus.stocktracker.portfolio.PortfolioActivity;
import com.example.janus.stocktracker.splashscreen.SplashScreen;

public class StockSearchActivity extends AppCompatActivity {

    private StockSearchContract.Presenter stockSearchPresenter;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_search_activity);

// Create the StockSearch Fragment
        StockSearchFragment stockSearchFragment = new StockSearchFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.stockSearchFrameLaout, stockSearchFragment);
        fragmentTransaction.commit();

// Set up the StockSearchPresenter
        StockQuoteDataSource stockQuoteDataSource = StockQuoteRemoteDataSource.getInstance(new StockQuotesAPI());
        stockSearchPresenter = new StockSearchPresenter(stockSearchFragment, stockQuoteDataSource);

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
                                Intent mainActivityIntent = new Intent(getApplicationContext(), SplashScreen.class);
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
