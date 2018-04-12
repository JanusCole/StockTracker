package com.example.janus.stocktracker.data.database;

import java.util.List;

public interface TickerSymbolsDataSource {

    interface LoadTickerSymbolsCallback {

        void onTickerSymbolsLoaded(List<String> tickerSymbols);

        void onDataBaseError();
    }

    interface GetTickerSymbolCallback {

        void onTickerSymbolRetrieved(String tickerSymbol);

        void onDataBaseError();
    }

    interface DeleteTickerSymbolCallback {

        void onTickerSymbolDeleted();

        void onDataBaseError();
    }

    interface AddTickerSymbolCallback {

        void onTickerSymbolAdded();

        void onDataBaseError();
    }

    void getAllTickerSymbols(LoadTickerSymbolsCallback loadTickerSymbolsCallback);
    void addTickerSymbol(String tickerSymbol, AddTickerSymbolCallback addTickerSymbolCallback);
    void deleteTickerSymbol(String tickerSymbol, DeleteTickerSymbolCallback deleteTickerSymbolCallback);
    void getTickerSymbol(String tickerSymbol, GetTickerSymbolCallback getTickerSymbolCallback);

}
