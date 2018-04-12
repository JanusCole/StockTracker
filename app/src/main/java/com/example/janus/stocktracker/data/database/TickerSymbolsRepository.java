package com.example.janus.stocktracker.data.database;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TickerSymbolsRepository implements TickerSymbolsDataSource {

    private static TickerSymbolsRepository INSTANCE = null;

    private TickerSymbolsDataSource tickerSymbolsDataSource;

    private Set<String> tickerSymbolCache = new HashSet<>();
    private boolean cacheIsDirty = true;

    private TickerSymbolsRepository(TickerSymbolsDataSource tickerSymbolsDataSource) {
        this.tickerSymbolsDataSource = tickerSymbolsDataSource;
    }

    public static TickerSymbolsRepository getInstance (TickerSymbolsDataSource tickerSymbolsDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new TickerSymbolsRepository(tickerSymbolsDataSource);
        }

        return INSTANCE;

    }


    @Override
    public void getAllTickerSymbols(final LoadTickerSymbolsCallback loadTickerSymbolsCallback) {

        if (!cacheIsDirty) {
            loadTickerSymbolsCallback.onTickerSymbolsLoaded(new ArrayList<String>(tickerSymbolCache));
        } else {

            tickerSymbolsDataSource.getAllTickerSymbols(new LoadTickerSymbolsCallback() {
                @Override
                public void onTickerSymbolsLoaded(List<String> tickerSymbols) {
                    tickerSymbolCache.clear();
                    tickerSymbolCache.addAll(tickerSymbols);
                    cacheIsDirty = false;
                    loadTickerSymbolsCallback.onTickerSymbolsLoaded(tickerSymbols);
                }

                @Override
                public void onDataBaseError() {
                    loadTickerSymbolsCallback.onDataBaseError();
                }
            });
        }

    }

    @Override
    public void addTickerSymbol(final String tickerSymbol, final AddTickerSymbolCallback addTickerSymbolCallback) {
        tickerSymbolsDataSource.addTickerSymbol(tickerSymbol, new AddTickerSymbolCallback() {
            @Override
            public void onTickerSymbolAdded() {
                cacheIsDirty = true;
                addTickerSymbolCallback.onTickerSymbolAdded();
            }

            @Override
            public void onDataBaseError() {
                cacheIsDirty = true;
                addTickerSymbolCallback.onDataBaseError();
            }
        });
    }

    @Override
    public void deleteTickerSymbol(String tickerSymbol, final DeleteTickerSymbolCallback deleteTickerSymbolCallback) {
        tickerSymbolsDataSource.deleteTickerSymbol(tickerSymbol, new DeleteTickerSymbolCallback() {
            @Override
            public void onTickerSymbolDeleted() {
                cacheIsDirty = true;
                deleteTickerSymbolCallback.onTickerSymbolDeleted();
            }

            @Override
            public void onDataBaseError() {
                cacheIsDirty = true;
                deleteTickerSymbolCallback.onDataBaseError();
            }
        });
    }

    @Override
    public void getTickerSymbol(String tickerSymbol, final GetTickerSymbolCallback getTickerSymbolCallback) {

        if (!cacheIsDirty) {
            if (tickerSymbolCache.contains(tickerSymbol)) {
                getTickerSymbolCallback.onTickerSymbolRetrieved(tickerSymbol);
            } else {
                getTickerSymbolCallback.onTickerSymbolRetrieved(null);
            }

        } else {
            tickerSymbolsDataSource.getTickerSymbol(tickerSymbol, new GetTickerSymbolCallback() {
                @Override
                public void onTickerSymbolRetrieved(String tickerSymbol) {
                    getTickerSymbolCallback.onTickerSymbolRetrieved(tickerSymbol);
                }

                @Override
                public void onDataBaseError() {
                    getTickerSymbolCallback.onDataBaseError();
                }
            });
        }
    }

    @VisibleForTesting
    public static void deleteInstance () {
        INSTANCE = null;
    }
}
