package com.example.janus.stocktracker;

import android.content.ContentValues;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.janus.stocktracker.data.database.PortfolioDBContract;
import com.example.janus.stocktracker.data.database.PortfolioDBOpenHelper;
import com.example.janus.stocktracker.splashscreen.SplashScreen;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class SearchMultipleStockTest {

    @Rule
    public ActivityTestRule<SplashScreen> mActivityRule = new ActivityTestRule<>(
            SplashScreen.class);

    String retroFitResponseIBM = "{\"symbol\":\"IBM\",\"companyName\":\"International Business Machines Corporation\",\"primaryExchange\":\"New York Stock Exchange\",\"sector\":\"Technology\",\"calculationPrice\":\"close\",\"open\":166.12,\"openTime\":1516977000714,\"close\":167.34,\"closeTime\":1517000510671,\"high\":167.414,\"low\":165.79,\"latestPrice\":167.34,\"latestSource\":\"Close\",\"latestTime\":\"January 26, 2018\",\"latestUpdate\":1517000510671,\"latestVolume\":3751199,\"iexRealtimePrice\":null,\"iexRealtimeSize\":null,\"iexLastUpdated\":null,\"delayedPrice\":167.29,\"delayedPriceTime\":1517003863274,\"previousClose\":165.47,\"change\":1.87,\"changePercent\":0.0113,\"iexMarketPercent\":null,\"iexVolume\":null,\"avgTotalVolume\":6165192,\"iexBidPrice\":null,\"iexBidSize\":null,\"iexAskPrice\":null,\"iexAskSize\":null,\"marketCap\":154921929195,\"peRatio\":12.1,\"week52High\":182.79,\"week52Low\":139.13,\"ytdChange\":0.0848622366288493}";
    String retroFitResponseAAPL = "{\"symbol\":\"AAPL\",\"companyName\":\"Apple Corporation\",\"primaryExchange\":\"New York Stock Exchange\",\"sector\":\"Technology\",\"calculationPrice\":\"close\",\"open\":188.12,\"openTime\":1516977000714,\"close\":190.34,\"closeTime\":1517000510671,\"high\":167.414,\"low\":165.79,\"latestPrice\":205.34,\"latestSource\":\"Close\",\"latestTime\":\"January 26, 2018\",\"latestUpdate\":1517000510671,\"latestVolume\":3751199,\"iexRealtimePrice\":null,\"iexRealtimeSize\":null,\"iexLastUpdated\":null,\"delayedPrice\":167.29,\"delayedPriceTime\":1517003863274,\"previousClose\":165.47,\"change\":1.87,\"changePercent\":0.0113,\"iexMarketPercent\":null,\"iexVolume\":null,\"avgTotalVolume\":6165192,\"iexBidPrice\":null,\"iexBidSize\":null,\"iexAskPrice\":null,\"iexAskSize\":null,\"marketCap\":154921929195,\"peRatio\":12.1,\"week52High\":182.79,\"week52Low\":139.13,\"ytdChange\":0.0848622366288493}";

    @Before
    public void setUp() throws Exception {
//      mActivityRule.getActivity().deleteDatabase(PortfolioDBOpenHelper.DATABASE_NAME);
        deletePortfolio();
    }

    @Test
    public void testSearchStock() throws Exception {

        addStockToPortfolio("IBM");

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(retroFitResponseIBM));

//        mActivityRule.getActivity().setBaseURL(mockWebServer.url("").toString());

//        Espresso.registerIdlingResources(mActivityRule.getActivity().getCountingIdlingResource());

        onView(withId(R.id.action_portfolio)).perform(click());
        onView(withId(R.id.displayMultipleStocksFragment)).check(matches((isDisplayed())));

        onView(ViewMatchers.withId(R.id.displayStocksRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(withId(R.id.tickerSymbolTextView_RecyclerView)).check(matches((withText("IBM"))));
        onView(withId(R.id.latestPriceTextView_RecyclerView)).check(matches((withText("167.34"))));
        onView(withId(R.id.priceChangeTextView_RecyclerView)).check(matches((withText("1.22"))));

//      deletePortfolio();

    }

    @Test
    public void testMultipleSearchStock() throws Exception {

        addStockToPortfolio("IBM");
        addStockToPortfolio("AAPL");

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(retroFitResponseAAPL));
        mockWebServer.enqueue(new MockResponse().setBody(retroFitResponseIBM));

//        mActivityRule.getActivity().setBaseURL(mockWebServer.url("").toString());

//        Espresso.registerIdlingResources(mActivityRule.getActivity().getCountingIdlingResource());

        onView(withId(R.id.action_portfolio)).perform(click());
        onView(withId(R.id.displayMultipleStocksFragment)).check(matches((isDisplayed())));

        onView(ViewMatchers.withId(R.id.displayStocksRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(ViewMatchers.withId(R.id.displayStocksRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));

        (onView(withText("AAPL"))).check(matches((isDisplayed())));
        (onView(withText("205.34"))).check(matches((isDisplayed())));
        (onView(withText("17.22"))).check(matches((isDisplayed())));

        onView(ViewMatchers.withId(R.id.displayStocksRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(1));

        (onView(withText("IBM"))).check(matches((isDisplayed())));
        (onView(withText("167.34"))).check(matches((isDisplayed())));
        (onView(withText("1.22"))).check(matches((isDisplayed())));

//      deletePortfolio();

    }

    @Test
    public void testSearchStockClick() throws Exception {

        addStockToPortfolio("IBM");

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse().setBody(retroFitResponseIBM));
        mockWebServer.enqueue(new MockResponse().setBody(retroFitResponseIBM));

//        mActivityRule.getActivity().setBaseURL(mockWebServer.url("").toString());

//        Espresso.registerIdlingResources(mActivityRule.getActivity().getCountingIdlingResource());

        onView(withId(R.id.action_portfolio)).perform(click());
        onView(withId(R.id.displayMultipleStocksFragment)).check(matches((isDisplayed())));

        onView(ViewMatchers.withId(R.id.displayStocksRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.displayOneStockFragment)).check(matches((isDisplayed())));
        onView(withId(R.id.tickerSymbolTextView_OneStockDisplay)).check(matches((withText("IBM"))));
        onView(withId(R.id.companyNameTextView_OneStockDisplay)).check(matches((withText("International Business Machines Corporation"))));
        onView(withId(R.id.sectorTextView_OneStockDisplay)).check(matches((withText("Technology"))));
        onView(withId(R.id.openPriceTextView_OneStockDisplay)).check(matches((withText("166.12"))));
        onView(withId(R.id.latestPriceTextView_OneStockDisplay)).check(matches((withText("167.34"))));
        onView(withId(R.id.closePriceTextView_OneStockDisplay)).check(matches((withText("167.34"))));
        onView(withId(R.id.priceChangeTextView_OneStockDisplay)).check(matches((withText("1.22"))));


//      deletePortfolio();

    }

    @After
    public void tearDown() throws Exception {
        deletePortfolio();
    }

    public void addStockToPortfolio (String stockTicker) {
        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(mActivityRule.getActivity());

        ContentValues contentValues = new ContentValues();
        contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, stockTicker);

        portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);

    }

    public void deletePortfolio () {

        PortfolioDBOpenHelper portfolioDBOpenHelper = new PortfolioDBOpenHelper(mActivityRule.getActivity());

        portfolioDBOpenHelper.getWritableDatabase().execSQL("DELETE FROM " + PortfolioDBContract.PortfolioEntry.TABLE_NAME);

    }
}