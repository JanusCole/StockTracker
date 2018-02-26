package com.example.janus.stocktracker;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.support.test.espresso.IdlingResource;
import com.example.janus.stocktracker.data.Portfolio;
import java.util.ArrayList;
import java.util.List;

// So the obvious question is "Why run Retrofit in in a handlerThread instead of running it stand alone as an async?"
// The answer is that the IEX stock quote API I'm using doesn't perform batch requests. So in order to search a whole portfolio,
// it runs in a loop and accumulates the results before returning them.

public class MainActivity extends AppCompatActivity implements ShowStocks, AccessPortfolio {

    private Portfolio userPortfolio;

    private HandlerThread getStockQuotesHandlerThread;
    private Handler getStockQuotesThreadHandler;

    private String baseURL;

    private AlertDialog networkActivityDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Get access to the portfolio database
        userPortfolio = new Portfolio(this);

// Get the URl for the stock quotes API
        baseURL = getString(R.string.stock_search_url);

// Set up the thread that does the internet access
        getStockQuotesHandlerThread = new HandlerThread(getString(R.string.stock_quotes_thread_name));
        getStockQuotesHandlerThread.start();

        getStockQuotesThreadHandler = new Handler(getStockQuotesHandlerThread.getLooper());

// Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_search_ticker:
                                replaceFragment(new SearchStockFragment());
                                break;
                            case R.id.action_splash_screen:
                                replaceFragment(new SplashScreenFragment());
                                break;
                            case R.id.action_portfolio:
                                showStocks(userPortfolio.getPortfolio(), new DisplayManyStocksFragment());
                                break;
                        }
                        return false;
                    }
                });

// Set up the dialog box for network activity
        setupNetworkActivityAlert();

        replaceFragment (new SplashScreenFragment());

    }

    private void replaceFragment (Fragment destination) {

        FragmentTransaction splashFragmentTransaction = getSupportFragmentManager().beginTransaction();
        splashFragmentTransaction.replace(R.id.mainDisplayFrameLaout, destination);
        splashFragmentTransaction.commit();
    }

// Method for displaying custom error messages
    private void displayErrorMessageAlertDialog(String alertMessage) {

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(dialogView);

        TextView alertDialogMessage = (TextView) dialogView.findViewById(R.id.messageTextView_AlertDialog);
        alertDialogMessage.setText(alertMessage);

        final AlertDialog errorMessageAlertDialog = alertDialogBuilder.create();
        errorMessageAlertDialog.setCanceledOnTouchOutside(true);

        Button dialogButton = (Button) dialogView.findViewById(R.id.okButton_AlertDialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessageAlertDialog.dismiss();
            }
        });

        errorMessageAlertDialog.show();

    }

// Method for setting up the network busy message
    private void setupNetworkActivityAlert() {

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.busy_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(dialogView);

        networkActivityDialog = alertDialogBuilder.create();

    }

// This processes the results of the internet stock quote API and opens the next fragment
    Handler searchStocksThreadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

                StockQuoteResult stockQuoteResult = (StockQuoteResult) msg.obj;

// The putParcelable method below only accepts ArrayLists, not Lists
                ArrayList<StockQuote> stockQuotesArrayList = (ArrayList) stockQuoteResult.getStockQuotes();

                networkActivityDialog.dismiss();

                if (stockQuotesArrayList.size() == 0) {
                    displayErrorMessageAlertDialog(getString(R.string.stock_not_found_message));
                } else {

                    Bundle args = new Bundle();
// The putParcelable method below only accepts ArrayLists, not Lists
                    args.putParcelableArrayList(getString(R.string.stock_search_list), stockQuotesArrayList);

                    Fragment nextFragment = stockQuoteResult.getDestinationFragment();
                    nextFragment.setArguments(args);

                    replaceFragment(nextFragment);
                }

                EspressoIdlingResource.decrement();

        }
    };

// ****************************************************
// Interfaces for communicating with the fragments
// ****************************************************

// This method kicks off the handlerthread that gets the stock quotes from the internet
    @Override
    public void showStocks(List<String> stockTickerArray, Fragment destinationFragment) {

        if (stockTickerArray.size() == 0) {
            displayErrorMessageAlertDialog(getString(R.string.empty_portfolio_message));

        } else {
            EspressoIdlingResource.increment();
            getStockQuotesThreadHandler.post(new GetStockQuotesBuilder()
                    .setCallingThreadHandler(searchStocksThreadHandler)
                    .setStockQuoteRequest(new StockQuoteRequest(stockTickerArray, destinationFragment))
                    .setWebStockQuote(new RetrofitStockQuote(baseURL))
                    .build());
            networkActivityDialog.show();
        }

    }

// Methods to allow fragments to interact with the portfolio database
    @Override
    public boolean checkPortfolio(String stockTicker) {
        return userPortfolio.stockInPortfolio(stockTicker);
    }

    @Override
    public void addToPortfolio(String stockTicker) {
        userPortfolio.addStockToPortfolio(stockTicker);
    }

    @Override
    public void removeFromPortfolio(String stockTicker) {
        userPortfolio.removeStockFromPortfolio(stockTicker);
    }

    @VisibleForTesting
    public void setBaseURL (String baseURL) {
        this.baseURL = baseURL;
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getStockQuotesHandlerThread.quitSafely();
    }
}
