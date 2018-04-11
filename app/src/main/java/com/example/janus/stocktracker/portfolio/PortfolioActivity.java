package com.example.janus.stocktracker.portfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.database.TickerSymbolsLocalDataSource;
import com.example.janus.stocktracker.data.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteRemoteService;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;
import com.example.janus.stocktracker.data.stockquotes.StockQuotesWebAPI;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.splashscreen.SplashScreen;
import com.example.janus.stocktracker.stocksearch.StockSearchActivity;

public class PortfolioActivity extends AppCompatActivity {

    private PortfolioContract.Presenter portfolioPresenter;

    PortfolioFragment portfolioFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity);

// Create the Portfolio Fragment
        portfolioFragment = new PortfolioFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.portfolioFrameLaout, portfolioFragment);
        fragmentTransaction.commit();

// Set up the PortfolioPresenter
        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(this);
        TickerSymbolsRepository portfolioSource = TickerSymbolsRepository.getInstance(TickerSymbolsLocalDataSource.getInstance(portfolioDBOpenHelper));
        StockQuoteService stockQuoteDataSource = StockQuoteRemoteService.getInstance(new StockQuotesWebAPI());
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

    @VisibleForTesting
    public void setPortfolioPresenter(PortfolioContract.Presenter portfolioPresenter) {
        this.portfolioPresenter = portfolioPresenter;
    }

    @VisibleForTesting
    public PortfolioFragment getPortfolioFragment() {
        return portfolioFragment;
    }
}
