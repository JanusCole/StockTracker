package com.example.janus.stocktracker.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import com.example.janus.stocktracker.model.database.DatabaseAccessAsync;
import com.example.janus.stocktracker.presenter.PortfolioAccessContract;
import com.example.janus.stocktracker.presenter.PortfolioAccessPresenter;
import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.presenter.StockQuote;


public class DisplayOneStockFragment extends Fragment implements PortfolioAccessContract.View {

    private PortfolioAccessPresenter portfolioAccessPresenter;

// Only one of these buttons will be visible at a time depending on whether the current stock is in the portfolio database
    private Button addStockButton;
    private Button removeStockButton;

    private StockQuote stockQuote;

    private Boolean refreshScreen = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

// Get the search results
        Bundle stockQuoteBundle = getArguments();
        List<StockQuote> stockQuotes = stockQuoteBundle.getParcelableArrayList(getActivity().getString(R.string.stock_quote_list));
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
                removeFromPortfolio(stockQuote.getSymbol());
            }
            });

        addStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                addToPortfolio(stockQuote.getSymbol());
            }
            });

        checkPortfolio(stockQuote.getSymbol());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
// Get access to the portfolio database
        portfolioAccessPresenter = new PortfolioAccessPresenter(new DatabaseAccessAsync(context));
        portfolioAccessPresenter.setPortfolioAccessView(this);
    }



// Decide whether the "add" or "remove" button should be displayed
    private void setButtons (Boolean stockIsInPortfolio) {

        if (stockIsInPortfolio) {
            addStockButton.setVisibility(View.VISIBLE);
            removeStockButton.setVisibility(View.GONE);
        } else {
            addStockButton.setVisibility(View.GONE);
            removeStockButton.setVisibility(View.VISIBLE);
        }

        refreshScreen = false;

    }

// These are for communicating with the portfolio through the  MainActivity
    private void checkPortfolio (String  searchStockTicker) {
        refreshScreen = true;
        portfolioAccessPresenter.checkStock(searchStockTicker);
    }

    private void addToPortfolio (String  searchStockTicker) {
        portfolioAccessPresenter.addStock(searchStockTicker);
    }

    private void removeFromPortfolio (String  searchStockTicker) {
        portfolioAccessPresenter.deleteStock(searchStockTicker);
    }

    @Override
    public void portfolioAccessSuccess(List<String> stockTickers) {

        if (refreshScreen) {
            setButtons(stockTickers.isEmpty());
        }
        else {
            checkPortfolio(stockQuote.getSymbol());
        }
    }

    @Override
    public void portfolioAccessFailure() {
        displayErrorMessageAlertDialog(this.getString(R.string.database_error_message));
    }

    // Method for displaying custom error messages
    public void displayErrorMessageAlertDialog(String alertMessage) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
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

}
