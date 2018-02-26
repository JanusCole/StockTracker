package com.example.janus.stocktracker;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class StocksRecyclerViewAdapter extends RecyclerView.Adapter<StocksRecyclerViewAdapter.ViewHolder> {

// Recycler View for the multiple stock display view
    private List<StockQuote> stockQuotes;

    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        public abstract void onItemSelected(String tickerSymbol);
    }

    public StocksRecyclerViewAdapter(List<StockQuote> stockQuotes, OnItemSelectedListener onItemSelectedListener) {
        this.stockQuotes = stockQuotes;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tickerSymbol;
        private TextView price;
        private TextView priceChange;
        private TextView priceChangePercent;

        public ViewHolder(View itemView) {
            super(itemView);

            tickerSymbol = (TextView) itemView.findViewById(R.id.tickerSymbolTextView_RecyclerView);
            price = (TextView) itemView.findViewById(R.id.latestPriceTextView_RecyclerView);
            priceChange = (TextView) itemView.findViewById(R.id.priceChangeTextView_RecyclerView);
            priceChangePercent = (TextView) itemView.findViewById(R.id.priceChangePercentageTextView_RecyclerView);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View thisItemsView = inflater.inflate(R.layout.stock_recycler_item,parent, false);

        thisItemsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedListener.onItemSelected(((TextView) v.findViewById(R.id.tickerSymbolTextView_RecyclerView)).getText().toString());
            }
        });

        return new ViewHolder(thisItemsView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tickerSymbol.setText(stockQuotes.get(position).getSymbol());
        holder.price.setText(String.format("%.2f", stockQuotes.get(position).getLatestPrice()));
        holder.priceChange.setText(String.format("%.2f", (stockQuotes.get(position).getLatestPrice() - stockQuotes.get(position).getOpen())));
        holder.priceChangePercent.setText("(" + String.format("%.3f", (100 * (stockQuotes.get(position).getLatestPrice() - stockQuotes.get(position).getOpen())/stockQuotes.get(position).getOpen())) + " %)");

        if (stockQuotes.get(position).getLatestPrice()  < stockQuotes.get(position).getOpen()) {
            holder.priceChange.setTextColor(Color.RED);
            holder.priceChangePercent.setTextColor(Color.RED);
        } else {
            holder.priceChangePercent.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accountingCreditGreen));
            holder.priceChange.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accountingCreditGreen));

        }

    }

    @Override
    public int getItemCount() {
        return stockQuotes.size();
    }

}
