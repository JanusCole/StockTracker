package com.example.janus.stocktracker.stockquote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.janus.stocktracker.BaseActivity;
import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.database.TickerSymbolsLocalDataSource;
import com.example.janus.stocktracker.data.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.portfolio.PortfolioActivity;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.splashscreen.SplashScreen;
import com.example.janus.stocktracker.stocksearch.StockSearchActivity;
import com.example.janus.stocktracker.util.BottomNavigationMap;
import com.example.janus.stocktracker.util.FormattedMessages;

import java.text.DecimalFormat;

// This class displays a single stock quote, received through the intent. It also shows if the stock
// is currently in the portfolio and displays a button to add or remove it.

public class StockQuoteActivity extends BaseActivity implements StockQuoteContract.View {

    public static final String STOCK_QUOTE = "stock_quote";

    private StockQuoteContract.Presenter stockQuotePresenter;

    private TextView tickerDisplay;
    private TextView companyNameDisplay;
    private TextView sectorDisplay;
    private TextView latestPriceDisplay;
    private TextView priceChangeDisplay;
    private TextView openPriceDisplay;
    private TextView closePriceDisplay;
    private TextView latestVolumeDisplay;
    private TextView priceChangePercentageDisplay;

    // Only one of these buttons will be visible at a time depending on whether the current stock is in the portfolio database
    private Button addStockButton;
    private Button removeStockButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_quote_activity);

        // Set up the UI
        findTextFields();

        findNumericFields();

        findPortfolioButtons();

        // Get the requested stock quote passed through the Intent
        StockQuote stockQuote = (StockQuote) getIntent().getSerializableExtra(STOCK_QUOTE);

        // Set up the stock quote display Presenter and pass it the stock quote obtained from the Intent
        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(this);
        TickerSymbolsRepository portfolioSource = TickerSymbolsRepository.getInstance(TickerSymbolsLocalDataSource.getInstance(portfolioDBOpenHelper));
        stockQuotePresenter = new StockQuotePresenter(this, stockQuote, portfolioSource);
        stockQuotePresenter.loadStock();
    }

    private void findPortfolioButtons() {
        // Set up the add and remove buttons. Only one will be displayed at a time.
        removeStockButton = (Button) findViewById(R.id.removeStockButton);
        addStockButton = (Button) findViewById(R.id.addStockButton);

        removeStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockQuotePresenter.deleteStock(tickerDisplay.getText().toString());
            }
        });

        addStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockQuotePresenter.addStock(tickerDisplay.getText().toString());
            }
        });
    }

    private void findNumericFields() {
        // Displays pricing and price change as well as Volume
        latestPriceDisplay = (TextView) findViewById(R.id.latestPriceTextView_OneStockDisplay);
        priceChangeDisplay = (TextView) findViewById(R.id.priceChangeTextView_OneStockDisplay);
        openPriceDisplay = (TextView) findViewById(R.id.openPriceTextView_OneStockDisplay);
        closePriceDisplay = (TextView) findViewById(R.id.closePriceTextView_OneStockDisplay);

        latestVolumeDisplay = (TextView) findViewById(R.id.latestVolumeTextView_OneStockDisplay);

        priceChangePercentageDisplay = (TextView) findViewById(R.id.priceChangePercentageTextView_OneStockDisplay);
    }

    private void findTextFields() {
        // Identifies the stock by name and sector
        tickerDisplay = (TextView) findViewById(R.id.tickerSymbolTextView_OneStockDisplay);
        companyNameDisplay = (TextView) findViewById(R.id.companyNameTextView_OneStockDisplay);
        sectorDisplay = (TextView) findViewById(R.id.sectorTextView_OneStockDisplay);
    }

    @Override
    public void setPresenter(StockQuoteContract.Presenter stockQuotePresenter) {
        this.stockQuotePresenter = stockQuotePresenter;
    }

    // Populate each display field as determined by the Presenter
    @Override
    public void setTickerSymbol(String tickerSymbol) {
        tickerDisplay.setText(tickerSymbol);
    }

    @Override
    public void setCompanyName (String companyName) {
        companyNameDisplay.setText(companyName);
    }

    @Override
    public void setSector(String sector) {
        sectorDisplay.setText(sector);
    }

    @Override
    public void setLatestPrice(double latestPrice) {
        latestPriceDisplay.setText(String.format("%.2f", latestPrice));
    }

    @Override
    public void setPriceChange (double priceChange) {
        priceChangeDisplay.setText(String.format("%.2f", priceChange));
    }

    @Override
    public void setOpenPrice (double openPrice) {
        openPriceDisplay.setText(String.format("%.2f", openPrice));
    }

    @Override
    public void setClosePrice (double closePrice) {
        closePriceDisplay.setText(String.format("%.2f", closePrice));
    }

    @Override
    public void setLatestVolume (double latstVolume) {
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        latestVolumeDisplay.setText(formatter.format(latstVolume));
    }

    @Override
    public void setPriceChangePercent (double priceChangePercent) {
        priceChangePercentageDisplay.setText("(" + String.format("%.3f", priceChangePercent) + " %)");
    }

    @Override
    public void setPriceChangeColor (int color) {
        priceChangeDisplay.setTextColor(color);
        priceChangePercentageDisplay.setTextColor(color);
    }

    @Override
    public void showStockNotInPortfolio() {
        addStockButton.setVisibility(View.VISIBLE);
        removeStockButton.setVisibility(View.GONE);
    }

    @Override
    public void showStockInPortfolio() {
        addStockButton.setVisibility(View.GONE);
        removeStockButton.setVisibility(View.VISIBLE);
    }

    // Display error messages if the portfolio database is unavailable
    @Override
    public void showDatabaseError() {
        FormattedMessages.displayErrorMessageAlertDialog(getString(R.string.database_error_message), getLayoutInflater(), this);
    }

    @VisibleForTesting
    public StockQuoteContract.View getStockQuoteView() {
        return this;
    }

    @VisibleForTesting
    public void setStockQuotePresenter(StockQuoteContract.Presenter stockQuotePresenter) {
        this.stockQuotePresenter = stockQuotePresenter;
    }

}
