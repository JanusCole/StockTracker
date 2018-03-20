package com.example.janus.stocktracker.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessAsync {

    private PortfolioDBOpenHelper portfolioDBOpenHelper;

    private OnPortfolioDBAccessCompletion onPortfolioDBAccessCompletion;


    public interface OnPortfolioDBAccessCompletion {
        void portfolioDBAccessSuccess(List<String> stockSymbols);
        void portfolioDBAccessFailure();

    }


    public DatabaseAccessAsync(Context context) {
        portfolioDBOpenHelper = new PortfolioDBOpenHelper(context);
    }

    public void setOnPortfolioDBAccessCompletion(OnPortfolioDBAccessCompletion onPortfolioDBAccessCompletion) {
        this.onPortfolioDBAccessCompletion = onPortfolioDBAccessCompletion;
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

            if (databaseResult.getResultCode() == DatabaseAccessResult.ResultCode.SUCCESS) {
                onPortfolioDBAccessCompletion.portfolioDBAccessSuccess(databaseResult.getStockSymbols());
            } else {
                onPortfolioDBAccessCompletion.portfolioDBAccessFailure();
            }
        }
    }


    private interface IProcessDatabaseRequest {

        DatabaseAccessResult getResult();
    }


    private class GetAllStocks implements IProcessDatabaseRequest {

        public GetAllStocks () {}

        public DatabaseAccessResult getResult() {
            List<String> stockList = new ArrayList<>();
            DatabaseAccessResult.ResultCode result;

            Cursor portfolioCursor = null;

            try {
                portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                        new String[]{PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                        null,
                        null,
                        null,
                        null,
                        PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                result = DatabaseAccessResult.ResultCode.SUCCESS;
            } catch (SQLException e) {
                result = DatabaseAccessResult.ResultCode.FAILURE;
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
            List<String> stockList = new ArrayList<>();
            DatabaseAccessResult.ResultCode result;

            Cursor portfolioCursor = null;
            try {
                portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                        new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                        PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                        new String [] {requestStock},
                        null,
                        null,
                        null);

                result = DatabaseAccessResult.ResultCode.SUCCESS;
            } catch (SQLException e) {
                result = DatabaseAccessResult.ResultCode.FAILURE;
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

            List<String> stockList = new ArrayList<>();
            DatabaseAccessResult.ResultCode result;

            ContentValues contentValues = new ContentValues();
            contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, requestStock);

            try {
                portfolioDBOpenHelper.getWritableDatabase().insertOrThrow(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);
                result = DatabaseAccessResult.ResultCode.SUCCESS;
            } catch (SQLException e) {
                result = DatabaseAccessResult.ResultCode.FAILURE;
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

            List<String> stockList = new ArrayList<>();
            DatabaseAccessResult.ResultCode result;

            try {
                int deletionResult = portfolioDBOpenHelper.getWritableDatabase().delete(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {requestStock});

                if (deletionResult == 1) {
                    result = DatabaseAccessResult.ResultCode.SUCCESS;
                } else {
                    result = DatabaseAccessResult.ResultCode.FAILURE;
                }

            } catch (SQLException e) {
                result = DatabaseAccessResult.ResultCode.FAILURE;
            }

            return new DatabaseAccessResult(result, stockList);
        }
    }

    public static class DatabaseAccessResult {

    //    DatabaseRequest request;
        ResultCode resultCode;
        List<String> stockSymbols;

        public DatabaseAccessResult(ResultCode resultCode, List<String> stockSymbols) {
            this.resultCode = resultCode;
            this.stockSymbols = stockSymbols;
        }

        public ResultCode getResultCode() {
            return resultCode;
        }

        public List<String> getStockSymbols() {
            return stockSymbols;
        }

        public enum ResultCode {

            SUCCESS, FAILURE;

        }

    }
}
