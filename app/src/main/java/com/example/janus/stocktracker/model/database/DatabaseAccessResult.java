package com.example.janus.stocktracker.model.database;

import java.util.List;
import com.example.janus.stocktracker.model.codes.ResultCode;

public class DatabaseAccessResult {

//    DatabaseRequest request;
    ResultCode resultCode;
    List<String> stockSymbols;

    public DatabaseAccessResult(ResultCode resultCode, List<String> stockSymbols) {
        this.resultCode = resultCode;
        this.stockSymbols = stockSymbols;
    }

    public DatabaseAccessResult(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public List<String> getStockSymbols() {
        return stockSymbols;
    }

//    public DatabaseRequest getRequest() {
//        return request;
//    }

    public void setResultCode(ResultCode resultCode) {

        this.resultCode = resultCode;
    }

    public void setStockSymbols(List<String> stockSymbols) {

        this.stockSymbols = stockSymbols;
    }

//    public void setRequest(DatabaseRequest request) {
//        this.request = request;
//    }

//    public enum ResultCode {

//        success, failure;

//    }

//    public enum RequestType {

//        checkStockInDatabase, addStockToDatabase, readAllStocksInDatabase, removeStockFromDatabase;

//    }
}
