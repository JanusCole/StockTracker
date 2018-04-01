package com.example.janus.stocktracker.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.model.stockquotes.StockQuote;
import com.example.janus.stocktracker.presenter.StockSearchContract;
import com.example.janus.stocktracker.presenter.StockSearchPresenter;

import java.io.Serializable;


public class StockSearchFragment extends Fragment implements StockSearchContract.View {

    private StockSearchPresenter stockSearchPresenter;

    private AlertDialog networkActivityDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_stocks_fragment, container, false);

        Button searchButton = (Button) rootView.findViewById (R.id.stockSearchButton);

        final EditText searchStockTicker = (EditText) rootView.findViewById (R.id.searchTickerEditText_StockSearch);
        searchStockTicker.requestFocus();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchStockTicker.getWindowToken(), 0);
                searchStock(searchStockTicker.getText().toString());
            }
        });

        networkActivityDialog = showNetworkActivityAlert(inflater);

        return rootView;
    }

    @Override
    public void setPresenter(StockSearchPresenter stockSearchPresenter) {
        this.stockSearchPresenter = stockSearchPresenter;
    }

    private void searchStock(String tickerSymbol) {
        stockSearchPresenter.searchStock(tickerSymbol);
    }

    @Override
    public void showStockQuoteUI(StockQuote stockQuote) {
        Intent stockQuoteIntent = new Intent(getContext(), StockQuoteActivity.class);
        stockQuoteIntent.putExtra(StockQuoteActivity.STOCK_QUOTE, (Serializable) stockQuote);
        startActivity(stockQuoteIntent);
    }

    @Override
    public void showLoadingIndicator() {
        networkActivityDialog.show();
    }

    @Override
    public void showNotFoundError() {
        networkActivityDialog.dismiss();
        displayErrorMessageAlertDialog(getString(R.string.stock_not_found_message));
    }

    @Override
    public void showLoadingError() {
        networkActivityDialog.dismiss();
        displayErrorMessageAlertDialog(getString(R.string.network_error_message));
    }

// Maybe put these in to a separate utility class

    // Method for setting up the network busy message
    private AlertDialog showNetworkActivityAlert(LayoutInflater inflater) {

        View dialogView = inflater.inflate(R.layout.busy_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(dialogView);

        return alertDialogBuilder.create();

    }

    // Method for displaying custom error messages
    private void displayErrorMessageAlertDialog(String alertMessage) {

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

        networkActivityDialog.dismiss();

        errorMessageAlertDialog.show();
    }
}
