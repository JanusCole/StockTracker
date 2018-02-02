package com.example.janus.stocktracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class Portfolio {

    private PortfolioDBOpenHelper portfolioDBOpenHelper;

    public Portfolio (Context context) {

        portfolioDBOpenHelper = new PortfolioDBOpenHelper(context);

    }

    public ArrayList<String> getPortfolio () {

        ArrayList<String> stockList = new ArrayList<>();

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                null,
                null,
                null,
                null,
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

        if ((portfolioCursor != null) && (portfolioCursor.getCount() != 0)) {
            portfolioCursor.moveToFirst();
            int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

            do {
                stockList.add(portfolioCursor.getString(stockTickerIndex));
            }
            while (portfolioCursor.moveToNext());
        }

        return stockList;
    }

    public boolean stockInPortfolio (String stockTicker) {

          Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {stockTicker},
                null,
                null,
                null);

        return ((portfolioCursor != null) && (portfolioCursor.getCount() != 0));

    }

    public void addStockToPortfolio (String stockTicker) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, stockTicker);

        portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);

    }

    public void removeStockFromPortfolio (String stockTicker) {

        portfolioDBOpenHelper.getWritableDatabase().delete(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {stockTicker});

    }

    public void removeAllStocksFromPortfolio () {

        portfolioDBOpenHelper.getWritableDatabase().execSQL("DELETE FROM " + PortfolioDBContract.PortfolioEntry.TABLE_NAME);

    }

}
