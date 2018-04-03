package com.example.janus.stocktracker.stocksearch;

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

import com.example.janus.stocktracker.util.DisplayFormattedMessages;
import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.stockquote.StockQuoteActivity;

import java.io.Serializable;


public class StockSearchFragment extends Fragment implements StockSearchContract.View {

    private StockSearchContract.Presenter stockSearchPresenter;

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

        networkActivityDialog = DisplayFormattedMessages.showNetworkActivityAlert(inflater, getContext());

        return rootView;
    }

    @Override
    public void setPresenter(StockSearchContract.Presenter stockSearchPresenter) {
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
        DisplayFormattedMessages.displayErrorMessageAlertDialog(getString(R.string.stock_not_found_message), getActivity(), getContext());
    }

    @Override
    public void showLoadingError() {
        networkActivityDialog.dismiss();
        DisplayFormattedMessages.displayErrorMessageAlertDialog(getString(R.string.network_error_message), getActivity(), getContext());
    }
}
