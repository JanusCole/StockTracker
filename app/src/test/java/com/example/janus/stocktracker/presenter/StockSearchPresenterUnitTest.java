package com.example.janus.stocktracker.presenter;

import com.example.janus.stocktracker.model.stockquotes.GetStockQuotes;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;

public class StockSearchPresenterUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    StockSearchContract.View mockStockSearchView;

    @Mock
    PortfolioAccessPresenter mockPortfolioSource;

    @Mock
    GetStockQuotes mockStockQuoteSource;

    @Captor
    ArgumentCaptor<List<String>> stockSearchListCaptor;

    // This is the Class under test
    StockSearchPresenter stockSearchPresenter;

    @Before
    public void setUp() throws Exception {
        mockPortfolioSource = mock(PortfolioAccessPresenter.class);
        mockStockQuoteSource = mock(GetStockQuotes.class);
        mockStockSearchView = mock(StockSearchContract.View.class);
        stockSearchPresenter = new StockSearchPresenter(mockStockSearchView, mockPortfolioSource, mockStockQuoteSource);
    }

    @Test
    public void testSearchPortfolioCallsGetPortfolioFromPortfolioDatabaseSource() throws Exception {
        stockSearchPresenter.searchPortfolio();
        verify(mockPortfolioSource).getPortfolio();
    }

    @Test
    public void testProcessStockQuoteFailureCallsDisplayNetworkErrorMessageFromView() throws Exception {
        stockSearchPresenter.processStockQuoteFailure();
        verify(mockStockSearchView).displayNetworkErrorMessage();
    }

    @Test
    public void testProcessStockQuoteNotFoundCallsDisplayNotFoundErrorMessageFromView() throws Exception {
        stockSearchPresenter.processStockQuoteNotFound();
        verify(mockStockSearchView).displayNotFoundErrorMessage();
    }

    @Test
    public void testPortfolioAccessFailureCallsDisplayDatabaseErrorMessageFromView() throws Exception {
        stockSearchPresenter.portfolioAccessFailure();
        verify(mockStockSearchView).displayDatabaseErrorMessage();
    }

    @Test
    public void testSearchStocksCallsGetStockQuotesFromViewSingleStock() throws Exception {

        stockSearchPresenter.searchStocks("IBM");
        verify(mockStockQuoteSource).getStockQuotes(stockSearchListCaptor.capture());
        assertEquals("IBM", stockSearchListCaptor.getValue().get(0));

    }

    @Test
    public void testSearchStocksCallsGetStockQuotesFromViewMultipleStocks() throws Exception {

        List<String> stockSearchList = new ArrayList<>();
        stockSearchList.add("IBM");
        stockSearchList.add("CAT");

        stockSearchPresenter.searchStocks(stockSearchList);
        verify(mockStockQuoteSource).getStockQuotes(stockSearchListCaptor.capture());
        assertEquals("IBM", stockSearchListCaptor.getValue().get(0));
        assertEquals("CAT", stockSearchListCaptor.getValue().get(1));

    }

    @Test
    public void testPortfolioAccessSuccessCallsDisplayEmptyPortfolioErrorMessageFromViewOnZeroStocksReturned() throws Exception {

        List<String> portfolioResultList = new ArrayList<>();

        stockSearchPresenter.portfolioAccessSuccess(portfolioResultList);
        verify(mockStockSearchView).displayEmptyPortfolioErrorMessage();

    }

    @After
    public void tearDown() throws Exception {
    }
}