package com.example.janus.stocktracker.portfolio;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;

import java.util.List;


public class StocksRecyclerViewAdapter extends RecyclerView.Adapter<StocksRecyclerViewAdapter.ViewHolder> {

// Recycler View for the multiple stock display view
    private List<StockQuote> stockQuotes;

    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(StockQuote stockQuote);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelectedListener.onItemSelected(stockQuotes.get(getAdapterPosition()));
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View thisItemsView = inflater.inflate(R.layout.stock_recycler_item,parent, false);

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
            holder.priceChange.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accountingCreditGreen));
            holder.priceChangePercent.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.accountingCreditGreen));
        }

    }

    @Override
    public int getItemCount() {
        return stockQuotes.size();
    }

}
