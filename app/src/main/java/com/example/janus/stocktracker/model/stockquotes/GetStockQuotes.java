package com.example.janus.stocktracker.model.stockquotes;

import java.util.List;
import com.example.janus.stocktracker.model.codes.ResultCode;

public abstract class GetStockQuotes {

    protected   ProcessStockQuoteResults processStockQuoteResults;

    public interface ProcessStockQuoteResults {
        void processStockQuoteResults (List<StockQuote> stockQuoteResults, ResultCode resultCode);
    }

    public GetStockQuotes(ProcessStockQuoteResults processStockQuoteResults) {
        this.processStockQuoteResults = processStockQuoteResults;
    }

    public abstract void getStockQuotes(List<String> stockTickers);

}
