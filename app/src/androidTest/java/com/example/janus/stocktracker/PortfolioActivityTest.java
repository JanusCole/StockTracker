package com.example.janus.stocktracker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.janus.stocktracker.data.database.PortfolioDBContract;
import com.example.janus.stocktracker.data.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.data.database.TickerSymbolsRepository;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.data.stockquotes.StockQuoteService;
import com.example.janus.stocktracker.portfolio.PortfolioActivity;

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
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


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
        deletePortfolio();
    }

    // Test Screen Startup

    @Test
    public void testMainActivityExists() throws Exception {
        checkNotNull(mActivityRule.getActivity());
    }

    @Test
    public void testDisplaysMultipleStock () {

        onView(withId(R.id.okButton_AlertDialog)).perform(click());


        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<StockQuote> testStockQuotes = new ArrayList<>();

                StockQuote testIBMStockQuote = new StockQuote("IBM", "International Business Machines", "Technology", 50.5, 55.5, 52.0, 5000);
                StockQuote testAAPLStockQuote = new StockQuote("AAPL", "Apple", "Technology", 60.5, 65.5, 62.0, 6000);

                testStockQuotes.add(testAAPLStockQuote);
                testStockQuotes.add(testIBMStockQuote);

                mActivityRule.getActivity().getPortfolioFragment().showStocks(testStockQuotes);
            }
        });

        onView(withText("IBM")).check(matches(isDisplayed()));
        onView(withText("52.00")).check(matches(isDisplayed()));

        onView(withText("AAPL")).check(matches(isDisplayed()));
        onView(withText("62.00")).check(matches(isDisplayed()));

    }

    @Test
    public void testEmptyPortfolio() throws Exception {

        onView(withText("Your portfolio is empty"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

    }


    public void deletePortfolio () {

        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(mActivityRule.getActivity());

        portfolioDBOpenHelper.getWritableDatabase().execSQL("DELETE FROM " + PortfolioDBContract.PortfolioEntry.TABLE_NAME);

    }

   @After
    public void tearDown() throws Exception {
    }
}