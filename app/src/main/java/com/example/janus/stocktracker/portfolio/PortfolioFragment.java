package com.example.janus.stocktracker.portfolio;

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

import com.example.janus.stocktracker.util.DisplayFormattedMessages;
import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.stockquote.StockQuoteActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PortfolioFragment extends Fragment implements StocksRecyclerViewAdapter.OnItemSelectedListener, PortfolioContract.View {

    private PortfolioContract.Presenter portfolioPresenter;

    private RecyclerView stockRecyclerView;
    private StocksRecyclerViewAdapter stocksRecyclerViewAdapter;

    private AlertDialog networkActivityDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stocksRecyclerViewAdapter = new StocksRecyclerViewAdapter(new ArrayList<StockQuote>(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.display_multiple_stocks_fragment, container, false);

        stockRecyclerView = (RecyclerView) rootView.findViewById(R.id.displayStocksRecyclerView);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stockRecyclerView.setAdapter(stocksRecyclerViewAdapter);

        networkActivityDialog = DisplayFormattedMessages.showNetworkActivityAlert(inflater, getContext());

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        portfolioPresenter.loadStocks();
    }

    @Override
    public void setPresenter(PortfolioContract.Presenter portfolioPresenter) {
        this.portfolioPresenter = portfolioPresenter;
    }

    @Override
    public void showStocks(List<StockQuote> stockQuotes) {
        networkActivityDialog.dismiss();
        stocksRecyclerViewAdapter = new StocksRecyclerViewAdapter(stockQuotes, this);
        stockRecyclerView.setAdapter(stocksRecyclerViewAdapter);
    }

    @Override
    public void showLoadingIndicator() {
        networkActivityDialog.show();
    }

    @Override
    public void showLoadingError() {
        networkActivityDialog.dismiss();
        DisplayFormattedMessages.displayErrorMessageAlertDialog(getString(R.string.portfolio_loading_error_message), getActivity(), getContext());
    }

    @Override
    public void showEmptyPortfolioMessage() {
        networkActivityDialog.dismiss();
        DisplayFormattedMessages.displayErrorMessageAlertDialog(getString(R.string.empty_portfolio_message), getActivity(), getContext());
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

}
