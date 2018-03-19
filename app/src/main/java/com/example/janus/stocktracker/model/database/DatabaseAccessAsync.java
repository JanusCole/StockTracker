package com.example.janus.stocktracker.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.example.janus.stocktracker.model.codes.ResultCode;

public class DatabaseAccessAsync {

    private PortfolioDBOpenHelper portfolioDBOpenHelper;

    private OnPortfolioDBAccessCompletion onPortfolioDBAccessCompletion;


    public interface OnPortfolioDBAccessCompletion {
        public abstract void portfolioDBAccessCompleted(ResultCode resultCode, List<String> stockSymbols);
    }


    public DatabaseAccessAsync(Context context, OnPortfolioDBAccessCompletion onPortfolioAccessCompletion) {
        portfolioDBOpenHelper = new PortfolioDBOpenHelper(context);
        this.onPortfolioDBAccessCompletion = onPortfolioAccessCompletion;
    }

    public void getAllStocksPortfolioDBAsync() {
        new ProcessPortfolioRequest().execute(new GetAllStocks());
    }

    public void getOneStockPortfolioDBAsync(String tickerSymbol) {
        new ProcessPortfolioRequest().execute(new GetOneStock(tickerSymbol));
    }

    public void addOneStockPortfolioDBAsync(String tickerSymbol) {
        new ProcessPortfolioRequest().execute(new AddOneStock(tickerSymbol));
    }

    public void deleteOneStockPortfolioDBAsync(String tickerSymbol) {
        new ProcessPortfolioRequest().execute(new DeleteOneStock(tickerSymbol));
    }


    public class ProcessPortfolioRequest extends AsyncTask<IProcessDatabaseRequest, Void, DatabaseAccessResult> {
        @Override
        protected DatabaseAccessResult doInBackground(IProcessDatabaseRequest... params) {
            return params[0].getResult();
        }

        @Override
        protected void onPostExecute(DatabaseAccessResult databaseResult) {
            onPortfolioDBAccessCompletion.portfolioDBAccessCompleted(databaseResult.getResultCode(), databaseResult.getStockSymbols());
        }
    }


    private interface IProcessDatabaseRequest {

        DatabaseAccessResult getResult();
    }


    private class GetAllStocks implements IProcessDatabaseRequest {

        public GetAllStocks () {}

        public DatabaseAccessResult getResult() {
            ArrayList<String> stockList = new ArrayList<>();
            ResultCode result;

            Cursor portfolioCursor = null;

            try {
                portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                        new String[]{PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                        null,
                        null,
                        null,
                        null,
                        PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                result = ResultCode.SUCCESS;
            } catch (SQLException e) {
                result = ResultCode.DATABASE_ERROR;
            }

            if ((portfolioCursor != null) && (portfolioCursor.getCount() != 0)) {
                portfolioCursor.moveToFirst();
                int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                do {
                    stockList.add(portfolioCursor.getString(stockTickerIndex));
                }
                while (portfolioCursor.moveToNext());
            }

            return new DatabaseAccessResult(result, stockList);
        }
    }

    private class GetOneStock implements IProcessDatabaseRequest {

        String requestStock;

        public GetOneStock (String requestStock) {
            this.requestStock = requestStock;
        }

        public DatabaseAccessResult getResult() {
            ArrayList<String> stockList = new ArrayList<>();
            ResultCode result;

            Cursor portfolioCursor = null;
            try {
                portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                        new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                        PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                        new String [] {requestStock},
                        null,
                        null,
                        null);

                result = ResultCode.SUCCESS;
            } catch (SQLException e) {
                result = ResultCode.DATABASE_ERROR;
            }

            if ((portfolioCursor != null) && (portfolioCursor.getCount() != 0)) {
                portfolioCursor.moveToFirst();
                int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                do {
                    stockList.add(portfolioCursor.getString(stockTickerIndex));
                } while (portfolioCursor.moveToNext());

            }

            return new DatabaseAccessResult(result, stockList);
        }
    }

    private class AddOneStock implements IProcessDatabaseRequest {

        String requestStock;

        public AddOneStock (String requestStock) {
            this.requestStock = requestStock;
        }
        public DatabaseAccessResult getResult() {

            ArrayList<String> stockList = new ArrayList<>();
            ResultCode result;

            ContentValues contentValues = new ContentValues();
            contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, requestStock);

            try {
                portfolioDBOpenHelper.getWritableDatabase().insertOrThrow(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);
                result = ResultCode.SUCCESS;
            } catch (SQLException e) {
                result = ResultCode.DATABASE_ERROR;
            }

            return new DatabaseAccessResult(result, stockList);
        }
    }

    private class DeleteOneStock implements IProcessDatabaseRequest {

        String requestStock;

        public DeleteOneStock (String requestStock) {
            this.requestStock = requestStock;
        }

        public DatabaseAccessResult getResult() {

            ArrayList<String> stockList = new ArrayList<>();
            ResultCode result;

            try {
                int deletionResult = portfolioDBOpenHelper.getWritableDatabase().delete(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {requestStock});

                if (deletionResult == 1) {
                    result = ResultCode.SUCCESS;
                } else {
                    result = ResultCode.DATABASE_ERROR;
                }

            } catch (SQLException e) {
                result = ResultCode.DATABASE_ERROR;
            }

            return new DatabaseAccessResult(result, stockList);
        }
    }
}
