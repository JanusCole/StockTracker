package com.example.janus.stocktracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.janus.stocktracker.data.Portfolio;
import com.example.janus.stocktracker.data.PortfolioDBContract;
import com.example.janus.stocktracker.data.PortfolioDBOpenHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.intent.Checks.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// THIS IS A UNIT TEST LOCATED IN THE ANDROID TEST FOLDER IN ORDER TO USE CONTEXT
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

@RunWith(AndroidJUnit4.class)
public class PortfolioUnitTest {

    PortfolioDBOpenHelper portfolioDBOpenHelper;
    Portfolio userPortfolio;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {

        mActivityRule.getActivity().deleteDatabase(PortfolioDBOpenHelper.DATABASE_NAME);
        portfolioDBOpenHelper = new PortfolioDBOpenHelper(mActivityRule.getActivity());
        userPortfolio = new Portfolio(mActivityRule.getActivity());

    }

    @Test
    public void testPortfolioCheckIBM() throws Exception {

        assertFalse(userPortfolio.stockInPortfolio("IBM"));

    }

    @Test
    public void testPortfolioCheckNullString() throws Exception {

        assertFalse(userPortfolio.stockInPortfolio(""));

    }


    @Test
    public void testPortfolioAddNull() throws Exception {

        userPortfolio.addStockToPortfolio("");

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {""},
                null,
                null,
                null);

        assert((portfolioCursor.getCount() == 0));

    }

    @Test
    public void testPortfolioAddAAPL() throws Exception {

        userPortfolio.addStockToPortfolio("AAPL");

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {"AAPL"},
                null,
                null,
                null);

        assert((portfolioCursor.getCount() == 1));

        portfolioCursor.moveToFirst();
        int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

        assertEquals("AAPL", portfolioCursor.getString(stockTickerIndex));

        assert(userPortfolio.stockInPortfolio("AAPL"));
    }


    @Test
    public void testPortfolioRemoveAAPL() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, "AAPL");

        portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);

        userPortfolio.removeStockFromPortfolio("AAPL");

        assertFalse(userPortfolio.stockInPortfolio("AAPL"));

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {"AAPL"},
                null,
                null,
                null);

        assert((portfolioCursor.getCount() == 0));

    }

    @Test
    public void testPortfolioAddMultipleStocks() throws Exception {

        userPortfolio.addStockToPortfolio("AAPL");
        userPortfolio.addStockToPortfolio("IBM");

        List<String> dbResults = userPortfolio.getPortfolio();

        assert (dbResults.size() == 2);
        assert (dbResults.get(0) == "AAPL");
        assert (dbResults.get(1) == "IBM");

    }

    @After
    public void tearDown() throws Exception {
        mActivityRule.getActivity().deleteDatabase(PortfolioDBOpenHelper.DATABASE_NAME);
    }

}