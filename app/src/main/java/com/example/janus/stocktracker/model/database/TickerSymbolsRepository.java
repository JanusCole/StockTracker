package com.example.janus.stocktracker.model.database;

import java.util.List;

public class TickerSymbolsRepository implements TickerSymbolsDataSource {


    private TickerSymbolsDataSource tickerSymbolsDataSource;

    public TickerSymbolsRepository(TickerSymbolsDataSource tickerSymbolsDataSource) {

        this.tickerSymbolsDataSource = tickerSymbolsDataSource;

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
    public void addTickerSymbol(String tickerSymbol) {
        tickerSymbolsDataSource.addTickerSymbol(tickerSymbol);
    }

    @Override
    public void deleteTickerSymbol(String tickerSymbol) {
        tickerSymbolsDataSource.deleteTickerSymbol(tickerSymbol);
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
