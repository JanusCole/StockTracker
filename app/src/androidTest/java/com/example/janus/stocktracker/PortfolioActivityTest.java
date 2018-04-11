package com.example.janus.stocktracker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.janus.stocktracker.data.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.data.database.TickerSymbolsDataSource;
import com.example.janus.stocktracker.data.database.TickerSymbolsLocalDataSource;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteRemoteService;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;
import com.example.janus.stocktracker.data.stockquotes.StockQuotesWebAPI;
import com.example.janus.stocktracker.portfolio.PortfolioActivity;
import com.example.janus.stocktracker.portfolio.PortfolioContract;
import com.example.janus.stocktracker.portfolio.PortfolioPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class PortfolioActivityTest {

    @Rule
    public ActivityTestRule<PortfolioActivity> mActivityRule = new ActivityTestRule<>(
            PortfolioActivity.class);

    @Mock
    TickerSymbolsRepository mockTickerSymbolsRepository;

    @Mock
    StockQuoteService mockStockQuoteService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    // Test Screen Startup

    @Test
    public void testMainActivityExists() throws Exception {
        checkNotNull(mActivityRule.getActivity());

    }

   @After
    public void tearDown() throws Exception {
    }
}