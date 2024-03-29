package com.example.janus.stocktracker.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.example.janus.stocktracker.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

// This is the class for interating with thte ticker symbol database. It does CRD (not U) processing off the main UI thread

public class TickerSymbolsLocalDataSource implements TickerSymbolsDataSource {

    private static TickerSymbolsLocalDataSource INSTANCE = null;

    private PortfolioDBOpenHelper portfolioDBOpenHelper;

    AppExecutors appExecutors;
    List<String> tickerSymbols = new ArrayList<>();

    // Private constructor for the singleton
    private TickerSymbolsLocalDataSource(PortfolioDBOpenHelper portfolioDBOpenHelper) {
        this.portfolioDBOpenHelper = portfolioDBOpenHelper;
        appExecutors = new AppExecutors();
    }

    // Public getInstance for the singleton
    public static TickerSymbolsLocalDataSource getInstance(PortfolioDBOpenHelper portfolioDBOpenHelper) {

        if (INSTANCE == null) {
            INSTANCE = new TickerSymbolsLocalDataSource(portfolioDBOpenHelper);
        }

        return INSTANCE;

    }

    @Override
    public void getAllTickerSymbols(final LoadTickerSymbolsCallback loadTickerSymbolsCallback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                tickerSymbols.clear();

                try {
                    Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                            new String[]{PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                            null,
                            null,
                            null,
                            null,
                            PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                    if ((portfolioCursor != null) && (portfolioCursor.getCount() != 0)) {
                        portfolioCursor.moveToFirst();
                        int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

                        do {
                            tickerSymbols.add(portfolioCursor.getString(stockTickerIndex));
                        }
                        while (portfolioCursor.moveToNext());
                    }


                } catch (SQLException e) {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            loadTickerSymbolsCallback.onDataBaseError();
                        }
                    });
                }

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        loadTickerSymbolsCallback.onTickerSymbolsLoaded(tickerSymbols);
                    }
                });

            }
        };

        appExecutors.diskIO().execute(runnable);

    }

    @Override
    public void addTickerSymbol(final String tickerSymbol, final AddTickerSymbolCallback addTickerSymbolCallback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                ContentValues contentValues = new ContentValues();
                contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, tickerSymbol);

                try {
                    portfolioDBOpenHelper.getWritableDatabase().insertOrThrow(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);
                } catch (SQLException e) {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            addTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        addTickerSymbolCallback.onTickerSymbolAdded();
                    }
                });

            }
        };

        appExecutors.diskIO().execute(runnable);

    }

    @Override
    public void deleteTickerSymbol(final String tickerSymbol, final DeleteTickerSymbolCallback deleteTickerSymbolCallback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                int deletionResult = 0;

                try {
                    deletionResult = portfolioDBOpenHelper.getWritableDatabase().delete(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                            PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                            new String[]{tickerSymbol});
                } catch (SQLException e) {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            deleteTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

                if (deletionResult != 1) {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            deleteTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        deleteTickerSymbolCallback.onTickerSymbolDeleted();
                    }
                });

            }
        };

        appExecutors.diskIO().execute(runnable);

    }

    @Override
    public void getTickerSymbol(final String tickerSymbol, final GetTickerSymbolCallback getTickerSymbolCallback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    final Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                            new String[]{PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                            PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                            new String[]{tickerSymbol},
                            null,
                            null,
                            null);

                    if (portfolioCursor != null) {
                        if (portfolioCursor.getCount() != 0) {
                            portfolioCursor.moveToFirst();
                        }
                        final String returnTickerSymbol = portfolioCursor.getCount() != 0 ?
                                portfolioCursor.getString(portfolioCursor.getColumnIndex(
                                        PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK)) :
                                null;
                        appExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                getTickerSymbolCallback.onTickerSymbolRetrieved(returnTickerSymbol);
                            }
                        });

                    }

                } catch (SQLException e) {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            getTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

            }
        };

        appExecutors.diskIO().execute(runnable);

    }

}
