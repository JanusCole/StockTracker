package com.example.janus.stocktracker;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.stockquote.StockQuoteActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class StockQuoteActivityTest {

    @Rule
    public ActivityTestRule<StockQuoteActivity> mActivityRule = new ActivityTestRule<StockQuoteActivity>(
            StockQuoteActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            StockQuote testStockQuote = new StockQuote("IBM", "International Business Machines", "Technology", 50.5, 55.5, 52.0, 5000);
            Intent stockQuoteIntent = new Intent();
            stockQuoteIntent.putExtra(StockQuoteActivity.STOCK_QUOTE, (Serializable) testStockQuote);
            return stockQuoteIntent;
        }
    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    // Test Screen Startup

    @Test
    public void testMainActivityExists() throws Exception {
        checkNotNull(mActivityRule.getActivity());

    }

    @Test
    public void testDisplayStockQuote () {
        onView(withId(R.id.displayOneStockFragment)).check(matches((isDisplayed())));

        onView(withText(("IBM"))).check(matches(isDisplayed()));
        onView(withText(("International Business Machines"))).check(matches(isDisplayed()));
        onView(withText(("Technology"))).check(matches(isDisplayed()));
        onView(withText(("50.50"))).check(matches(isDisplayed()));
        onView(withText(("55.50"))).check(matches(isDisplayed()));
        onView(withText(("52.00"))).check(matches(isDisplayed()));
        onView(withText(("5,000"))).check(matches(isDisplayed()));

    }

   @After
    public void tearDown() throws Exception {
    }
}