package com.example.janus.stocktracker.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.RemoteStockQuoteService;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;
import com.example.janus.stocktracker.portfolio.PortfolioActivity;
import com.example.janus.stocktracker.splashscreen.SplashScreen;
import com.example.janus.stocktracker.stockquote.StockQuoteActivity;
import com.example.janus.stocktracker.util.BottomNavigationMap;
import com.example.janus.stocktracker.util.FormattedMessages;

import java.io.Serializable;

// This is the activity that allows the user to search for a specific stock ticker symbol. It shows just an EditText field
// for the ticker symbol to be searched and a search button.

public class StockSearchActivity extends AppCompatActivity implements StockSearchContract.View {

    private StockSearchContract.Presenter stockSearchPresenter;

    private AlertDialog networkActivityDialog;

    private EditText searchStockTicker;
    private Button searchButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_search_activity);

    // Set up the fields involed in the stock search function
        setupSearchUI();


    // Set up the StockSearchPresenter
        StockQuoteService stockQuoteDataSource = RemoteStockQuoteService.getInstance();
        stockSearchPresenter = new StockSearchPresenter(this, stockQuoteDataSource);

    // Set up the bottom navigation bar
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent stockSearchIntent = new Intent(getApplicationContext(), BottomNavigationMap.NAVIGATION_MAP.get(item.getItemId()));
                        startActivity(stockSearchIntent);
                        return false;
                    }
                });

    }

    private void setupSearchUI() {

        // Assign the search button and stock ticker input textbox
        searchStockTicker = (EditText) findViewById (R.id.searchTickerEditText_StockSearch);
        searchStockTicker.requestFocus();

        searchButton = (Button) findViewById (R.id.stockSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the soft keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchStockTicker.getWindowToken(), 0);
                // Calls the method that passes the search request to the Presenter
                searchStock(searchStockTicker.getText().toString());
            }
        });

    // Set up the network dialog message box
        networkActivityDialog = FormattedMessages.getNetworkActivityAlert(getLayoutInflater(), this);
    }

    // Handles the onclick even when the user wants to search for a stock ticker
    private void searchStock(String tickerSymbol) {
        stockSearchPresenter.searchStock(tickerSymbol);
    }

    @Override
    public void setPresenter(StockSearchContract.Presenter stockSearchPresenter) {
        this.stockSearchPresenter = stockSearchPresenter;
    }

    // Gets called when a successful ticker search has been completed. Transfers control to the
    // stock quote display activity and passes it the stock quote as an Extra
    @Override
    public void showStockQuoteUI(StockQuote stockQuote) {
        Intent stockQuoteIntent = new Intent(this, StockQuoteActivity.class);
        stockQuoteIntent.putExtra(StockQuoteActivity.STOCK_QUOTE, (Serializable) stockQuote);
        startActivity(stockQuoteIntent);
    }


    // Display a message while the network call is running
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
    public void showNotFoundError() {
        FormattedMessages.displayErrorMessageAlertDialog(getString(R.string.stock_not_found_message), getLayoutInflater(), this);
    }

    @Override
    public void showLoadingError() {
        FormattedMessages.displayErrorMessageAlertDialog(getString(R.string.network_error_message), getLayoutInflater(), this);
    }

}
