package com.example.janus.stocktracker.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.janus.stocktracker.R;


public class SearchStockFragment extends Fragment {

    private ShowStock stockSearcher;

    private EditText searchStockTicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_stocks_fragment, container, false);

        Button searchButton = (Button) v.findViewById (R.id.stockSearchButton);

        searchStockTicker = (EditText) v.findViewById (R.id.searchTickerEditText_StockSearch);
        searchStockTicker.requestFocus();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchStockTicker.getWindowToken(), 0);
                searchStock(searchStockTicker.getText().toString());
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

// For communicating with the  MainActivity
        stockSearcher = (ShowStock) context;

    }

    private void searchStock (String  searchStockTicker) {
        stockSearcher.showStock(searchStockTicker);
    }


}
