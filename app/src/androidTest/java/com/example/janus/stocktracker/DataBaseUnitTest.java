package com.example.janus.stocktracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.intent.Checks.checkNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.janus.stocktracker.data.PortfolioDBContract;
import com.example.janus.stocktracker.data.PortfolioDBOpenHelper;

import java.math.BigDecimal;

// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// THIS IS A UNIT TEST LOCATED IN THE ANDROID TEST FOLDER IN ORDER TO USE CONTEXT
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

@RunWith(AndroidJUnit4.class)
public class DataBaseUnitTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    PortfolioDBOpenHelper portfolioDBOpenHelper;


    @Before
    public void setUp() throws Exception {
        mActivityRule.getActivity().deleteDatabase(PortfolioDBOpenHelper.DATABASE_NAME);
        portfolioDBOpenHelper = new PortfolioDBOpenHelper(mActivityRule.getActivity());

    }

    @Test
    public void testDatabaseCreation () {

        assert(portfolioDBOpenHelper.getReadableDatabase().isOpen());
    }

    @Test
    public void testDatabaseColumnCreation () {

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                null,
                null,
                null,
                null,
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

        int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

        assert(stockTickerIndex != -1);
    }

    @Test
    public void testPortfolioCheckIBM() throws Exception {

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {"IBM"},
                null,
                null,
                null);

        assert((portfolioCursor.getCount() == 0));

    }

    @Test
    public void testPortfolioAddAAPL() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, "AAPL");

        long insertResult = portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);

        assert(insertResult != -1);

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {"AAPL"},
                null,
                null,
                null);

        assert((portfolioCursor.getCount() != 0));

        if (portfolioCursor.moveToFirst()) {
            int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

            assertEquals("AAPL", portfolioCursor.getString(stockTickerIndex));
        }

    }

    @Test
    public void testPortfolioAddDupes() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, "AAPL");

        long firstInsertResult = portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);
        long secondInsertResult = portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);

        assert(secondInsertResult == -1);

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {"AAPL"},
                null,
                null,
                null);

        assert((portfolioCursor.getCount() == 1));

        if (portfolioCursor.moveToFirst()) {
            int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

            assertEquals("AAPL", portfolioCursor.getString(stockTickerIndex));
        }

    }

    @Test
    public void testPortfolioAddNull() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, "");

        long insertResult = portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, contentValues);

        assert(insertResult != -1);

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
    public void testPortfolioDeleteAAPL() throws Exception {

        portfolioDBOpenHelper.getWritableDatabase().delete(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK + " = ?",
                new String [] {"AAPL"});

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

        ContentValues aaplContentValues = new ContentValues();
        aaplContentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, "AAPL");
        portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, aaplContentValues);

        ContentValues ibmContentValues = new ContentValues();
        ibmContentValues.put(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK, "IBM");
        portfolioDBOpenHelper.getWritableDatabase().insert(PortfolioDBContract.PortfolioEntry.TABLE_NAME, null, ibmContentValues);

        Cursor portfolioCursor = portfolioDBOpenHelper.getReadableDatabase().query(PortfolioDBContract.PortfolioEntry.TABLE_NAME,
                new String [] {PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK},
                null,
                null,
                null,
                null,
                PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

        assert (portfolioCursor.getCount() == 2);

        portfolioCursor.moveToFirst();
        int stockTickerIndex = portfolioCursor.getColumnIndex(PortfolioDBContract.PortfolioEntry.COLUMN_NAME_STOCK);

        assertEquals("AAPL", portfolioCursor.getString(stockTickerIndex));

        portfolioCursor.moveToNext();

        assertEquals("IBM", portfolioCursor.getString(stockTickerIndex));


    }

    @After
    public void tearDown() throws Exception {
        mActivityRule.getActivity().deleteDatabase(PortfolioDBOpenHelper.DATABASE_NAME);

    }

}