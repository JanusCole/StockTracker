package com.example.janus.stocktracker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DisplayOneStockFragment extends Fragment {

// These are for communicating with the  MainActivity
    private AccessPortfolio accessPortfolio;

// Only one of these buttons will be visible at a time depending on whether the current stock is in the portfolio database
    private Button addStockButton;
    private Button removeStockButton;

    private StockQuote stockQuote;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

// Get the search results
        Bundle stockQuoteBundle = getArguments();
        List<StockQuote> stockQuotes = stockQuoteBundle.getParcelableArrayList(getActivity().getString(R.string.stock_search_list));

        stockQuote = stockQuotes.get(0);


// Populate the view with values from the stock quote result
        View v = inflater.inflate(R.layout.display_one_stock_fragment, container, false);

        TextView tickerDisplay = (TextView) v.findViewById(R.id.tickerSymbolTextView_OneStockDisplay);
        tickerDisplay.setText(stockQuote.getSymbol());

        TextView companyNameDisplay = (TextView) v.findViewById(R.id.companyNameTextView_OneStockDisplay);
        companyNameDisplay.setText(stockQuote.getCompanyName());

        TextView sectorDisplay = (TextView) v.findViewById(R.id.sectorTextView_OneStockDisplay);
        sectorDisplay.setText(stockQuote.getSector());

        TextView latestPriceDisplay = (TextView) v.findViewById(R.id.latestPriceTextView_OneStockDisplay);
        latestPriceDisplay.setText(String.format("%.2f", stockQuote.getLatestPrice()));

        TextView priceChangeDisplay = (TextView) v.findViewById(R.id.priceChangeTextView_OneStockDisplay);
        priceChangeDisplay.setText(String.format("%.2f", (stockQuote.getLatestPrice() - stockQuote.getOpen())));
        priceChangeDisplay.setTypeface(null, Typeface.BOLD);

        TextView openPriceDisplay = (TextView) v.findViewById(R.id.openPriceTextView_OneStockDisplay);
        openPriceDisplay.setText(String.format("%.2f", stockQuote.getOpen()));

        TextView closePriceDisplay = (TextView) v.findViewById(R.id.closePriceTextView_OneStockDisplay);
        closePriceDisplay.setText(String.format("%.2f", stockQuote.getClose()));

        TextView latestVolumeDisplay = (TextView) v.findViewById(R.id.latestVolumeTextView_OneStockDisplay);
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        latestVolumeDisplay.setText(formatter.format(stockQuote.getLatestVolume()));

        TextView priceChangePercentageDisplay = (TextView) v.findViewById(R.id.priceChangePercentageTextView_OneStockDisplay);
        priceChangePercentageDisplay.setText("(" + String.format("%.3f", (100 * (stockQuote.getLatestPrice() - stockQuote.getOpen())/stockQuote.getOpen())) + " %)");
        priceChangePercentageDisplay.setTypeface(null, Typeface.BOLD);

        if (stockQuote.getLatestPrice() < stockQuote.getOpen()) {
            priceChangeDisplay.setTextColor(Color.RED);
            priceChangePercentageDisplay.setTextColor(Color.RED);
        } else {
            priceChangeDisplay.setTextColor(ContextCompat.getColor(getActivity(), R.color.accountingCreditGreen));
            priceChangePercentageDisplay.setTextColor(ContextCompat.getColor(getActivity(), R.color.accountingCreditGreen));
        }

// Set up the add and remove buttons. Only one will be displayed at a time.
        removeStockButton = (Button) v.findViewById(R.id.removeStockButton);
        addStockButton = (Button) v.findViewById(R.id.addStockButton);

        removeStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                removeFromPortfolio();
                setButtons();
            }
            });

        addStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                addToPortfolio();
                setButtons();
            }
            });

        setButtons();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

// This is for communicating with the MainActivity
        accessPortfolio = (AccessPortfolio) context;

    }

// Decide whether the "add" or "remove" button should be displayed
    private void setButtons () {

        if (checkPortfolio(stockQuote.getSymbol())) {
            addStockButton.setVisibility(View.GONE);
            removeStockButton.setVisibility(View.VISIBLE);
        } else {
            addStockButton.setVisibility(View.VISIBLE);
            removeStockButton.setVisibility(View.GONE);
        }

    }

// These are for communicating with the MainActivity
    private boolean checkPortfolio (String  searchStockTicker) {
        return accessPortfolio.checkPortfolio(searchStockTicker);
    }

    private void addToPortfolio () {
        accessPortfolio.addToPortfolio(stockQuote.getSymbol());
        setButtons();
    }

    private void removeFromPortfolio () {
        accessPortfolio.removeFromPortfolio(stockQuote.getSymbol());
        setButtons();
    }
}
