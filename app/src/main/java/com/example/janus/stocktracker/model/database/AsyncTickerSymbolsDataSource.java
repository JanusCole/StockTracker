package com.example.janus.stocktracker.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class AsyncTickerSymbolsDataSource implements TickerSymbolsDataSource {

    private PortfolioDBOpenHelper portfolioDBOpenHelper;

    public AsyncTickerSymbolsDataSource(PortfolioDBOpenHelper portfolioDBOpenHelper) {
        this.portfolioDBOpenHelper = portfolioDBOpenHelper;
    }

    @Override
    public void getAllTickerSymbols(final LoadTickerSymbolsCallback loadTickerSymbolsCallback) {

        new AsyncTask<Void, Void, List<String>> () {

            @Override
            protected List<String> doInBackground(Void... params) {

                List<String> tickerSymbolList = new ArrayList<>();

                Cursor portfolioCursor = null;

                try {
                    portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                            new String[]{PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                            null,
                            null,
                            null,
                            null,
                            PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                } catch (SQLException e) {
                    cancel(true);
                }

                if ((!isCancelled()) && (portfolioCursor != null) && (portfolioCursor.getCount() != 0)) {
                    portfolioCursor.moveToFirst();
                    int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                    do {
                        tickerSymbolList.add(portfolioCursor.getString(stockTickerIndex));
                    }
                    while (portfolioCursor.moveToNext());
                }

                return tickerSymbolList;
            }

            @Override
            protected void onPostExecute(List<String> tickerSymbols) {
                loadTickerSymbolsCallback.onTickerSymbolsLoaded(tickerSymbols);
            }

            @Override
            protected void onCancelled() {
                loadTickerSymbolsCallback.onDataNotAvailable();
            }

        }.execute();

    }

    @Override
    public void addTickerSymbol(String tickerSymbol) {

        new AsyncTask<String, Void, Void> () {

            @Override
            protected Void doInBackground(String... params) {
                String tickerSymbol = params[0];

                ContentValues contentValues = new ContentValues();
                contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, tickerSymbol);

                portfolioDBOpenHelper.getWritableDatabase().insertOrThrow(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);

                return null;
            }

        }.execute(tickerSymbol);

    }

    @Override
    public void deleteTickerSymbol(String tickerSymbol) {

        new AsyncTask<String, Void, Void> () {

            @Override
            protected Void doInBackground(String... params) {
                String tickerSymbol = params[0];

                int deletionResult = portfolioDBOpenHelper.getWritableDatabase().delete(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                        PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                        new String [] {tickerSymbol});

                return null;
            }

        }.execute(tickerSymbol);

    }

    @Override
    public void getTickerSymbol(String tickerSymbol, final GetTickerSymbolCallback getTickerSymbolCallback) {

        new AsyncTask<String, Void, String> () {

            @Override
            protected String doInBackground(String... params) {

                String tickerSymbol = params[0];
                String tickerSymbolResult = null;

                Cursor portfolioCursor = null;

                try {
                    portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                            new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                            PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                            new String [] {tickerSymbol},
                            null,
                            null,
                            null);

                } catch (SQLException e) {
                    cancel(true);
                }

                if ((!isCancelled()) && (portfolioCursor != null) && (portfolioCursor.getCount() != 0)) {
                    portfolioCursor.moveToFirst();
                    int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);
                    tickerSymbolResult = portfolioCursor.getString(stockTickerIndex);
                }

                return tickerSymbolResult;
            }

            @Override
            protected void onPostExecute(String tickerSymbol) {
                getTickerSymbolCallback.onTickerSymbolRetrieved(tickerSymbol);
            }

            @Override
            protected void onCancelled() {
                getTickerSymbolCallback.onDataNotAvailable();
            }

        }.execute(tickerSymbol);

    }

}
