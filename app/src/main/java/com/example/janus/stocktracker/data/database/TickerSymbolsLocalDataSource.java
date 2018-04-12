package com.example.janus.stocktracker.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;

import com.example.janus.stocktracker.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class TickerSymbolsLocalDataSource implements TickerSymbolsDataSource {

    private static TickerSymbolsLocalDataSource INSTANCE = null;

    private PortfolioDBOpenHelper portfolioDBOpenHelper;

    AppExecutors ioThread;
    List<String> tickerSymbols = new ArrayList<>();

    private TickerSymbolsLocalDataSource(PortfolioDBOpenHelper portfolioDBOpenHelper) {
        this.portfolioDBOpenHelper = portfolioDBOpenHelper;
        ioThread = new AppExecutors();
    }

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
                    ioThread.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            loadTickerSymbolsCallback.onDataBaseError();
                        }
                    });
                }

                ioThread.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        loadTickerSymbolsCallback.onTickerSymbolsLoaded(tickerSymbols);
                    }
                });

            }
        };

        ioThread.diskIO().execute(runnable);

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
                    ioThread.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            addTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

                ioThread.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        addTickerSymbolCallback.onTickerSymbolAdded();
                    }
                });

            }
        };

        ioThread.diskIO().execute(runnable);

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
                            new String [] {tickerSymbol});
                } catch (SQLException e) {
                    ioThread.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            deleteTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

                if (deletionResult != 1) {
                    ioThread.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            deleteTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

                ioThread.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        deleteTickerSymbolCallback.onTickerSymbolDeleted();
                    }
                });

            }
        };

        ioThread.diskIO().execute(runnable);

    }

    @Override
    public void getTickerSymbol(final String tickerSymbol, final GetTickerSymbolCallback getTickerSymbolCallback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    final Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                            new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                            PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                            new String [] {tickerSymbol},
                            null,
                            null,
                            null);

                    if ((portfolioCursor != null) && (portfolioCursor.getCount() != 0)) {
                        portfolioCursor.moveToFirst();
                        final int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);
                        ioThread.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                getTickerSymbolCallback.onTickerSymbolRetrieved(portfolioCursor.getString(stockTickerIndex));
                            }
                        });

                    }

                } catch (SQLException e) {
                    ioThread.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            getTickerSymbolCallback.onDataBaseError();
                        }
                    });
                }

            }
        };

        ioThread.diskIO().execute(runnable);

    }

}
