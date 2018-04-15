package com.example.janus.stocktracker.portfolio;

import com.example.janus.stocktracker.data.stockquotes.PortfolioQuoteService;
import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
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

public class PortfolioPresenterUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private StockQuote mockStockQuote;

    @Mock
    private PortfolioContract.View mockPortfolioView;

    @Mock
    private TickerSymbolsDataSource mockTickerSymbolsRepository;
    private StockQuoteService mockStockQuoteDataSource;
    private PortfolioQuoteService mockPortfolioQuoteService;

    // This is the Class under test
    private PortfolioContract.Presenter portfolioPresenter;

    @Before
    public void setUp() throws Exception {

        mockPortfolioView = mock(PortfolioContract.View.class);
        mockTickerSymbolsRepository = mock(TickerSymbolsRepository.class);
        mockStockQuoteDataSource = mock(StockQuoteService.class);
        mockPortfolioQuoteService = mock(PortfolioQuoteService.class);

        mockStockQuote = mock(StockQuote.class);

        portfolioPresenter = new PortfolioPresenter(mockPortfolioView, mockPortfolioQuoteService);

    }

    @Test
    public void testLoadStocksCallsGetPortfolioQuotesInPortfolioQuoteServiceAndShowStocksInView() throws Exception {

        List<String> stockSearchList = new ArrayList<>();
        stockSearchList.add("IBM");
        stockSearchList.add("CAT");

        List<StockQuote> stockQuotesList = new ArrayList<>();
        stockQuotesList.add(mockStockQuote);

        portfolioPresenter.loadPortfolio();

        ArgumentCaptor<PortfolioQuoteService.GetPortfolioQuotesCallback> mGetPortfolioQuotesCallbackCaptor =
                ArgumentCaptor.forClass(PortfolioQuoteService.GetPortfolioQuotesCallback.class);

        verify(mockPortfolioQuoteService).getPortfolioQuotes(mGetPortfolioQuotesCallbackCaptor.capture());

        mGetPortfolioQuotesCallbackCaptor.getValue().onStockQuotesLoaded(stockQuotesList);
        verify(mockPortfolioView).showStocks(stockQuotesList);

    }

    @Test
    public void testLoadStocksCallsPortfolioQuoteServiceAndCallsLoadingErrorInView() throws Exception {

        portfolioPresenter.loadPortfolio();

        ArgumentCaptor<PortfolioQuoteService.GetPortfolioQuotesCallback> mGetPortfolioQuotesCallbackCaptor =
                ArgumentCaptor.forClass(PortfolioQuoteService.GetPortfolioQuotesCallback.class);

        verify(mockPortfolioQuoteService).getPortfolioQuotes(mGetPortfolioQuotesCallbackCaptor.capture());

        mGetPortfolioQuotesCallbackCaptor.getValue().onDataNotAvailable();
        verify(mockPortfolioView).showLoadingError();

    }

    @Test
    public void testLoadStocksCallsPortfolioQuoteServiceAndShowEmptyPortfolioMessageOnEmptyPortfolioInView() throws Exception {

        portfolioPresenter.loadPortfolio();

        ArgumentCaptor<PortfolioQuoteService.GetPortfolioQuotesCallback> mGetPortfolioQuotesCallbackCaptor =
                ArgumentCaptor.forClass(PortfolioQuoteService.GetPortfolioQuotesCallback.class);

        verify(mockPortfolioQuoteService).getPortfolioQuotes(mGetPortfolioQuotesCallbackCaptor.capture());

        mGetPortfolioQuotesCallbackCaptor.getValue().onStockQuotesLoaded(new ArrayList<StockQuote>());

        verify(mockPortfolioView).showEmptyPortfolioMessage();

    }

    @Test
    public void testLoadStocksCallsGetAllTickerSymbolInRepositoryAndCallsLoadingFailureInView() throws Exception {

        portfolioPresenter.loadPortfolio();

        ArgumentCaptor<PortfolioQuoteService.GetPortfolioQuotesCallback> mGetPortfolioQuotesCallbackCaptor =
                ArgumentCaptor.forClass(PortfolioQuoteService.GetPortfolioQuotesCallback.class);

        verify(mockPortfolioQuoteService).getPortfolioQuotes(mGetPortfolioQuotesCallbackCaptor.capture());

        mGetPortfolioQuotesCallbackCaptor.getValue().onDataBaseError();

        verify(mockPortfolioView).showLoadingError();

    }

    @Test
    public void testSelectIndividualStockQuoteCallsShowStockQuoteInView() throws Exception {

        portfolioPresenter.selectIndividualStockQuote(mockStockQuote);

        verify(mockPortfolioView).showIndividualStockQuote(mockStockQuote);

    }

    @After
    public void tearDown() throws Exception {
    }

}