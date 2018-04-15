package com.example.janus.stocktracker.stocksearch;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StockSearchPresenterUnitTest {

    private StockQuote stockQuote;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private StockQuote mockStockQuote;

    @Mock
    private StockSearchContract.View mockStockSearchView;

    @Mock
    private StockQuoteService mockStockQuoteDataSource;

    // This is the Class under test
    private StockSearchContract.Presenter stockSearchPresenter;

    @Before
    public void setUp() throws Exception {

        mockStockSearchView = mock(StockSearchContract.View.class);
        mockStockQuoteDataSource = mock(StockQuoteService.class);

        mockStockQuote = mock(StockQuote.class);

        stockSearchPresenter = new StockSearchPresenter(mockStockSearchView, mockStockQuoteDataSource);

        stockQuote = new StockQuote("IBM", "International Business Machines", "Technology", 50.5, 55.5, 52.0, 5000);

    }

    @Test
    public void testSearchStocksFound() throws Exception {

        String tickerSymbol = "IBM";

        stockSearchPresenter.searchStock(tickerSymbol);

        List <StockQuote> returnResults = new ArrayList<>();
        returnResults.add(stockQuote);

        ArgumentCaptor<StockQuoteService.GetStockQuotesCallback> mLoadStockQuoteCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteService.GetStockQuotesCallback.class);

        ArgumentCaptor<List<String>> mTickersToSearch = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<StockQuote>> returnedStockQuotes = ArgumentCaptor.forClass(List.class);

        verify(mockStockQuoteDataSource).getStockQuotes(mTickersToSearch.capture(), mLoadStockQuoteCallbackCaptor.capture());
        mLoadStockQuoteCallbackCaptor.getValue().onStockQuotesLoaded(returnResults);

        assertEquals("IBM", returnedStockQuotes.getValue().get(0).getSymbol());

        verify(mockStockSearchView).showStockQuoteUI(stockQuote);

    }

    @Test
    public void testSearchStocksNotFound() throws Exception {

        String tickerSymbol = "IBM";

        stockSearchPresenter.searchStock(tickerSymbol);

        ArgumentCaptor<StockQuoteService.GetStockQuotesCallback> mLoadStockQuoteCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteService.GetStockQuotesCallback.class);

        ArgumentCaptor<List<String>> mTickersToSearch = ArgumentCaptor.forClass(List.class);

        verify(mockStockQuoteDataSource).getStockQuotes(mTickersToSearch.capture(), mLoadStockQuoteCallbackCaptor.capture());
        mLoadStockQuoteCallbackCaptor.getValue().onStockQuotesLoaded(new ArrayList<StockQuote>());

        verify(mockStockSearchView).showNotFoundError();

    }

    @Test
    public void testSearchStocksError() throws Exception {

        String tickerSymbol = "IBM";

        stockSearchPresenter.searchStock(tickerSymbol);

        ArgumentCaptor<StockQuoteService.GetStockQuotesCallback> mLoadStockQuoteCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteService.GetStockQuotesCallback.class);

        ArgumentCaptor<List<String>> mTickersToSearch = ArgumentCaptor.forClass(List.class);

        verify(mockStockQuoteDataSource).getStockQuotes(mTickersToSearch.capture(), mLoadStockQuoteCallbackCaptor.capture());
        mLoadStockQuoteCallbackCaptor.getValue().onDataNotAvailable();

        verify(mockStockSearchView).showLoadingError();

    }


    @After
    public void tearDown() throws Exception {
    }

}