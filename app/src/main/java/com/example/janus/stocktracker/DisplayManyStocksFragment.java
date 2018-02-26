package com.example.janus.stocktracker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class DisplayManyStocksFragment extends Fragment implements StocksRecyclerViewAdapter.OnItemSelectedListener {

// This is for communicating with the MainActivity
    private ShowStocks searchStocks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle stockQuoteBundle = getArguments();
        List<StockQuote> stockQuotes = stockQuoteBundle.getParcelableArrayList(getActivity().getString(R.string.stock_search_list));

        View v = inflater.inflate(R.layout.display_multiple_stocks_fragment, container, false);

        RecyclerView stockRecyclerView = (RecyclerView) v.findViewById(R.id.displayStocksRecyclerView);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stockRecyclerView.setAdapter(new StocksRecyclerViewAdapter(stockQuotes, this));

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

// This is for communicating with the MainActivity
        searchStocks = (ShowStocks) context;

    }

// Process OnClick Of AN Item From The RecyclerView
    @Override
    public void onItemSelected(String tickerSymbol) {

        List<String> stockTickerArray = new ArrayList <>();
        stockTickerArray.add(tickerSymbol);

// Commuinicates with the MainActivity
        searchStocks.showStocks(stockTickerArray, new DisplayOneStockFragment());
    }

}
