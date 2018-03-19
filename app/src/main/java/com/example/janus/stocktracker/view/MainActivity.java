package com.example.janus.stocktracker.view;

import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.model.stockquotes.StockQuote;
import com.example.janus.stocktracker.presenter.StockSearchContract;
import com.example.janus.stocktracker.presenter.StockSearchPresenter;

// This app currently supports views for single stocks, multiple stocks (as a portfolio), and a single stock search screen.
// It uses Retrofit to access a JSON stock search API from IEX.

// The user's portfolio stocks are stored in an SQLite database

public class MainActivity extends AppCompatActivity implements ShowStock, StockSearchContract.View {

    private StockSearchPresenter stockSearchPresenter;

    private AlertDialog networkActivityDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockSearchPresenter = new StockSearchPresenter(this, this);

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
                                displayPortfolio();
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


    public void displayPortfolio () {

        stockSearchPresenter.searchPortfolio();
        networkActivityDialog.show();

    }

// ****************************************************
// Method for communicating with the fragments
// ****************************************************

// This method kicks off the Stock Search Presenter that gets the stock quotes from the internet
    @Override
    public void showStock(String stockTicker) {

       stockSearchPresenter.searchStocks(stockTicker);
       networkActivityDialog.show();

    }

// ************************************************************
// Methods for getting results from the Stock Search Presenter
// ************************************************************

    @Override
    public void displayOneStock(StockQuote quote) {

        networkActivityDialog.dismiss();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.stock_quote), quote);

        Fragment nextFragment = new DisplayOneStockFragment();
        nextFragment.setArguments(args);

        replaceFragment(nextFragment);

    }

    @Override
    public void displayManyStocks(List<StockQuote> quotes) {

        networkActivityDialog.dismiss();

        Bundle args = new Bundle();
// The putParcelable method below only accepts ArrayLists, not Lists
        args.putParcelableArrayList(getString(R.string.stock_quote_list), (ArrayList) quotes);

        Fragment nextFragment = new DisplayManyStocksFragment();
        nextFragment.setArguments(args);

        replaceFragment(nextFragment);

    }
    // Method for displaying custom error messages
    public void displayErrorMessageAlertDialog(String alertMessage) {

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

        networkActivityDialog.dismiss();

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

    @Override
    public void displayDatabaseErrorMessage() {
        displayErrorMessageAlertDialog(getString(R.string.database_error_message));
    }

    @Override
    public void displayEmptyPortfolioErrorMessage() {
        displayErrorMessageAlertDialog(getString(R.string.empty_portfolio_message));
    }

    @Override
    public void displayNotFoundErrorMessage() {
        displayErrorMessageAlertDialog(getString(R.string.stock_not_found_message));
    }

    @Override
    public void displayNetworkErrorMessage() {
        displayErrorMessageAlertDialog(getString(R.string.network_error_message));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
