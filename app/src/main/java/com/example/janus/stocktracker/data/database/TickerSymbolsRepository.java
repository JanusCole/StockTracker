package com.example.janus.stocktracker.data.database;

import java.util.List;

public class TickerSymbolsRepository implements TickerSymbolsDataSource {

    private static TickerSymbolsRepository INSTANCE = null;

    private TickerSymbolsDataSource tickerSymbolsDataSource;

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

        tickerSymbolsDataSource.getAllTickerSymbols(new LoadTickerSymbolsCallback() {
            @Override
            public void onTickerSymbolsLoaded(List<String> tickerSymbols) {
                loadTickerSymbolsCallback.onTickerSymbolsLoaded(tickerSymbols);
            }

            @Override
            public void onDataNotAvailable() {
                loadTickerSymbolsCallback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void addTickerSymbol(String tickerSymbol, final AddTickerSymbolCallback addTickerSymbolCallback) {
        tickerSymbolsDataSource.addTickerSymbol(tickerSymbol, new AddTickerSymbolCallback() {
            @Override
            public void onTickerSymbolAdded() {
                addTickerSymbolCallback.onTickerSymbolAdded();
            }

            @Override
            public void onDataBaseError() {
                addTickerSymbolCallback.onDataBaseError();
            }
        });
    }

    @Override
    public void deleteTickerSymbol(String tickerSymbol, final DeleteTickerSymbolCallback deleteTickerSymbolCallback) {
        tickerSymbolsDataSource.deleteTickerSymbol(tickerSymbol, new DeleteTickerSymbolCallback() {
            @Override
            public void onTickerSymbolDeleted() {
                deleteTickerSymbolCallback.onTickerSymbolDeleted();
            }

            @Override
            public void onDataBaseError() {
                deleteTickerSymbolCallback.onDataBaseError();
            }
        });
    }

    @Override
    public void getTickerSymbol(String tickerSymbol, final GetTickerSymbolCallback getTickerSymbolCallback) {
        tickerSymbolsDataSource.getTickerSymbol(tickerSymbol, new GetTickerSymbolCallback() {
            @Override
            public void onTickerSymbolRetrieved(String tickerSymbol) {
                getTickerSymbolCallback.onTickerSymbolRetrieved(tickerSymbol);
            }

            @Override
            public void onDataNotAvailable() {
                getTickerSymbolCallback.onDataNotAvailable();
            }
        });
    }
}
