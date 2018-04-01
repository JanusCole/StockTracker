package com.example.janus.stocktracker.model.database;

import java.util.List;

public interface TickerSymbolsDataSource {

    interface LoadTickerSymbolsCallback {

        void onTickerSymbolsLoaded(List<String> tickerSymbols);

        void onDataNotAvailable();
    }

    interface GetTickerSymbolCallback {

        void onTickerSymbolRetrieved(String tickerSymbol);

        void onDataNotAvailable();
    }

    void getAllTickerSymbols(LoadTickerSymbolsCallback loadTickerSymbolsCallback);
    void addTickerSymbol(String tickerSymbol);
    void deleteTickerSymbol(String tickerSymbol);
    void getTickerSymbol(String tickerSymbol, GetTickerSymbolCallback getTickerSymbolCallback);

}
