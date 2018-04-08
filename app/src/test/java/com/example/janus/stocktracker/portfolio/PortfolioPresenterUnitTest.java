package com.example.janus.stocktracker.portfolio;

import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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
    private StockQuoteDataSource mockStockQuoteDataSource;

    // This is the Class under test
    private PortfolioContract.Presenter portfolioPresenter;

    @Before
    public void setUp() throws Exception {

        mockPortfolioView = mock(PortfolioContract.View.class);
        mockTickerSymbolsRepository = mock(TickerSymbolsRepository.class);
        mockStockQuoteDataSource = mock(StockQuoteDataSource.class);

        mockStockQuote = mock(StockQuote.class);

        portfolioPresenter = new PortfolioPresenter(mockPortfolioView, mockTickerSymbolsRepository, mockStockQuoteDataSource);

    }

    @Test
    public void testGetPortfolioCallsShowStocks() throws Exception {

        List<String> stockSearchList = new ArrayList<>();
        stockSearchList.add("IBM");
        stockSearchList.add("CAT");

        List<StockQuote> stockQuotesList = new ArrayList<>();
        stockQuotesList.add(mockStockQuote);

        portfolioPresenter.loadStocks();

        ArgumentCaptor<TickerSymbolsDataSource.LoadTickerSymbolsCallback> mLoadTickerSymbolsCallbackCaptor =
                ArgumentCaptor.forClass(TickerSymbolsDataSource.LoadTickerSymbolsCallback.class);

        verify(mockTickerSymbolsRepository).getAllTickerSymbols(mLoadTickerSymbolsCallbackCaptor.capture());
        mLoadTickerSymbolsCallbackCaptor.getValue().onTickerSymbolsLoaded(stockSearchList);


        ArgumentCaptor<StockQuoteDataSource.GetStockQuotesCallback> mLoadStockQuotesCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteDataSource.GetStockQuotesCallback.class);

        ArgumentCaptor<List<String>> mStockListCaptor =
                ArgumentCaptor.forClass(List.class);

        verify(mockStockQuoteDataSource).getStockQuotes(mStockListCaptor.capture(), mLoadStockQuotesCallbackCaptor.capture());
        mLoadStockQuotesCallbackCaptor.getValue().onStockQuotesLoaded(stockQuotesList);

        assertEquals("IBM", mStockListCaptor.getValue().get(0));
        assertEquals("CAT", mStockListCaptor.getValue().get(1));

        verify(mockPortfolioView).showStocks(stockQuotesList);

    }

    @Test
    public void testGetPortfolioNetworkErrorCallsLoadingError() throws Exception {

        List<String> stockSearchList = new ArrayList<>();
        stockSearchList.add("IBM");
        stockSearchList.add("CAT");

        portfolioPresenter.loadStocks();

        ArgumentCaptor<TickerSymbolsDataSource.LoadTickerSymbolsCallback> mockLoadTickerSymbolsCallbackCaptor =
                ArgumentCaptor.forClass(TickerSymbolsDataSource.LoadTickerSymbolsCallback.class);

        verify(mockTickerSymbolsRepository).getAllTickerSymbols(mockLoadTickerSymbolsCallbackCaptor.capture());
        mockLoadTickerSymbolsCallbackCaptor.getValue().onTickerSymbolsLoaded(stockSearchList);

        ArgumentCaptor<StockQuoteDataSource.GetStockQuotesCallback> mLoadStockQuotesCallbackCaptor =
                ArgumentCaptor.forClass(StockQuoteDataSource.GetStockQuotesCallback.class);

        ArgumentCaptor<List<String>> mStockListCaptor =
                ArgumentCaptor.forClass(List.class);

        verify(mockStockQuoteDataSource).getStockQuotes(mStockListCaptor.capture(), mLoadStockQuotesCallbackCaptor.capture());
        mLoadStockQuotesCallbackCaptor.getValue().onDataNotAvailable();

        assertEquals("IBM", mStockListCaptor.getValue().get(0));
        assertEquals("CAT", mStockListCaptor.getValue().get(1));

        verify(mockPortfolioView).showLoadingError();

    }

    @Test
    public void testGetPortfolioCallsShowEmptyPortfolioMessageOnEmptyPortfolio() throws Exception {

        portfolioPresenter.loadStocks();

        ArgumentCaptor<TickerSymbolsDataSource.LoadTickerSymbolsCallback> mLoadTickerSymbolsCallbackCaptor =
                ArgumentCaptor.forClass(TickerSymbolsDataSource.LoadTickerSymbolsCallback.class);

        verify(mockTickerSymbolsRepository).getAllTickerSymbols(mLoadTickerSymbolsCallbackCaptor.capture());
        mLoadTickerSymbolsCallbackCaptor.getValue().onTickerSymbolsLoaded(new ArrayList<String>());

        verify(mockPortfolioView).showEmptyPortfolioMessage();

    }

    @Test
    public void testPortfolioDatabaseErrorCallsLoadingFailure() throws Exception {

        portfolioPresenter.loadStocks();

        ArgumentCaptor<TickerSymbolsDataSource.LoadTickerSymbolsCallback> mLoadTickerSymbolsCallbackCaptor =
                ArgumentCaptor.forClass(TickerSymbolsDataSource.LoadTickerSymbolsCallback.class);

        verify(mockTickerSymbolsRepository).getAllTickerSymbols(mLoadTickerSymbolsCallbackCaptor.capture());
        mLoadTickerSymbolsCallbackCaptor.getValue().onDataNotAvailable();

        verify(mockPortfolioView).showLoadingError();

    }

    @Test
    public void testPortfolioCallsSelectIndividualStockQuote() throws Exception {

        portfolioPresenter.selectIndividualStockQuote(mockStockQuote);

        verify(mockPortfolioView).showStockQuote(mockStockQuote);

    }

    @After
    public void tearDown() throws Exception {
    }

}