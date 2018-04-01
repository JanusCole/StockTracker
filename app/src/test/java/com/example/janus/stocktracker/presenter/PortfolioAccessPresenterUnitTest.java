package com.example.janus.stocktracker.presenter;

import com.example.janus.stocktracker.model.database.TickerSymbolsRepository;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PortfolioAccessPresenterUnitTest {

/*    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    PortfolioAccessContract.View mockPortfolioAccessView;

    @Mock
    DatabaseAccessAsync mockDatabaseAccessAsync;

    @Captor
    ArgumentCaptor<List<String>> stockListCaptor;
    @Captor
    ArgumentCaptor<String> stockTickerCaptor;

    // This is the Class under test
    TickerSymbolsRepository portfolioAccessPresenter;

    @Before
    public void setUp() throws Exception {
        mockPortfolioAccessView = mock(PortfolioAccessContract.View.class);
        mockDatabaseAccessAsync = mock(DatabaseAccessAsync.class);
//        portfolioAccessPresenter = new TickerSymbolsRepository(mockDatabaseAccessAsync);
        portfolioAccessPresenter.setPortfolioAccessView(mockPortfolioAccessView);
    }

    @Test
    public void testAddStockCallsAddOneStockPortfolioDBAsync() throws Exception {

        portfolioAccessPresenter.addStock("IBM");
        verify(mockDatabaseAccessAsync).addOneStockPortfolioDBAsync(stockTickerCaptor.capture());
        assertEquals("IBM", stockTickerCaptor.getValue());
    }

    @Test
    public void testCheckStockCallsGetOneStockPortfolioDBAsync() throws Exception {

        portfolioAccessPresenter.checkStock("IBM");
        verify(mockDatabaseAccessAsync).getOneStockPortfolioDBAsync(stockTickerCaptor.capture());
        assertEquals("IBM", stockTickerCaptor.getValue());
    }

    @Test
    public void testDeleteStockCallsDeleteOneStockPortfolioDBAsync() throws Exception {

        portfolioAccessPresenter.deleteStock("IBM");
        verify(mockDatabaseAccessAsync).deleteOneStockPortfolioDBAsync(stockTickerCaptor.capture());
        assertEquals("IBM", stockTickerCaptor.getValue());
    }


    @Test
    public void testGetPortfolioCallsGetAllStocksPortfolioDBAsync() throws Exception {
        portfolioAccessPresenter.getPortfolio();
        verify(mockDatabaseAccessAsync).getAllStocksPortfolioDBAsync();
    }

    @Test
    public void testPortfolioDBAccessSuccessCallsPortfolioAccessSuccessFromView() throws Exception {

        List<String> stockSearchList = new ArrayList<>();
        stockSearchList.add("IBM");
        stockSearchList.add("CAT");

        portfolioAccessPresenter.portfolioDBAccessSuccess(stockSearchList);
        verify(mockPortfolioAccessView).portfolioAccessSuccess(stockListCaptor.capture());
        assertEquals("IBM", stockListCaptor.getValue().get(0));
        assertEquals("CAT", stockListCaptor.getValue().get(1));

    }

    @Test
    public void testPortfolioDBAccessFailureCallsPortfolioAccessFailureFromView() throws Exception {
        portfolioAccessPresenter.portfolioDBAccessFailure();
        verify(mockPortfolioAccessView).portfolioAccessFailure();
    }

 */   @After
    public void tearDown() throws Exception {
    }

}