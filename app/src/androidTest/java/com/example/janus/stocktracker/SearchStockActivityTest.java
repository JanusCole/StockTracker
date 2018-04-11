package com.example.janus.stocktracker;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.janus.stocktracker.stocksearch.StockSearchActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class SearchStockActivityTest {

    @Rule
    public ActivityTestRule<StockSearchActivity> mActivityRule = new ActivityTestRule<>(
            StockSearchActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    // Test Screen Startup

    @Test
    public void testMainActivityExists() throws Exception {
        checkNotNull(mActivityRule.getActivity());

    }

    @Test
    public void testStockSearchActivityAtStartup() throws Exception {
        onView(withId(R.id.searchStocksFragment)).check(matches((isDisplayed())));

    }

    @After
    public void tearDown() throws Exception {
    }
}