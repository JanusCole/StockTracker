package com.example.janus.stocktracker.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.presenter.StockQuoteContract;
import com.example.janus.stocktracker.presenter.StockQuotePresenter;

import java.text.DecimalFormat;


public class StockQuoteFragment extends Fragment implements StockQuoteContract.View {

    private StockQuotePresenter stockQuotePresenter;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

// Populate the view with values from the stock quote result
        View rootView = inflater.inflate(R.layout.display_one_stock_fragment, container, false);

        tickerDisplay = (TextView) rootView.findViewById(R.id.tickerSymbolTextView_OneStockDisplay);
        companyNameDisplay = (TextView) rootView.findViewById(R.id.companyNameTextView_OneStockDisplay);
        sectorDisplay = (TextView) rootView.findViewById(R.id.sectorTextView_OneStockDisplay);

        latestPriceDisplay = (TextView) rootView.findViewById(R.id.latestPriceTextView_OneStockDisplay);
        priceChangeDisplay = (TextView) rootView.findViewById(R.id.priceChangeTextView_OneStockDisplay);
        openPriceDisplay = (TextView) rootView.findViewById(R.id.openPriceTextView_OneStockDisplay);
        closePriceDisplay = (TextView) rootView.findViewById(R.id.closePriceTextView_OneStockDisplay);

        latestVolumeDisplay = (TextView) rootView.findViewById(R.id.latestVolumeTextView_OneStockDisplay);

        priceChangePercentageDisplay = (TextView) rootView.findViewById(R.id.priceChangePercentageTextView_OneStockDisplay);

// Set up the add and remove buttons. Only one will be displayed at a time.
        removeStockButton = (Button) rootView.findViewById(R.id.removeStockButton);
        addStockButton = (Button) rootView.findViewById(R.id.addStockButton);

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        stockQuotePresenter.loadStock();
    }

    @Override
    public void setPresenter(StockQuotePresenter stockQuotePresenter) {
        this.stockQuotePresenter = stockQuotePresenter;
    }

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

}
