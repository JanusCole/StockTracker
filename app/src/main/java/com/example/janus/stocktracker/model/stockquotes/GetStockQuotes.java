package com.example.janus.stocktracker.model.stockquotes;

import java.util.List;

import com.example.janus.stocktracker.presenter.StockQuote;

public abstract class GetStockQuotes {

    protected ProcessStockQuoteResults processStockQuoteResults;
    protected GetStockQuotesAPI getStockQuotesAPI;

    public GetStockQuotes(GetStockQuotesAPI getStockQuotesAPI) {
        this.getStockQuotesAPI = getStockQuotesAPI;
    }

    public interface ProcessStockQuoteResults {
        void processStockQuoteSuccess (List<StockQuote> stockQuoteResults);
        void processStockQuoteFailure ();
        void processStockQuoteNotFound ();
    }

    public abstract void getStockQuotes(List<String> stockTickers);

    public void setProcessStockQuoteResults(ProcessStockQuoteResults processStockQuoteResults) {
        this.processStockQuoteResults = processStockQuoteResults;
    }
}
