package com.example.janus.stocktracker.stockquote;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.janus.stocktracker.R;

import java.text.DecimalFormat;


public class StockQuoteFragment extends Fragment implements StockQuoteContract.View {

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

// Populate the view with values from the stock quote result
        View rootView = inflater.inflate(R.layout.display_one_stock_fragment, container, false);

        findTextFields(rootView);

        findNumericFields(rootView);

        findPortfolioButtons(rootView);

        return rootView;
    }

    private void findPortfolioButtons(View rootView) {
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
    }

    private void findNumericFields(View rootView) {
        latestPriceDisplay = (TextView) rootView.findViewById(R.id.latestPriceTextView_OneStockDisplay);
        priceChangeDisplay = (TextView) rootView.findViewById(R.id.priceChangeTextView_OneStockDisplay);
        openPriceDisplay = (TextView) rootView.findViewById(R.id.openPriceTextView_OneStockDisplay);
        closePriceDisplay = (TextView) rootView.findViewById(R.id.closePriceTextView_OneStockDisplay);

        latestVolumeDisplay = (TextView) rootView.findViewById(R.id.latestVolumeTextView_OneStockDisplay);

        priceChangePercentageDisplay = (TextView) rootView.findViewById(R.id.priceChangePercentageTextView_OneStockDisplay);
    }

    private void findTextFields(View rootView) {
        tickerDisplay = (TextView) rootView.findViewById(R.id.tickerSymbolTextView_OneStockDisplay);
        companyNameDisplay = (TextView) rootView.findViewById(R.id.companyNameTextView_OneStockDisplay);
        sectorDisplay = (TextView) rootView.findViewById(R.id.sectorTextView_OneStockDisplay);
    }

    @Override
    public void onResume() {
        super.onResume();
        stockQuotePresenter.loadStock();
    }

    @Override
    public void setPresenter(StockQuoteContract.Presenter stockQuotePresenter) {
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
    public void displayErrorMessageAlertDialog(String alertMessage, Activity activity, Context context) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
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
    public AlertDialog showNetworkActivityAlert(LayoutInflater inflater,Context context) {

        View dialogView = inflater.inflate(R.layout.busy_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(dialogView);

        return alertDialogBuilder.create();

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

    @Override
    public void showDatabaseError() {
        displayErrorMessageAlertDialog(getString(R.string.database_error_message), getActivity(), getContext());
    }
}
