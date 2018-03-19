package com.example.janus.stocktracker.model.stockquotes;

import java.util.List;
import com.example.janus.stocktracker.model.codes.ResultCode;

public class GetStockQuotesResult {

    private List <StockQuote> stockQuotes;
    private ResultCode resultCode;

    public GetStockQuotesResult(List<StockQuote> stockQuotes, ResultCode resultCode) {
        this.stockQuotes = stockQuotes;
        this.resultCode = resultCode;
    }

    public List<StockQuote> getStockQuotes() {
        return stockQuotes;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

}
