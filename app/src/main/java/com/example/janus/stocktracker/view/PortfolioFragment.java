package com.example.janus.stocktracker.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.adapters.StocksRecyclerViewAdapter;
import com.example.janus.stocktracker.presenter.PortfolioContract;
import com.example.janus.stocktracker.presenter.PortfolioPresenter;
import com.example.janus.stocktracker.model.stockquotes.StockQuote;

import java.io.Serializable;
import java.util.List;


public class PortfolioFragment extends Fragment implements StocksRecyclerViewAdapter.OnItemSelectedListener, PortfolioContract.View {

    private PortfolioPresenter portfolioPresenter;

    private RecyclerView stockRecyclerView;

    private AlertDialog networkActivityDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.display_multiple_stocks_fragment, container, false);

        stockRecyclerView = (RecyclerView) rootView.findViewById(R.id.displayStocksRecyclerView);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        networkActivityDialog = showNetworkActivityAlert(inflater);

        return rootView;
    }


// This will display new data when the fragment is first displayed and refresh the data after pausing
    @Override
    public void onResume() {
        super.onResume();
        portfolioPresenter.loadStocks();
    }

    @Override
    public void setPresenter(PortfolioPresenter portfolioPresenter) {
        this.portfolioPresenter = portfolioPresenter;
    }

    @Override
    public void showStocks(List<StockQuote> stockQuotes) {
        networkActivityDialog.dismiss();
        stockRecyclerView.setAdapter(new StocksRecyclerViewAdapter(stockQuotes, this));
    }

    @Override
    public void showLoadingIndicator() {
        networkActivityDialog.show();
    }

    @Override
    public void showLoadingError() {
        networkActivityDialog.dismiss();
        displayErrorMessageAlertDialog(getString(R.string.portfolio_loading_error_message));
    }

    @Override
    public void showEmptyPortfolioMessage() {
        networkActivityDialog.dismiss();
        displayErrorMessageAlertDialog(getString(R.string.empty_portfolio_message));
    }

    @Override
    public void showStockQuote(StockQuote stockQuote) {
        Intent intent = new Intent(getContext(), StockQuoteActivity.class);
        intent.putExtra(StockQuoteActivity.STOCK_QUOTE, (Serializable) stockQuote);
        startActivity(intent);
    }

    // Process OnClick Of AN Item From The RecyclerView
    @Override
    public void onItemSelected(StockQuote stockQuote) {
        portfolioPresenter.selectIndividualStockQuote(stockQuote);
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
