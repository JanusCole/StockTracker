package com.example.janus.stocktracker.stockquote;

import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.stockquote.StockQuoteContract;
import com.example.janus.stocktracker.stockquote.StockQuotePresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StockQuotePresenterUnitTest {

    private StockQuote stockQuote;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private StockQuoteContract.View mockStockQuoteView;

    @Mock
    private TickerSymbolsDataSource mockTickerSymbolsRepository;

    @Captor
    private ArgumentCaptor<List<String>> stockListCaptor;
    @Captor
    private ArgumentCaptor<String> stockTickerCaptor;

    // This is the Class under test
    private StockQuoteContract.Presenter stockQuotePresenter;

    @Before
    public void setUp() throws Exception {

        mockStockQuoteView = mock(StockQuoteContract.View.class);
        mockTickerSymbolsRepository = mock(TickerSymbolsRepository.class);

        stockQuote = new StockQuote("IBM", "International Business Machines", "Technology", 50.5, 55.5, 52.0, 5000);

        stockQuotePresenter = new StockQuotePresenter(mockStockQuoteView, stockQuote, mockTickerSymbolsRepository);
    }


    @Test
    public void testCheckStockCallsSetsDisplayFieldsInView() throws Exception {

        stockQuotePresenter.loadStock();

        ArgumentCaptor<String> mTickerSymbol = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> mCompanyName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> mSector = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> mOpenPrice = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> mClosePrice = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> mLatestPrice = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Long> mVolume = ArgumentCaptor.forClass(Long.class);

        verify(mockStockQuoteView).setTickerSymbol(mTickerSymbol.capture());
        verify(mockStockQuoteView).setCompanyName(mCompanyName.capture());
        verify(mockStockQuoteView).setSector(mSector.capture());
        verify(mockStockQuoteView).setOpenPrice(mOpenPrice.capture());
        verify(mockStockQuoteView).setClosePrice(mClosePrice.capture());
        verify(mockStockQuoteView).setLatestPrice(mLatestPrice.capture());
        verify(mockStockQuoteView).setLatestVolume(mVolume.capture());

        assertEquals("IBM", mTickerSymbol.getValue());
        assertEquals("International Business Machines", mCompanyName.getValue());
        assertEquals("Technology", mSector.getValue());
        assertEquals((Double) 50.5, mOpenPrice.getValue());
        assertEquals((Double) 55.5, mClosePrice.getValue());
        assertEquals((Double) 52.0, mLatestPrice.getValue());

    }


    @Test
    public void testAddStockCallsAddTickerSymbol() throws Exception {

        String tickerSymbol = "IBM";

        stockQuotePresenter.addStock(tickerSymbol);

        ArgumentCaptor<TickerSymbolsDataSource.AddTickerSymbolCallback> mAddTickerSymbolCallbackCaptor =
                ArgumentCaptor.forClass(TickerSymbolsDataSource.AddTickerSymbolCallback.class);

        ArgumentCaptor<String> mTickerToAdd = ArgumentCaptor.forClass(String.class);

        verify(mockTickerSymbolsRepository).addTickerSymbol(mTickerToAdd.capture(), mAddTickerSymbolCallbackCaptor.capture());
        mAddTickerSymbolCallbackCaptor.getValue().onTickerSymbolAdded();

        assertEquals("IBM", mTickerToAdd.getValue());

        verify(mockStockQuoteView).showStockInPortfolio();
    }

    @Test
    public void testAddStockCallsDeleteTickerSymbol() throws Exception {

        String tickerSymbol = "IBM";

        stockQuotePresenter.deleteStock(tickerSymbol);

        ArgumentCaptor<TickerSymbolsDataSource.DeleteTickerSymbolCallback> mDeleteTickerSymbolCallbackCaptor =
                ArgumentCaptor.forClass(TickerSymbolsDataSource.DeleteTickerSymbolCallback.class);

        ArgumentCaptor<String> mTickerToDelete = ArgumentCaptor.forClass(String.class);

        verify(mockTickerSymbolsRepository).deleteTickerSymbol(mTickerToDelete.capture(), mDeleteTickerSymbolCallbackCaptor.capture());
        mDeleteTickerSymbolCallbackCaptor.getValue().onTickerSymbolDeleted();

        assertEquals("IBM", mTickerToDelete.getValue());

        verify(mockStockQuoteView).showStockNotInPortfolio();
    }


    @Test
    public void testAddStockCallsAddTickerSymbolShowsError() throws Exception {

        String tickerSymbol = "IBM";

        stockQuotePresenter.addStock(tickerSymbol);

        ArgumentCaptor<TickerSymbolsDataSource.AddTickerSymbolCallback> mAddTickerSymbolCallbackCaptor =
                ArgumentCaptor.forClass(TickerSymbolsDataSource.AddTickerSymbolCallback.class);

        ArgumentCaptor<String> mTickerToAdd = ArgumentCaptor.forClass(String.class);

        verify(mockTickerSymbolsRepository).addTickerSymbol(mTickerToAdd.capture(), mAddTickerSymbolCallbackCaptor.capture());
        mAddTickerSymbolCallbackCaptor.getValue().onDataBaseError();

        assertEquals("IBM", mTickerToAdd.getValue());

        verify(mockStockQuoteView).showDatabaseError();
    }


   @After
    public void tearDown() throws Exception {
    }

}