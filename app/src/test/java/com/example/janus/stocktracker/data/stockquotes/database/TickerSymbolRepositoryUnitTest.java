package com.example.janus.stocktracker.data.stockquotes.database;

import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class TickerSymbolRepositoryUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TickerSymbolsDataSource mockTickerSymbolsDataSource;

    @Mock
    private TickerSymbolsDataSource.LoadTickerSymbolsCallback mockLoadTickerSymbolsCallback;
    private TickerSymbolsDataSource.AddTickerSymbolCallback mockAddTickerSymbolCallback;
    private TickerSymbolsDataSource.GetTickerSymbolCallback mockGetTickerSymbolCallback;
    private TickerSymbolsDataSource.DeleteTickerSymbolCallback mockDeleteTickerSymbolCallback;

    // This is the Class under test
    private TickerSymbolsRepository tickerSymbolsRepository;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        tickerSymbolsRepository = TickerSymbolsRepository.getInstance(mockTickerSymbolsDataSource);

    }


    @Test
    public void testGetAllTickerSymbolsCallsGetAllTickerSymbolsFromDataSource() throws Exception {

        tickerSymbolsRepository.getAllTickerSymbols(mockLoadTickerSymbolsCallback);

        verify(mockTickerSymbolsDataSource).getAllTickerSymbols(any(TickerSymbolsDataSource.LoadTickerSymbolsCallback.class));

    }


    @Test
    public void testDeleteTickerSymbolCallsDeleteTickerSymbolFromDataSource() throws Exception {

        String tickerSymbol = "IBM";

        tickerSymbolsRepository.deleteTickerSymbol(tickerSymbol, mockDeleteTickerSymbolCallback);

        ArgumentCaptor<String> mTickerToDelete = ArgumentCaptor.forClass(String.class);

        verify(mockTickerSymbolsDataSource).deleteTickerSymbol(mTickerToDelete.capture(), any(TickerSymbolsDataSource.DeleteTickerSymbolCallback.class));

        assertEquals(tickerSymbol, mTickerToDelete.getValue());

    }

    @Test
    public void testAddTickerSymbolCallsAddTickerSymbolFromDataSource() throws Exception {

        String tickerSymbol = "IBM";

        tickerSymbolsRepository.addTickerSymbol(tickerSymbol, mockAddTickerSymbolCallback);

        ArgumentCaptor<String> mTickerToAdd = ArgumentCaptor.forClass(String.class);

        verify(mockTickerSymbolsDataSource).addTickerSymbol(mTickerToAdd.capture(), any(TickerSymbolsDataSource.AddTickerSymbolCallback.class));

        assertEquals(tickerSymbol, mTickerToAdd.getValue());

    }

    @Test
    public void testGetTickerSymbolCallsGetTickerSymbolFromDataSource() throws Exception {

        String tickerSymbol = "IBM";

        tickerSymbolsRepository.getTickerSymbol(tickerSymbol, mockGetTickerSymbolCallback);

        ArgumentCaptor<String> mTickerToGet = ArgumentCaptor.forClass(String.class);

        verify(mockTickerSymbolsDataSource).getTickerSymbol(mTickerToGet.capture(), any(TickerSymbolsDataSource.GetTickerSymbolCallback.class));

        assertEquals(tickerSymbol, mTickerToGet.getValue());

    }


    @After
    public void tearDown() throws Exception {
        TickerSymbolsRepository.deleteInstance();
    }

}