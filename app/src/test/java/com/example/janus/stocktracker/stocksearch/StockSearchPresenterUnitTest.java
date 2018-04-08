package com.example.janus.stocktracker.stocksearch;

import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteDataSource;
import com.example.janus.stocktracker.portfolio.PortfolioContract;
import com.example.janus.stocktracker.portfolio.PortfolioPresenter;

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
    private StockQuoteDataSource mockStockQuoteDataSource;

    // This is the Class under test
    private StockSearchContract.Presenter stockSearchPresenter;

    @Before
    public void setUp() throws Exception {

        mockStockSearchView = mock(StockSearchContract.View.class);
        mockStockQuoteDataSource = mock(StockQuoteDataSource.class);

        mockStockQuote = mock(StockQuote.class);

        stockSearchPresenter = new StockSearchPresenter(mockStockSearchView, mockStockQuoteDataSource);

        stockQuote = new StockQuote("IBM", "International Business Machines", "Technology", 50.5, 55.5, 52.0, 5000);

    }

    @Test
    public void testSearchStocksFound() throws Exception {

        String tickerSymbol = "IBM";

        stockSearchPresenter.searchStock(tickerSymbol);

        ArgumentCaptor<StockQuoteDataSource.GetStockQuoteCallback> mLoadStockQuoteCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteDataSource.GetStockQuoteCallback.class);

        ArgumentCaptor<String> mTickerToSearch = ArgumentCaptor.forClass(String.class);

        verify(mockStockQuoteDataSource).getStockQuote(mTickerToSearch.capture(), mLoadStockQuoteCallbackCaptor.capture());
        mLoadStockQuoteCallbackCaptor.getValue().onStockQuoteLoaded(stockQuote);

        assertEquals("IBM", mTickerToSearch.getValue());

        verify(mockStockSearchView).showStockQuoteUI(stockQuote);

    }

    @Test
    public void testSearchStocksNotFound() throws Exception {

        String tickerSymbol = "IBM";

        stockSearchPresenter.searchStock(tickerSymbol);

        ArgumentCaptor<StockQuoteDataSource.GetStockQuoteCallback> mLoadStockQuoteCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteDataSource.GetStockQuoteCallback.class);

        ArgumentCaptor<String> mTickerToSearch = ArgumentCaptor.forClass(String.class);

        verify(mockStockQuoteDataSource).getStockQuote(mTickerToSearch.capture(), mLoadStockQuoteCallbackCaptor.capture());
        mLoadStockQuoteCallbackCaptor.getValue().onStockQuoteLoaded(null);

        verify(mockStockSearchView).showNotFoundError();

    }

    @Test
    public void testSearchStocksError() throws Exception {

        String tickerSymbol = "IBM";

        stockSearchPresenter.searchStock(tickerSymbol);

        ArgumentCaptor<StockQuoteDataSource.GetStockQuoteCallback> mLoadStockQuoteCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteDataSource.GetStockQuoteCallback.class);

        ArgumentCaptor<String> mTickerToSearch = ArgumentCaptor.forClass(String.class);

        verify(mockStockQuoteDataSource).getStockQuote(mTickerToSearch.capture(), mLoadStockQuoteCallbackCaptor.capture());
        mLoadStockQuoteCallbackCaptor.getValue().onDataNotAvailable();

        verify(mockStockSearchView).showLoadingError();

    }


    @After
    public void tearDown() throws Exception {
    }

}